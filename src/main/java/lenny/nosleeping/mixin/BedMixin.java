package lenny.nosleeping.mixin;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BedBlock.class)
public class BedMixin {

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    protected void injectOnUseMethod(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (player instanceof ServerPlayerEntity) {
            ServerAdvancementLoader serverAdvancementLoader = player.getEntityWorld().getServer().getAdvancementLoader();

            var players = world.getPlayers();

            AdvancementEntry killDragon = serverAdvancementLoader.get(Identifier.tryParse("minecraft:end/kill_dragon"));
            AdvancementEntry killWither = serverAdvancementLoader.get(Identifier.tryParse("minecraft:nether/create_beacon"));
            AdvancementEntry defeatRaid = serverAdvancementLoader.get(Identifier.tryParse("minecraft:adventure/hero_of_the_village"));

            for (int i = 0; i < players.size(); i++) {

                if (!((ServerPlayerEntity) player).getAdvancementTracker().getProgress(killDragon).isDone()
                        && !((ServerPlayerEntity) player).getAdvancementTracker().getProgress(killWither).isDone()
                        && !((ServerPlayerEntity) player).getAdvancementTracker().getProgress(defeatRaid).isDone()) {
                    Text literal = Text.of("You may not rest yet, there is still work to be done!");
                    player.sendMessage(literal, false);
                    cir.setReturnValue(ActionResult.FAIL);
                    cir.cancel();
                    i = players.size();
                }
            }
        }
    }
}
