package com.alertblock.blockentity.custom;

import com.alertblock.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ProximityAlertBlockEntity extends AlertBlockEntity {
  public static final int MAX_RADIUS = 6;

  protected UUID lastCalled;
  public int radius;

  public ProximityAlertBlockEntity(BlockPos pPos, BlockState pBlockState) {
    super(ModBlockEntities.PROXIMITY_ALERT_BLOCK_ENTITY.get(), pPos, pBlockState);
    this.radius = MAX_RADIUS;
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag pTag) {
    pTag.putInt("radius", radius);
    super.saveAdditional(pTag);
  }

  @Override
  public void load(@NotNull CompoundTag pTag) {
    super.load(pTag);

    this.radius = pTag.getInt("radius");
  }

  @Override
  public boolean subscribePlayer(Player pPlayer) {
    boolean didAdd = super.subscribePlayer(pPlayer);

    if (didAdd) {
      this.lastCalled = null;
    }

    return didAdd;
  }

  public int shiftRadius() {
    this.radius++;

    if (radius > MAX_RADIUS) {
      radius = 1;
    }

    return this.radius;
  }

  public UUID lastCalled() {
    return this.lastCalled;
  }

  public void setLastCalled(UUID lastCalled) {
    this.lastCalled = lastCalled;
  }
}
