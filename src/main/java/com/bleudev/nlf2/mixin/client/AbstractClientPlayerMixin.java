package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.bleudev.nlf2.Nlf2ConstKt.*;
import static com.bleudev.nlf2.Nlf2Kt.getIdentifier;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {
    @ModifyReturnValue(method = "getSkin", at = @At("RETURN"))
    private PlayerSkin modifySkinForSomeLore(PlayerSkin original) {
        String nickname = ((AbstractClientPlayer) (Object) this).getGameProfile().name().toLowerCase();
        String player_nickname = Minecraft.getInstance().getGameProfile().name().toLowerCase();
        if (!PLAYERS_NICKNAMES.contains(player_nickname)) return original;

        String lore_char = "";
        switch (nickname) {
            case VERT_NICKNAME -> lore_char = "vert";
            case ROUGE_NICKNAME -> lore_char = "rouge";
        }

        PlayerModelType modelType = PlayerModelType.WIDE;
        // If there will be slim skins, will add code for them

        if (lore_char.isEmpty()) return original;
        else return new PlayerSkin(new ClientAsset.ResourceTexture(getIdentifier("entity/player/skin/" + lore_char + "/" + player_nickname)), null, null, modelType, true);
    }
}
