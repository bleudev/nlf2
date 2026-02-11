package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.SUPPORTED_NICKNAMES
import com.bleudev.nlf2.client.dataSyncedActor
import com.bleudev.nlf2.client.dataSyncedActorLoreChar
import com.bleudev.nlf2.custom.data.saved.ActorChar
import com.bleudev.nlf2.getIdentifier
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.core.ClientAsset.ResourceTexture
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.PlayerModelType
import net.minecraft.world.entity.player.PlayerSkin

private val EYES_EMPTY = getIdentifier("textures/entity/player/eyes/empty.png")

fun getEyesTexture(entityNickname: String, clientNickname: String): Identifier {
    var clientNickname = clientNickname

    if (clientNickname !in SUPPORTED_NICKNAMES) return EYES_EMPTY

    var loreChar: ActorChar = ActorChar.NULL
    if (entityNickname == dataSyncedActor) loreChar = ActorChar.fromString(dataSyncedActorLoreChar)
    if (loreChar == ActorChar.NULL || loreChar.eyes == null) return EYES_EMPTY

    if (clientNickname == dataSyncedActor) clientNickname = "actor"

    return getIdentifier("textures/entity/player/eyes/${loreChar.eyes}/$clientNickname.png")
}
fun getEyesTexture(entityNickname: String): Identifier =
    getEyesTexture(entityNickname, Minecraft.getInstance().gameProfile.name().lowercase())

fun getEyesRenderType(entityNickname: String): RenderType = RenderTypes.eyes(getEyesTexture(entityNickname))

fun getSkinTexture(entityNickname: String, clientNickname: String): Identifier? {
    var clientNickname = clientNickname

    if (clientNickname !in SUPPORTED_NICKNAMES) return null

    var loreChar: ActorChar = ActorChar.NULL
    if (entityNickname == dataSyncedActor) loreChar = ActorChar.fromString(dataSyncedActorLoreChar)
    if (loreChar == ActorChar.NULL) return null

    if (clientNickname == dataSyncedActor) clientNickname = "actor"
    if (!loreChar.renderVessel) clientNickname = "empty"

    return getIdentifier("entity/player/skin/$clientNickname")
}

fun getSkin(entityNickname: String, clientNickname: String): PlayerSkin? {
    val texture = getSkinTexture(entityNickname, clientNickname) ?: return null
    val modelType = PlayerModelType.WIDE
    // If there will be slim skins, will add code for them
    return PlayerSkin(ResourceTexture(texture), null, null, modelType, true)
}
fun getSkin(entityNickname: String): PlayerSkin? =
    getSkin(entityNickname, Minecraft.getInstance().gameProfile.name().lowercase())
