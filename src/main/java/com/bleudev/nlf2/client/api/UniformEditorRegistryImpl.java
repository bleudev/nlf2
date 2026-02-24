package com.bleudev.nlf2.client.api;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MappableRingBuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UniformEditorRegistryImpl {
    private static final HashMap<String, Supplier<MappableRingBuffer>> BUFFER_QUERY = new HashMap<>();
    private static final HashMap<String, MappableRingBuffer> BUFFERS = new HashMap<>();
    private static final HashMap<String, UniformEditorRegistry.UniformEditorFunction> FUNCTIONS = new HashMap<>();

    protected static void register(String uniformName, int size, UniformEditorRegistry.UniformEditorFunction editorFunction) {
        BUFFER_QUERY.put(uniformName, () -> new MappableRingBuffer(() -> uniformName + " UBO", 130, size));
        FUNCTIONS.put(uniformName, editorFunction);
    }

    public static void internalInitBuffers() {
        for (Map.Entry<String, Supplier<MappableRingBuffer>> entry : BUFFER_QUERY.entrySet()) {
            BUFFERS.put(entry.getKey(), entry.getValue().get());
        }
    }

    public static void internalUpdateBuffers() {
        for (Map.Entry<String, MappableRingBuffer> entry : BUFFERS.entrySet()) {
            try (GpuBuffer.MappedView view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(entry.getValue().currentBuffer(), false, true)) {
                view.data().position(0);
                FUNCTIONS.get(entry.getKey()).edit(Std140Builder.intoBuffer(view.data()));
            }
        }
    }

    public static Map<String, GpuBuffer> internalModifyCustomUniforms(Map<String, GpuBuffer> uniforms) {
        for (Map.Entry<String, MappableRingBuffer> entry : BUFFERS.entrySet()) {
            if (uniforms.containsKey(entry.getKey())) {
                uniforms.put(entry.getKey(), entry.getValue().currentBuffer());
            }
        }
        return uniforms;
    }
}
