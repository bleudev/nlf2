package com.bleudev.nlf2.util

import com.bleudev.nlf2.mixin.accessor.LivingEntityAccessor
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource

fun ServerPlayer.dropAllDeathLoot(source: DamageSource) = (this as LivingEntityAccessor)
    .`nlf2$dropAllDeathLoot`(level(), source)
fun ServerPlayer.dropAllDeathLoot() = dropAllDeathLoot(level().damageSources().generic())
