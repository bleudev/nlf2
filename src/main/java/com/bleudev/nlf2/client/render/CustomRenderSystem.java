package com.bleudev.nlf2.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import org.jetbrains.annotations.Nullable;

public class CustomRenderSystem {
    private static GpuBuffer anaglyph = null;

    public static void setShaderAnaglyph(@Nullable GpuBuffer bufferSlice) {
        anaglyph = bufferSlice;
    }

    public static @Nullable GpuBuffer getShaderAnaglyph() {
        return anaglyph;
    }
}
