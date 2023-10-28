package com.alertblock.block;

import com.alertblock.tileentity.ProximityAlertTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ProximityAlertBlock extends AlertBlock {
  public ProximityAlertBlock(String name, Material material, CreativeTabs tab) {
    super(name, material, tab);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new ProximityAlertTileEntity();
  }

  @Nullable
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new ProximityAlertTileEntity();
  }
}
