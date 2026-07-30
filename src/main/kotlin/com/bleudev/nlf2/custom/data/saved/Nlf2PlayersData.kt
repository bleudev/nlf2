package com.bleudev.nlf2.custom.data.saved

import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.custom.Nlf2Commands.causeEvent
import com.bleudev.nlf2.custom.data.saved.Nlf2PlayersData.LoreData
import com.bleudev.nlf2.custom.data.saved.Nlf2PlayersData.PlayerData
import com.bleudev.nlf2.custom.packet.payload.ClientNlf2DataSync
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock
import com.bleudev.nlf2.getIdentifier
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.world.level.saveddata.SavedDataType


class Nlf2PlayersData(loreData: LoreData = LoreData(), playersData: List<PlayerData> = listOf(), var serverToSync: MinecraftServer? = null) : SavedData() {
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
            getPlayerData(gameProfile.name)?.simulateNickname ?: "self",
        ))
    }
    private fun MinecraftServer.sync() { for (i in playerList.players) i.sync() }

    var loreData: LoreData = loreData
        set(value) {
            value.actor = value.actor.lowercase()
            field = value
            setDirty()
            serverToSync?.sync()
        }

    private val players: ArrayList<PlayerData> = ArrayList(playersData)
    fun getPlayers(): List<PlayerData> = players.toList()
    fun getPlayerData(nickname: String): PlayerData? = players.toList().firstOrNull { it.name == nickname }
    fun setPlayerData(data: PlayerData) {
        val index = players.indexOfFirst { it.name == data.name }
        if (index == -1) players.add(data)
        else players[index] = data
        setDirty()
        serverToSync?.sync()
    }
    fun changePlayerData(name: String, changer: (PlayerData) -> PlayerData) =
        setPlayerData((getPlayers().find { it.name == name }?: PlayerData(name)).let(changer))

    fun onJoin(player: ServerPlayer) {
        causeEvent(34)
        val name = player.gameProfile.name
        if (players.none { it.name == name }) players.add(PlayerData(name))
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
            ).apply(it, ::LoreData) }
        }
    }

    class PlayerData(var name: String, var teleported: Boolean = false, var simulateNickname: String = "self", var curse: Float = 0f) {
        companion object {
            val CODEC: Codec<PlayerData> = RecordCodecBuilder.create { it.group(
                Codec.STRING.fieldOf("name").forGetter(PlayerData::name),
                Codec.BOOL.fieldOf("teleported").forGetter(PlayerData::teleported),
                Codec.STRING.fieldOf("simulate").forGetter(PlayerData::simulateNickname),
                Codec.FLOAT.fieldOf("curse").forGetter(PlayerData::curse),
            ).apply(it, ::PlayerData)
            }
        }
    }
}

object BorderStates {
    const val STANDARD = 0
    const val STANDARD_LOSE_HEARTS = 1
    const val BLACK_FOG_KILL = 2
}

private val CODEC: Codec<Nlf2PlayersData> = RecordCodecBuilder.create { it.group(
    LoreData.CODEC.fieldOf("lore").forGetter(Nlf2PlayersData::loreData),
    Codec.list(PlayerData.CODEC).fieldOf("players").forGetter(Nlf2PlayersData::getPlayers),
).apply(it, ::Nlf2PlayersData) }

private val TYPE: SavedDataType<Nlf2PlayersData> = SavedDataType(getIdentifier("lore_players_data"), ::Nlf2PlayersData, CODEC,
    DataFixTypes.SAVED_DATA_MAP_DATA)

val MinecraftServer.nlf2data: Nlf2PlayersData
    get() = dataStorage.computeIfAbsent(TYPE).apply { serverToSync = this@nlf2data }