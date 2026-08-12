package com.bleudev.nlf2.custom.world.level.saveddata

import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.custom.Nlf2Commands.causeEvent
import com.bleudev.nlf2.custom.packet.payload.ClientNlf2DataSync
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.clearVertItemsIfNeed
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.world.level.saveddata.SavedDataType


class Nlf2Data(loreData: LoreData = LoreData(), playersData: Map<String, PlayerData> = mapOf(), var serverToSync: MinecraftServer? = null) : SavedData() {
    private fun ServerPlayer.sync() {
        sendPacket(ClientNlf2DataSync(
            loreData.actor,
            loreData.actorChar,
            loreData.borderState,
            loreData.meetState,
            loreData.labOverlayState,
            loreData.event101,
            loreData.manualId,
            loreData.compassCrazy,
            players[gameProfile.name]?.simulateNickname ?: "self",
            loreData.visibleActor,
        ))
        clearVertItemsIfNeed(level().server)
    }
    private fun MinecraftServer.sync() { for (i in playerList.players) i.sync() }

    var loreData: LoreData = loreData
        set(value) {
            value.actor = value.actor.lowercase()
            field = value
            setDirty()
            serverToSync?.sync()
        }

    private val varPlayers: HashMap<String, PlayerData> = HashMap(playersData)
    val players: Map<String, PlayerData> get() = varPlayers.toMap()
    fun setPlayerData(nickname: String, data: PlayerData) {
        varPlayers[nickname] = data
        setDirty()
        serverToSync?.sync()
    }
    fun changePlayerData(name: String, changer: (PlayerData) -> PlayerData) =
        setPlayerData(name, (players[name] ?: PlayerData()).let(changer))

    fun onJoin(player: ServerPlayer) {
        causeEvent(34)
        val name = player.gameProfile.name
        varPlayers.computeIfAbsent(name) { PlayerData() }
        player.sync()
    }

    class LoreData(
        var actor: String = "", var actorChar: String = "null",
        var borderState: Int = BorderStates.STANDARD,
        var meetState: Int = 0,
        var labOverlayState: Int = 0,
        var event101: Boolean = false,
        var manualId: Int = 0,
        var compassCrazy: Boolean = false,
        var forceJukeboxMus: StrangeJukeboxBlock.SelectedMus = StrangeJukeboxBlock.SelectedMus.NULL,
        var mobAvoidPlayers: Boolean = false,
        var noVert: Boolean = false,
        var visibleActor: Boolean = false,
    ) {
        companion object {
            val CODEC: Codec<LoreData> = RecordCodecBuilder.create { it.group(
                Codec.STRING.fieldOf("actor").forGetter(LoreData::actor),
                Codec.STRING.fieldOf("actorChar").forGetter(LoreData::actorChar),
                Codec.INT.fieldOf("borderState").forGetter(LoreData::borderState),
                Codec.INT.fieldOf("meetState").forGetter(LoreData::meetState),
                Codec.INT.fieldOf("labOverlayState").forGetter(LoreData::labOverlayState),
                Codec.BOOL.fieldOf("event101").forGetter(LoreData::event101), // Sam vessel event
                Codec.INT.fieldOf("manualId").forGetter(LoreData::manualId),
                Codec.BOOL.fieldOf("compassCrazy").forGetter(LoreData::compassCrazy),
                StrangeJukeboxBlock.SelectedMus.CODEC.fieldOf("forceJukeboxMus").forGetter(LoreData::forceJukeboxMus),
                Codec.BOOL.fieldOf("mobAvoidPlayers").forGetter(LoreData::mobAvoidPlayers),
                Codec.BOOL.fieldOf("noVert").forGetter(LoreData::noVert),
                Codec.BOOL.fieldOf("visibleActor").forGetter(LoreData::visibleActor),
            ).apply(it, ::LoreData) }
        }
    }

    class PlayerData(var teleported: Boolean = false, var simulateNickname: String = "self", var curse: Float = 0f) {
        companion object {
            val CODEC: Codec<PlayerData> = RecordCodecBuilder.create { it.group(
                Codec.BOOL.fieldOf("teleported").forGetter(PlayerData::teleported),
                Codec.STRING.fieldOf("simulate").forGetter(PlayerData::simulateNickname),
                Codec.FLOAT.fieldOf("curse").forGetter(PlayerData::curse),
            ).apply(it, ::PlayerData)
            }
        }
    }

    companion object {
        val CODEC: Codec<Nlf2Data> = RecordCodecBuilder.create { it.group(
            LoreData.CODEC.fieldOf("lore").forGetter(Nlf2Data::loreData),
            Codec.unboundedMap(Codec.STRING, PlayerData.CODEC).fieldOf("players").forGetter(Nlf2Data::players),
        ).apply(it, ::Nlf2Data) }
        val TYPE: SavedDataType<Nlf2Data> = SavedDataType(getIdentifier("data"), ::Nlf2Data, CODEC,
            DataFixTypes.SAVED_DATA_MAP_DATA)
    }
}

object BorderStates {
    const val STANDARD = 0
    const val STANDARD_LOSE_HEARTS = 1
    const val BLACK_FOG_KILL = 2
}

val MinecraftServer.nlf2data: Nlf2Data
    get() = dataStorage.computeIfAbsent(Nlf2Data.TYPE).apply { serverToSync = this@nlf2data }
