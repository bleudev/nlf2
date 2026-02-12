package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.interfaces.mixin.client.IAvatarRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements IAvatarRenderState {
    @Unique
    private String nickname;

    @Override
    public String nlf2$getNickname() {
        return nickname;
    }

    @Override
    public void nlf2$setNickname(String newNickname) {
        nickname = newNickname;
    }

    @Unique
    private boolean armor = false;

    @Override
    public boolean nlf2$shouldRenderArmor() {
        return armor;
    }

    @Override
    public void nlf2$setShouldRenderArmor(boolean newShouldRenderArmor) {
        armor = newShouldRenderArmor;
    }
}
