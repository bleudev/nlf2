package com.bleudev.nlf2.sys.util

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

open class TextBoxSequence(lines: List<TextBoxInfo> = listOf()) {
    data class TextBoxInfo(val text: String, val speed: Int, val start: Int, val end: Int, val color: Int) {
        companion object {
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TextBoxInfo> = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, TextBoxInfo::text,
                ByteBufCodecs.INT, TextBoxInfo::speed,
                ByteBufCodecs.INT, TextBoxInfo::start,
                ByteBufCodecs.INT, TextBoxInfo::end,
                ByteBufCodecs.INT, TextBoxInfo::color,
                ::TextBoxInfo
            )
        }
    }

    protected val lines: ArrayList<TextBoxInfo> = ArrayList(lines)

    protected var internalSpeed: Int = lines.lastOrNull()?.speed ?: 1
    protected var internalColor: Int = lines.lastOrNull()?.color ?: TextColorConstants.DEFAULT
    fun speed(new: Int) { internalSpeed = new }
    fun color(new: Int) { internalColor = new }

    var start: Int = lines.lastOrNull()?.end ?: 0
        private set

    fun add(text: String, wait: Int) {
        val end = start + text.replace("&", "").length * internalSpeed + wait
        lines.add(TextBoxInfo(text, internalSpeed, start, end, internalColor))
        start = end
    }
    fun wait(duration: Int) {
        start += duration
    }

    fun extractLines() = lines.toList()

    companion object {
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TextBoxSequence> = object : StreamCodec<RegistryFriendlyByteBuf, TextBoxSequence> {
            override fun encode(output: RegistryFriendlyByteBuf, value: TextBoxSequence) {
                val lines = value.extractLines()
                output.writeInt(lines.size)
                for (line in lines) {
                    TextBoxInfo.STREAM_CODEC.encode(output, line)
                }
            }

            override fun decode(input: RegistryFriendlyByteBuf): TextBoxSequence {
                val s = input.readInt()
                val lines = ArrayList<TextBoxInfo>()
                repeat(s) {
                    lines.add(TextBoxInfo.STREAM_CODEC.decode(input))
                }
                return TextBoxSequence(lines)
            }
        }
    }
}
