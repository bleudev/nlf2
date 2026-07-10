package com.bleudev.nlf2.custom

import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.EVENT101_NICKNAME
import com.bleudev.nlf2.LOGGER
import com.bleudev.nlf2.TELEPORT_LOCATIONS
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.custom.packet.payload.unit.CrashClientPayload
import com.bleudev.nlf2.custom.packet.payload.unit.Event101ScreenEffectClear
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
                                    it.actorChar = "null"
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
        101 to "orange_skin_overlay_remove_wip",
        34 to "try_teleport_on_first_join_internal",
        35 to "reset_teleports_on_first_join",
        80 to "meet_before_crocko",
        81 to "meet_after_crocko",
        82 to "meet_before_waflla",
        83 to "meet_after_waflla",
        92 to "manual_0_reset",
        93 to "manual_1_update_internal",
        94 to "manual_2_update_internal",
        140 to "lab_overlay_0",
        141 to "lab_overlay_1",
        189 to "end_serie_crash",
        200 to "compass_crazy_off",
        201 to "compass_crazy_on",
    )

    private fun CommandSourceStack.causeEvent(eventId: Int): Boolean = server.causeEvent<Any>(eventId)
    fun <T1 : Any> MinecraftServer.causeEvent(eventId: Int, info0: T1? = null): Boolean {
        when (eventId) {
            // orange_skin_overlay_remove
            101 -> {
                playerList.getPlayer(EVENT101_NICKNAME)?.sendPacket(Event101ScreenEffectClear.INSTANCE)
                nlf2data::loreData.transform {
                    it.event101 = true
                    it
                }
            }
            // try_teleport_on_first_join_internal
            34 -> {
                for (name in nlf2data.getPlayers().filter { !it.teleported }.map { it.name }) {
                    val lc = name.lowercase()
                    if (lc in TELEPORT_LOCATIONS) {
                        playerList.getPlayer(name)?.let { pl ->
                            val loc = TELEPORT_LOCATIONS[lc] ?: return@let
                            pl.teleportTo(loc.x, loc.y, loc.z)
                            nlf2data.changePlayerData(name) {
                                it.teleported = true
                                it
                            }
                        }
                    }
                }
            }
            // reset_teleports_on_first_join
            35 -> {
                for (name in nlf2data.getPlayers().map { it.name }) {
                    nlf2data.changePlayerData(name) {
                        it.teleported = false
                        it
                    }
                }
            }
            // meet_before_crocko
            80 -> {
                nlf2data::loreData.transform {
                    it.meetState = 0
                    it
                }
            }
            // meet_after_crocko
            81 -> {
                nlf2data::loreData.transform {
                    it.meetState = 1
                    it
                }
            }
            // meet_before_waflla
            82 -> {
                nlf2data::loreData.transform {
                    it.meetState = 2
                    it
                }
            }
            // meet_after_waflla
            83 -> {
                nlf2data::loreData.transform {
                    it.meetState = 3
                    it
                }
            }
            // manual_0_reset
            92 -> {
                nlf2data::loreData.transform {
                    it.manualId = 0
                    it
                }
            }
            // manual_1_update_internal
            93 -> {
                nlf2data::loreData.transform {
                    it.manualId = 1
                    it
                }
                val owner = info0 as? ServerPlayer?
                owner?.sendPacket(ManualUpdateEffect.INSTANCE)
            }
            // manual_2_update_internal
            94 -> {
                nlf2data::loreData.transform {
                    it.manualId = 2
                    it
                }
                val owner = info0 as? ServerPlayer?
                owner?.sendPacket(ManualUpdateEffect.INSTANCE)
            }
            // lab_overlay_0
            140 -> {
                nlf2data::loreData.transform {
                    it.labOverlayState = 0
                    it.compassCrazy = false
                    it
                }
            }
            // lab_overlay_1
            141 -> {
                nlf2data::loreData.transform {
                    it.labOverlayState = 1
                    it.compassCrazy = true
                    it
                }
            }
            // end_serie_crash
            189 -> {
                sendToEveryone { player ->
                    if (player.isActor) return@sendToEveryone arrayOf()
                    return@sendToEveryone arrayOf(CrashClientPayload.INSTANCE)
                }
            }
            // compass_crazy_off
            200 -> {
                nlf2data::loreData.transform {
                    it.compassCrazy = false
                    it
                }
            }
            // compass_crazy_on
            201 -> {
                nlf2data::loreData.transform {
                    it.compassCrazy = true
                    it
                }
            }
            else -> return false
        }
        LOGGER.info("Caused nlf2:{} event", eventMap[eventId])
        return true
    }

    private val debugMap: Map<Int, String> = mapOf(
        1001 to "lost_vessel_control_activate",
        101 to "orange_skin_overlay",
        34 to "re_teleport_on_first_join",
    )
    private fun CommandSourceStack.causeDebug(debugId: Int): Boolean = server.causeDebug(debugId, player)
    private fun MinecraftServer.causeDebug(debugId: Int, player: ServerPlayer?): Boolean {
        when (debugId) {
            1001 -> {
                player?.sendPacket(LostVesselControlActivate.INSTANCE)
            }
            101 -> {
                nlf2data::loreData.transform {
                    it.event101 = false
                    it
                }
            }
            34 -> {
                causeEvent<Any>(35)
                causeEvent<Any>(34)
            }
            else -> return false
        }
        LOGGER.info("Caused nlf2:{} debug", debugMap[debugId])
        return true
    }
}