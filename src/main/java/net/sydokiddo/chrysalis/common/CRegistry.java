package net.sydokiddo.chrysalis.common;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.client.entities.rendering.custom_entities.EncounterSpawnerRenderer;
import net.sydokiddo.chrysalis.client.entities.rendering.custom_entities.EntitySpawnerRenderer;
import net.sydokiddo.chrysalis.client.entities.rendering.custom_entities.SeatRenderer;
import net.sydokiddo.chrysalis.client.particles.types.*;
import net.sydokiddo.chrysalis.common.entities.custom_entities.spawners.entity_spawner.EntitySpawnerData;
import net.sydokiddo.chrysalis.common.items.CDataComponents;
import net.sydokiddo.chrysalis.util.blocks.codecs.BlockPropertyData;
import net.sydokiddo.chrysalis.util.entities.codecs.ChargedMobDropData;
import net.sydokiddo.chrysalis.util.entities.codecs.PlayerLootTableData;
import net.sydokiddo.chrysalis.util.helpers.DebugHelper;
import net.sydokiddo.chrysalis.util.helpers.RegistryHelper;
import net.sydokiddo.chrysalis.util.sounds.codecs.BlockSoundData;
import net.sydokiddo.chrysalis.util.entities.CMemoryModules;
import net.sydokiddo.chrysalis.common.entities.registry.CEntities;
import net.sydokiddo.chrysalis.common.items.CItems;
import net.sydokiddo.chrysalis.common.misc.*;
import net.sydokiddo.chrysalis.common.status_effects.CStatusEffects;
import org.lwjgl.glfw.GLFW;
import java.awt.*;
import java.util.*;

public class CRegistry {

    // region Entity Data

    public static final EntityDataAccessor<Integer>
        ITEM_GLOW_COLOR = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.INT)
    ;

    public static final EntityDataAccessor<Optional<UUID>>
        ENCOUNTERED_MOB_UUID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UUID)
    ;

    // endregion

    // region Custom Music

    public static Map<String, StructureMusicSound> registeredStructures = new HashMap<>();
    public record StructureMusicSound(Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {}

    // endregion

    // region Data Driven Features

    public static final ResourceKey<Registry<BlockPropertyData>> BLOCK_PROPERTY_DATA = RegistryHelper.registerBlockDataType("properties");
    public static final ResourceKey<Registry<BlockSoundData>> BLOCK_SOUND_DATA = RegistryHelper.registerBlockDataType("sound_group");
    public static final ResourceKey<Registry<ChargedMobDropData>> CHARGED_MOB_DROP_DATA = RegistryHelper.registerEntityDataType("charged_mob_drop");
    public static final ResourceKey<Registry<PlayerLootTableData>> PLAYER_LOOT_TABLE_DATA = RegistryHelper.registerEntityDataType("player_loot_table");
    public static final ResourceKey<Registry<EntitySpawnerData.EntitySpawnerConfig>> ENTITY_SPAWNER_CONFIG_DATA = RegistryHelper.registerEntityDataType("entity_spawner_config");

    // endregion

    public static void registerAll(IEventBus eventBus) {

        if (Chrysalis.IS_DEBUG) {
            if (Chrysalis.registerTestItems) CItems.registerTestItems(eventBus);
            if (Chrysalis.registerExampleRegistry) CExampleRegistry.init(eventBus);
        }

        CItems.register(eventBus);
        CDataComponents.register(eventBus);
        CCreativeModeTabs.register(eventBus);
        CSoundEvents.register(eventBus);
        CParticles.register(eventBus);
        CEntities.register(eventBus);
        CMemoryModules.register(eventBus);
        CDamageTypes.register();
        CAttributes.register(eventBus);
        CStatusEffects.register(eventBus);
        CGameRules.register();
        CCriteriaTriggers.register(eventBus);
    }

    @SuppressWarnings("unused")
    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistry {

        public static final KeyMapping PANORAMIC_SCREENSHOT_KEY = new KeyMapping("key.chrysalis.panoramic_screenshot", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F6, "key.categories.misc");

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register(CEntities.SEAT.get(), SeatRenderer::new);
            EntityRenderers.register(CEntities.ENTITY_SPAWNER.get(), EntitySpawnerRenderer::new);
            EntityRenderers.register(CEntities.ENCOUNTER_SPAWNER.get(), EncounterSpawnerRenderer::new);

            try {
                Toolkit.getDefaultToolkit().getSystemClipboard();
            } catch (HeadlessException exception) {
                Chrysalis.LOGGER.warn("java.awt.headless property was not set properly!");
            }

            DebugHelper.sendInitializedMessage(Chrysalis.LOGGER, Chrysalis.CHRYSALIS_VERSION, true);
        }

        @SubscribeEvent
        public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(CParticles.MEMORY_FLAME.get(), FlameParticle.Provider::new);
            event.registerSpriteSet(CParticles.RADIANCE.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.ARTHROPOD_SIGHT.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.CREEPER_SIGHT.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.ENDER_SIGHT.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.COLORED_DUST_PLUME.get(), ColoredDustPlumeParticle.Provider::new);
            event.registerSpriteSet(CParticles.COLORED_DIRECTIONAL_DUST.get(), ColoredDirectionalDustParticle.Provider::new);
            event.registerSpriteSet(CParticles.COLORED_PORTAL.get(), ColoredPortalParticle.Provider::new);
            event.registerSpriteSet(CParticles.DUST_EXPLOSION.get(), DustExplosionParticle.Provider::new);
            event.registerSpriteSet(CParticles.ROTATING_DUST.get(), RotatingDustParticle.Provider::new);
            event.registerSpriteSet(CParticles.SPARKLE.get(), SparkleParticle.Provider::new);
            event.registerSpriteSet(CParticles.SPARK.get(), SparkParticle.Provider::new);
            event.registerSpriteSet(CParticles.SMALL_PULSATION.get(), PulsationParticle.SmallProvider::new);
            event.registerSpriteSet(CParticles.LARGE_PULSATION.get(), PulsationParticle.LargeProvider::new);
        }
    }
}