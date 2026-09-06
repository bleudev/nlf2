package com.bleudev.nlf2.client.custom.renderer.blockentity

import com.bleudev.nlf2.client.Nlf2Client
import com.bleudev.nlf2.client.custom.renderer.blockentity.state.ComputerRenderState
import com.bleudev.nlf2.custom.world.level.block.entity.ComputerBlockEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.world.phys.Vec3

class ComputerRenderer(val context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<ComputerBlockEntity, ComputerRenderState> {
    override fun createRenderState(): ComputerRenderState = ComputerRenderState()

    override fun extractRenderState(
        blockEntity: ComputerBlockEntity,
        state: ComputerRenderState,
        partialTicks: Float,
        cameraPosition: Vec3,
        breakProgress: ModelFeatureRenderer.CrumblingOverlay?
    ) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress)
        state.nameTagLines = Nlf2Client.getComputerMessage(blockEntity.blockPos)
        state.nameTagAttachment = Vec3(0.5, 1.0, 0.5)
    }

    override fun submit(
        state: ComputerRenderState,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        camera: CameraRenderState
    ) {
        poseStack.pushPose()
        val yo = 9.0f * 1.15f * 0.025f
        val lines = state.nameTagLines
        poseStack.translate(0f, yo * (lines.size-1), 0f)
        for (curr in lines) {
            submitNodeCollector.submitNameTag(poseStack, state.nameTagAttachment, 0, curr, true, 15728880, camera)
            poseStack.translate(0f, -yo, 0f)
        }
        poseStack.popPose()
    }
}