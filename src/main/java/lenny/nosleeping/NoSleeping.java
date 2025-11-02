package lenny.nosleeping;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoSleeping implements ModInitializer {
    public static final String MOD_ID = "no-sleeping";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        SuspiciousSubstance.initialize();

        LOGGER.info("Hello Fabric world!");

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            BlockState state = world.getBlockState(pos);

            // Manual spectator check is necessary because AttackBlockCallbacks fire before the spectator check
            if (!player.isSpectator() && player.getMainHandStack().isEmpty() && state.isToolRequired() && world instanceof ServerWorld serverWorld) {
                player.damage(serverWorld, world.getDamageSources().generic(), 1.0F);
            }

            return ActionResult.PASS;
        });

        SheepShearCallback.EVENT.register((player, sheep) -> {
            sheep.setSheared(true);

            // Create diamond item entity at sheep's position.
            ItemStack stack = new ItemStack(Items.DIAMOND);
            ItemEntity itemEntity = new ItemEntity(player.getEntityWorld(), sheep.getX(), sheep.getY(), sheep.getZ(), stack);
            player.getEntityWorld().spawnEntity(itemEntity);

            return ActionResult.FAIL;
        });
    }
}