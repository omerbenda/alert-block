package com.alertblock.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class PoweredAlertBlock extends Block {
  public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
  private Player placer;

  public PoweredAlertBlock(Properties pProperties) {
    super(pProperties);
    this.registerDefaultState(this.defaultBlockState().setValue(TRIGGERED, Boolean.FALSE));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(TRIGGERED);
  }

  @Override
  public void setPlacedBy(
      Level pLevel,
      BlockPos pPos,
      BlockState pState,
      @Nullable LivingEntity pPlacer,
      ItemStack pStack) {
    super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);

    if (pPlacer instanceof Player playerPlacer) {
      placer = playerPlacer;
    }
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
      alert();
    } else if (!isSignaled && triggered) {
      pLevel.setBlock(pPos, pState.setValue(TRIGGERED, false), 4);
    }
  }

  private void alert() {
    placer.sendSystemMessage(Component.translatable("system.alert.powered"));
  }
}
