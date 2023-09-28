package com.alertblock.block;

import com.alertblock.tileentity.AlertTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AlertBlock extends BlockBase implements ITileEntityProvider {
  public AlertBlock(String name, Material material, CreativeTabs tab) {
    super(name, material, tab);
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new AlertTileEntity();
  }

  @Nullable
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new AlertTileEntity();
  }

  @Override
  public boolean onBlockActivated(
      World worldIn,
      BlockPos pos,
      IBlockState state,
      EntityPlayer playerIn,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ) {
    TileEntity tileEntity = worldIn.getTileEntity(pos);

    if (hand.equals(EnumHand.MAIN_HAND)) {
      if (!worldIn.isRemote && tileEntity instanceof AlertTileEntity) {
        ((AlertTileEntity) tileEntity).subscribePlayer(playerIn);
      }

      return true;
    }

    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
  }
}
