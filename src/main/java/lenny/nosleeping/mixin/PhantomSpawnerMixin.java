package lenny.nosleeping.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Unique
    private int cooldown;

    @Inject(method = "spawn", at = @At("HEAD"), cancellable = true)
    protected void injectOnUseMethod(ServerWorld world, boolean spawnMonsters, CallbackInfo ci) {
        if (spawnMonsters) {
            if (world.getMoonPhase() == 4) {
                Random random = world.random;
                this.cooldown--;
                if (this.cooldown <= 0) {
                    this.cooldown = this.cooldown + (60 + random.nextInt(60)) * 20;
                    if (world.getAmbientDarkness() >= 5 || !world.getDimension().hasSkyLight()) {
                        for (ServerPlayerEntity serverPlayerEntity : world.getPlayers()) {
                            if (!serverPlayerEntity.isSpectator()) {
                                BlockPos blockPos = serverPlayerEntity.getBlockPos();
                                if (!world.getDimension().hasSkyLight() || blockPos.getY() >= world.getSeaLevel() && world.isSkyVisible(blockPos)) {
                                    LocalDifficulty localDifficulty = world.getLocalDifficulty(blockPos);
                                    if (localDifficulty.isHarderThan(random.nextFloat() * 3.0F)) {
                                            BlockPos blockPos2 = blockPos.up(20 + random.nextInt(15)).east(-10 + random.nextInt(40)).south(-10 + random.nextInt(40));
                                            BlockState blockState = world.getBlockState(blockPos2);
                                            FluidState fluidState = world.getFluidState(blockPos2);
                                            if (SpawnHelper.isClearForSpawn(world, blockPos2, blockState, fluidState, EntityType.PHANTOM)) {
                                                EntityData entityData = null;
                                                int k = 1 + random.nextInt(localDifficulty.getGlobalDifficulty().getId() + 1);

                                                for (int l = 0; l < k; l++) {
                                                    PhantomEntity phantomEntity = EntityType.PHANTOM.create(world, SpawnReason.NATURAL);
                                                    if (phantomEntity != null) {
                                                        phantomEntity.refreshPositionAndAngles(blockPos2, 0.0F, 0.0F);
                                                        entityData = phantomEntity.initialize(world, localDifficulty, SpawnReason.NATURAL, entityData);
                                                        world.spawnEntityAndPassengers(phantomEntity);
                                                    }
                                                }
                                            }
//                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        ci.cancel();
    }
}
