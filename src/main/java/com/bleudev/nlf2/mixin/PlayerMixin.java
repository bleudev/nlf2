package com.bleudev.nlf2.mixin;

import com.bleudev.nlf2.i.mixin.IPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Player.class)
public class PlayerMixin implements IPlayer {
    @Override
    public void broadcastNewCardCheck() {
    }
}
