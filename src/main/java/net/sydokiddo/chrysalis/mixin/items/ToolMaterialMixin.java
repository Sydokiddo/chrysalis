package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.sydokiddo.chrysalis.common.misc.CTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(ToolMaterial.class)
public abstract class ToolMaterialMixin {

    @Shadow protected abstract Item.Properties applyCommonProperties(Item.Properties properties);
    @Shadow protected abstract ItemAttributeModifiers createSwordAttributes(float attackDamage, float attackSpeed);

    /**
     * Any blocks in the mineable/sword tags will be mined faster with swords.
     **/

    @Inject(method = "applySwordProperties", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$createSwordProperties(Item.Properties properties, float attackDamage, float attackSpeed, CallbackInfoReturnable<Item.Properties> cir) {

        HolderGetter<Block> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);

        cir.setReturnValue(this.applyCommonProperties(properties).component(DataComponents.TOOL,
        new Tool(List.of(
            Tool.Rule.minesAndDrops(holderGetter.getOrThrow(CTags.MINEABLE_WITH_SWORDS_DROPS_BLOCK), 15.0F),
            Tool.Rule.overrideSpeed(holderGetter.getOrThrow(CTags.MINEABLE_WITH_SWORDS_DOES_NOT_DROP_BLOCK), 1.5F)
        ), 1.0F, 2))
        .attributes(this.createSwordAttributes(attackDamage, attackSpeed)));
    }
}