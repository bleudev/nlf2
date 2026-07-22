package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.util.areMobsAvoid
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal
import net.minecraft.world.entity.ai.goal.GoalSelector
import net.minecraft.world.entity.player.Player

fun <T : PathfinderMob> registerAvoidGoal(mob: T, goalSelector: GoalSelector) {
    mob.goalSelector
    goalSelector.addGoal(1, AvoidEntityGoal(mob, Player::class.java, 5.0f, 1.0, 1.0, ::areMobsAvoid))
}