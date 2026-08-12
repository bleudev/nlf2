package com.bleudev.nlf2.custom.world.effect

import com.bleudev.nine_lifes.custom.NineLifesDamageTypes
import com.bleudev.nlf2.custom.Nlf2MobEffects
import com.bleudev.nlf2.util.explode
import com.bleudev.nlf2.util.hurtUnknown
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

class GlidingEffect : MobEffect(MobEffectCategory.BENEFICIAL, 0xff22ff) {
    override fun onEffectAdded(mob: LivingEntity, amplifier: Int) {
        super.onEffectAdded(mob, amplifier)
        (mob as? ServerPlayer)?.let { player ->
            if (player.random.nextFloat() <= .1f) {
                player.level().explode(player.position(), 1f, NineLifesDamageTypes::unknown, Level.ExplosionInteraction.MOB, player, false)
                player.hurtUnknown(3f)
                mob.removeEffect(Nlf2MobEffects.GLIDING)
            }
        }
    }
}