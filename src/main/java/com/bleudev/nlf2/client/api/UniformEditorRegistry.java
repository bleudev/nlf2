package com.bleudev.nlf2.client.api;

import com.mojang.blaze3d.buffers.Std140Builder;

public interface UniformEditorRegistry {
    static void register(String uniformName, int size, UniformEditorFunction editorFunction) {
        UniformEditorRegistryImpl.register(uniformName, size, editorFunction);
    }

    @FunctionalInterface
    interface UniformEditorFunction {
        void edit(Std140Builder builder);
    }
}
