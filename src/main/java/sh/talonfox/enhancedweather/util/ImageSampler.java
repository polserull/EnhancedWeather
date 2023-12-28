package sh.talonfox.enhancedweather.util;

import net.minecraft.util.math.MathHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageSampler {
    private final float[] data;
    private final int width;
    private final int height;

    private boolean smooth;

    public ImageSampler(String path) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        BufferedImage image;

        try {
            image = ImageIO.read(url);
        }
        catch (IOException e) {
            e.printStackTrace();
            image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }

        width = image.getWidth();
        height = image.getWidth();
        data = new float[width * height];

        int[] pixels = new int[data.length];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        for (int i = 0; i < data.length; i++) {
            data[i] = (pixels[i] & 255) / 255F;
        }
    }

    private static float interpolate2D(
            double x, double y,
            float v1, float v2, float v3, float v4
    ) {
        return (float)MathHelper.lerp(y, MathHelper.lerp(x, v1, v2), MathHelper.lerp(x, v3, v4));
    }

    public float sample(double x, double z) {
        int x1 = MathHelper.floor(x);
        int z1 = MathHelper.floor(z);
        int x2 = (int)MathHelper.wrap(x1 + 1, width);
        int z2 = (int)MathHelper.wrap(z1 + 1, height);
        float dx = (float) (x - x1);
        float dz = (float) (z - z1);
        x1 = (int)MathHelper.wrap(x1, width);
        z1 = (int)MathHelper.wrap(z1, height);

        float a = data[getIndex(x1, z1)];
        float b = data[getIndex(x2, z1)];
        float c = data[getIndex(x1, z2)];
        float d = data[getIndex(x2, z2)];

        if (smooth) {
            dx = smoothStep(dx);
            dz = smoothStep(dz);
        }

        return interpolate2D(
                dx, dz, a, b, c, d
        );
    }

    private int getIndex(int x, int z) {
        return z * width + x;
    }

    public ImageSampler setSmooth(boolean smooth) {
        this.smooth = smooth;
        return this;
    }

    private float smoothStep(float x) {
        return x * x * x * (x * (x * 6 - 15) + 10);
    }
}