package com.alertblock.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlertTileEntity extends TileEntity {
  private final List<UUID> subscriberList;

  public AlertTileEntity() {
    this.subscriberList = new ArrayList<>();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    NBTTagList subscriberTagList = new NBTTagList();

    for (UUID uuid : this.subscriberList) {
      subscriberTagList.appendTag(new NBTTagString(uuid.toString()));
    }

    nbt.setTag("alertblock:subscribers", subscriberTagList);

    return super.writeToNBT(nbt);
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    NBTTagList subscribersTagList = nbt.getTagList("alertblock:subscribers", 8);

    for (int index = 0; index < subscribersTagList.tagCount(); index++) {
      String uuidString = subscribersTagList.getStringTagAt(index);
      this.subscriberList.add(UUID.fromString(uuidString));
    }
  }

  public boolean subscribePlayer(EntityPlayer player) {
    UUID playerId = player.getUniqueID();
    int index = subscriberList.indexOf(playerId);

    if (index == -1) {
      this.subscriberList.add(player.getUniqueID());
      player.sendMessage(new TextComponentTranslation("alertblock.alert.subscribed"));

      return true;
    }

    this.subscriberList.remove(index);
    player.sendMessage(new TextComponentTranslation("alertblock.alert.unsubscribed"));

    return false;
  }
}
