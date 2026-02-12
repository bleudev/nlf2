package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.interfaces.mixin.client.IHumanoidRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(HumanoidRenderState.class)
public class HumanoidRenderStateMixin implements IHumanoidRenderState {
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
