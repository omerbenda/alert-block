package com.alertblock.block;

import com.alertblock.AlertBlock;
import com.alertblock.init.BlockInit;
import com.alertblock.init.ItemInit;
import com.alertblock.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel {
  public BlockBase(String name, Material material, CreativeTabs tab) {
    super(material);
    setUnlocalizedName(name);
    setRegistryName(name);
    setCreativeTab(tab);

    BlockInit.BLOCKS.add(this);
    ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
  }

  @Override
  public void registerModels() {
    AlertBlock.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
  }
}
