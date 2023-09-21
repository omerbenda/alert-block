package com.alertblock.block.custom;

import com.alertblock.blockentity.ModBlockEntities;
import com.alertblock.blockentity.custom.AlertBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PoweredAlertBlock extends Block implements EntityBlock {
  public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
  private Player subscriber;

  public PoweredAlertBlock(Properties pProperties) {
    super(pProperties);
    this.registerDefaultState(this.defaultBlockState().setValue(TRIGGERED, Boolean.FALSE));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(TRIGGERED);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return ModBlockEntities.ALERT_BLOCK_ENTITY.get().create(pPos, pState);
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
      subscriber = playerPlacer;
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
      if (pLevel.getBlockEntity(pPos) instanceof AlertBlockEntity alertEntity) {
        alertEntity.alert(pPos);
      }
    } else if (!isSignaled && triggered) {
      pLevel.setBlock(pPos, pState.setValue(TRIGGERED, false), 4);
    }
  }

  @Override
  public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
    if (!pLevel.isClientSide() && pHand.equals(InteractionHand.MAIN_HAND)) {
      if (pLevel.getBlockEntity(pPos) instanceof AlertBlockEntity alertEntity) {
        alertEntity.subscribePlayer(pPlayer);
      }
    }

    return InteractionResult.SUCCESS;
  }
}
