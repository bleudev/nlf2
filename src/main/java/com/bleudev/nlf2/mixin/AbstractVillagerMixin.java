package com.bleudev.nlf2.mixin;

import com.bleudev.nlf2.client.util.MixinSharedHelperKt;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin extends AgeableMob {
    protected AbstractVillagerMixin(EntityType<? extends AgeableMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        MixinSharedHelperKt.registerAvoidGoal(this, this.goalSelector);
    }
}
