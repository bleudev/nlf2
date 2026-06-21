package com.bleudev.nlf2.client.custom.gui.screens

import com.bleudev.nlf2.client.util.black
import net.minecraft.CrashReport
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import unknown.depths.r.man.ROHJEN67SW51

class JoinDisconnectScreen(val client: String, val server: String) : Screen(Component.literal(":(")) {
    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        graphics.black()
        val l = (1.5 * minecraft.font.lineHeight).toInt()
        listOf(
            ".-.. --- --- -.- ... / .-.. .. -.- . / -.-- --- ..- / -. . . -..",
            "- --- / ..- .--. -.. .- - . / ... --- -- . - .... .. -. --. .-.-.- .-.-.- .-.-.-",
            "$client -> $server"
        ).forEachIndexed { index, string ->
            graphics.centeredText(minecraft.font, string, width / 2, height / 2 + l * index, 0xffff0000.toInt())
        }
    }
    override fun onClose() {
        minecraft.delayCrash(CrashReport("...", ROHJEN67SW51("\nYOU KNOW...\nWHAT\nTO DO...")))
    }
}