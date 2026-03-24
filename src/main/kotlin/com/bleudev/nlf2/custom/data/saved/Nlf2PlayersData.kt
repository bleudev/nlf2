package com.bleudev.nlf2.custom.data.saved

import com.bleudev.nlf2.DATA_FOG_EVENT_STATE_SYNC_DURATION
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
import net.minecraft.util.Mth
import net.minecraft.util.StringRepresentable
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.world.level.saveddata.SavedDataType
import org.joml.Vector4f


class Nlf2PlayersData(loreData: LoreData = LoreData(), playersData: List<PlayerData> = listOf(), var serverToSync: MinecraftServer? = null) : SavedData() {
    var loreData: LoreData = loreData
        set(value) {
            value.actor = value.actor.lowercase()
            field = value
            setDirty()
            if (serverToSync != null) serverToSync!!.sendToEveryone {
                arrayOf(ClientNlf2DataSync(value.actor, value.actorChar.serializedName, value.borderState, value.fogEventState.value, value.event101, value.manualId))
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

        player.sendPackets(ClientNlf2DataSync(loreData.actor, loreData.actorChar.toString(), loreData.borderState, loreData.fogEventState.value, loreData.event101, loreData.manualId))
    }

    class LoreData(var actor: String = "", var actorChar: ActorChar = ActorChar.NULL, var borderState: Int = BorderStates.STANDARD, var fogEventState: FogEventState = FogEventState.BEFORE_MEET_CROCKO, var event101: Boolean = false, var manualId: Int = 0) {
        companion object {
            val CODEC: Codec<LoreData> = RecordCodecBuilder.create { it.group(
                Codec.STRING.fieldOf("actor").forGetter(LoreData::actor),
                StringRepresentable.fromEnum(ActorChar::values).fieldOf("actorChar").forGetter(LoreData::actorChar),
                Codec.INT.fieldOf("borderState").forGetter(LoreData::borderState),
                StringRepresentable.fromEnum(FogEventState::values).fieldOf("fogEventState").forGetter(LoreData::fogEventState),
                Codec.BOOL.fieldOf("event101").forGetter(LoreData::event101), // waffla vessel event
                Codec.INT.fieldOf("manualId").forGetter(LoreData::manualId),
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

enum class ActorChar(private val char: String): StringRepresentable {
    NULL("null"),
    ROUGE("rouge"),
    VERT("vert"),
    VESSEL("vessel"),
    ROUGE_EYES("rouge_eyes"),
    INV("inv");

    override fun getSerializedName(): String = char
    override fun toString(): String = serializedName

    val eyes: String?
        get() = when (this) {
            ROUGE -> "rouge"
            ROUGE_EYES -> "rouge"
            VERT -> "vert"
            else -> null
        }
    val isInvisibleLike: Boolean
        get() = this in listOf(ROUGE_EYES, INV)
    val renderVessel: Boolean
        get() = this in listOf(ROUGE, VERT, VESSEL)

    companion object {
        fun fromString(char: String): ActorChar {
            return when (char) {
                "rouge" -> ROUGE
                "vert" -> VERT
                "vessel" -> VESSEL
                "rouge_eyes" -> ROUGE_EYES
                "inv" -> INV
                else -> NULL
            }
        }
    }
}

object BorderStates {
    const val STANDARD = 0
    @Deprecated("Use standard instead")
    const val STANDARD_X_DEPRECATED = 1
    const val BLACK_FOG_KILL = 2
}

data class FogEventStateFogProperties(val fogColor: Vector4f, val fogStart: Float, val fogEnd: Float)

enum class FogEventState(val value: Int, val fogColor: Vector4f) : StringRepresentable {
    BEFORE_MEET_CROCKO(0, Vector4f(1f, 1f, 1f, 1f)),
    AFTER_MEET_CROCKO(1, Vector4f(1f, 1f, 1f, 0f)),
    BEFORE_MEET_WAFFLA(2, Vector4f(1f, .6f, 0f, 1f)),
    AFTER_MEET_WAFFLA(3, Vector4f(1f, .6f, 0f, 0f));

    fun getFogProperties(prev: FogEventState?, tick: Int): FogEventStateFogProperties {
//        println("GET FOG PROPERTIES CURR $value PREV $prev (tick = $tick)")
        var prev = prev
        if (prev == null) prev = this

        val lerpAmount = tick.toFloat() / DATA_FOG_EVENT_STATE_SYNC_DURATION
        val direction =
            if (prev.fogColor.w == this.fogColor.w) 0
            else if (prev.fogColor.w < this.fogColor.w) 1
            else -1
//        println("DIRECTION $direction")

        val lerpFogStartEnd = when(direction) {
            1 -> lerpAmount
            -1 -> 1f - lerpAmount
            else -> {
                if (fogColor.w == 0f) 1f
                else 0f
            }
        }
        return FogEventStateFogProperties(
            prev.fogColor.lerp(this.fogColor, lerpAmount),
            Mth.lerp(lerpFogStartEnd, .03f, 1f),
            Mth.lerp(lerpFogStartEnd, .1f, 1f),
        )
    }

    override fun getSerializedName(): String = value.toString()
    override fun toString(): String = serializedName

    companion object {
        fun fromValue(value: Int): FogEventState = when(value) {
            0 -> BEFORE_MEET_CROCKO
            1 -> AFTER_MEET_CROCKO
            2 -> BEFORE_MEET_WAFFLA
            3 -> AFTER_MEET_WAFFLA
            else -> throw MatchException(null, null)
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