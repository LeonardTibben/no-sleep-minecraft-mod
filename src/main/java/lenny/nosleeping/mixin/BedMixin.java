package lenny.nosleeping.mixin;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.minecraft.block.BedBlock.PART;
import static net.minecraft.block.HorizontalFacingBlock.FACING;

@Mixin(BedBlock.class)
public class BedMixin {

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    protected void injectOnUseMethod(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (player instanceof ServerPlayerEntity) {

            ServerAdvancementLoader serverAdvancementLoader = player.getEntityWorld().getServer().getAdvancementLoader();

            var players = world.getPlayers();

            AdvancementEntry killDragon = serverAdvancementLoader.get(Identifier.tryParse("minecraft:end/kill_dragon"));

            for (int i = 0; i < players.size(); i++) {

                if (!((ServerPlayerEntity) player).getAdvancementTracker().getProgress(killDragon).isDone()
                ) {
                    ((ServerPlayerEntity) player).setSpawnPointFrom((ServerPlayerEntity) player);
                    Text literal = Text.of("You may not rest yet, there is still work to be done!");
                    player.sendMessage(literal, false);
                    cir.setReturnValue(ActionResult.FAIL);

                    ((ServerPlayerEntity) player).setSpawnPoint(new ServerPlayerEntity.Respawn(WorldProperties.SpawnPoint.create(player.getEntityWorld().getRegistryKey(), pos, player.getYaw(), player.getPitch()), false), false);
                    cir.cancel();
                    i = players.size();
                }
            }
        }
    }
}
