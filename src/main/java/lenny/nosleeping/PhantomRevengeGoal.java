package lenny.nosleeping;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Comparator;
import java.util.List;

public class PhantomRevengeGoal extends Goal {
    private final TargetPredicate PLAYERS_IN_RANGE_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(64.0);
    private int delay = toGoalTicks(20);
    private final PhantomEntity phantom;

    public PhantomRevengeGoal(PhantomEntity phantom) {
        this.phantom = phantom;
    }

    @Override
    public boolean canStart() {
        if (phantom.getLastAttackedTime() == 0) {
            System.out.println(phantom.getLastAttackedTime());
            return false;
        }
        if (this.delay > 0) {
            this.delay--;
        } else {
            this.delay = toGoalTicks(60);
            ServerWorld serverWorld = castToServerWorld(phantom.getEntityWorld());
            List<PlayerEntity> list = serverWorld.getPlayers(
                    this.PLAYERS_IN_RANGE_PREDICATE, phantom, phantom.getBoundingBox().expand(16.0, 64.0, 16.0)
            );
            if (!list.isEmpty()) {
                list.sort(Comparator.comparing(Entity::getY).reversed());

                for (PlayerEntity playerEntity : list) {
                    if (phantom.testTargetPredicate(serverWorld, playerEntity, TargetPredicate.DEFAULT)) {
                        phantom.setTarget(playerEntity);
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
