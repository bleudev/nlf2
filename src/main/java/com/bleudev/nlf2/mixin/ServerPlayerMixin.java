package com.bleudev.nlf2.mixin;

import com.bleudev.nlf2.custom.world.level.saveddata.Nlf2Data;
import com.bleudev.nlf2.custom.world.level.saveddata.Nlf2DataKt;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @WrapMethod(method = "hurtServer")
    private boolean wrapHurtServer(ServerLevel level, DamageSource source, float damage, Operation<Boolean> original) {
        Nlf2Data.LoreData loreData = Nlf2DataKt.getNlf2data(level.getServer()).getLoreData();
        float health = getHealth();
        boolean bl1 = getGameProfile().name().equalsIgnoreCase(loreData.getActor());
        boolean bl2 = damage >= getHealth();
        boolean bl3 = loreData.getInBattle();
        if (!bl1 && bl2 && bl3) {
            float newHealth = Math.max(health - damage, 1f);
            if (health != newHealth) {
                return original.call(level, source, health - newHealth);
            } else {
                return false;
            }
        }
        return original.call(level, source, damage);
    }
}
