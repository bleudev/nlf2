package com.bleudev.nlf2.client.custom.renderer.entity.layers;

import com.bleudev.nlf2.client.util.AvatarSkinHelper;
import com.bleudev.nlf2.interfaces.mixin.client.IAvatarRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;

import static com.bleudev.nlf2.client.util.AvatarSkinHelper.EYES_EMPTY;

public class AvatarEyesLayer extends RenderLayer<@NotNull AvatarRenderState, @NotNull PlayerModel> {
    private static final RenderType RT_EMPTY = RenderTypes.eyes(EYES_EMPTY);

    public AvatarEyesLayer(RenderLayerParent<@NotNull AvatarRenderState, @NotNull PlayerModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, @NotNull AvatarRenderState state, float yRot, float xRot) {
        String nickname = ((IAvatarRenderState) state).nlf2$getNickname();
        submitNodeCollector.order(1).submitModel(this.getParentModel(), state, poseStack, this.getRenderType(nickname), lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }

    public @NotNull RenderType getRenderType(String nickname) {
        var eyes = AvatarSkinHelper.getEyesTexture(nickname);
        System.out.println("EYES " + eyes);
        if (eyes == null) return RT_EMPTY;
        return RenderTypes.eyes(eyes);
    }
}
