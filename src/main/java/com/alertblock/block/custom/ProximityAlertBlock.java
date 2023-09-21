package com.alertblock.block.custom;

import com.alertblock.blockentity.custom.AlertBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ProximityAlertBlock extends AlertBlock {
  private static final float RADIUS = 10;

  public ProximityAlertBlock(Properties pProperties) {
    super(pProperties);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
    return pLevel.isClientSide()
        ? null
        : (level, pos, state, blockEntity) -> {
          if (blockEntity instanceof AlertBlockEntity alertBlockEntity) {
            Player nearest = nearestPlayerInRadius(level, pos);

            if (nearest != null) {
              alertBlockEntity.alert(getAlertComponent(pos, nearest));
            }
          }
        };
  }

  private Player nearestPlayerInRadius(Level level, BlockPos pos) {
    return level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), RADIUS, false);
  }

  Component getAlertComponent(BlockPos pos, Player player) {
    Vec3 playerPos = player.position();
    return Component.translatable("system.alert.powered")
        .append("\nXYZ: " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ())
        .append(
            "\nby: "
                + player.getName().getString()
                + " at XYZ: "
                + playerPos.x()
                + " / "
                + playerPos.y()
                + " / "
                + playerPos.z());
  }
}
