package com.bleudev.nlf2.mixin.client.voicechat;

import de.maxhenkel.voicechat.gui.volume.AdjustVolumeList;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static com.bleudev.nlf2.client.util.ClientCallbacksKt.renderPlayerInVoiceChat;

@Environment(EnvType.CLIENT)
@Mixin(AdjustVolumeList.class)
public class AdjustVolumeListMixin {
    @Redirect(method = "updateEntryList", at = @At(value = "INVOKE", target = "Lde/maxhenkel/voicechat/voice/client/ClientPlayerStateManager;getPlayerStates(Z)Ljava/util/List;"))
    private List<PlayerState> removeSpecificPlayersFromList(ClientPlayerStateManager instance, boolean includeSelf) {
        return instance.getPlayerStates(includeSelf).stream()
            .filter((state) -> renderPlayerInVoiceChat(state.getName())).toList();
    }
}
