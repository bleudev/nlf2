package com.bleudev.nlf2.client

import com.bleudev.nlf2.*
import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.bleudev.nlf2.client.custom.gui.screens.JoinDisconnectScreen
import com.bleudev.nlf2.client.custom.gui.screens.ManualScreen
import com.bleudev.nlf2.client.custom.gui.screens.PuzzleScreen
import com.bleudev.nlf2.client.util.Animation
import com.bleudev.nlf2.client.util.black
import com.bleudev.nlf2.client.util.blitAnim
import com.bleudev.nlf2.client.util.overlay
import com.bleudev.nlf2.client.util.sound.BorderAmbientSoundInstance
import com.bleudev.nlf2.client.util.sound.ambientTick
import com.bleudev.nlf2.client.util.sound.shouldPlayAmbient
import com.bleudev.nlf2.custom.packet.payload.*
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nlf2.custom.packet.payload.serverbound.JoinSendVersion
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.KillBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.unit.BorderBlackFogStart
import com.bleudev.nlf2.custom.packet.payload.unit.CancelBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.unit.CrashClientPayload
import com.bleudev.nlf2.custom.packet.payload.unit.LostVesselControlActivate
import com.github.sarxos.webcam.Webcam
import com.terraformersmc.modmenu.config.ModMenuConfig
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundSource
import net.minecraft.util.ARGB
import unknown.depths.r.man.ROHJEN67SW51

class Nlf2Client : ClientModInitializer {

    override fun onInitializeClient() {
        Nlf2Sounds.initialize()
        tryHideMod()

        ClientTickEvents.END_CLIENT_TICK.register { mc ->
            mc.window.setTitle(if (webcamEnabled) "EYES ARE WATCHING" else "\"Minecraft\" 26.1")
            if (Webcam.getDefault().isOpen) {
                if (!webcamEnabled) Webcam.getDefault().close()
            } else if (webcamEnabled) Webcam.getDefault().open()
            tryHideMod()
            shouldPlayAmbient = pause_screen_red_screen_effect
            ambientTick()
            pause_screen_ticks++
        }
        ClientTickEvents.END_LEVEL_TICK.register {
            join_blackness_delay_ticks = (join_blackness_delay_ticks - 1).coerceAtLeast(0)
            if (join_blackness_delay_ticks == 0)
                join_blackness = (join_blackness - .05f).coerceAtLeast(0f)

            noise_tick++

            if (dataSyncedBorderState != 2) return@register
            if (borderBlackFogTicks == -1) {
                borderBlackFog = 0f
                join_blackness = 0f
                return@register
            }
            if (borderBlackFogTicks == 0) {
                borderBlackFogTicks = -1
                deathScreenBlackFogEffect = true
                ClientPlayNetworking.send(KillBorderBlackFog.INSTANCE)
            }
            if (borderBlackFogTicks > 0) {
                if (borderBlackFogTicks > BORDER_BLACK_FOG_KILL_TRANS_DURATION + BORDER_BLACK_FOG_KILL_TRANS_DELAY) {
                    borderBlackFog = 1f - ((borderBlackFogTicks - BORDER_BLACK_FOG_KILL_TRANS_DURATION - BORDER_BLACK_FOG_KILL_TRANS_DELAY).toFloat() / BORDER_BLACK_FOG_DURATION)
                } else {
                    borderBlackFog = 1f
                    join_blackness = if (borderBlackFogTicks > BORDER_BLACK_FOG_KILL_TRANS_DURATION) 0f
                    else 1f - (borderBlackFogTicks.toFloat() / BORDER_BLACK_FOG_KILL_TRANS_DURATION)
                }
                if (borderBlackFogStarting) borderBlackFogTicks--
                else borderBlackFogTicks++
                if (borderBlackFogTicks == BORDER_BLACK_FOG_KILL_TRANS_DURATION + BORDER_BLACK_FOG_KILL_TRANS_DELAY + BORDER_BLACK_FOG_DURATION && !borderBlackFogStarting) borderBlackFogTicks = -1
            }
        }
        ClientPlayConnectionEvents.JOIN.register { _, s, mc ->
            s.sendPacket(JoinSendVersion(FabricLoader.getInstance().getModContainer(MODID).get().metadata.version.friendlyString))
            mc.soundManager.play(BorderAmbientSoundInstance())
        }
        ClientPlayConnectionEvents.DISCONNECT.register { _, mc ->
            mc.soundManager.stop(Nlf2Sounds.BORDER_AMBIENT.location, SoundSource.AMBIENT)
        }

        HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR, Layer.BORDER_NOISE) { graphics, _ ->
            graphics.nlf2noise()
        }
        HudElementRegistry.attachElementAfter(VanillaHudElements.SLEEP, Layer.OVERLAY_FIX) { graphics, _ ->
            if (Minecraft.getInstance().options.hideGui) {
                graphics.nlf2noise()
                // Render red vignette
                graphics.blit(RenderPipelines.VIGNETTE, Sprite.Vanilla.VIGNETTE, 0, 0, 0f, 0f, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(),
                    ARGB.colorFromFloat(1f, 0f, noise_amount * 2, noise_amount * 2))
            }
            graphics.black(join_blackness)
        }
        HudElementRegistry.addLast(Layer.OVERLAY_PAUSE_SCREEN) { graphics, _ ->
            if (pause_screen_red_screen_effect) {
                graphics.overlay(0x20ff0000)
                graphics.overlay(ARGB.colorFromFloat(((pause_screen_ticks - 200) / 400f).coerceIn(0f, 1f), 0f, 0f, 0f))
            }
        }

        registerPacketReceiver(ClientNlf2DataSync) { payload ->
            dataSyncedActor = payload.actor
            dataSyncedActorLoreChar = payload.actorChar
            dataSyncedBorderState = payload.borderState
            dataSyncedEvent101 = payload.event101
        }
        registerPacketReceiver(OpenManual) { payload, ctx ->
            ctx.client().setScreen(ManualScreen(payload.manualId))
        }
        registerPacketReceiver(OpenPuzzle) { payload, ctx ->
            payload.packetPuzzle?.let {
                ctx.client().setScreen(PuzzleScreen(it))
            }
        }
        registerPacketReceiver(WorldBorderNoisePayload) { payload ->
            val maxBlocks = 50.0
            val maxNoise = .5f
            noise_amount = (1f - ((payload.distance.coerceAtMost(maxBlocks)) / maxBlocks)).toFloat() * maxNoise
        }
        registerPacketReceiver(CrashClientPayload) {
            throw ROHJEN67SW51("\nYOU...\nREACHED...\n\nTHE END")
        }
        registerPacketReceiver(JoinDisconnectVersion) { payload, ctx ->
            ctx.client().setScreen(JoinDisconnectScreen(payload.client, payload.server))
        }
        registerPacketReceiver(BorderBlackFogStart) {
            if (borderBlackFogTicks == -1) {
                borderBlackFogTicks = BORDER_BLACK_FOG_DURATION + BORDER_BLACK_FOG_KILL_TRANS_DURATION + BORDER_BLACK_FOG_KILL_TRANS_DELAY
                borderBlackFogStarting = true
            }
        }
        registerPacketReceiver(CancelBorderBlackFog) {
            if (borderBlackFogTicks > BORDER_BLACK_FOG_KILL_TRANS_DURATION)
                borderBlackFogStarting = false
        }
        registerPacketReceiver(LostVesselControlActivate) {
            shaderLostVesselControl = !shaderLostVesselControl
        }
    }

    private fun tryHideMod() {
        ModMenuConfig.HIDDEN_MODS.value.run {
            if (this.add(MODID))
                ModMenuConfig.HIDDEN_MODS.value = this
        }
    }

    private fun <T : CustomPacketPayload> registerPacketReceiver(packet: PacketPayloadCompanion<T>, handler: (payload: T, ctx: ClientPlayNetworking.Context) -> Unit) =
        ClientPlayNetworking.registerGlobalReceiver(packet.type, handler)
    private fun <T : CustomPacketPayload> registerPacketReceiver(packet: PacketPayloadCompanion<T>, handler: (payload: T) -> Unit) =
        registerPacketReceiver(packet) {payload, _ -> handler(payload)}

    private fun GuiGraphics.nlf2noise() {
        val noiseTicks = 5
        val noiseCount = 5
        blitAnim(Animation.NOISE, 1 + ((noise_tick % noiseTicks) / (noiseTicks / noiseCount)), color = ARGB.colorFromFloat(noise_amount, 1f, 0f, 0f))
    }

    object Layer {
        val BORDER_NOISE: Identifier = getIdentifier("layer/border/noise")
        val OVERLAY_FIX: Identifier = getIdentifier("layer/overlay/fix")
        val OVERLAY_PAUSE_SCREEN: Identifier = getIdentifier("layer/overlay/pause_screen")
    }

    object Sprite {
        object Vanilla {
            val VIGNETTE: Identifier = Identifier.withDefaultNamespace("textures/misc/vignette.png");
        }
    }
}
