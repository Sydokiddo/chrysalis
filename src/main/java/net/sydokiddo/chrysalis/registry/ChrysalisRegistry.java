package net.sydokiddo.chrysalis.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDataComponents;
import net.sydokiddo.chrysalis.util.blocks.codecs.BlockPropertyData;
import net.sydokiddo.chrysalis.util.helpers.RegistryHelper;
import net.sydokiddo.chrysalis.util.sounds.codecs.BlockSoundData;
import net.sydokiddo.chrysalis.util.entities.ChrysalisMemoryModules;
import net.sydokiddo.chrysalis.registry.entities.registry.ChrysalisEntities;
import net.sydokiddo.chrysalis.registry.items.ChrysalisItems;
import net.sydokiddo.chrysalis.registry.misc.*;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;
import java.util.*;

public class ChrysalisRegistry {

    // region Entity Data

//    public static final EntityDataAccessor<Integer>
//        ITEM_GLOW_COLOR = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.INT)
//    ;

//    public static final EntityDataAccessor<Optional<UUID>>
//        ENCOUNTERED_MOB_UUID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UUID)
//    ;

    // endregion

    // region Custom Music

    public static Map<String, StructureMusicSound> registeredStructures = new HashMap<>();
    public record StructureMusicSound(Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {}

    // endregion

    // region Data Driven Features

    public static final ResourceKey<Registry<BlockPropertyData>> BLOCK_PROPERTY_DATA = RegistryHelper.registerBlockDataType("properties");
    public static final ResourceKey<Registry<BlockSoundData>> BLOCK_SOUND_DATA = RegistryHelper.registerBlockDataType("sound_group");

    // endregion

    public static void registerAll() {

        IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();

        if (modBus != null) {
            if (Dist.CLIENT.isClient()) modBus.addListener(ChrysalisRegistry::onClientSetup);
        }

        // region Base Registries

        ChrysalisItems.register(modBus); // Works
        ChrysalisDataComponents.register(modBus); // Works
        ChrysalisCreativeModeTabs.register(modBus); // Works
        ChrysalisSoundEvents.register(modBus); // Works
        ChrysalisParticles.registerServer(modBus); // Doesn't Work
        ChrysalisEntities.register(modBus); // Works
        ChrysalisMemoryModules.register(modBus); // Probably Works?
        ChrysalisDamageTypes.register(); // Works
        ChrysalisAttributes.register(modBus); // Doesn't Work
        ChrysalisEffects.register(modBus); // Works
        ChrysalisGameRules.register(); // Works
        ChrysalisCriteriaTriggers.register(modBus); // Probably Works?

        // endregion
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ChrysalisClientRegistry.registerAll(event);
        ChrysalisMod.sendInitializedMessage(true);
    }
}