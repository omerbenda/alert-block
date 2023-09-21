package com.alertblock.blockentity.custom;

import com.alertblock.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class ProximityAlertBlockEntity extends AlertBlockEntity {
  protected UUID lastCalled;

  public ProximityAlertBlockEntity(BlockPos pPos, BlockState pBlockState) {
    super(ModBlockEntities.PROXIMITY_ALERT_BLOCK_ENTITY.get(), pPos, pBlockState);
  }

  @Override
  public boolean subscribePlayer(Player pPlayer) {
    boolean didAdd = super.subscribePlayer(pPlayer);

    if (didAdd) {
      this.lastCalled = null;
    }

    return didAdd;
  }

  public UUID lastCalled() {
    return this.lastCalled;
  }

  public void setLastCalled(UUID lastCalled) {
    this.lastCalled = lastCalled;
  }
}
