package com.alertblock.init;

import com.alertblock.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {
  public static final List<Block> BLOCKS = new ArrayList<>();

  public static final Block testBlock = new BlockBase("proximity_alert_block", Material.IRON, CreativeTabs.REDSTONE);
}
