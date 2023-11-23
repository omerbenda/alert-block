package com.alertblock.block.custom;

import com.alertblock.blockentity.ModBlockEntities;
import com.alertblock.blockentity.custom.ProximityAlertBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ProximityAlertBlock extends AlertBlock {
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public ProximityAlertBlock(Properties pProperties) {
    super(pProperties);
    this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(POWERED);
  }

  @Override
  public int getSignal(
      BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
    return pBlockState.getValue(POWERED) ? 15 : 0;
  }

  @Override
  public boolean isSignalSource(BlockState pState) {
    return true;
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
                level.getNearestPlayer(
                    pos.getX(), pos.getY(), pos.getZ(), proxAlertBlockEntity.radius, false);

            if (nearest != null) {
              if (!nearest.getUUID().equals(proxAlertBlockEntity.lastCalled())) {
                proxAlertBlockEntity.setLastCalled(nearest.getUUID());
                proxAlertBlockEntity.alert(getAlertComponent(pos, nearest));

                pLevel.setBlock(pos, pState.setValue(POWERED, Boolean.TRUE), 3);
                pLevel.updateNeighborsAt(pos, this);
                pLevel.scheduleTick(pos, this, 30);
              }
            } else {
              proxAlertBlockEntity.setLastCalled(null);
            }
          }
        };
  }

  @Override
  public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    super.tick(pState, pLevel, pPos, pRandom);

    if (pState.getValue(POWERED)) {
      pLevel.setBlock(pPos, pState.setValue(POWERED, Boolean.FALSE), 3);
      pLevel.updateNeighborsAt(pPos, this);
      pLevel.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pPos);
    }
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
      if (pLevel.getBlockEntity(pPos) instanceof ProximityAlertBlockEntity proxAlertEntity) {
        if (pPlayer.isCrouching()) {
          int newRadius = proxAlertEntity.shiftRadius();
          pPlayer.sendSystemMessage(
              Component.translatable("alertblock.action.radius_shift").append("" + newRadius));
        } else {
          proxAlertEntity.subscribePlayer(pPlayer);
        }
      }
    }

    return InteractionResult.SUCCESS;
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
