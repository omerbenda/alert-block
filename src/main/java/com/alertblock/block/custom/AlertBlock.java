package com.alertblock.block.custom;

import com.alertblock.blockentity.ModBlockEntities;
import com.alertblock.blockentity.custom.AlertBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class AlertBlock extends Block implements EntityBlock {
  public AlertBlock(Properties pProperties) {
    super(pProperties);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return ModBlockEntities.ALERT_BLOCK_ENTITY.get().create(pPos, pState);
  }

  @Override
  public InteractionResult use(
      BlockState pState,
      Level pLevel,
      BlockPos pPos,
      Player pPlayer,
      InteractionHand pHand,
      BlockHitResult pHit) {
    if (!pLevel.isClientSide() && pHand.equals(InteractionHand.MAIN_HAND)) {
      if (pLevel.getBlockEntity(pPos) instanceof AlertBlockEntity alertEntity) {
        alertEntity.subscribePlayer(pPlayer);
      }
    }

    return InteractionResult.SUCCESS;
  }
}
