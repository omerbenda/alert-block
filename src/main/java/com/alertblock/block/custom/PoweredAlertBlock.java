package com.alertblock.block.custom;

import com.alertblock.blockentity.custom.AlertBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class PoweredAlertBlock extends AlertBlock implements EntityBlock {
  public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

  public PoweredAlertBlock(Properties pProperties) {
    super(pProperties);
    this.registerDefaultState(this.defaultBlockState().setValue(TRIGGERED, Boolean.FALSE));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(TRIGGERED);
  }

  public void neighborChanged(
      BlockState pState,
      Level pLevel,
      BlockPos pPos,
      Block pBlock,
      BlockPos pFromPos,
      boolean pIsMoving) {
    boolean isSignaled = pLevel.hasNeighborSignal(pPos);
    boolean triggered = pState.getValue(TRIGGERED);

    if (isSignaled && !triggered) {
      pLevel.scheduleTick(pPos, this, 4);
      pLevel.setBlock(pPos, pState.setValue(TRIGGERED, true), 4);
      if (pLevel.getBlockEntity(pPos) instanceof AlertBlockEntity alertEntity) {
        alertEntity.alert(getAlertComponent(pPos));
      }
    } else if (!isSignaled && triggered) {
      pLevel.setBlock(pPos, pState.setValue(TRIGGERED, false), 4);
    }
  }

  Component getAlertComponent(BlockPos pos) {
    return Component.translatable("system.alert.powered")
        .append("\nXYZ: " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ());
  }
}
