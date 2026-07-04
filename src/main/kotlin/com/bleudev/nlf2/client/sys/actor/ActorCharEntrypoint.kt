package com.bleudev.nlf2.client.sys.actor

import com.bleudev.nlf2.SUPPORTED_NICKNAMES
import com.bleudev.nlf2.client.dataSyncedActor
import com.bleudev.nlf2.client.dataSyncedActorLoreChar
import com.bleudev.nlf2.client.sys.actor.chars.ActorChar
import com.bleudev.nlf2.client.sys.actor.chars.res.*
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.with
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.core.ClientAsset
import net.minecraft.world.entity.player.PlayerModelType
import net.minecraft.world.entity.player.PlayerSkin

object ActorCharEntrypoint {
    private val chars: HashMap<String, ActorChar> = HashMap()

    fun initialize() {
        chars["rouge"] = RougeChar()
        chars["vert"] = VertChar()
        chars["vessel"] = VesselChar()
        chars["rouge_eyes"] = RougeEyesChar()
        chars["vert_eyes"] = VertEyesChar()
        chars["inv"] = InvChar()
    }

    @JvmStatic
    fun current(client: String, entity: String): ActorChar? {
        var client = client.lowercase()
        val entity = entity.lowercase()
        val actor = dataSyncedActor.lowercase()
        val char = dataSyncedActorLoreChar
        if (client == actor) client = "bleugame"

        if (entity == actor && client in SUPPORTED_NICKNAMES) return chars[char]
        return null
    }

    @JvmStatic
    fun skin(client: String, entity: String): PlayerSkin? {
        var client = client.lowercase()
        val entity = entity.lowercase()
        val actor = dataSyncedActor.lowercase()
        val char = dataSyncedActorLoreChar
        if (client == actor) client = "bleugame"
        if (entity != actor || client !in SUPPORTED_NICKNAMES) return null

        val txt = getIdentifier(chars[char]?.skin(client) ?: return null)
        val modelType = if (client in listOf("waflla_kek")) PlayerModelType.SLIM else PlayerModelType.WIDE
        return PlayerSkin(ClientAsset.ResourceTexture(txt), null, null, modelType, true)
    }

    private val EMPTY_FALLBACK = getIdentifier("textures/entity/player/empty.png")
    private val EYES_FALLBACK = RenderTypes.eyes(EMPTY_FALLBACK)
    private val OVERLAY_FALLBACK = RenderTypes.entityTranslucent(EMPTY_FALLBACK)

    @JvmStatic
    fun eyes(client: String, entity: String): RenderType {
        var client = client.lowercase()
        val entity = entity.lowercase()
        val actor = dataSyncedActor.lowercase()
        val char = dataSyncedActorLoreChar
        if (client == actor) client = "bleugame"
        if (entity != actor || client !in SUPPORTED_NICKNAMES.with(actor)) return EYES_FALLBACK

        val txt = getIdentifier("textures/${chars[char]?.eyes(client) ?: return EYES_FALLBACK}.png")
        return RenderTypes.eyes(txt)
    }

    @JvmStatic
    fun overlay(client: String, entity: String): RenderType {
        // TODO: event101 support
        return OVERLAY_FALLBACK
    }
}