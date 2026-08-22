package com.bleudev.nlf2.client

import com.bleudev.nine_lifes.api.event.client.ClientEnvironmentSetupEvents
import com.bleudev.nine_lifes.api.event.client.ClientRespawnEvents
import com.bleudev.nine_lifes.api.render.client.PostEffectRegistry
import com.bleudev.nine_lifes.client.util.overlayWithColor
import com.bleudev.nine_lifes.client.util.white
import com.bleudev.nine_lifes.util.lerp
import com.bleudev.nine_lifes.util.reverseDelta
import com.bleudev.nlf2.*
import com.bleudev.nlf2.client.custom.gui.screens.JoinDisconnectScreen
import com.bleudev.nlf2.client.custom.gui.screens.ManualScreen
import com.bleudev.nlf2.client.custom.gui.screens.PuzzleScreen
import com.bleudev.nlf2.client.sys.actor.ActorCharEntrypoint
import com.bleudev.nlf2.client.sys.actor.ActorCharEntrypointClient
import com.bleudev.nlf2.client.sys.util.GuiTextBoxSequence
import com.bleudev.nlf2.client.util.*
import com.bleudev.nlf2.client.util.sound.BorderAmbientSoundInstance
import com.bleudev.nlf2.client.util.sound.Event101NoiseSoundInstance
import com.bleudev.nlf2.client.util.sound.TickableLoopedAmbientSoundInstance
import com.bleudev.nlf2.custom.Nlf2Sounds
import com.bleudev.nlf2.custom.packet.payload.*
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nlf2.custom.packet.payload.serverbound.JoinSendVersion
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.KillBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.RipSanyaTakeStart
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.RipVertEnd
import com.bleudev.nlf2.custom.packet.payload.unit.*
import com.bleudev.nlf2.custom.world.level.saveddata.BorderStates
import com.bleudev.nlf2.sys.util.TextBoxSequence
import com.bleudev.nlf2.util.normalize
import com.bleudev.nlf2.util.onlyIf
import com.github.sarxos.webcam.Webcam
import com.google.common.primitives.Floats.max
import com.google.common.primitives.Floats.min
import com.terraformersmc.modmenu.config.ModMenuConfig
import de.maxhenkel.voicechat.gui.volume.AdjustVolumeList
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.ARGB
import net.minecraft.util.Mth
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW
import unknown.depths.r.man.ROHJEN67SW51
import kotlin.math.roundToInt
import kotlin.random.Random

class Nlf2Client : ClientModInitializer {
    class MusManBeforeInstance(private val end: Int) : TickableLoopedAmbientSoundInstance(Nlf2Sounds.MUS_PART4_BYE_VERT) {
        override fun vol(): Float = TICKER_RIP_VERT.delta(0, end, 40).also { println("MUS MAN BEFORE VOLUME IS $it") }
    }
    class MusPart4ByeInstance : TickableLoopedAmbientSoundInstance(Nlf2Sounds.MUS_PART4_BYE) {
        init {
            this.looping = false
        }
    }
    class MusManWeirdInstanceSnippet(private val start: Int, private val mid: Int, sound: SoundEvent) : TickableLoopedAmbientSoundInstance(sound) {
        override fun vol(): Float = TICKER_RIP_VERT.delta(start, 40)
        override fun pit(): Float = TICKER_RIP_VERT.delta(mid, mid + 60).lerp(1f, 2f)
    }

    override fun onInitializeClient() {
        ActorCharEntrypoint.initialize()
        tryHideMod()
        their.validateAll()

        ClientEnvironmentSetupEvents.SKY_COLOR.register { _, current ->
            ARGB.srgbLerp(blackFogEffect, current, ARGB.color(ARGB.alpha(current), 0))
        }
        ClientEnvironmentSetupEvents.SKY_COLOR.register { _, current ->
            if (skyColorEffectTicks <= 0f) return@register current
            ARGB.srgbLerp(skyColorEffectAmount, current, skyColorEffectColor.normalize())
        }
        ClientEnvironmentSetupEvents.SKY_COLOR.register { _, current ->
            ARGB.srgbLerp(beforeMeetWaterFluidAmount, current, 0xff000000.toInt())
        }
        ClientEnvironmentSetupEvents.SKY_COLOR.register { _, current ->
            if (!ripSanyaEventEnabled) return@register current
            ARGB.srgbLerp(TICKER_RIP_SANYA.delta(
                ripSanyaEventByeTick + RIP_SANYA_EVENT_WHITE_FADE,
                ripSanyaEventByeTick + RIP_SANYA_EVENT_WHITE_FADE + RIP_SANYA_EVENT_SANYA_TAKE + RIP_SANYA_EVENT_SANYA_TAKE_FADE_OUT,
                0, RIP_SANYA_EVENT_SANYA_TAKE_FADE_OUT
            ), current, 0xffff0000.toInt())
        }
        ClientEnvironmentSetupEvents.FOG_COLOR.register { _, current ->
            current.lerp(Vector4f(0f, 0f, 0f, 1f), blackFogEffect)
        }
        ClientEnvironmentSetupEvents.FOG_COLOR.register { _, current ->
            if (skyColorEffectAmount <= 0f) return@register current
            current.lerp(ARGB.vector4fFromARGB32(skyColorEffectColor.normalize()), skyColorEffectAmount)
        }
        ClientEnvironmentSetupEvents.FOG_COLOR.register { _, current ->
            current.lerp(Vector4f(0f, 0f, 0f, 1f), beforeMeetWaterFluidAmount)
        }
        ClientEnvironmentSetupEvents.FOG_COLOR.register { _, current ->
            if (!ripSanyaEventEnabled) return@register current
            current.lerp(Vector4f(1f, 0f, 0f, 1f), min(
                TICKER_RIP_SANYA.delta(ripSanyaEventByeTick + RIP_SANYA_EVENT_WHITE_FADE, 0),
                TICKER_RIP_SANYA.delta(ripSanyaEventByeTick + RIP_SANYA_EVENT_WHITE_FADE + RIP_SANYA_EVENT_SANYA_TAKE, RIP_SANYA_EVENT_SANYA_TAKE_FADE_OUT).reverseDelta()
            ))
        }
        ClientEnvironmentSetupEvents.FOG_START.register { original, current ->
            Mth.lerp(blackFogEffect, current, original * .03f)
        }
        ClientEnvironmentSetupEvents.FOG_END.register { original, current ->
            Mth.lerp(blackFogEffect, current, original * .03f)
        }

        PostEffectRegistry.registerNlf2("before_meet", ::shaderBeforeMeet)
            .uniform("CopyConfig") {
                putVec2(shaderBeforeMeetCopySize, shaderBeforeMeetCopySize).putFloat(shaderBeforeMeetCopyStrength)
            }
            .uniform("ChmajConfig") {
                putVec3(1f, 0f, 0f).putFloat(shaderBeforeMeetRedmajStrength)
            }
            .uniform("AnaglyphConfig") {
                putVec2(shaderBeforeMeetAnaglyphX, 0f)
            }
        PostEffectRegistry.registerNlf2("before_meet_orange_overlay", ::shaderBeforeMeetOrangeOverlay)
            .uniform("CustomConvolveConfig") {
                putFloat(shaderBeforeMeetOrangeOverlayStrength).putFloat(5.4f)
            }
        PostEffectRegistry.registerNlf2("event101", ::shaderEvent101)
            .uniform("CustomConvolveConfig") {
                putFloat(shaderEvent101Strength).putFloat(2.4f)
            }
            .uniform("CustomBitsConfig") {
                putFloat(shaderEvent101Strength)
            }
        PostEffectRegistry.registerNlf2("lab_overlay", ::shaderLabOverlay)
            .uniform("CustomConvolveConfig") {
                putFloat(shaderLabOverlayConvolveStrength).putFloat(shaderLabOverlayConvolveSaturation)
            }
            .uniform("CustomBitsConfig") {
                putFloat(shaderLabOverlayBitsStrength)
            }
        PostEffectRegistry.registerNlf2("rainbow", ::shaderRainbow)
            .uniform("RainbowConfig") {
                putFloat(shaderRainbowHue).putFloat(shaderRainbowStrength)
            }
        PostEffectRegistry.registerNlf2("border_lag", ::shaderBorderLag)
        PostEffectRegistry.registerNlf2("border_end", ::shaderBorderEnd)

        ClientTickEvents.END_CLIENT_TICK.register { mc ->
            if (Webcam.getDefault().isOpen) {
                if (!webcamEnabled) Webcam.getDefault().close()
            } else if (webcamEnabled) Webcam.getDefault().open()
            tryHideMod()
            pause_screen_ticks++
            noise_tick++
            ActorCharEntrypointClient.tick()
            keyTick()
            volumeTick()

            // RIP Vert sounds
            if (ripVertEventEnabled) {
                val sm = mc.soundManager
                when (TICKER_RIP_VERT.ticks) {
                    1L -> {
                        sm.play(MusManBeforeInstance(ripVertEventShakeStartTick))
                    }
                    ripVertEventShakeStartTick.toLong() -> {
                        sm.play(MusManWeirdInstanceSnippet(ripVertEventShakeStartTick, ripVertEventLagStartTick, Nlf2Sounds.MUS_MAN_WEIRD))
                        sm.play(MusManWeirdInstanceSnippet(ripVertEventShakeStartTick, ripVertEventLagStartTick, Nlf2Sounds.MUS_QUAKE))
                        sm.stop(Nlf2Sounds.MUS_PART4_BYE_VERT.location, null)
                    }
                    in (ripVertEventLagStartTick+RIP_VERT_EVENT_LAG_FADE_IN)..(ripVertEventLagStartTick+RIP_VERT_EVENT_LAG_FADE_IN+RIP_VERT_EVENT_ERASE_DURATION) -> {
                        sm.stop(Nlf2Sounds.MUS_MAN_WEIRD.location, null)
                        sm.stop(Nlf2Sounds.MUS_QUAKE.location, null)
                    }
                }
            }
            // RIP Sanya sounds
            if (ripSanyaEventEnabled) {
                if (TICKER_RIP_SANYA.ticks == 1L) {
                    mc.soundManager.play(MusPart4ByeInstance())
                }
            }
            // RIP Sanya window shake
            val maxDxy = ripSanyaShakeWindowStrength.lerp(0f, RIP_SANYA_EVENT_WINDOW_SHAKE_MAX_DXY.toFloat()).roundToInt()
            if (maxDxy > 0) {
                val w = mc.window
                w.toggleFullScreen()
                w.alwaysOnTop = true
                GLFW.glfwSetInputMode(w.handle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)
                w.sizeLimit(WINDOW_WIDTH_DEFAULT, WINDOW_HEIGHT_DEFAULT)
                val dx = Random.nextInt(-maxDxy, maxDxy)
                val dy = Random.nextInt(-maxDxy, maxDxy)
                w.setPos(MONITOR_WIDTH / 2 + dx, MONITOR_HEIGHT / 2 + dy)
                w.setTitle("!!!") // Sync title
                mc.gui.setScreen(null)
            }
        }
        ClientTickEvents.END_LEVEL_TICK.register {
            if (rainbowTicks >= 0) rainbowTicks++
            if (beforeMeetTick >= 0) beforeMeetTick++
            if (labOverlayTick >= 0) labOverlayTick++
            if (beforeMeetTransTicks > 0) beforeMeetTransTicks--
            if (labOverlayTransTicks > 0) labOverlayTransTicks--
            if (skyColorEffectTicks in 0..skyColorEffectStartDuration + skyColorEffectEndDuration) skyColorEffectTicks++
            if (skyColorEffectTicks == skyColorEffectStartDuration + skyColorEffectEndDuration) skyColorEffectTicks = -1
            if (beforeMeetWaterFluidIn) {
                if (beforeMeetWaterFluidTicks < BEFORE_MEET_WATER_FLUID_TICKS) beforeMeetWaterFluidTicks++
            } else if (beforeMeetWaterFluidTicks > 0) beforeMeetWaterFluidTicks--

            if (manualUpdateTicks > 0) manualUpdateTicks--
            if (event101screenEffectClearTicks > 0) event101screenEffectClearTicks--

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
        ClientRespawnEvents.RESPAWN.register { killedTimes++ }
        ClientPlayConnectionEvents.JOIN.register { _, s, mc ->
            s.sendPacket(JoinSendVersion(FabricLoader.getInstance().getModContainer(MODID).get().metadata.version.friendlyString))
            val snd = mc.soundManager
            snd.play(BorderAmbientSoundInstance())
            snd.play(Event101NoiseSoundInstance())
        }
        ClientPlayConnectionEvents.DISCONNECT.register { _, mc ->
            mc.soundManager.stop(Nlf2Sounds.BORDER_AMBIENT.location, SoundSource.AMBIENT)
        }
        ClientLifecycleEvents.CLIENT_STOPPING.register { closedTimes++ }

        HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR, Layer.BORDER_NOISE) { graphics, _ ->
            graphics.nlf2noise()
            graphics.nlf2event101()
            if (manualUpdateOwner) graphics.overlayWithColor(manualUpdateEffectAmount / 2, 1f, 0f, 0f)
        }
        HudElementRegistry.attachElementAfter(VanillaHudElements.SLEEP, Layer.OVERLAY_FIX) { graphics, _ ->
            val mc = Minecraft.getInstance()
            if (mc.gui.hud.isHidden) {
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
        HudElementRegistry.addLast(Layer.OVERLAY_TEXT_BOX) { graphics, _ ->
            if (shouldRenderTextBox) {
                val t = TICKER_TEXT_BOX.tick().toInt()
                val seq = GuiTextBoxSequence(textBoxSequence)
                seq.tick(t)
                if (t >= seq.start) {
                    shouldRenderTextBox = false
                    TICKER_TEXT_BOX.reset()
                    textBoxSequence = TextBoxSequence()
                    textBoxFadeOut = 0
                    return@addLast
                }

                graphics.pose().pushMatrix()
                if (textBoxShake > 0) {
                    graphics.pose().translate(
                        Random.nextDouble(-textBoxShake, +textBoxShake).toFloat(),
                        Random.nextDouble(-textBoxShake, +textBoxShake).toFloat()
                    )
                }
                seq.extractRenderState(graphics,
                    TICKER_TEXT_BOX.delta(seq.start - textBoxFadeOut, textBoxFadeOut).onlyIf(textBoxFadeOut > 0).reverseDelta()
                )
                graphics.pose().popMatrix()
            }
        }
        HudElementRegistry.addLast(Layer.EVENT_RIP_VERT) { graphics, _ ->
            if (ripVertEventEnabled) {
                TICKER_RIP_VERT.tick()
                if (TICKER_RIP_VERT >= ripVertEventLagStartTick + RIP_VERT_EVENT_LAG_FADE_IN + RIP_VERT_EVENT_ERASE_DURATION) {
                    ripVertEventEnabled = false
                    webcamEnabled = false
                    Minecraft.getInstance().updateTitle()
                    return@addLast
                }
                // Noise
                val noiseForce = TICKER_RIP_VERT.delta(ripVertEventLagStartTick, RIP_VERT_EVENT_LAG_FADE_IN)
                val colors = ArrayList<Int>()
                colors.add(0xffffff)
                for (i in 0..7) {
                    colors.add(ARGB.color(
                        if ((i / 4) == 0) 0xff else 0xe0,
                        if ((i / 2) % 2 == 0) 0xff else 0xe0,
                        if (i % 2 == 0) 0xff else 0xe0
                    ))
                }
                graphics.blitAnim(Animation.NOISE, 1 + noise_tick % 5, color = ARGB.color(noiseForce, colors.random()))
                // ERASE
                if (TICKER_RIP_VERT - ripVertEventLagStartTick - RIP_VERT_EVENT_LAG_FADE_IN in 0..RIP_VERT_EVENT_ERASE_DURATION) {
                    if (TICKER_RIP_VERT.ticks.toInt() == ripVertEventLagStartTick + RIP_VERT_EVENT_LAG_FADE_IN + 5)
                        ClientPlayNetworking.send(RipVertEnd.INSTANCE)
                    webcamEnabled = true
                    Minecraft.getInstance().updateTitle()
                    val font = Minecraft.getInstance().font
                    val str = "ERASE"
                    graphics.black()
                    graphics.pose().pushMatrix()
                    graphics.pose().translate(graphics.guiWidth().toFloat() / 2, graphics.guiHeight().toFloat() / 2)
                    graphics.pose().scale(2f)
                    graphics.text(font, str, - font.width(str) / 2, 0, 0xffffffff.toInt(), false)
                    graphics.pose().popMatrix()
                }
            }
        }
        HudElementRegistry.addLast(Layer.EVENT_RIP_SANYA) { graphics, _ ->
            if (ripSanyaEventEnabled) {
                TICKER_RIP_SANYA.tick()
                if (TICKER_RIP_SANYA >= ripSanyaEventByeTick + RIP_SANYA_EVENT_WHITE_FADE + RIP_SANYA_EVENT_SANYA_TAKE + RIP_SANYA_EVENT_SANYA_TAKE_FADE_OUT) {
                    ripSanyaEventEnabled = false
                    return@addLast
                }
                if (TICKER_RIP_SANYA.ticks.toInt() == ripSanyaEventByeTick + RIP_SANYA_EVENT_WHITE_FADE) {
                    ClientPlayNetworking.send(RipSanyaTakeStart.INSTANCE)
                }
                val alpha = max(
                    TICKER_RIP_SANYA.delta(ripSanyaEventByeTick, ripSanyaEventByeTick + RIP_SANYA_EVENT_WHITE_FADE + 20, RIP_SANYA_EVENT_WHITE_FADE),
                    ripSanyaShakeWindowStrength,
                )
                graphics.white(alpha)
            }
        }
        HudElementRegistry.attachElementBefore(Layer.OVERLAY_TEXT_BOX, Layer.EVENT_RIP_SANYA_BEFORE_BOX) { graphics, _ ->
            if (ripSanyaEventEnabled && TICKER_RIP_SANYA <= ripSanyaEventByeTick + RIP_SANYA_EVENT_WHITE_FADE) {
                graphics.black(TICKER_RIP_SANYA.delta(0, 100) * .5f)
            }
        }

        // Packets
        registerPacketReceiver(BeforeMeetWaterFluid) { payload ->
            beforeMeetWaterFluidIn = payload.isIn
        }
        registerPacketReceiver(ClientNlf2DataSync) { payload ->
            val oldActor = dataSyncedActor
            dataSyncedActor = payload.actor
            if (oldActor != dataSyncedActor)
                AdjustVolumeList.update()

            dataSyncedActorLoreChar = payload.actorChar
            dataSyncedBorderState = payload.borderState

            val oldMeetState = dataSyncedMeetState
            dataSyncedMeetState = payload.meetState
            if (oldMeetState != dataSyncedMeetState) {
                if (oldMeetState != -1 && dataSyncedMeetState in listOf(1, 3))
                    beforeMeetTransTicks = BEFORE_MEET_TRANS_DURATION
                AdjustVolumeList.update()
            }

            val oldLabOverlayState = dataSyncedLabOverlayState
            dataSyncedLabOverlayState = payload.labOverlayState
            if (oldLabOverlayState != dataSyncedLabOverlayState && oldLabOverlayState != -1)
                labOverlayTransTicks = LAB_OVERLAY_TRANS_DURATION

            dataSyncedEvent101 = payload.event101
            dataSyncedManualId = payload.manualId
            dataSyncedCompassCrazy = payload.compassCrazy
            dataSyncedSimulateNickname = payload.simulateNickname
            dataSyncedVisibleActor = payload.visibleActor

            TIMER_MASK.reset()
        }
        registerPacketReceiver(DistanceUpdate) { payload ->
            val maxBlocks = 50.0
            val maxNoise = .5f
            noise_amount = (1f - ((payload.borderDistance.coerceAtMost(maxBlocks)) / maxBlocks)).toFloat() * maxNoise

            event101_distance = payload.event101Distance
            amethyst_daisy_distance = payload.amethystDaisyDistance
        }
        registerPacketReceiver(JoinDisconnectVersion) { payload, ctx ->
            ctx.client().gui.setScreen(JoinDisconnectScreen(payload.client, payload.server))
        }
        registerPacketReceiver(ManualUpdateEffect) { payload ->
            manualUpdateOwner = payload.owner
            manualUpdateTicks = MANUAL_UPDATE_EFFECT_DURATION
        }
        registerPacketReceiver(OpenManual) { payload, ctx ->
            ctx.client().gui.setScreen(ManualScreen(payload.manualId))
        }
        registerPacketReceiver(OpenPuzzle) { payload, ctx ->
            payload.packetPuzzle?.let {
                ctx.client().gui.setScreen(PuzzleScreen(it))
            }
        }
        registerPacketReceiver(OpenTextBox) { payload ->
            TICKER_TEXT_BOX.reset()
            shouldRenderTextBox = true
            textBoxSequence = payload.sequence
            textBoxFadeOut = payload.fadeOut
        }
        registerPacketReceiver(RIPSanyaEvent) { payload ->
            ripSanyaEventByeTick = payload.byeTick
            TICKER_RIP_SANYA.reset()
            ripSanyaEventEnabled = true
        }
        registerPacketReceiver(RIPVertEvent) { payload ->
            ripVertEventShakeStartTick = payload.shakeStartTick
            ripVertEventLagStartTick = payload.lagStartTick
            TICKER_RIP_VERT.reset()
            ripVertEventEnabled = true
        }
        registerPacketReceiver(SkyColorEffect) { payload ->
            skyColorEffectTicks = 0
            skyColorEffectStartDuration = payload.startDuration
            skyColorEffectEndDuration = payload.endDuration
            skyColorEffectColor = payload.color
        }
        registerPacketReceiver(StrangeJukeboxRainbowControl) { payload ->
            rainbowTicks = if (payload.start) 0 else -1
        }

        registerPacketReceiver(CrashClientPayload) {
            throw ROHJEN67SW51("\nYOU...\nREACHED...\n\nTHE END")
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

        registerPacketReceiver(Event101ScreenEffectClear) {
            event101screenEffectClearTicks = EVENT101_SCREEN_EFFECT_CLEAR_DURATION
        }
        registerPacketReceiver(StoryStart) {
            TICKER_STORY.reset()
            shouldRenderStory = true
        }
        registerPacketReceiver(StrangeJukeboxMusicStop) {
            val sm = Minecraft.getInstance().soundManager

            for (mus in Nlf2Sounds.allJukeboxMusic()) {
                sm.stop(mus.location, SoundSource.RECORDS)
            }
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

    private fun GuiGraphicsExtractor.nlf2noise() {
        val noiseTicks = 5
        val noiseCount = 5
        blitAnim(Animation.NOISE, 1 + ((noise_tick % noiseTicks) / (noiseTicks / noiseCount)), color = ARGB.color(noise_amount, 0xffffff))
    }
    private fun GuiGraphicsExtractor.nlf2event101() {
        val darken = Minecraft.getInstance().level?.skyDarken ?: 0
        if (shaderEvent101) overlayWithColor(((shaderEvent101Strength - darken.toFloat() / 7) / 5).coerceIn(0f, .2f), 1f, .5f, 0f)
    }

    object Layer {
        val BORDER_NOISE: Identifier = getIdentifier("layer/border/noise")
        val OVERLAY_FIX: Identifier = getIdentifier("layer/overlay/fix")
        val OVERLAY_PAUSE_SCREEN: Identifier = getIdentifier("layer/overlay/pause_screen")
        val OVERLAY_TEXT_BOX: Identifier = getIdentifier("layer/overlay/text_box")
        val EVENT_RIP_VERT: Identifier = getIdentifier("layer/event/rip_vert")
        val EVENT_RIP_SANYA: Identifier = getIdentifier("layer/event/rip_sanya")
        val EVENT_RIP_SANYA_BEFORE_BOX: Identifier = getIdentifier("layer/event/rip_sanya/before_box")
    }

    object Sprite {
        object Vanilla {
            val VIGNETTE: Identifier = Identifier.withDefaultNamespace("textures/misc/vignette.png")
        }
    }
}
