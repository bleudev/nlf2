package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.SUPPORTED_NICKNAMES
import com.bleudev.nlf2.client.dataSyncedActor
import com.bleudev.nlf2.client.dataSyncedActorLoreChar
import com.bleudev.nlf2.custom.data.saved.ActorChar
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.with
import net.minecraft.client.Minecraft

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

