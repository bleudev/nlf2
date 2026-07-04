package com.bleudev.nlf2.client.custom.renderer.entity.layers;

import com.bleudev.nlf2.interfaces.mixin.client.IAvatarRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;

public class AvatarOverlayLayer extends RenderLayer<@NotNull AvatarRenderState, @NotNull PlayerModel> {
    private final RenderTypeSupplier renderTypeSupplier;

    public AvatarOverlayLayer(RenderLayerParent<@NotNull AvatarRenderState, @NotNull PlayerModel> renderer, RenderTypeSupplier renderTypeSupplier) {
        super(renderer);
        this.renderTypeSupplier = renderTypeSupplier;
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, @NotNull AvatarRenderState state, float yRot, float xRot) {
        String nickname = ((IAvatarRenderState) state).nlf2$getNickname();
        submitNodeCollector.order(1).submitModel(this.getParentModel(), state, poseStack, renderTypeSupplier.supply(nickname), lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }

    @FunctionalInterface
    public interface RenderTypeSupplier {
        RenderType supply(String entityNickname);
    }
}
