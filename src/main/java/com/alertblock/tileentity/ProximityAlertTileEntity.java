package com.alertblock.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.UUID;

public class ProximityAlertTileEntity extends AlertTileEntity implements ITickable {
  private static final float RADIUS = 5;

  private UUID lastCalled;

  public ProximityAlertTileEntity() {}

  @Override
  public void update() {
    World world = this.getWorld();

    if (!world.isRemote) {
      EntityPlayer closest =
          world.getClosestPlayer(this.pos.getX(), this.pos.getY(), this.pos.getZ(), RADIUS, false);

      if (closest != null) {
        UUID closestId = closest.getUniqueID();

        if (!closestId.equals(this.lastCalled) && !this.isEmpty()) {
          lastCalled = closestId;
          alert(
              new TextComponentTranslation("alertblock.alert.proximity")
                  .appendText("\nat XYZ: " + this.pos.getX() + " / " + this.pos.getY() + " / " + this.pos.getZ())
                  .appendText("\nby: " + closest.getName())
                  .appendText("\nat XYZ: " + closest.posX + " / " + closest.posY + " / " + closest.posZ));
        }
      } else {
        lastCalled = null;
      }
    }
  }

  @Override
  public boolean subscribePlayer(EntityPlayer player) {
    boolean didAdd = super.subscribePlayer(player);

    if (didAdd) {
      this.lastCalled = null;
    }

    return didAdd;
  }
}
