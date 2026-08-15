package com.bleudev.nlf2.datagen.provider

import com.bleudev.nlf2.custom.Nlf2Sounds
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class Nlf2SoundsProvider(output: PackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) : FabricSoundsProvider(output, registriesFuture) {
    override fun configure(
        registryLookup: HolderLookup.Provider,
        exporter: SoundExporter
    ) {
        for (sound in Nlf2Sounds.allWithSubtitle()) {
            exporter.add(sound, SoundTypeBuilder.of(sound)
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(sound.location))
            )
        }
        for (sound in Nlf2Sounds.allWithoutSubtitle()) {
            exporter.add(sound, SoundTypeBuilder.of()
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(sound.location))
            )
        }
    }

    override fun getName(): String = "Nlf2SoundsProvider"
}