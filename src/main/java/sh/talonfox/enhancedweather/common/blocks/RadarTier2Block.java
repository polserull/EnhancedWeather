package sh.talonfox.enhancedweather.common.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sh.talonfox.enhancedweather.client.screens.RadarScreen;

public class
RadarTier2Block extends Block {
    public RadarTier2Block(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if(world.isClient()) {
            MinecraftClient.getInstance().setScreen(new RadarScreen(blockPos, true, false));
        }
        return ActionResult.SUCCESS;
    }
}
