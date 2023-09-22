package com.alertblock;

import com.alertblock.block.ModBlocks;
import com.alertblock.blockentity.ModBlockEntities;
import com.alertblock.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AlertBlock.MOD_ID)
public class AlertBlock {
  public static final String MOD_ID = "alertblock";
  public static final Logger LOGGER = LogUtils.getLogger();

  public AlertBlock() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    ModItems.register(modEventBus);
    ModBlocks.register(modEventBus);
    ModBlockEntities.register(modEventBus);
    modEventBus.addListener(this::commonSetup);
    MinecraftForge.EVENT_BUS.register(this);
    modEventBus.addListener(this::addCreative);
  }

  private void commonSetup(final FMLCommonSetupEvent event) {}

  private void addCreative(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey().equals(CreativeModeTabs.REDSTONE_BLOCKS)) {
      event.accept(ModBlocks.POWERED_ALERT_BLOCK);
      event.accept(ModBlocks.PROXIMITY_ALERT_BLOCK);
    }
  }

  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {}

  @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
  public static class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {}
  }
}
