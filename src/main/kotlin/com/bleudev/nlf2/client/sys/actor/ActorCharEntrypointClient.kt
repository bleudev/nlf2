package com.bleudev.nlf2.client.sys.actor

import com.bleudev.nlf2.SUPPORTED_NICKNAMES
import com.bleudev.nlf2.client.dataSyncedActor
import com.bleudev.nlf2.client.dataSyncedActorLoreChar
import com.bleudev.nlf2.client.dataSyncedEvent101
import com.bleudev.nlf2.client.dataSyncedSimulateNickname
import com.bleudev.nlf2.client.sys.actor.ActorCharEntrypoint.chars
import com.bleudev.nlf2.client.sys.actor.chars.ActorChar
import com.bleudev.nlf2.client.util.isEvent101Char
import com.bleudev.nlf2.getIdentifier
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.core.ClientAsset
import net.minecraft.world.entity.player.PlayerSkin

@Environment(EnvType.CLIENT)
object ActorCharEntrypointClient {
    private val clientNickname get() = Minecraft.getInstance().gameProfile.name

    fun tick() {
        for (i in chars.values) i.tick()
    }

    private data class PrepareData(val client: String, val entity: String, val char: ActorChar?)
    private fun prepare(client: String, entity: String): PrepareData {
        var client = client.lowercase()
        var entity = entity.lowercase()
        var actor = dataSyncedActor.lowercase()
        val char = dataSyncedActorLoreChar

        dataSyncedSimulateNickname.lowercase().let {
            if (it != "self") {
                if (client == actor) actor = it
                if (client == entity) entity = it
                client = it
            }
        }

        if (client == actor && client !in SUPPORTED_NICKNAMES) client = "bleugame"

        if (!dataSyncedEvent101 && isEvent101Char(entity)) return PrepareData(client, entity, chars["event101"])
        // Other event specific chars...
        if (entity == actor && client in SUPPORTED_NICKNAMES) return PrepareData(client, entity, chars[char])
        return PrepareData(client, entity, null)
    }

    fun current(client: String, entity: String): ActorChar? = prepare(client, entity).char
    @JvmStatic
    fun current(entity: String): ActorChar? = current(clientNickname, entity)

    fun skin(client: String, entity: String): PlayerSkin? {
        val pr = prepare(client, entity)
        val ch = pr.char ?: return null
        val txt = getIdentifier(ch.skin(pr.client) ?: return null)
        val modelType = ch.skinModel(pr.client)
        return PlayerSkin(ClientAsset.ResourceTexture(txt), null, null, modelType, true)
    }
    @JvmStatic
    fun skin(entity: String): PlayerSkin? = skin(clientNickname, entity)

    private val EMPTY_FALLBACK = getIdentifier("textures/entity/player/empty.png")
    private val EYES_FALLBACK = RenderTypes.eyes(EMPTY_FALLBACK)
    private val OVERLAY_FALLBACK = RenderTypes.entityTranslucent(EMPTY_FALLBACK)

    fun eyes(client: String, entity: String): RenderType {
        val pr = prepare(client, entity)
        val txt = getIdentifier("textures/${pr.char?.eyes(pr.client) ?: return EYES_FALLBACK}.png")
        return RenderTypes.eyes(txt)
    }
    @JvmStatic
    fun eyes(entity: String): RenderType = eyes(clientNickname, entity)

    fun overlay(client: String, entity: String): RenderType {
        val pr = prepare(client, entity)
        val txt = getIdentifier("textures/${pr.char?.overlay(pr.client) ?: return OVERLAY_FALLBACK}.png")
        return RenderTypes.entityTranslucent(txt)
    }
    @JvmStatic
    fun overlay(entity: String): RenderType = overlay(clientNickname, entity)
}