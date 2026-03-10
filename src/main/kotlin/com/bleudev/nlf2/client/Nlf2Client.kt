package com.bleudev.nlf2.client

import com.bleudev.nine_lifes.client.api.render.DynamicUniformsRegistry
import com.bleudev.nine_lifes.client.util.overlayWithColor
import com.bleudev.nlf2.*
import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.bleudev.nlf2.client.custom.gui.screens.JoinDisconnectScreen
import com.bleudev.nlf2.client.custom.gui.screens.ManualScreen
import com.bleudev.nlf2.client.custom.gui.screens.PuzzleScreen
import com.bleudev.nlf2.client.util.Animation
import com.bleudev.nlf2.client.util.ClientFogRenderTransformEvents
import com.bleudev.nlf2.client.util.black
import com.bleudev.nlf2.client.util.blitAnim
import com.bleudev.nlf2.client.util.sound.BorderAmbientSoundInstance
import com.bleudev.nlf2.client.util.sound.ambientTick
import com.bleudev.nlf2.client.util.sound.shouldPlayAmbient
import com.bleudev.nlf2.custom.data.saved.BorderStates
import com.bleudev.nlf2.custom.data.saved.FogEventState
import com.bleudev.nlf2.custom.packet.payload.*
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nlf2.custom.packet.payload.serverbound.JoinSendVersion
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.KillBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.unit.*
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
import net.minecraft.util.Mth
import org.joml.Vector4f
import unknown.depths.r.man.ROHJEN67SW51
import kotlin.math.max

class Nlf2Client : ClientModInitializer {

    override fun onInitializeClient() {
        Nlf2Sounds.initialize()
        tryHideMod()

        DynamicUniformsRegistry.register(DynamicUniformsRegistry.Context("CustomConvolveConfig", getIdentifier("event101")), {putFloat().putFloat()}) {
            putFloat(shaderEvent101Strength).putFloat(2.4f)
        }

        ClientTickEvents.END_CLIENT_TICK.register { mc ->
            mc.window.setTitle(if (webcamEnabled) "EYES ARE WATCHING" else "\"Minecraft\" 26.1")
            if (Webcam.getDefault().isOpen) {
                if (!webcamEnabled) Webcam.getDefault().close()
            } else if (webcamEnabled) Webcam.getDefault().open()
            tryHideMod()
            shouldPlayAmbient = pause_screen_red_screen_effect
            ambientTick()
            pause_screen_ticks++
            noise_tick++
        }
        ClientTickEvents.END_LEVEL_TICK.register {
            shaderRedMajUniformStrength = max(join_blackness, manualUpdateTicks.toFloat() / MANUAL_UPDATE_EFFECT_DURATION)
            compassForceSpin = noise_amount > .6f

            if (manualUpdateTicks > 0) manualUpdateTicks--

            join_blackness_delay_ticks = (join_blackness_delay_ticks - 1).coerceAtLeast(0)
            if (join_blackness_delay_ticks == 0)
                join_blackness = (join_blackness - .05f).coerceAtLeast(0f)

            if (shaderBorderLag) {
                borderLagTicks--
                if (borderLagTicks == 0) shaderBorderLag = false
            }

            if (dataSyncedBorderState != BorderStates.BLACK_FOG_KILL && noise_amount > .4f && !shaderBorderLag) {
                if (it.random.nextFloat() <= .01f) {
                    shaderBorderLag = true
                    borderLagTicks = 5
                }
            }

            if (dataSyncedBorderState == BorderStates.BLACK_FOG_KILL) {
                if (borderBlackFogTicks == -1) {
                    borderBlackFog = 0f
                    join_blackness = 0f
                    return@register
                }
                if (borderBlackFogTicks == 0) {
                    borderBlackFogTicks = -1
                    deathScreenBlackFogEffect = true
                    shaderAnaglyphUniformX = 0f
                    ClientPlayNetworking.send(KillBorderBlackFog.INSTANCE)
                    shaderBorderEnd = false
                }
                if (borderBlackFogTicks > 0) {
                    if (borderBlackFogTicks > BORDER_BLACK_FOG_KILL_TRANS_DURATION + BORDER_BLACK_FOG_KILL_TRANS_DELAY) {
                        borderBlackFog = 1f - ((borderBlackFogTicks - BORDER_BLACK_FOG_KILL_TRANS_DURATION - BORDER_BLACK_FOG_KILL_TRANS_DELAY).toFloat() / BORDER_BLACK_FOG_DURATION)
                    } else {
                        borderBlackFog = 1f
                        join_blackness = if (borderBlackFogTicks > BORDER_BLACK_FOG_KILL_TRANS_DURATION) 0f
                        else 1f - (borderBlackFogTicks.toFloat() / BORDER_BLACK_FOG_KILL_TRANS_DURATION)
                        shaderAnaglyphUniformX = join_blackness * 0.01f
                    }
                    if (borderBlackFogStarting) {
                        borderBlackFogTicks--
                        deathScreenBlackFogEffect = true
                    }
                    else borderBlackFogTicks++
                    if (borderBlackFogTicks == BORDER_BLACK_FOG_KILL_TRANS_DURATION + BORDER_BLACK_FOG_KILL_TRANS_DELAY + BORDER_BLACK_FOG_DURATION && !borderBlackFogStarting) borderBlackFogTicks = -1
                }
            }

            if (dataFogEventStateSyncTicks > 0) dataFogEventStateSyncTicks--
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
            graphics.nlf2event101()
            graphics.overlayWithColor(manualUpdateEffectOverlay, 1f, 0f, 0f)
        }
        HudElementRegistry.attachElementAfter(VanillaHudElements.SLEEP, Layer.OVERLAY_FIX) { graphics, _ ->
            val mc = Minecraft.getInstance()
            if (mc.options.hideGui) {
                graphics.nlf2noise()
                graphics.nlf2event101()
                // Render red vignette
                graphics.blit(RenderPipelines.VIGNETTE, Sprite.Vanilla.VIGNETTE, 0, 0, 0f, 0f, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(),
                    ARGB.colorFromFloat(1f, 0f, noise_amount * 2, noise_amount * 2))
            }
            graphics.black(join_blackness)
        }
        HudElementRegistry.addLast(Layer.OVERLAY_PAUSE_SCREEN) { graphics, _ ->
            if (pause_screen_red_screen_effect) {
                graphics.overlayWithColor(0x20ff0000)
                graphics.overlayWithColor(((pause_screen_ticks - 200) / 400f).coerceIn(0f, 1f), 0f, 0f, 0f)
            }
        }

        ClientFogRenderTransformEvents.FOG_COLOR.register { _, current ->
            current.lerp(Vector4f(0f, 0f, 0f, 1f), blackFogEffect)
        }
        ClientFogRenderTransformEvents.FOG_COLOR.register { _, current ->
            val color = dataSyncedFogProperties.fogColor
            current.lerp(Vector4f(color.x, color.y, color.z, current.w), color.w)
        }

        ClientFogRenderTransformEvents.FOG_START.register { original, current ->
            Mth.lerp(blackFogEffect, current, original * .03f)
        }
        ClientFogRenderTransformEvents.FOG_START.register { _, current ->
            current * dataSyncedFogProperties.fogStart
        }

        ClientFogRenderTransformEvents.FOG_END.register { original, current ->
            Mth.lerp(blackFogEffect, current, original * .03f)
        }
        ClientFogRenderTransformEvents.FOG_END.register { _, current ->
            current * dataSyncedFogProperties.fogEnd
        }

        registerPacketReceiver(ClientNlf2DataSync) { payload ->
            dataSyncedActor = payload.actor
            dataSyncedActorLoreChar = payload.actorChar
            dataSyncedBorderState = payload.borderState

            val prevFogEventState = dataSyncedFogEventState
            if (prevFogEventState.value != payload.fogEventState) {
                dataSyncedFogEventState = FogEventState.fromValue(payload.fogEventState)
                dataFogEventStateSyncPrev = prevFogEventState
                dataFogEventStateSyncTicks = DATA_FOG_EVENT_STATE_SYNC_DURATION
            }

            dataSyncedEvent101 = payload.event101
            dataSyncedManualId = payload.manualId
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
//            shaderBorderLag = !shaderBorderLag
            shaderRedMajUniformStrength = 1f
//            shaderLostVesselControl = !shaderLostVesselControl
        }
        registerPacketReceiver(ManualUpdateEffect) {
            manualUpdateTicks = MANUAL_UPDATE_EFFECT_DURATION
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
    private fun GuiGraphics.nlf2event101() {
        val darken = Minecraft.getInstance().level?.skyDarken ?: 0
        if (shaderEvent101) overlayWithColor(((shaderEvent101Strength - darken.toFloat() / 7) / 5).coerceIn(0f, .2f), 1f, .5f, 0f)
    }

    object Layer {
        val BORDER_NOISE: Identifier = getIdentifier("layer/border/noise")
        val OVERLAY_FIX: Identifier = getIdentifier("layer/overlay/fix")
        val OVERLAY_PAUSE_SCREEN: Identifier = getIdentifier("layer/overlay/pause_screen")
    }

    object Sprite {
        object Vanilla {
            val VIGNETTE: Identifier = Identifier.withDefaultNamespace("textures/misc/vignette.png")
        }
    }
}
