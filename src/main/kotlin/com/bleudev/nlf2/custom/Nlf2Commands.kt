package com.bleudev.nlf2.custom

import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.LOGGER
import com.bleudev.nlf2.custom.data.saved.ActorChar
import com.bleudev.nlf2.custom.data.saved.FogEventState
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.custom.packet.payload.unit.CrashClientPayload
import com.bleudev.nlf2.custom.packet.payload.unit.LostVesselControlActivate
import com.bleudev.nlf2.custom.packet.payload.unit.ManualUpdateEffect
import com.bleudev.nlf2.util.isActor
import com.bleudev.nlf2.util.requiresAdmin
import com.bleudev.nlf2.util.sendToEveryone
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
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

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
                                    it.actorChar = ActorChar.NULL
                                    it
                                }
                                return@executes ctx.source.sendSuccess(Component.literal("Actor was set"))
                            }))
                    .then(Commands
                        .literal("actorChar")
                        .then(Commands
                            .argument("actorChar", StringArgumentType.word())
                            .executes { ctx ->
                                val char = StringArgumentType.getString(ctx, "actorChar")
                                ctx.source.server.nlf2data::loreData.transform {
                                    it.actorChar = ActorChar.fromString(char)
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
                            val eventId = builder.input.let {
                                if (it.startsWith("/nlf2 event ", true))
                                    return@let it.replaceFirst("/nlf2 event ", "", true)
                                else return@let it
                            }

                            eventId.toIntOrNull()?.let {
                                if (it in eventMap) {
                                    builder.suggest(eventMap[it]!!)
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
                            val debugId = builder.input.let {
                                if (it.startsWith("/nlf2 debug ", true))
                                    return@let it.replaceFirst("/nlf2 debug ", "", true)
                                else return@let it
                            }

                            debugId.toIntOrNull()?.let {
                                if (it in debugMap) {
                                    builder.suggest(debugMap[it]!!)
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

    private val eventMap: Map<Int, String> = mapOf(
        189 to "crash_vert_disappear",
        101 to "orange_skin_overlay_remove",
        93 to "manual_1_update",
        94 to "manual_2_update",
    )

    private fun CommandSourceStack.causeEvent(eventId: Int): Boolean = server.causeEvent<Any>(eventId)
    fun <T1 : Any> MinecraftServer.causeEvent(eventId: Int, info0: T1? = null): Boolean {
        when (eventId) {
            // crash_vert_disappear
            189 -> {
                sendToEveryone { player ->
                    if (player.isActor) return@sendToEveryone arrayOf()
                    return@sendToEveryone arrayOf(CrashClientPayload.INSTANCE)
                }
            }
            // orange_skin_overlay_remove
            101 -> {
                nlf2data::loreData.transform {
                    it.event101 = true
                    it
                }
            }
            // manual_1_update
            93 -> {
                nlf2data::loreData.transform {
                    it.manualId = 1
                    it
                }
                val owner = info0 as? ServerPlayer?
                owner?.sendPacket(ManualUpdateEffect.INSTANCE)
            }
            // manual_2_update
            94 -> {
                nlf2data::loreData.transform {
                    it.manualId = 2
                    it
                }
                val owner = info0 as? ServerPlayer?
                owner?.sendPacket(ManualUpdateEffect.INSTANCE)
            }
            else -> return false
        }
        LOGGER.info("Caused nlf2:{} event", eventMap[eventId])
        return true
    }

    private val debugMap: Map<Int, String> = mapOf(
        1001 to "lost_vessel_control_activate",
        89 to "fog_event_state_0",
        90 to "fog_event_state_1",
        91 to "fog_event_state_2",
        92 to "fog_event_state_3",
        995 to "manual_0",
        996 to "manual_1",
        997 to "manual_2",
    )

    // TODO: Fix strange fog event state shift after time (for example, 89 set fog event state to 1 for some reason)
    // TODO: Fix strange fast fog start and end lerping
    // TODO: Fix fog event wrong duration
    private fun CommandSourceStack.causeDebug(debugId: Int): Boolean {
        when (debugId) {
            1001 -> {
                player?.sendPacket(LostVesselControlActivate.INSTANCE)
            }
            89 -> {
                server.nlf2data::loreData.transform {
                    it.fogEventState = FogEventState.BEFORE_MEET_CROCKO
                    it
                }
            }
            90 -> {
                server.nlf2data::loreData.transform {
                    it.fogEventState = FogEventState.AFTER_MEET_CROCKO
                    it
                }
            }
            91 -> {
                server.nlf2data::loreData.transform {
                    it.fogEventState = FogEventState.BEFORE_MEET_WAFFLA
                    it
                }
            }
            92 -> {
                server.nlf2data::loreData.transform {
                    it.fogEventState = FogEventState.AFTER_MEET_WAFFLA
                    it
                }
            }
            101 -> {
                server.nlf2data::loreData.transform {
                    it.event101 = false
                    it
                }
            }
            995 -> {
                server.nlf2data::loreData.transform {
                    it.manualId = 0
                    it
                }
            }
            996 -> {
                server.nlf2data::loreData.transform {
                    it.manualId = 1
                    it
                }
            }
            997 -> {
                server.nlf2data::loreData.transform {
                    it.manualId = 2
                    it
                }
            }
            else -> return false
        }
        LOGGER.info("Caused nlf2:{} debug", debugMap[debugId])
        return true
    }
}