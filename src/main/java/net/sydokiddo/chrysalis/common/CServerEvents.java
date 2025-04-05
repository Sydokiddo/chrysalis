package net.sydokiddo.chrysalis.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.entities.custom_entities.spawners.entity_spawner.EntitySpawnerData;
import net.sydokiddo.chrysalis.common.items.CDataComponents;
import net.sydokiddo.chrysalis.common.misc.CGameRules;
import net.sydokiddo.chrysalis.common.misc.CSoundEvents;
import net.sydokiddo.chrysalis.common.misc.CTags;
import net.sydokiddo.chrysalis.util.blocks.codecs.BlockPropertyData;
import net.sydokiddo.chrysalis.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.util.entities.codecs.ChargedMobDropData;
import net.sydokiddo.chrysalis.util.entities.codecs.PlayerLootTableData;
import net.sydokiddo.chrysalis.util.entities.interfaces.EncounterMusicMob;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.util.sounds.codecs.BlockSoundData;
import net.sydokiddo.chrysalis.util.sounds.music.*;
import net.sydokiddo.chrysalis.util.technical.commands.*;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class CServerEvents {

    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class GameEventBus {

        @SubscribeEvent
        private static void onServerAboutToStart(ServerAboutToStartEvent event) {
            Chrysalis.registryAccess = event.getServer().registryAccess();
        }

        @SubscribeEvent
        private static void onServerPreTick(ServerTickEvent.Pre event) {

            if (MusicTracker.onServer.ticks > 0) {
                MusicTracker.onServer.ticks -= 1;
                return;
            }

            List<ServerPlayer> list = event.getServer().getPlayerList().getPlayers();

            for (ServerPlayer serverPlayer : list) {
                if (!serverPlayer.isAlive()) return;
                MusicTracker.onServer.checkAllStructures(serverPlayer.serverLevel(), serverPlayer);
            }

            MusicTracker.onServer.ticks = 250;
        }

        @SubscribeEvent
        private static void onServerStopping(ServerStoppingEvent event) {
            MusicTracker.onClient.clearMusic(false);
            if (!MusicTracker.onServer.playerStructures.isEmpty()) MusicTracker.onServer.playerStructures.clear();
        }

        @SubscribeEvent
        private static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                EventHelper.clearMusicOnServer(serverPlayer, false);
                MusicTracker.onServer.playerStructures.remove(serverPlayer);
            }
        }

        private static boolean shouldClearMusic = false;

        @SubscribeEvent
        private static void onPlayerPreTick(PlayerTickEvent.Pre event) {

            if (event.getEntity().level().isClientSide()) return;
            Optional<UUID> encounteredMobUuid = EntityDataHelper.getEncounteredMobUUID(event.getEntity());

            if (shouldClearMusic && event.getEntity() instanceof ServerPlayer serverPlayer) {
                EntityDataHelper.setEncounteredMobUUID(serverPlayer, null);
                EventHelper.clearMusicOnServer(serverPlayer, true);
                shouldClearMusic = false;
            }

            if (encounteredMobUuid.isPresent()) {

                List<? extends Mob> nearbyEncounteredMobs = event.getEntity().level().getEntitiesOfClass(Mob.class, event.getEntity().getBoundingBox().inflate(128.0D), entity -> {
                    boolean defaultReturnValue = entity instanceof EncounterMusicMob encounterMusicMob && entity.isAlive() && entity.getUUID() == encounteredMobUuid.get() && entity.distanceTo(event.getEntity()) <= encounterMusicMob.chrysalis$getFinalEncounterMusicRange();
                    if (entity.getType().is(CTags.ALWAYS_PLAYS_ENCOUNTER_MUSIC)) return defaultReturnValue;
                    else return defaultReturnValue && entity.getTarget() != null;
                });

                if (nearbyEncounteredMobs.isEmpty()) shouldClearMusic = true;
            }
        }

        @SubscribeEvent
        private static void onPlayerPostTick(PlayerTickEvent.Post event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer && serverPlayer.getItemBySlot(EquipmentSlot.HEAD).is(ItemTags.SKULLS) && serverPlayer.tickCount % 20 == 0) EntityDataHelper.updateCurrentShader(serverPlayer);
        }

        @SubscribeEvent
        private static void mobGriefingEvents(EntityMobGriefingEvent event) {
            if (!CConfigOptions.REWORKED_MOB_GRIEFING.get()) return;
            if (event.getEntity() instanceof Allay allay && allay.getServer() != null) event.setCanGrief(allay.getServer().getGameRules().getBoolean(CGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Evoker evoker && evoker.getServer() != null) event.setCanGrief(evoker.getServer().getGameRules().getBoolean(CGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Fox fox && fox.getServer() != null) event.setCanGrief(fox.getServer().getGameRules().getBoolean(CGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Piglin piglin && piglin.getServer() != null) event.setCanGrief(piglin.getServer().getGameRules().getBoolean(CGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Rabbit rabbit && rabbit.getServer() != null) event.setCanGrief(rabbit.getServer().getGameRules().getBoolean(CGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Sheep sheep && sheep.getServer() != null) event.setCanGrief(sheep.getServer().getGameRules().getBoolean(CGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof SnowGolem snowGolem && snowGolem.getServer() != null) event.setCanGrief(snowGolem.getServer().getGameRules().getBoolean(CGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Villager villager && villager.getServer() != null) event.setCanGrief(villager.getServer().getGameRules().getBoolean(CGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof EnderDragon enderDragon && enderDragon.getServer() != null) event.setCanGrief(enderDragon.getServer().getGameRules().getBoolean(CGameRules.RULE_DRAGON_GRIEFING));
            if (event.getEntity() instanceof WitherBoss wither && wither.getServer() != null) event.setCanGrief(wither.getServer().getGameRules().getBoolean(CGameRules.RULE_WITHER_GRIEFING));
        }

        @SubscribeEvent
        private static void commandRegistry(RegisterCommandsEvent event) {
            CameraShakeCommand.register(event.getDispatcher());
            ClearSpawnpointCommand.register(event.getDispatcher());
            CooldownCommand.register(event.getDispatcher(), event.getBuildContext());
            CoordinatesCommand.register(event.getDispatcher());
            DisenchantCommand.register(event.getDispatcher(), event.getBuildContext());
            DurabilityCommand.register(event.getDispatcher());
            ExplosionCommand.register(event.getDispatcher());
            FoodCommand.register(event.getDispatcher());
            GameEventCommand.register(event.getDispatcher());
            HatCommand.register(event.getDispatcher());
            HealCommand.register(event.getDispatcher());
            MusicCommand.register(event.getDispatcher());
            OxygenCommand.register(event.getDispatcher());
            ShowcaseCommand.register(event.getDispatcher());
            SurfaceCommand.register(event.getDispatcher());
            TrimCommand.register(event.getDispatcher(), event.getBuildContext());
            UuidCommand.register(event.getDispatcher());
            VelocityCommand.register(event.getDispatcher());
        }
    }

    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEventBus {

        @SubscribeEvent
        private static void datapackRegistry(DataPackRegistryEvent.NewRegistry event) {
            event.dataPackRegistry(CRegistry.BLOCK_PROPERTY_DATA, BlockPropertyData.CODEC);
            event.dataPackRegistry(CRegistry.BLOCK_SOUND_DATA, BlockSoundData.CODEC);
            event.dataPackRegistry(CRegistry.CHARGED_MOB_DROP_DATA, ChargedMobDropData.CODEC);
            event.dataPackRegistry(CRegistry.PLAYER_LOOT_TABLE_DATA, PlayerLootTableData.CODEC);
            event.dataPackRegistry(CRegistry.ENTITY_SPAWNER_CONFIG_DATA, EntitySpawnerData.EntitySpawnerConfig.CODEC);
        }

        @SubscribeEvent
        private static void modifyDefaultItemComponents(ModifyDefaultComponentsEvent event) {
            event.modify(Items.DRAGON_EGG, components -> components.set(CDataComponents.IMMUNE_TO_ALL_DAMAGE.get(), Unit.INSTANCE).set(CDataComponents.IMMUNE_TO_DESPAWNING.get(), Unit.INSTANCE));
            event.modify(Items.NETHER_STAR, components -> components.set(CDataComponents.INCREASED_DESPAWN_TIME.get(), Unit.INSTANCE));
            event.modify(Items.SHIELD, components -> components.set(DataComponents.EQUIPPABLE, ItemHelper.setOffhandEquippableComponent(CSoundEvents.EQUIP_SHIELD, true)));
            event.modify(Items.TOTEM_OF_UNDYING, components -> components.set(DataComponents.EQUIPPABLE, ItemHelper.setOffhandEquippableComponent(CSoundEvents.EQUIP_TOTEM_OF_UNDYING, false)));
            event.modify(Items.CARVED_PUMPKIN, components -> components.set(DataComponents.EQUIPPABLE, ItemHelper.setHeadOverlayEquippableComponent(CSoundEvents.EQUIP_CARVED_PUMPKIN, ResourceLocation.withDefaultNamespace("misc/pumpkinblur"))));
            event.modify(Items.SKELETON_SKULL, components -> components.set(DataComponents.EQUIPPABLE, ItemHelper.setHeadEquippableComponent(CSoundEvents.EQUIP_SKELETON_SKULL)));
            event.modify(Items.WITHER_SKELETON_SKULL, components -> components.set(DataComponents.EQUIPPABLE, ItemHelper.setHeadEquippableComponent(CSoundEvents.EQUIP_WITHER_SKELETON_SKULL)));
            event.modify(Items.PLAYER_HEAD, components -> components.set(DataComponents.EQUIPPABLE, ItemHelper.setHeadEquippableComponent(CSoundEvents.EQUIP_PLAYER_HEAD)));
            event.modify(Items.ZOMBIE_HEAD, components -> components.set(DataComponents.EQUIPPABLE, ItemHelper.setHeadEquippableComponent(CSoundEvents.EQUIP_ZOMBIE_HEAD)));
            event.modify(Items.CREEPER_HEAD, components -> components.set(DataComponents.EQUIPPABLE, ItemHelper.setHeadEquippableComponent(CSoundEvents.EQUIP_CREEPER_HEAD)));
            event.modify(Items.PIGLIN_HEAD, components -> components.set(DataComponents.EQUIPPABLE, ItemHelper.setHeadEquippableComponent(CSoundEvents.EQUIP_PIGLIN_HEAD)));
            event.modify(Items.DRAGON_HEAD, components -> components.set(DataComponents.EQUIPPABLE, ItemHelper.setHeadEquippableComponent(CSoundEvents.EQUIP_DRAGON_HEAD)));
        }
    }
}