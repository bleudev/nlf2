package com.bleudev.nlf2.mixin.client.voicechat;

import com.bleudev.nlf2.client.sys.actor.chars.ActorChar;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import de.maxhenkel.voicechat.voice.client.RenderEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

import static com.bleudev.nlf2.client.sys.actor.ActorCharEntrypointClient.current;

@Environment(EnvType.CLIENT)
@Mixin(RenderEvents.class)
public class RenderEventsMixin {
    @Unique
    private boolean isEntityForceDisconnected(Entity entity) {
        if (entity instanceof Player player) {
            ActorChar actorChar = current(player.getGameProfile().name());
            if (actorChar != null) {
                return actorChar.isForceNameTagVoiceDisconnected();
            }
        }
        return false;
    }

    @WrapOperation(method = "onRenderName", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;equals(Ljava/lang/Object;)Z"))
    private boolean renderIconEvenIfEntityIsClient(Entity instance, Object obj, Operation<Boolean> original) {
        return !isEntityForceDisconnected(instance) && original.call(instance, obj);
    }
    @WrapOperation(method = "onRenderName", at = @At(value = "INVOKE", target = "Lde/maxhenkel/voicechat/voice/client/ClientPlayerStateManager;isPlayerDisconnected(Ljava/util/UUID;)Z"))
    private boolean forceDisconnectPlayer(ClientPlayerStateManager instance, UUID playerId, Operation<Boolean> original, @Local(name = "entity") Entity entity) {
        return isEntityForceDisconnected(entity) || original.call(instance, playerId);
    }
}
