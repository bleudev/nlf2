package com.bleudev.nlf2.custom

import com.bleudev.nlf2.LOGGER
import com.bleudev.nlf2.util.requiresAdmin
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

object Nlf2Commands {
    fun initialize() {
        CommandRegistrationCallback.EVENT.register { commandDispatcher, _, _ ->
            commandDispatcher.register(Commands
                .literal("nlf2")
                .requiresAdmin()
                .then(Commands
                    .literal("event")
                    .then(Commands
                        .argument("id", IntegerArgumentType.integer(1))
                        .executes { ctx ->
                            val id = IntegerArgumentType.getInteger(ctx, "id")
                            if (ctx.source.causeEvent(id)) {
                                return@executes ctx.source.sendSuccess(Component.literal("Event was successfully triggered")
                                    .withStyle(ChatFormatting.GREEN))
                            } else {
                                ctx.source.sendFailure(Component.literal("Unknown event id"))
                                return@executes -1
                            }
                }))
                .then(Commands
                    .literal("debug")
                    .then(Commands
                        .argument("di", IntegerArgumentType.integer(1))
                        .executes { ctx ->
                            val di = IntegerArgumentType.getInteger(ctx, "di")
                            if (ctx.source.causeDebug(di)) {
                                return@executes ctx.source.sendSuccess(Component.literal("Debug event was successfully triggered")
                                    .withStyle(ChatFormatting.GREEN))
                            } else {
                                ctx.source.sendFailure(Component.literal("Unknown debug id"))
                                return@executes -1
                            }
                        })))
        }
    }

    private fun CommandSourceStack.sendSuccess(message: Component, broadcast: Boolean = false): Int {
        sendSuccess({message}, broadcast)
        return 1
    }

    private fun CommandSourceStack.causeEvent(eventId: Int): Boolean {
        val eventName: String
        when (eventId) {
            189 -> {
                eventName = "crash_vert_disappear"
            }
            else -> return false
        }
        LOGGER.info("Caused nlf2:{} event", eventName)
        return true
    }

    private fun CommandSourceStack.causeDebug(debugId: Int): Boolean {
        val debugName: String
        when (debugId) {
            1001 -> {
                debugName = "debugtest_nickname_test"
                if (player != null) {
                    player!!.sendSystemMessage(Component.literal("Nickname: " + player!!.gameProfile.name))
                }
            }
            else -> return false
        }
        LOGGER.info("Caused nlf2:{} debug", debugName)
        return true
    }
}