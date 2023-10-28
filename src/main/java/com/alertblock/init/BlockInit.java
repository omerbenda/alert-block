package com.alertblock.init;

import com.alertblock.block.PoweredAlertBlock;
import com.alertblock.block.ProximityAlertBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {
  public static final List<Block> BLOCKS = new ArrayList<>();
  public static final Block POWERED_ALERT_BLOCK =
      new PoweredAlertBlock("powered_alert_block", Material.IRON, CreativeTabs.REDSTONE);
  public static final Block PROXIMITY_ALERT_BLOCK =
      new ProximityAlertBlock("proximity_alert_block", Material.IRON, CreativeTabs.REDSTONE);
}
