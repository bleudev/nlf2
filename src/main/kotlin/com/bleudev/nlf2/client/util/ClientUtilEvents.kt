package com.bleudev.nlf2.client.util

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import org.joml.Vector4f

object ClientFogRenderTransformEvents {
    typealias FogColorTransform = (original: Vector4f, current: Vector4f) -> Vector4f

    @JvmField
    val FOG_COLOR: Event<FogColorTransform> = EventFactory.createArrayBacked(FogColorTransform::class.java) { callbacks ->
        { original, current ->
            var current = current
            for (callback in callbacks) {
                current = callback(original, current)
            }
            current
        }
    }

    typealias FogStartTransform = (original: Float, current: Float) -> Float

    @JvmField
    val FOG_START: Event<FogStartTransform> = EventFactory.createArrayBacked(FogStartTransform::class.java) { callbacks ->
        { original, current ->
            var current = current
            for (callback in callbacks) {
                current = callback(original, current)
            }
            current
        }
    }

    typealias FogEndTransform = (original: Float, current: Float) -> Float

    @JvmField
    val FOG_END: Event<FogEndTransform> = EventFactory.createArrayBacked(FogEndTransform::class.java) { callbacks ->
        { original, current ->
            var current = current
            for (callback in callbacks) {
                current = callback(original, current)
            }
            current
        }
    }
}