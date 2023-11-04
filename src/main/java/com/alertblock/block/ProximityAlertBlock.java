package com.alertblock.block;

import com.alertblock.tileentity.ProximityAlertTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class ProximityAlertBlock extends AlertBlock {
  public static final PropertyBool POWERED = PropertyBool.create("powered");

  public ProximityAlertBlock(String name, Material material, CreativeTabs tab) {
    super(name, material, tab);
    this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.FALSE));
    this.setTickRandomly(true);
  }

  @Override
  public IBlockState getStateForPlacement(
      World world,
      BlockPos pos,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ,
      int meta,
      EntityLivingBase placer,
      EnumHand hand) {
    return this.getDefaultState().withProperty(POWERED, Boolean.FALSE);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, POWERED);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(POWERED, (meta & 8) > 0);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int i = 0;

    if (state.getValue(POWERED)) {
      i |= 8;
    }

    return i;
  }

  public boolean canProvidePower(IBlockState state) {
    return true;
  }

  @Override
  public int tickRate(World worldIn) {
    return 30;
  }

  @Override
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    super.updateTick(worldIn, pos, state, rand);

    if (!worldIn.isRemote) {
      if (state.getValue(POWERED)) {
        worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.FALSE));
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
      }
    }
  }

  @Override
  public int getWeakPower(
      IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? 15 : 0;
  }

  @Override
  public int getStrongPower(
      IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? 15 : 0;
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
