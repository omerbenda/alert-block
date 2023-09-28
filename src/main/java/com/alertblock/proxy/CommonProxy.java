package com.alertblock.proxy;

import com.alertblock.tileentity.AlertTileEntity;
import com.alertblock.tileentity.ProximityAlertTileEntity;
import com.alertblock.util.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
  public void registerItemRenderer(Item item, int meta, String id) {}

  public void registerTileEntity() {
    // TODO make init class
    GameRegistry.registerTileEntity(AlertTileEntity.class, Reference.MOD_ID + ":alert_block");
    GameRegistry.registerTileEntity(ProximityAlertTileEntity.class, Reference.MOD_ID + ":proximity_alert_block");
  }
}
