package com.bleudev.nlf2.mixin;

import com.bleudev.nlf2.custom.Nlf2Items;
import com.bleudev.nlf2.custom.packet.payload.OpenManual;
import com.bleudev.nlf2.custom.packet.payload.OpenPuzzle;
import com.bleudev.nlf2.custom.packet.payload.PacketPuzzle;
import com.bleudev.nlf2.custom.packet.payload.unit.BroadcastNewCardCheck;
import com.bleudev.nlf2.custom.world.level.saveddata.Nlf2Data;
import com.bleudev.nlf2.custom.world.level.saveddata.Nlf2DataKt;
import com.bleudev.nlf2.i.mixin.IPlayer;
import com.bleudev.nlf2.i.mixin.IServerPlayer;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements IPlayer, IServerPlayer {
    @Unique
    private boolean isForceVisible = false;

    @Shadow
    public abstract @NonNull ServerLevel level();

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

    @Unique
    private void sendPacket(CustomPacketPayload customPacketPayload) {
        ServerPlayNetworking.send((ServerPlayer) (Object) this, customPacketPayload);
    }

    @Inject(method = "openItemGui", at = @At("TAIL"))
    private void openCustomGuis(ItemStack itemStack, InteractionHand hand, CallbackInfo ci) {
        if (itemStack.is(Nlf2Items.WD_PUZZLE)) {
            sendPacket(new OpenPuzzle(PacketPuzzle.WD));
        }
        else if (itemStack.is(Nlf2Items.MANUAL)) {
            sendPacket(new OpenManual(Nlf2DataKt.getNlf2data(level().getServer()).getLoreData().getManualId()));
        }
    }

    @Override
    public void broadcastNewCardCheck() {
        sendPacket(BroadcastNewCardCheck.INSTANCE);
    }

    @Override
    public void setIsForceVisible(boolean isForceVisible) {
        this.isForceVisible = isForceVisible;
    }

    @Override
    public boolean isForceVisible() {
        return this.isForceVisible;
    }

    @ModifyExpressionValue(
        method = "broadcastToPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"
        )
    )
    private boolean makeSpecForceVisible(boolean original) {
        return original && !isForceVisible();
    }
}
