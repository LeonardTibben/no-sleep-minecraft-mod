package lenny.nosleeping;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface SheepShearCallback {
    Event<SheepShearCallback> EVENT = EventFactory.createArrayBacked(SheepShearCallback.class,
            (listeners) -> (player, sheep) -> {
                for (SheepShearCallback listener : listeners) {
                    ActionResult result = listener.interact(player, sheep);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player, SheepEntity sheep);
}

