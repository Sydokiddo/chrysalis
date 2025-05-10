package net.sydokiddo.chrysalis.common;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.Unit;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.entities.custom_entities.spawners.entity_spawner.EntitySpawnerData;
import net.sydokiddo.chrysalis.common.items.CDataComponents;
import net.sydokiddo.chrysalis.common.misc.CGameEvents;
import net.sydokiddo.chrysalis.common.misc.CGameRules;
import net.sydokiddo.chrysalis.common.misc.CSoundEvents;
import net.sydokiddo.chrysalis.common.misc.CTags;
import net.sydokiddo.chrysalis.util.blocks.codecs.BlockConversionData;
import net.sydokiddo.chrysalis.util.blocks.codecs.BlockPropertyData;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
import net.sydokiddo.chrysalis.util.helpers.EntityHelper;
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
            Optional<UUID> encounteredMobUuid = EntityHelper.getEncounteredMobUUID(event.getEntity());

            if (shouldClearMusic && event.getEntity() instanceof ServerPlayer serverPlayer) {
                EntityHelper.setEncounteredMobUUID(serverPlayer, null);
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
            if (event.getEntity() instanceof ServerPlayer serverPlayer && serverPlayer.getItemBySlot(EquipmentSlot.HEAD).is(ItemTags.SKULLS) && serverPlayer.tickCount % 20 == 0) EntityHelper.updateCurrentShader(serverPlayer);
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

        @SubscribeEvent
        private static void onUseItemOnBlock(UseItemOnBlockEvent event) {

            if (Chrysalis.registryAccess == null) return;

            Optional<BlockConversionData> optional = Chrysalis.registryAccess.lookupOrThrow(CRegistry.BLOCK_CONVERSION_DATA).stream().filter
            (
                codec ->
                codec.startingBlocks().contains(event.getLevel().getBlockState(event.getPos()).getBlockHolder()) &&
                codec.usedItems().contains(event.getItemStack().getItemHolder())
            ).findFirst();

            if (optional.isPresent() && event.getPlayer() != null) {

                if (optional.get().forTesting() && !Chrysalis.IS_DEBUG || event.getHand().equals(InteractionHand.MAIN_HAND) && event.getPlayer().getOffhandItem().is(Tags.Items.TOOLS_SHIELD) && !event.getPlayer().isSecondaryUseActive()) return;

                switch (optional.get().sneakingRequirement()) {

                    case ComponentHelper.noneString, ComponentHelper.nullString -> {}
                    case "sneaking" -> {
                        if (!event.getPlayer().isShiftKeyDown()) return;
                    }
                    case "not_sneaking" -> {
                        if (event.getPlayer().isShiftKeyDown()) return;
                    }

                    default -> Chrysalis.LOGGER.warn("Unknown Sneaking Requirement: '{}'", optional.get().sneakingRequirement());
                }

                event.getLevel().setBlockAndUpdate(event.getPos(), optional.get().resultingBlock().value().withPropertiesOf(event.getLevel().getBlockState(event.getPos())));

                SoundEvent soundEvent = optional.get().soundEvent().value();
                Holder<GameEvent> gameEvent = optional.get().gameEvent();
                BlockState particleState = optional.get().particleState().value().defaultBlockState();

                if (soundEvent != SoundEvents.EMPTY) event.getLevel().playSound(null, event.getPos(), soundEvent, SoundSource.BLOCKS, 1.0F, optional.get().randomizeSoundPitch() ? 0.8F + event.getLevel().getRandom().nextFloat() * 0.4F : 1.0F);
                if (gameEvent != CGameEvents.EMPTY) event.getLevel().gameEvent(gameEvent, event.getPos(), GameEvent.Context.of(event.getPlayer(), event.getLevel().getBlockState(event.getPos())));

                if (event.getFace() != null) {
                    if (particleState.getRenderShape() != RenderShape.INVISIBLE) ParticleUtils.spawnParticlesOnBlockFace(event.getLevel(), event.getPos(), new BlockParticleOption(ParticleTypes.BLOCK, particleState), UniformInt.of(3, 5), event.getFace(), () -> ParticleUtils.getRandomSpeedRanges(event.getLevel().getRandom()), 0.55D);
                    if (optional.get().returnedItem() != Items.AIR) Block.popResourceFromFace(event.getLevel(), event.getPos(), event.getFace(), new ItemStack(optional.get().returnedItem()));
                }

                switch (optional.get().useInteraction()) {

                    case ComponentHelper.noneString, ComponentHelper.nullString -> {}
                    case "consume_item" -> {
                        if (!event.getItemStack().getCraftingRemainder().isEmpty()) event.getPlayer().setItemInHand(event.getHand(), ItemUtils.createFilledResult(event.getItemStack(), event.getPlayer(), event.getItemStack().getCraftingRemainder()));
                        else event.getItemStack().consume(1, event.getPlayer());
                    }
                    case "consume_durability" -> event.getItemStack().hurtAndBreak(1, event.getPlayer(), LivingEntity.getSlotForHand(event.getHand()));

                    default -> Chrysalis.LOGGER.warn("Unknown Use Interaction: {}", optional.get().useInteraction());
                }

                event.getPlayer().awardStat(Stats.ITEM_USED.get(event.getItemStack().getItem()));
                if (event.getPlayer() instanceof ServerPlayer serverPlayer) CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, event.getPos(), event.getItemStack());

                event.cancelWithResult(InteractionResult.SUCCESS);
            }
        }
    }

    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEventBus {

        @SubscribeEvent
        private static void resourcePackRegistry(AddPackFindersEvent event) {
            event.addPackFinders(Chrysalis.resourceLocationId("resourcepacks/no_carved_pumpkin_overlay"), PackType.CLIENT_RESOURCES, Component.translatable("pack.chrysalis.no_carved_pumpkin_overlay.name"), PackSource.BUILT_IN, false, Pack.Position.TOP);
        }

        @SubscribeEvent
        private static void dataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
            event.dataPackRegistry(CRegistry.BLOCK_PROPERTY_DATA, BlockPropertyData.CODEC);
            event.dataPackRegistry(CRegistry.BLOCK_SOUND_DATA, BlockSoundData.CODEC);
            event.dataPackRegistry(CRegistry.BLOCK_CONVERSION_DATA, BlockConversionData.CODEC);
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