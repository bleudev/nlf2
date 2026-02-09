package com.bleudev.nlf2.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jetbrains.annotations.Nullable;

import static com.bleudev.nlf2.Nlf2ConstKt.*;
import static com.bleudev.nlf2.Nlf2Kt.getIdentifier;

public class AvatarSkinHelper {
    private static @Nullable Identifier getTexture(String base, String entity_nickname, String client_nickname, boolean withFormat) {
        if (!PLAYERS_NICKNAMES.contains(client_nickname)) return null;

        String lore_char = "";
        switch (entity_nickname) {
            case VERT_NICKNAME -> lore_char = "vert";
            case ROUGE_NICKNAME -> lore_char = "rouge";
        }
        if (lore_char.isEmpty()) return null;

        base += lore_char + "/" + client_nickname;
        if (withFormat) return getIdentifier(base + ".png");
        else return getIdentifier(base);
    }

    public static Identifier EYES_EMPTY = getIdentifier("textures/entity/player/eyes/empty.png");

    public static @Nullable Identifier getEyesTexture(String entity_nickname, String client_nickname) {
        return getTexture("textures/entity/player/eyes/", entity_nickname, client_nickname, true);
    }
    public static @Nullable Identifier getEyesTexture(String entity_nickname) {
        return getEyesTexture(entity_nickname, Minecraft.getInstance().getGameProfile().name().toLowerCase());
    }

    public static @Nullable Identifier getSkinTexture(String entity_nickname, String client_nickname) {
        return getTexture("entity/player/skin/", entity_nickname, client_nickname, false);
    }

    public static @Nullable PlayerSkin getSkin(String entity_nickname, String client_nickname) {
        var texture = getSkinTexture(entity_nickname, client_nickname);
        System.out.println("SKIN " + texture);
        if (texture == null) return null;

        PlayerModelType modelType = PlayerModelType.WIDE;
        // If there will be slim skins, will add code for them
        return new PlayerSkin(new ClientAsset.ResourceTexture(texture), null, null, modelType, true);
    }
    public static @Nullable PlayerSkin getSkin(String entity_nickname) {
        return getSkin(entity_nickname, Minecraft.getInstance().getGameProfile().name().toLowerCase());
    }
}
