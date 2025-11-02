package lenny.nosleeping.mixin;

import lenny.nosleeping.SheepShearCallback;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public class SheepEntityMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SheepEntity;sheared(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/sound/SoundCategory;Lnet/minecraft/item/ItemStack;)V"), method = "interactMob", cancellable = true)

    private void onShear(final PlayerEntity player, final Hand hand, final CallbackInfoReturnable<ActionResult> info) {
        ActionResult result = SheepShearCallback.EVENT.invoker().interact(player, (SheepEntity) (Object) this);

        if (result == ActionResult.FAIL) {
            info.setReturnValue(result);
        }
    }
}

