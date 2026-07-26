package com.bleudev.nlf2.custom

import com.bleudev.nlf2.client.sys.actor.ActorCharEntrypoint
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.sys.event.DebugEntrypoint
import com.bleudev.nlf2.sys.event.EventEntrypoint
import com.bleudev.nlf2.util.requiresAdmin
import com.bleudev.nlf2.util.transform
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component

object Nlf2Commands {
    fun initialize() {
        CommandRegistrationCallback.EVENT.register { commandDispatcher, _, _ ->
            commandDispatcher.register(Commands
                .literal("nlf2")
                .requiresAdmin()
                .then(Commands
                    .literal("lore")
                    .then(Commands
                        .literal("actor")
                        .then(Commands
                            .argument("player", EntityArgument.player())
                            .executes { ctx ->
                                val player = EntityArgument.getPlayer(ctx, "player")
                                ctx.source.server.nlf2data::loreData.transform {
                                    it.actor = player.gameProfile.name
                                    it.actorChar = "null"
                                    it
                                }
                                return@executes ctx.source.sendSuccess(Component.literal("Actor was set"))
                            })
                        .then(Commands
                            .literal("reset")
                            .executes { ctx ->
                                ctx.source.server.nlf2data::loreData.transform {
                                    it.actor = "null"
                                    it.actorChar = "null"
                                    it
                                }
                                return@executes ctx.source.sendSuccess(Component.literal("Actor was reset"))
                            }))
                    .then(Commands
                        .literal("actorChar")
                        .then(Commands
                            .argument("actorChar", StringArgumentType.word())
                            .suggests { _, builder ->
                                for (name in ActorCharEntrypoint.getAllCharsNames()) {
                                    if (name.startsWith(builder.remainingLowerCase)) {
                                        builder.suggest(name)
                                    }
                                }
                                builder.buildFuture()
                            }
                            .executes { ctx ->
                                val char = StringArgumentType.getString(ctx, "actorChar")
                                ctx.source.server.nlf2data::loreData.transform {
                                    it.actorChar = char
                                    it
                                }
                                return@executes ctx.source.sendSuccess(Component.literal("Actor char was set"))
                            }))
                    .then(Commands
                        .literal("borderState")
                        .then(Commands
                            .argument("borderState", IntegerArgumentType.integer(0, 2))
                            .executes { ctx ->
                                val state = IntegerArgumentType.getInteger(ctx, "borderState")
                                ctx.source.server.nlf2data::loreData.transform {
                                    it.borderState = state
                                    it
                                }
                                return@executes ctx.source.sendSuccess(Component.literal("Border state was set"))
                            })))
                .then(Commands
                    .literal("event")
                    .then(integer("id", 1)
                        .suggests { _, builder ->
                            builder.remainingLowerCase.toIntOrNull()?.let {
                                if (it in EventEntrypoint.names) {
                                    builder.suggest(EventEntrypoint.names[it]!!)
                                }
                            }
                            builder.buildFuture()
                        }
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
                    .then(integer("di", 1)
                        .suggests { _, builder ->
                            builder.remainingLowerCase.toIntOrNull()?.let {
                                if (it in DebugEntrypoint.names) {
                                    builder.suggest(DebugEntrypoint.names[it]!!)
                                }
                            }
                            builder.buildFuture()
                        }
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
    private fun integer(name: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): RequiredArgumentBuilder<CommandSourceStack, Int> =
        Commands.argument(name, IntegerArgumentType.integer(min, max))

    private fun CommandSourceStack.causeEvent(eventId: Int, vararg args: Any?): Boolean =
        EventEntrypoint.cause(player, eventId, *args)
    fun causeEvent(eventId: Int, vararg args: Any?): Boolean =
        EventEntrypoint.cause(null, eventId, *args)
    private fun CommandSourceStack.causeDebug(debugId: Int, vararg args: Any?): Boolean =
        DebugEntrypoint.cause(player, debugId, *args)
    fun causeDebug(debugId: Int, vararg args: Any?): Boolean =
        DebugEntrypoint.cause(null, debugId, *args)
}