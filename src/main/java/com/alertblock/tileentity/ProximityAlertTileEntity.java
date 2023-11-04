package com.alertblock.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.UUID;

import static com.alertblock.block.ProximityAlertBlock.POWERED;

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
          DecimalFormat decimalFormat = new DecimalFormat("0.00");
          alert(
              new TextComponentTranslation("alertblock.alert.proximity")
                  .appendText(
                      "\nat XYZ: "
                          + this.pos.getX()
                          + " / "
                          + this.pos.getY()
                          + " / "
                          + this.pos.getZ())
                  .appendText("\nby: " + closest.getName())
                  .appendText(
                      "\nat XYZ: "
                          + decimalFormat.format(closest.posX)
                          + " / "
                          + decimalFormat.format(closest.posY)
                          + " / "
                          + decimalFormat.format(closest.posZ))
                  .setStyle(new Style().setColor(TextFormatting.RED)));

          world.setBlockState(
              this.pos, world.getBlockState(this.pos).withProperty(POWERED, Boolean.TRUE), 3);
          world.notifyNeighborsOfStateChange(pos, this.getBlockType(), false);
          world.scheduleUpdate(this.pos, this.getBlockType(), this.getBlockType().tickRate(world));
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
