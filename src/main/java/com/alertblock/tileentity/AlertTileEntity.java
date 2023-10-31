package com.alertblock.tileentity;

import com.alertblock.block.AlertBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlertTileEntity extends TileEntity {
  protected final List<UUID> subscriberList;

  public AlertTileEntity() {
    this.subscriberList = new ArrayList<>();
  }

  @Override
  public boolean shouldRefresh(
      World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
    return !(oldState.getBlock() instanceof AlertBlock);
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

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    NBTTagCompound nbtTag = new NBTTagCompound();

    writeToNBT(nbtTag);

    return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    NBTTagCompound tag = pkt.getNbtCompound();
    readFromNBT(tag);
  }

  public boolean subscribePlayer(EntityPlayer player) {
    UUID playerId = player.getUniqueID();
    int index = subscriberList.indexOf(playerId);
    this.markDirty();

    if (index == -1) {
      this.subscriberList.add(player.getUniqueID());
      player.sendMessage(
          new TextComponentTranslation("alertblock.alert.subscribed")
              .setStyle(new Style().setColor(TextFormatting.YELLOW)));

      return true;
    }

    this.subscriberList.remove(index);
    player.sendMessage(
        new TextComponentTranslation("alertblock.alert.unsubscribed")
            .setStyle(new Style().setColor(TextFormatting.YELLOW)));

    return false;
  }

  public void alert(ITextComponent alertComponent) {
    for (UUID id : subscriberList) {
      this.world
          .getPlayers(EntityPlayer.class, player -> player.getUniqueID().equals(id))
          .forEach(player -> player.sendMessage(alertComponent));
    }
  }

  public boolean isEmpty() {
    return this.subscriberList.isEmpty();
  }
}
