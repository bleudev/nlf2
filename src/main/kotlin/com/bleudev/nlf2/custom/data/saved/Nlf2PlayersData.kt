package com.bleudev.nlf2.custom.data.saved

import com.bleudev.nlf2.getIdentifier
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.server.MinecraftServer
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.world.level.saveddata.SavedDataType


class Nlf2PlayersData(playersData: List<PlayerData> = listOf()) : SavedData() {
    private val players: ArrayList<PlayerData> = ArrayList(playersData)
    fun getPlayers(): List<PlayerData> = players.toList()
    fun setPlayerData(data: PlayerData) {
        val index = players.indexOfFirst { it.name == data.name }
        if (index == -1) players.add(data)
        else players[index] = data
        this.setDirty()
    }
    fun changePlayerData(name: String, changer: (PlayerData) -> PlayerData) =
        setPlayerData((getPlayers().find { it.name == name }?: PlayerData(name)).let(changer))
    fun trySetDefault(name: String) {
        if (players.none { it.name == name }) players.add(PlayerData(name))
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

    companion object {
        private val CODEC: Codec<Nlf2PlayersData> = Codec.list(PlayerData.CODEC)
            .xmap(::Nlf2PlayersData, Nlf2PlayersData::players)
        private val TYPE: SavedDataType<Nlf2PlayersData> = SavedDataType(getIdentifier("lore_players_data"), ::Nlf2PlayersData, CODEC,
            DataFixTypes.SAVED_DATA_MAP_DATA)

        fun get(server: MinecraftServer): Nlf2PlayersData = server.dataStorage.computeIfAbsent(TYPE)
    }
}