package com.bleudev.nlf2.mixin;

import com.bleudev.nlf2.client.util.MixinSharedHelperKt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({
    AbstractCow.class,
    Pig.class,
    Sheep.class,
    Chicken.class,
})
public abstract class AnimalsMixin extends Animal {
    protected AnimalsMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void avoidPlayer(CallbackInfo ci) {
        MixinSharedHelperKt.registerAvoidGoal(this, this.goalSelector);
    }
}
