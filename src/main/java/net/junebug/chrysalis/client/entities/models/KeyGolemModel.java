package net.junebug.chrysalis.client.entities.models;

import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.client.entities.animations.KeyGolemAnimations;
import net.junebug.chrysalis.client.entities.rendering.render_states.CLivingEntityRenderState;
import net.junebug.chrysalis.common.entities.custom_entities.mobs.key_golem.KeyGolem;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class KeyGolemModel extends EntityModel<CLivingEntityRenderState> {

    /**
     * The model for key golems.
     **/

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Chrysalis.resourceLocationId("key_golem"), "main");

    public KeyGolemModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static LayerDefinition createBodyLayer() {

        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition keyGolem = partDefinition.addOrReplaceChild("Key_Golem", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = keyGolem.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
        .texOffs(0, 16).addBox(-1.0F, -18.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
        .texOffs(8, 20).addBox(1.0F, -17.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
        .texOffs(8, 24).addBox(1.0F, -14.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition eyes = body.addOrReplaceChild("Eyes", CubeListBuilder.create(), PartPose.offset(0.0F, -3.5F, -4.1F));

        PartDefinition normalEyes = eyes.addOrReplaceChild("Normal_Eyes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 0.0F));
        normalEyes.addOrReplaceChild("Right_Eye", CubeListBuilder.create().texOffs(33, 11).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0005F)), PartPose.offset(-2.0F, 0.0F, 0.0F));
        normalEyes.addOrReplaceChild("Left_Eye", CubeListBuilder.create().texOffs(33, 13).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0005F)), PartPose.offset(2.0F, 0.0F, 0.0F));

        PartDefinition wideEyes = eyes.addOrReplaceChild("Wide_Eyes", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));
        wideEyes.addOrReplaceChild("Right_Wide_Eye", CubeListBuilder.create().texOffs(38, 11).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0006F)), PartPose.offset(-2.0F, 0.0F, 0.0F));
        wideEyes.addOrReplaceChild("Left_Wide_Eye", CubeListBuilder.create().texOffs(38, 14).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0006F)), PartPose.offset(2.0F, 0.0F, 0.0F));

        body.addOrReplaceChild("Mouth", CubeListBuilder.create().texOffs(33, 15).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0005F)), PartPose.offset(0.0F, -1.5F, -4.1F));

        keyGolem.addOrReplaceChild("Right_Leg", CubeListBuilder.create().texOffs(18, 20).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -5.0F, 0.0F));
        keyGolem.addOrReplaceChild("Left_Leg", CubeListBuilder.create().texOffs(30, 20).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -5.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void setupAnim(@NotNull CLivingEntityRenderState renderState) {

        super.setupAnim(renderState);
        this.root().setPos(this.root().x, this.root().y - 0.1F, this.root().z);

        if (!(CLivingEntityRenderState.livingEntity instanceof KeyGolem keyGolem)) return;

        this.animate(keyGolem.getIdleAnimation(), KeyGolemAnimations.IDLE, renderState.ageInTicks); // Idle Animation
        this.animate(keyGolem.getNoveltyAnimation(), KeyGolemAnimations.NOVELTY, renderState.ageInTicks); // Novelty Animation
        this.animate(keyGolem.fallAsleepAnimationState, KeyGolemAnimations.FALL_ASLEEP, renderState.ageInTicks); // Fall Asleep Animation
        this.animate(keyGolem.sleepAnimationState, KeyGolemAnimations.SLEEP, renderState.ageInTicks); // Sleep Animation
        this.animate(keyGolem.wakeUpAnimationState, KeyGolemAnimations.WAKE_UP, renderState.ageInTicks); // Wake Up Animation
        this.animate(keyGolem.noticeAnimationState, KeyGolemAnimations.NOTICE, renderState.ageInTicks); // Notice Animation
        this.animate(keyGolem.carryAnimationState, KeyGolemAnimations.CARRY, renderState.ageInTicks); // Carry Animation
        this.animate(keyGolem.spawnAnimationState, KeyGolemAnimations.SPAWN, renderState.ageInTicks); // Spawn Animation
        this.animate(keyGolem.despawnAnimationState, KeyGolemAnimations.DESPAWN, renderState.ageInTicks); // Despawn Animation

        AnimationDefinition walkAnimation;
        if (keyGolem.isSprinting()) walkAnimation = KeyGolemAnimations.RUN;
        else walkAnimation = KeyGolemAnimations.WALK;

        this.animateWalk(walkAnimation, renderState.walkAnimationPos, renderState.walkAnimationSpeed, 9.0F, 100.0F); // Walk Animation
        if (keyGolem.isRidingPlayer()) this.applyStatic(KeyGolemAnimations.FLIP); // Flip Animation
        if (!this.canShowWideEyes(keyGolem)) this.applyStatic(KeyGolemAnimations.HIDE_WIDE_EYES); // Hide Wide Eyes Animation
    }

    private boolean canShowWideEyes(KeyGolem keyGolem) {
        return keyGolem.isSprinting() || keyGolem.getNoveltyAnimation().isStarted() || keyGolem.fallAsleepAnimationState.isStarted() ||
        keyGolem.wakeUpAnimationState.isStarted() || keyGolem.noticeAnimationState.isStarted() || keyGolem.carryAnimationState.isStarted();
    }
}