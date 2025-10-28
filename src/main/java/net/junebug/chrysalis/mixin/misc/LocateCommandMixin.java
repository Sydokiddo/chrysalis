package net.junebug.chrysalis.mixin.misc;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocateCommand.class)
public class LocateCommandMixin {

    @Shadow @Final private static DynamicCommandExceptionType ERROR_STRUCTURE_NOT_FOUND;

    /**
     * If structures cannot generate in the world, the /locate command automatically throws the structure not found error instead of repeatedly trying to find the structure and getting caught in a lag loop.
     **/

    @Inject(method = "locateStructure", at = @At(value = "HEAD"))
    private static void chrysalis$fixLocateStructureLag(CommandSourceStack commandSourceStack, ResourceOrTagKeyArgument.Result<Structure> structure, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        if (!commandSourceStack.getLevel().structureManager().shouldGenerateStructures()) throw ERROR_STRUCTURE_NOT_FOUND.create(structure.asPrintable());
    }
}