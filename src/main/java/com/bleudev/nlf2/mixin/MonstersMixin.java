package com.bleudev.nlf2.mixin;

import com.bleudev.nlf2.client.util.MixinSharedHelperKt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({
    Raider.class,
    Zombie.class,
    AbstractSkeleton.class,
    EnderMan.class,
    Creeper.class,
    Spider.class,
})
public class MonstersMixin extends Monster {
    protected MonstersMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void avoidPlayer(CallbackInfo ci) {
        MixinSharedHelperKt.registerAvoidGoal(this, this.goalSelector);
    }
}
