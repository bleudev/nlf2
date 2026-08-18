package com.bleudev.nlf2.client.util

import com.bleudev.nine_lifes.api.EmptyPredicate
import com.bleudev.nine_lifes.api.render.client.PostEffectRegistry
import com.bleudev.nine_lifes.client.util.overlayWithColor
import com.bleudev.nlf2.LOGGER
import com.bleudev.nlf2.getIdentifier
import com.mojang.blaze3d.GpuFormat
import com.mojang.blaze3d.platform.Window
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.sounds.SoundEngine
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import org.lwjgl.glfw.GLFW
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

// Windows
fun Window.sizeLimit(minWidth: Int, minHeight: Int, maxWidth: Int, maxHeight: Int) {
    try {
        GLFW.glfwSetWindowSizeLimits(handle(), minWidth, minHeight, maxWidth, maxHeight)
    } catch (e: Exception) {
        LOGGER.info("Got $e during try to set size limit")
    }
}
fun Window.sizeLimit(width: Int, height: Int) = sizeLimit(width, height, width, height)
fun Window.removeSizeLimit() {
    try {
        val maxSize = RenderSystem.getDevice().deviceInfo.limits.maxTextureSizeForFormat(GpuFormat.RGBA8_UNORM)
        sizeLimit(-1, -1, maxSize, maxSize)
    } catch (e: Exception) {
        LOGGER.info("Got $e during try to remove size limit")
    }
}
var Window.alwaysOnTop: Boolean get() = GLFW.glfwGetWindowAttrib(handle(), GLFW.GLFW_FLOATING) == GLFW.GLFW_TRUE
set(v) {
    if (v == alwaysOnTop) return
    try {
        if (v) {
            GLFW.glfwFocusWindow(handle())
        }
        GLFW.glfwSetWindowAttrib(handle(), GLFW.GLFW_FLOATING, if (v) GLFW.GLFW_TRUE else GLFW.GLFW_FALSE)
    } catch (e: Exception) {
        LOGGER.info("Got $e during try to set always on top $v")
    }
}
fun Window.setPos(xpos: Int, ypos: Int) {
    try {
        GLFW.glfwSetWindowPos(handle(), xpos, ypos)
    } catch (e: Exception) {
        LOGGER.info("Got $e during try to set pos ($xpos, $ypos)")
    }
}

// Sounds
fun SoundEngine.refreshAll() = refreshCategoryVolume(SoundSource.MASTER)
