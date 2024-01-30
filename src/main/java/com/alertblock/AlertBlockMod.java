package com.alertblock;

import com.alertblock.block.ModBlocks;
import com.alertblock.blockentity.ModBlockEntities;
import com.alertblock.item.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AlertBlockMod.MOD_ID)
public class AlertBlockMod {
  public static final String MOD_ID = "alertblock";

  public AlertBlockMod() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    ModItems.register(modEventBus);
    ModBlocks.register(modEventBus);
    ModBlockEntities.register(modEventBus);
    MinecraftForge.EVENT_BUS.register(this);
  }
}
