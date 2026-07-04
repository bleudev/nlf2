package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.EVENT101_NICKNAME
import com.bleudev.nlf2.SUPPORTED_NICKNAMES
import com.bleudev.nlf2.client.dataSyncedActor
import com.bleudev.nlf2.client.dataSyncedActorLoreChar
import com.bleudev.nlf2.client.dataSyncedEvent101
import com.bleudev.nlf2.custom.data.saved.ActorChar
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.with
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.core.ClientAsset.ResourceTexture
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.PlayerModelType
import net.minecraft.world.entity.player.PlayerSkin

private val EYES_EMPTY = getIdentifier("textures/entity/player/eyes/empty.png")

val clientNickname: String
    get() = Minecraft.getInstance().gameProfile.name.lowercase()
val actorNickname: String
    get() = dataSyncedActor.lowercase()

fun getLoreCharacter(entityNickname: String): ActorChar {
    return if ((clientNickname in SUPPORTED_NICKNAMES.with(actorNickname)) && entityNickname.lowercase() == actorNickname)
                ActorChar.fromString(dataSyncedActorLoreChar)
           else ActorChar.NULL
}

fun getSkinOverlayTexture(entityNickname: String): Identifier {
    if (entityNickname == EVENT101_NICKNAME && !dataSyncedEvent101) {
        return getIdentifier("textures/entity/player/event101.png")
    }
    return EYES_EMPTY
}
fun getSkinOverlayRenderType(entityNickname: String): RenderType = RenderTypes.entityTranslucent(getSkinOverlayTexture(entityNickname))

fun getEyesTexture(entityNickname: String): Identifier {
    var clientNickname = clientNickname
    if (entityNickname == EVENT101_NICKNAME && !dataSyncedEvent101)
        return getIdentifier("textures/entity/player/eyes/event101.png")
    val loreChar = getLoreCharacter(entityNickname)
    if (loreChar == ActorChar.NULL || loreChar.eyes == null) return EYES_EMPTY
    if (clientNickname == actorNickname) clientNickname = "bleugame"

    return getIdentifier("textures/entity/player/eyes/${loreChar.eyes}/$clientNickname.png")
}
fun getEyesRenderType(entityNickname: String): RenderType = RenderTypes.eyes(getEyesTexture(entityNickname))

fun getSkin(entityNickname: String): PlayerSkin? {
    var clientNickname = clientNickname
    val loreChar: ActorChar = getLoreCharacter(entityNickname)
    if (loreChar == ActorChar.NULL) return null
    if (clientNickname == actorNickname) clientNickname = "bleugame"
    if (!loreChar.renderVessel) clientNickname = "empty"
    val texture = getIdentifier("entity/player/skin/$clientNickname")

    val modelType = if (clientNickname in listOf("waflla_kek")) PlayerModelType.SLIM else PlayerModelType.WIDE
    return PlayerSkin(ResourceTexture(texture), null, null, modelType, true)
}
