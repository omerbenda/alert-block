package com.alertblock.blockentity.custom;

import com.alertblock.blockentity.ModBlockEntities;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AlertBlockEntity extends BlockEntity {
  private final List<UUID> subscribers;

  public AlertBlockEntity(BlockPos pPos, BlockState pBlockState) {
    super(ModBlockEntities.ALERT_BLOCK_ENTITY.get(), pPos, pBlockState);
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

  public void subscribePlayer(Player pPlayer) {
    UUID playerUUID = pPlayer.getUUID();
    int index = subscribers.indexOf(playerUUID);

    if (index == -1) {
      subscribers.add(playerUUID);
      pPlayer.sendSystemMessage(Component.translatable("system.alert.subscribed"));
    } else {
      subscribers.remove(index);
      pPlayer.sendSystemMessage(Component.translatable("system.alert.unsubscribed"));
    }
  }

  public void alert(BlockPos pos) {
    PlayerList playerList = Minecraft.getInstance().getSingleplayerServer().getPlayerList();

    subscribers.forEach(
        subscriberId -> {
          Player subscriber = playerList.getPlayer(subscriberId);

          if (subscriber != null) {
            subscriber.sendSystemMessage(
                Component.translatable("system.alert.powered")
                    .append("\nXYZ: " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ()));
          }
        });
  }
}
