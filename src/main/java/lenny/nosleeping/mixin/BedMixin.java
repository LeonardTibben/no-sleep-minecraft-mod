package lenny.nosleeping.mixin;

import lenny.nosleeping.SheepShearCallback;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedMixin {

    @Inject(method = "onUse", at = @At("HEAD"))
    protected void injectOnUseMethod(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        System.out.println("BED USED");

        System.out.println("DIT IS SUPER COOL");

        AdvancementEntry advancementEntry =

        if (player instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) player).getAdvancementTracker().getProgress(advancementEntry).isDone();
        }
    }
}


// -Dfabric.dli.config=/home/leonard/Projects/minecraft/.gradle/loom-cache/launch.cfg
//-Dfabric.dli.env=client
//-Dfabric.dli.main=net.fabricmc.loader.impl.launch.knot.KnotClient
//-XX:+AllowEnhancedClassRedefinition
//-javaagent:/home/leonard/.gradle/caches/modules-2/files-2.1/net.fabricmc/sponge-mixin/0.16.5+mixin.0.8.7/80fc3a9f592673cea87f4cd702f87991c6c9fe4d/sponge-mixin-0.16.5+mixin.0.8.7.jar
