package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.client.custom.renderer.entity.layers.AvatarEyesLayer;
import com.bleudev.nlf2.interfaces.mixin.client.IAvatarRenderState;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.util.AvatarSkinUtilKt.getLoreCharacter;

@Environment(EnvType.CLIENT)
@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarLike extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<@NotNull AvatarLike, @NotNull AvatarRenderState, @NotNull PlayerModel> {
    public AvatarRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
        super(context, model, shadow);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addEyesLayer(EntityRendererProvider.Context context, boolean slimSteve, CallbackInfo ci) {
        this.addLayer(new AvatarEyesLayer(this));
    }

    @ModifyReturnValue(method = "shouldShowName", at = @At("RETURN"))
    private boolean doNOTshow(boolean original) {
        return false;
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void setStateNickname(AvatarLike entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if (entity instanceof Player player) {
            IAvatarRenderState renderState = (IAvatarRenderState) state;
            final var nickname = player.getGameProfile().name().toLowerCase();
            renderState.nlf2$setNickname(nickname);

            if (getLoreCharacter(nickname).isInvisibleLike()) {
                renderState.nlf2$setShouldRenderArmor(false);
                state.isInvisible = true;
                state.leftHandItemState.clear();
                state.leftHandItemStack = ItemStack.EMPTY;
                state.rightHandItemState.clear();
                state.rightHandItemStack = ItemStack.EMPTY;
            } else {
                renderState.nlf2$setShouldRenderArmor(true);
            }
        }
    }

//    @Override
//    protected float getShadowRadius(@NotNull AvatarRenderState state) {
//        if (getLoreCharacter(((IAvatarRenderState) state).nlf2$getNickname()).isInvisibleLike()) {
//            System.out.println("INVISIBLE LIKE SHADOW RENDERING MIXIN");
//            return 0f;
//        }
//        System.out.println("NOT INVISIBLE SHADOW RENDERING MIXIN");
//        return super.getShadowRadius(state);
//    }
}
