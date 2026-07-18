package com.bleudev.nlf2.client.sys.actor

import com.bleudev.nlf2.SUPPORTED_NICKNAMES
import com.bleudev.nlf2.client.dataSyncedActor
import com.bleudev.nlf2.client.dataSyncedActorLoreChar
import com.bleudev.nlf2.client.sys.actor.chars.ActorChar
import com.bleudev.nlf2.client.sys.actor.chars.res.*
import com.bleudev.nlf2.client.util.isEvent101Char
import com.bleudev.nlf2.getIdentifier
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.core.ClientAsset
import net.minecraft.world.entity.player.PlayerModelType
import net.minecraft.world.entity.player.PlayerSkin

object ActorCharEntrypoint {
    private val clientNickname get() = Minecraft.getInstance().gameProfile.name

    private val chars: HashMap<String, ActorChar> = HashMap()

    private val SLIM_SKINS = listOf("waflla_kek")

    fun initialize() {
        chars["rouge"] = RougeChar()
        chars["vert"] = VertChar()
        chars["vessel"] = VesselChar()
        chars["ghost"] = GhostChar()
        chars["rouge_eyes"] = RougeEyesChar()
        chars["vert_eyes"] = VertEyesChar()
        chars["inv"] = InvChar()
        // Mask
        chars["mask_bleugame"] = MaskBleugameChar()
        chars["mask_crockomd"] = MaskCrockoMDChar()
        chars["mask_waflla_kek"] = MaskWafllaKekChar()
        chars["mask_boberobo7"] = MaskBoberobo7Char()
        // Event 101
        chars["event101"] = Event101Char()
    }

    fun tick() {
        for (i in chars.values) i.tick()
    }

    private data class PrepareData(val client: String, val entity: String, val char: ActorChar?)
    private fun prepare(client: String, entity: String): PrepareData {
        var client = client.lowercase()
        val entity = entity.lowercase()
        val actor = dataSyncedActor.lowercase()
        val char = dataSyncedActorLoreChar

        if (client == actor) client = "bleugame"

        if (isEvent101Char(entity)) return PrepareData(client, entity, chars["event101"])
        // Other event specific chars...
        if (entity == actor && client in SUPPORTED_NICKNAMES) return PrepareData(client, entity, chars[char])
        return PrepareData(client, entity, null)
    }

    fun current(client: String, entity: String): ActorChar? = prepare(client, entity).char
    @JvmStatic
    fun current(entity: String): ActorChar? = current(clientNickname, entity)

    fun skin(client: String, entity: String): PlayerSkin? {
        val pr = prepare(client, entity)
        val txt = getIdentifier(pr.char?.skin(pr.client) ?: return null)
        val modelType = if (pr.client in SLIM_SKINS) PlayerModelType.SLIM else PlayerModelType.WIDE
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