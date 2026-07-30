package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.bleudev.nlf2.client.sys.actor.ActorCharEntrypointClient.skin;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {
    @ModifyReturnValue(method = "getSkin", at = @At("RETURN"))
    private PlayerSkin modifySkinForSomeLore(PlayerSkin original) {
        var skin = skin(((AbstractClientPlayer) (Object) this).getGameProfile().name());
        if (skin == null) return original;
        return skin;
    }
}
