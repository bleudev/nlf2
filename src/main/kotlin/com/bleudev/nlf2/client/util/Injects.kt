package com.bleudev.nlf2.client.util

import com.bleudev.nine_lifes.api.EmptyPredicate
import com.bleudev.nine_lifes.api.render.client.PostEffectRegistry
import com.bleudev.nine_lifes.client.util.overlayWithColor
import com.bleudev.nlf2.getIdentifier
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import kotlin.random.Random

fun GuiGraphicsExtractor.black(alpha: Float) = overlayWithColor(alpha, 0f, 0f, 0f)
fun GuiGraphicsExtractor.black() = black(1f)

enum class Animation(val id: String) {
    INTRO("intro"), NOISE("noise");

    fun getIdentifier(n: Int): Identifier = getIdentifier("textures/anim/$id/$n.png")
}
fun GuiGraphicsExtractor.blitAnim(animation: Animation, n: Int, x: Int = 0, y: Int = 0, width: Int = -1, height: Int = -1, color: Int = 0xffffffff.toInt()) {
    var w = width; var h = height
    if (w == -1) w = guiWidth()
    if (h == -1) h = guiHeight()
    blit(RenderPipelines.GUI_TEXTURED, animation.getIdentifier(n), x, y, 0f, 0f, w, h, w, h, color)
}

fun PostEffectRegistry.Companion.registerNlf2(postEffectPath: String, renderPredicate: EmptyPredicate) =
    PostEffectRegistry.register(getIdentifier(postEffectPath), renderPredicate)

fun Player.sound(sound: SoundEvent, source: SoundSource, volume: Float = 1f, pitch: Float = 1f) {
    if (this is ServerPlayer) {
        connection.send(
            ClientboundSoundPacket(
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound),
                source,
                x, y, z,
                volume, pitch,
                Random.nextLong()
            )
        )
    } else {
        playSound(sound, volume, pitch)
    }
}
