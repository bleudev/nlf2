package com.bleudev.nlf2.util

import com.bleudev.nlf2.custom.data.saved.nlf2data
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity

fun areMobsAvoid(entity: LivingEntity): Boolean =
    !(entity !is ServerPlayer || entity.isActor) && entity.level().server.nlf2data.loreData.mobAvoidPlayers
