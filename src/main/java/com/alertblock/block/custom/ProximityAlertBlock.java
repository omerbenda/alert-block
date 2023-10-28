package com.alertblock.block.custom;

import com.alertblock.blockentity.ModBlockEntities;
import com.alertblock.blockentity.custom.ProximityAlertBlockEntity;
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
  private static final float RADIUS = 5;

  public ProximityAlertBlock(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return ModBlockEntities.PROXIMITY_ALERT_BLOCK_ENTITY.get().create(pPos, pState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
    return pLevel.isClientSide()
        ? null
        : (level, pos, state, blockEntity) -> {
          if (blockEntity instanceof ProximityAlertBlockEntity proxAlertBlockEntity
              && proxAlertBlockEntity.doSubscribersExists()) {
            Player nearest =
                level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), RADIUS, false);

            if (nearest != null) {
              if (!nearest.getUUID().equals(proxAlertBlockEntity.lastCalled())) {
                proxAlertBlockEntity.setLastCalled(nearest.getUUID());
                proxAlertBlockEntity.alert(getAlertComponent(pos, nearest));
              }
            } else {
              proxAlertBlockEntity.setLastCalled(null);
            }
          }
        };
  }

  Component getAlertComponent(BlockPos pos, Player player) {
    Vec3 playerPos = player.position();
    return Component.translatable("alertblock.alert.proximity")
        .append("\nXYZ: " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ())
        .append(
            "\nby: "
                + player.getName().getString()
                + " at XYZ: "
                + String.format("%.2f", playerPos.x())
                + " / "
                + String.format("%.2f", playerPos.y())
                + " / "
                + String.format("%.2f", playerPos.z()));
  }
}
