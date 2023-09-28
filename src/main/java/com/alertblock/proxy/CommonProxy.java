package com.alertblock.proxy;

import com.alertblock.tileentity.AlertTileEntity;
import com.alertblock.util.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
  public void registerItemRenderer(Item item, int meta, String id) {}

  public void registerTileEntity() {
    GameRegistry.registerTileEntity(AlertTileEntity.class, Reference.MOD_ID + ":alert_block");
  }
}
