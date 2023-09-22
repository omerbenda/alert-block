package com.alertblock.blockentity.custom;

import com.alertblock.blockentity.ModBlockEntities;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AlertBlockEntity extends BlockEntity {
  protected final List<UUID> subscribers;

  public AlertBlockEntity(BlockPos pPos, BlockState pBlockState) {
    super(ModBlockEntities.ALERT_BLOCK_ENTITY.get(), pPos, pBlockState);
    this.subscribers = new ArrayList<>();
  }

  public AlertBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(pType, pPos, pBlockState);
    this.subscribers = new ArrayList<>();
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag pTag) {
    super.saveAdditional(pTag);
    CompoundTag subscriberList = new CompoundTag();

    subscribers.forEach(
        subscriberId -> subscriberList.putUUID(subscriberId.toString(), subscriberId));

    pTag.put("subscriber_list", subscriberList);
    setChanged();
  }

  @Override
  public void load(@NotNull CompoundTag pTag) {
    super.load(pTag);

    if (pTag.get("subscriber_list") instanceof CompoundTag uuidListTag) {
      uuidListTag.getAllKeys().forEach(key -> subscribers.add(uuidListTag.getUUID(key)));
    }
  }

  public boolean subscribePlayer(Player pPlayer) {
    UUID playerUUID = pPlayer.getUUID();
    int index = subscribers.indexOf(playerUUID);

    if (index == -1) {
      subscribers.add(playerUUID);
      pPlayer.sendSystemMessage(Component.translatable("system.alert.subscribe"));

      return true;
    }

    subscribers.remove(index);
    pPlayer.sendSystemMessage(Component.translatable("system.alert.unsubscribe"));

    return false;
  }

  public boolean doSubscribersExists() {
    return !subscribers.isEmpty();
  }

  public void alert(Component alertComponent) {
    PlayerList playerList = level.getServer().getPlayerList();

    subscribers.forEach(
        subscriberId -> {
          Player subscriber = playerList.getPlayer(subscriberId);

          if (subscriber != null) {
            subscriber.sendSystemMessage(alertComponent);
          }
        });
  }
}
