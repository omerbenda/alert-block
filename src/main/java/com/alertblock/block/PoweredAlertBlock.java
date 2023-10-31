package com.alertblock.block;

import com.alertblock.tileentity.AlertTileEntity;
import net.minecraft.block.Block;
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
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.Random;

public class PoweredAlertBlock extends AlertBlock {
  public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");

  public PoweredAlertBlock(String name, Material material, CreativeTabs tab) {
    super(name, material, tab);
    this.setDefaultState(this.blockState.getBaseState().withProperty(TRIGGERED, Boolean.FALSE));
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
    return this.getDefaultState().withProperty(TRIGGERED, Boolean.FALSE);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, TRIGGERED);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(TRIGGERED, (meta & 8) > 0);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int i = 0;

    if (state.getValue(TRIGGERED)) {
      i |= 8;
    }

    return i;
  }

  @Override
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote && state.getValue(TRIGGERED)) {
      this.alert(worldIn, pos);
    }
  }

  @Override
  public void neighborChanged(
      IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
    boolean flag1 = state.getValue(TRIGGERED);

    if (flag && !flag1) {
      worldIn.scheduleUpdate(pos, this, 2);
      worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.TRUE), 2);
    } else if (!flag && flag1) {
      worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.FALSE), 2);
    }
  }

  private void alert(World world, BlockPos pos) {
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof AlertTileEntity && !((AlertTileEntity) tileEntity).isEmpty()) {
      ((AlertTileEntity) tileEntity)
          .alert(
              new TextComponentTranslation("alertblock.alert.powered")
                  .appendText("\nat XYZ: " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ())
                  .setStyle(new Style().setColor(TextFormatting.RED)));
    }
  }
}
