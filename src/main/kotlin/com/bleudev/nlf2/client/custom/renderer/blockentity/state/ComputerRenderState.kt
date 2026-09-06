package com.bleudev.nlf2.client.custom.renderer.blockentity.state

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

class ComputerRenderState : BlockEntityRenderState() {
    var nameTagLines: List<Component> = listOf()
    var nameTagAttachment: Vec3? = null
}