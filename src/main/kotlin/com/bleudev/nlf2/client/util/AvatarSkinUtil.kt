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

fun getLoreCharacter(entityNickname: String): ActorChar {
    return if (Minecraft.getInstance().gameProfile.name.lowercase() in SUPPORTED_NICKNAMES && entityNickname == dataSyncedActor)
                ActorChar.fromString(dataSyncedActorLoreChar)
           else ActorChar.NULL
}

fun getEyesTexture(entityNickname: String): Identifier {
    var clientNickname = Minecraft.getInstance().gameProfile.name.lowercase()
    val loreChar = getLoreCharacter(entityNickname)
    if (loreChar == ActorChar.NULL || loreChar.eyes == null) return EYES_EMPTY
    if (clientNickname == dataSyncedActor) clientNickname = "actor"

    return getIdentifier("textures/entity/player/eyes/${loreChar.eyes}/$clientNickname.png")
}
fun getEyesRenderType(entityNickname: String): RenderType = RenderTypes.eyes(getEyesTexture(entityNickname))

fun getSkinTexture(entityNickname: String): Identifier? {
    var clientNickname = Minecraft.getInstance().gameProfile.name.lowercase()
    val loreChar: ActorChar = getLoreCharacter(entityNickname)
    if (loreChar == ActorChar.NULL) return null
    if (clientNickname == dataSyncedActor) clientNickname = "actor"
    if (!loreChar.renderVessel) clientNickname = "empty"
    return getIdentifier("entity/player/skin/$clientNickname")
}

fun getSkin(entityNickname: String): PlayerSkin? {
    val texture = getSkinTexture(entityNickname) ?: return null
    val modelType = PlayerModelType.WIDE
    // If there will be slim skins, will add code for them
    return PlayerSkin(ResourceTexture(texture), null, null, modelType, true)
}
