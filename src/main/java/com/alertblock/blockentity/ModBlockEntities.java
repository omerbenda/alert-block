package com.alertblock.blockentity;

import com.alertblock.AlertBlockMod;
import com.alertblock.block.ModBlocks;
import com.alertblock.blockentity.custom.AlertBlockEntity;
import com.alertblock.blockentity.custom.ProximityAlertBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
      DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AlertBlockMod.MOD_ID);

  public static final RegistryObject<BlockEntityType<AlertBlockEntity>> ALERT_BLOCK_ENTITY =
      BLOCK_ENTITIES.register(
          "alert_block_entity",
          () -> BlockEntityType.Builder.of(AlertBlockEntity::new).build(null));

  public static final RegistryObject<BlockEntityType<ProximityAlertBlockEntity>>
      PROXIMITY_ALERT_BLOCK_ENTITY =
          BLOCK_ENTITIES.register(
              "proximity_alert_block_entity",
              () ->
                  BlockEntityType.Builder.of(
                          ProximityAlertBlockEntity::new, ModBlocks.PROXIMITY_ALERT_BLOCK.get())
                      .build(null));

  public static void register(IEventBus eventBus) {
    BLOCK_ENTITIES.register(eventBus);
  }
}
