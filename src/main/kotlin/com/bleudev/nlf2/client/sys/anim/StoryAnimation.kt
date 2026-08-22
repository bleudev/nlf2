@file:Environment(EnvType.CLIENT)

package com.bleudev.nlf2.client.sys.anim

import com.bleudev.nine_lifes.util.lerp
import com.bleudev.nlf2.ANIM_Y_REF
import com.bleudev.nlf2.client.shouldRenderStory
import com.bleudev.nlf2.client.sys.util.GuiTextBoxSequence
import com.bleudev.nlf2.client.util.TICKER_STORY
import com.bleudev.nlf2.client.util.black
import com.bleudev.nlf2.client.util.canUseKeyboard
import com.bleudev.nlf2.client.util.canUseMouse
import com.bleudev.nlf2.client.util.sound.RelativeSoundInstance
import com.bleudev.nlf2.client.util.sound.TickableLoopedAmbientSoundInstance
import com.bleudev.nlf2.custom.Nlf2Sounds
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.StoryEnd
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.hueToTriple
import com.bleudev.nlf2.util.reverseDelta
import de.maxhenkel.voicechat.VoicechatClient
import de.maxhenkel.voicechat.voice.client.ClientManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.sounds.SoundSource
import net.minecraft.util.ARGB
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.random.Random

object StoryAnimation {
    private var playedMusStory = false
    private var playedNoise = false
    private var playedMusManStory = false
    private var preSavedHideIcons = false
    private var preSavedDisabled = false
    internal var endTick = 99999
    private var ruler3Tick = 99999

    @JvmStatic
    val otherVolume: Float get() = max(
        TICKER_STORY.delta(0, 30).reverseDelta(),
        TICKER_STORY.delta(endTick - 60, 60)
    )

    fun extractRenderState(graphics: GuiGraphicsExtractor) {
        if (!shouldRenderStory) return
        val t = TICKER_STORY.tick().toInt()
        graphics.black(otherVolume.reverseDelta())
        if (t == endTick - 60) {
            if (VoicechatClient.CLIENT_CONFIG.hideIcons.get() != preSavedHideIcons) {
                VoicechatClient.CLIENT_CONFIG.hideIcons.set(preSavedHideIcons).save()
            }
            if (ClientManager.getPlayerStateManager().isDisabled != preSavedDisabled) {
                ClientManager.getPlayerStateManager().isDisabled = preSavedDisabled
            }
            ClientPlayNetworking.send(StoryEnd.INSTANCE)
        }
        if (t >= endTick + 5) {
            TICKER_STORY.reset()
            return
        }
        if (t >= endTick) {
            shouldRenderStory = false
            playedMusStory = false
            playedNoise = false
            playedMusManStory = false
            canUseMouse = true
            canUseKeyboard = true
            val sm = Minecraft.getInstance().soundManager
            sm.stop(Nlf2Sounds.MUS_STORY.location, SoundSource.AMBIENT)
            sm.stop(Nlf2Sounds.MUS_MAN_STORY.location, SoundSource.AMBIENT)
            sm.stop(Nlf2Sounds.NOISE.location, SoundSource.AMBIENT)
            return
        }
        if (t >= endTick - 60) {
            canUseMouse = true
            canUseKeyboard = true
            return
        }
        if (t == 10) {
            preSavedHideIcons = VoicechatClient.CLIENT_CONFIG.hideIcons.get()
            preSavedDisabled = ClientManager.getPlayerStateManager().isDisabled
        }
        if (t >= 10) {
            canUseMouse = false
            canUseKeyboard = false
            Minecraft.getInstance().gui.setScreen(null)
            if (!VoicechatClient.CLIENT_CONFIG.hideIcons.get()) {
                VoicechatClient.CLIENT_CONFIG.hideIcons.set(true).save()
            }
            if (!ClientManager.getPlayerStateManager().isDisabled) {
                ClientManager.getPlayerStateManager().isDisabled = true
            }
        }

        val seq = GuiTextBoxSequence()
        seq.tick(t)
        seq.speed(2)
        seq.add("Давным-Давно...", 20)
        val appear1 = seq.start
        seq.add("В Мире жили&ТРИ различных Личности", 10)
        seq.add("ТРИ различных Образа&ТРИ различных Характера", 20)
        seq.speed(5)
        seq.add("ТРИ...   &СОСУДА", 20)
        seq.wait(10)
        seq.speed(2)
        seq.add("Они жили в Мире,&В Гармонии.", 20)
        val soul1 = seq.start
        seq.add("И с годами&Меняясь местами...", 20)
        val soul2 = seq.start
        seq.add("ДУША переходила&Из одного Сосуда&В другой", 30)
        seq.add("Так продолжалось&До Одного Момента...", 20)
        val soul3 = seq.start
        seq.add("Один из Сосудов&Покинул это Место", 20)
        val soul4 = seq.start
        seq.add("Навстречу Новому Миру&Навстречу УСПЕХУ", 20)
        val soul5 = seq.start
        seq.add("Оставив Остальных&Навечно Вдвоём...", 20)
        val soul6 = seq.start
        seq.add("Но Один Сосуд&Не пал Духом", 20)
        seq.speed(2)
        val ruler1 = seq.start
        seq.add("Он стал Властителем&Всего Мира", 30)
        val ruler2 = seq.start
        seq.add("И в порыве&Зависти и Гнева", 10)
        seq.add("Лишь Одна Цель&Его волновала...", 20)
        seq.speed(3)
        val ruler3 = seq.start
        seq.add("МЕСТЬ", 30)
        seq.add("Ведь...", 10)
        seq.speed(2)
        seq.add("Почему&Именно&ОН?", 60)
        val end = seq.start - 60

        var rouge = 1f
        var bleu = 1f
        var vert = 1f
        var rougeOutline = 1f
        var bleuOutline = 1f
        var vertOutline = 1f
        var rougeSoul = 0f
        var bleuSoul = 0f
        var vertSoul = 0f
        var rougeInCenter = false
        var rougeCrown = 1f
        var rougeGradient = 0f
        var noise = 0f

        if (t <= appear1 + 80) {
            rouge = ((t - appear1).toFloat() / 40).coerceIn(0f, 1f)
            rougeOutline = rouge
            bleu = ((t - appear1 - 20).toFloat() / 40).coerceIn(0f, 1f)
            bleuOutline = bleu
            vert = ((t - appear1 - 40).toFloat() / 40).coerceIn(0f, 1f)
            vertOutline = vert
        }
        if (t in soul1..soul2) {
            rouge = ((t - soul1).toFloat() / 20).coerceIn(0f, 1f).reverseDelta() / 2 + .5f
            bleu = rouge
            vert = rouge
        }
        val soulDeltas = ((t - soul2).toFloat() / 60).hueToTriple()
        val preSoulDelta = when (t) {
            in soul2..soul2+30 -> ((t - soul2).toFloat() / 30).coerceIn(0f, 1f)
            in soul3-30..soul3 -> ((t - soul3 + 30).toFloat() / 30).coerceIn(0f, 1f).reverseDelta()
            else -> 1f
        }
        if (t in soul2..soul3) {
            rouge = .5f
            rougeSoul = soulDeltas.first * preSoulDelta
            bleu = .5f
            bleuSoul = soulDeltas.second * preSoulDelta
            vert = .5f
            vertSoul = soulDeltas.third * preSoulDelta

            if (t in soul3-30..soul3) {
                rouge = preSoulDelta.reverseDelta() / 2 + .5f
                bleu = rouge
                vert = rouge
            }
        }
        if (t in soul3..soul4) {
            bleuSoul = 1f
            bleu = ((t - soul3).toFloat() / 60).coerceIn(0f, 1f).reverseDelta()
            bleuOutline = bleu
        }
        if (t in soul4..soul5) {
            bleuSoul = ((t - soul4).toFloat() / 60).coerceIn(0f, 1f).reverseDelta()
        }
        if (t >= soul4) {
            bleu = 0f
            bleuOutline = 0f
        }
        if (t in soul5..soul6) {
            rouge = ((t - soul5).toFloat() / 60).coerceIn(0f, 1f).reverseDelta()
            rougeOutline = rouge
            vert = rouge
            vertOutline = rouge
        }
        if (t >= soul6) {
            rougeInCenter = true
            rouge = 1f
            rougeOutline = 1f
            vert = 0f
            vertOutline = 0f
        }
        if (t <= ruler1) {
            rougeCrown = 0f
        }
        if (t in ruler1..ruler2) {
            rougeCrown = ((t - ruler1).toFloat() / 60).coerceIn(0f, 1f)
        }
        if (t >= ruler2) {
            rougeGradient = ((t - ruler2).toFloat() / 100).coerceIn(0f, 1f)
        }
        if (t >= ruler3) {
            noise = ((t - ruler3).toFloat() / 120).coerceIn(0f, 1f)
        }

        graphics.anim(
            listOf(Sprite.NOISE_0, Sprite.NOISE_1, Sprite.NOISE_2).random(),
            noise * .15f,
            listOf(0xffffff, 0xff0000, 0x00ff00, 0x0000ff).random(),
            0f, 0f
        )

        graphics.pose().pushMatrix()
        graphics.pose().translate(
            Random.nextDouble(-2.5, 2.5).toFloat() * noise,
            Random.nextDouble(-2.5, 2.5).toFloat() * noise
        )
        graphics.pose().pushMatrix()
        if (!rougeInCenter) {
            graphics.pose().translate(t.circlex(60, 30)/3, t.circley(60, 30)/3)
        }
        seq.extractRenderState(graphics)
        graphics.pose().popMatrix()
        graphics.pose().scale(graphics.guiHeight().toFloat() / ANIM_Y_REF)
        graphics.pose().translate(120f, 60f)
        graphics.pose().scale(3.5f)

        graphics.pose().pushMatrix()
        graphics.pose().translate(t.circlex(60, 0), t.circley(60, 0))
        if (!rougeInCenter) {
            graphics.anim(Sprite.VESSEL_OUTLINE_UNI, rougeOutline, 0xff0000, 0f, 0f)
            graphics.anim(Sprite.VESSEL_UNI, rouge, 0xff0000, 0f, 0f)
            graphics.anim(Sprite.SOUL_UNI, rougeSoul, 0xff0000, 3.5f, 6.5f, .5f)
        }
        graphics.pose().popMatrix()

        graphics.pose().pushMatrix()
        if (!rougeInCenter) {
            graphics.pose().translate(t.circlex(60, 10), t.circley(60, 10))
        }
        graphics.anim(Sprite.VESSEL_OUTLINE_UNI, bleuOutline, 0x3622ff, 20f, -5f)
        graphics.anim(Sprite.VESSEL_UNI, bleu, 0x3622ff, 20f, -5f)
        graphics.anim(Sprite.SOUL_UNI, bleuSoul, 0x3622ff, 23.5f, 1.5f, .5f)
        if (rougeInCenter) {
            graphics.anim(Sprite.CROWN_UNI, rougeCrown, 0xff0000, 20f, -7.5f, .5f)
            graphics.anim(Sprite.VESSEL_OUTLINE_UNI, rougeOutline, 0xff0000, 20f, -5f)
            graphics.anim(Sprite.VESSEL_UNI, rouge, 0xff0000, 20f, -5f)
            graphics.anim(Sprite.SOUL_UNI, rougeSoul, 0xff0000, 23.5f, 1.5f, .5f)
            graphics.anim(Sprite.SMILE, rougeGradient, 0xffffff, 23f, -4.6f, .17f)
        }
        graphics.pose().popMatrix()

        graphics.pose().pushMatrix()
        graphics.pose().translate(t.circlex(60, 20), t.circley(60, 20))
        graphics.anim(Sprite.VESSEL_OUTLINE_UNI, vertOutline, 0x00c100, 40f, 0f)
        graphics.anim(Sprite.VESSEL_UNI, vert, 0x00c100, 40f, 0f)
        graphics.anim(Sprite.SOUL_UNI, vertSoul, 0x00c100, 43.5f, 6.5f, .5f)
        graphics.pose().popMatrix()

        graphics.pose().popMatrix()

        graphics.fillGradient(0, 0, graphics.guiWidth(), graphics.guiHeight(), 0, ARGB.color(rougeGradient * .2f, 0xff0000))

        graphics.black(when (t) {
            in end..<end+20 -> .25f
            in end+20..<end+40 -> .5f
            in end+40..<end+60 -> .75f
            in end+60..end+8000 -> 1f // idk how to do that normal way
            else -> 0f
        })

        // Music + sounds
        val sm = Minecraft.getInstance().soundManager
        if (t in 5..25 && !playedMusStory) {
            sm.play(MusStoryInstance(soul6))
            playedMusStory = true
        }
        if (t in soul6..soul6+20) {
            if (!playedNoise) {
                sm.play(object : RelativeSoundInstance(Nlf2Sounds.NOISE) {
                    init {
                        this.volume = .5f
                    }
                })
                playedNoise = true
            }
            if (!playedMusManStory) {
                sm.play(MusManStoryInstance(end + 60))
                playedMusManStory = true
            }
        }
        // Other
        if (endTick == 99999) endTick = end + 120
        if (ruler3Tick == 99999) ruler3Tick = ruler3
    }

    private class MusStoryInstance(val endTick: Int) : TickableLoopedAmbientSoundInstance(Nlf2Sounds.MUS_STORY) {
        override fun vol(): Float =
            ((TICKER_STORY.ticks - endTick + 40).toFloat() / 40).coerceIn(0f, 1f).reverseDelta() / 2
        override fun pit(): Float = 1f
    }
    private class MusManStoryInstance(val endTick: Int) : TickableLoopedAmbientSoundInstance(Nlf2Sounds.MUS_MAN_STORY) {
        override fun tick() {
            super.tick()
        }
        override fun vol(): Float =
            ((TICKER_STORY.ticks - endTick + 40).toFloat() / 40).coerceIn(0f, 1f).reverseDelta() * 0.75f
        override fun pit(): Float =
            ((TICKER_STORY.ticks - ruler3Tick).toFloat() / 200).coerceIn(0f, 1f).lerp(1f, 2f)
    }

    private fun Int.circlex(duration: Int, offset: Int): Float {
        val angle = (((this + offset) % duration).toFloat() / duration) * 2 * PI
        return cos(angle).toFloat() * .3f
    }
    private fun Int.circley(duration: Int, offset: Int): Float {
        val angle = (((this + offset) % duration).toFloat() / duration) * 2 * PI
        return sin(angle).toFloat() * .3f
    }

    private fun GuiGraphicsExtractor.anim(sprite: Sprite, alpha: Float, rgb: Int, x: Float, y: Float, size: Float = 1f) {
        pose().pushMatrix()
        pose().translate(x, y)
        pose().scale(size)
        blit(
            RenderPipelines.GUI_TEXTURED,
            getIdentifier("textures/anim/story/${sprite.filename}.png"),
            0, 0, 0f, 0f,
            sprite.width, sprite.height, sprite.width, sprite.height,
            ARGB.color(alpha, rgb),
        )
        pose().popMatrix()
    }

    private enum class Sprite(val filename: String, val width: Int, val height: Int) {
        CROWN_UNI("crown_uni", 16, 6),
        SOUL_UNI("soul_uni", 9, 9),
        VESSEL_OUTLINE_UNI("vessel_outline_uni", 11, 20),
        VESSEL_UNI("vessel_uni", 11, 20),
        NOISE_0("noise_0", 1920, 1080),
        NOISE_1("noise_1", 1920, 1080),
        NOISE_2("noise_2", 1920, 1080),
        SMILE("smile", 28, 27),
    }
}