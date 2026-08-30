package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.client.custom.renderer.entity.layers.AvatarOverlayLayer;
import com.bleudev.nlf2.client.sys.actor.ActorCharEntrypointClient;
import com.bleudev.nlf2.interfaces.mixin.client.IAvatarRenderState;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static com.bleudev.nlf2.client.sys.actor.ActorCharEntrypointClient.current;
import static com.bleudev.nlf2.client.util.ClientCallbacksKt.modifyPlayerNameTag;
import static com.bleudev.nlf2.client.util.ClientCallbacksKt.renderPlayerNameTag;

@Environment(EnvType.CLIENT)
@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarLike extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<@NotNull AvatarLike, @NotNull AvatarRenderState, @NotNull PlayerModel> {
    public AvatarRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
        super(context, model, shadow);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addOverlayLayers(EntityRendererProvider.Context context, boolean slimSteve, CallbackInfo ci) {
        this.addLayer(new AvatarOverlayLayer(this, ActorCharEntrypointClient::overlay));
        this.addLayer(new AvatarOverlayLayer(this, ActorCharEntrypointClient::eyes));
    }

    @WrapMethod(method = "shouldShowName(Lnet/minecraft/world/entity/Avatar;D)Z")
    private boolean doNOTshow(AvatarLike entity, double distanceToCameraSq, Operation<Boolean> original) {
        Optional<GameProfile> gameProfile = Optional.empty();
        if (entity instanceof Player player) {
            gameProfile = Optional.of(player.getGameProfile());
        }
        if (entity instanceof Mannequin mannequin) {
            gameProfile = Optional.of(mannequin.getProfile().partialProfile());
        }
        return gameProfile.map(p -> renderPlayerNameTag(p.name())).orElse(false);
    }

    @WrapMethod(method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V")
    private void modifyNameTag(AvatarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, Operation<Void> original) {
        state.nameTag = modifyPlayerNameTag(state.nameTag);
        original.call(state, poseStack, submitNodeCollector, camera);
    }

    @Inject(method = "extractRenderState*", at = @At("TAIL"))
    private void setStateNickname(AvatarLike entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        String nickname = null;
        if (entity instanceof Player player) {
            nickname = player.getGameProfile().name();
        }
        if (entity instanceof Mannequin mannequin) {
            nickname = mannequin.getProfile().name().orElse(null);
        }

        if (nickname != null) {
            nickname = nickname.toLowerCase();
            IAvatarRenderState renderState = (IAvatarRenderState) state;
            renderState.nlf2$setNickname(nickname);

            var ch = current(nickname);
            if (ch != null) {
                if (ch.isInvisible()) {
                    state.isInvisible = true;
                } else if (ch.isForceVisible()) {
                    state.isInvisible = false;
                    state.isSpectator = false;
                }
                if (ch.isInvisible() || ch.getHideHandItems()) {
                    state.leftHandItemState.clear();
                    state.leftHandItemStack = ItemStack.EMPTY;
                    state.rightHandItemState.clear();
                    state.rightHandItemStack = ItemStack.EMPTY;
                    state.rightArmPose = HumanoidModel.ArmPose.EMPTY;
                }
                renderState.nlf2$setShouldRenderArmor(!ch.getHideArmor());
            }
        }
    }
}
