package com.bleudev.nlf2.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static com.bleudev.nlf2.client.ClientStorageKt.dataSyncedActor;
import static com.bleudev.nlf2.util.InjectsKt.isActor;

@Mixin(Player.class)
public abstract class PlayerMixin extends Avatar {
    @Shadow
    @Final
    private GameProfile gameProfile;

    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean isTransmittingWaypoint() {
        if (((Player) (Object) this) instanceof ServerPlayer serverPlayer) {
            return !isActor(serverPlayer);
        }
        return !(gameProfile.name().equalsIgnoreCase(dataSyncedActor));
    }
}
