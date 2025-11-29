package lenny.nosleeping.mixin;

import lenny.nosleeping.PhantomRevengeGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomEntity.class)
public class PhantomEntityMixin extends MobEntity {
    protected PhantomEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"), cancellable = true)
    protected void injectOnUseMethod(CallbackInfo ci) {
        MobEntity thisObject = (MobEntity) (Object) this;
        thisObject.clearGoalsAndTasks();
        PhantomEntity phantomEntity = (PhantomEntity) thisObject;

        this.goalSelector.add(1, phantomEntity.new StartAttackGoal());
        this.goalSelector.add(2, phantomEntity.new SwoopMovementGoal());
        this.goalSelector.add(3, phantomEntity.new CircleMovementGoal());
        this.targetSelector.add(1, new PhantomRevengeGoal(phantomEntity));

        ci.cancel();
    }
}

