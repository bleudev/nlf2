package com.bleudev.nlf2.custom.data.saved

import com.bleudev.nlf2.custom.data.saved.Nlf2PlayersData.LoreData
import com.bleudev.nlf2.custom.data.saved.Nlf2PlayersData.PlayerData
import com.bleudev.nlf2.custom.packet.payload.ClientNlf2DataSync
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.sendPackets
import com.bleudev.nlf2.util.sendToEveryone
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.StringRepresentable
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.world.level.saveddata.SavedDataType


class Nlf2PlayersData(loreData: LoreData = LoreData(), playersData: List<PlayerData> = listOf(), var serverToSync: MinecraftServer? = null) : SavedData() {
    var loreData: LoreData = loreData
        set(value) {
            value.actor = value.actor.lowercase()
            field = value
            setDirty()
            if (serverToSync != null) serverToSync!!.sendToEveryone {
                arrayOf(ClientNlf2DataSync(value.actor, value.actorChar.serializedName))
            }
        }

    private val players: ArrayList<PlayerData> = ArrayList(playersData)
    fun getPlayers(): List<PlayerData> = players.toList()
    fun setPlayerData(data: PlayerData) {
        val index = players.indexOfFirst { it.name == data.name }
        if (index == -1) players.add(data)
        else players[index] = data
        setDirty()
    }
    fun changePlayerData(name: String, changer: (PlayerData) -> PlayerData) =
        setPlayerData((getPlayers().find { it.name == name }?: PlayerData(name)).let(changer))

    fun onJoin(player: ServerPlayer) {
        val name = player.gameProfile.name
        if (players.none { it.name == name }) players.add(PlayerData(name))

        player.sendPackets(ClientNlf2DataSync(loreData.actor, loreData.actorChar.toString()))
    }

    enum class ActorChar(private val char: String): StringRepresentable {
        NULL("null"), ROUGE("rouge"), VERT("vert"), INVISIBLE("inv");

        override fun getSerializedName(): String = char
        override fun toString(): String = char

        companion object {
            fun fromString(char: String): ActorChar {
                return when (char) {
                    "rouge" -> ROUGE
                    "vert" -> VERT
                    "inv" -> INVISIBLE
                    else -> NULL
                }
            }
        }
    }

    class LoreData(var actor: String = "", var actorChar: ActorChar = ActorChar.NULL) {
        constructor(actor: String, actorChar: String) : this(actor, ActorChar.fromString(actorChar))

        companion object {
            val CODEC: Codec<LoreData> = RecordCodecBuilder.create { it.group(
                Codec.STRING.fieldOf("actor").forGetter(LoreData::actor),
                StringRepresentable.fromEnum(ActorChar::values).fieldOf("actorChar").forGetter(LoreData::actorChar)
            ).apply(it, ::LoreData) }
        }
    }

    class PlayerData(var name: String, var curse: Float = 0f) {
        companion object {
            val CODEC: Codec<PlayerData> = RecordCodecBuilder.create { it.group(
                Codec.STRING.fieldOf("name").forGetter(PlayerData::name),
                Codec.FLOAT.fieldOf("curse").forGetter(PlayerData::curse),
            ).apply(it, ::PlayerData)
            }
        }
    }
}

private val CODEC: Codec<Nlf2PlayersData> = RecordCodecBuilder.create { it.group(
    LoreData.CODEC.fieldOf("lore").forGetter(Nlf2PlayersData::loreData),
    Codec.list(PlayerData.CODEC).fieldOf("players").forGetter(Nlf2PlayersData::getPlayers)
).apply(it, ::Nlf2PlayersData) }

private val TYPE: SavedDataType<Nlf2PlayersData> = SavedDataType(getIdentifier("lore_players_data"), ::Nlf2PlayersData, CODEC,
    DataFixTypes.SAVED_DATA_MAP_DATA)

val MinecraftServer.nlf2data: Nlf2PlayersData
    get() = dataStorage.computeIfAbsent(TYPE).apply { serverToSync = this@nlf2data }