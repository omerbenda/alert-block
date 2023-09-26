package com.alertblock;

import com.alertblock.proxy.CommonProxy;
import com.alertblock.util.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class AlertBlock {

  public static Logger logger;
  @Mod.Instance public static AlertBlock instance;

  @SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
  public static CommonProxy proxy;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {}

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {}
}
