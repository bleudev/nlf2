package com.bleudev.nlf2.datagen.provider

import com.bleudev.nine_lifes.custom.NineLifesItems
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.advancements.predicates.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class Nlf2RecipesProvider(output: FabricPackOutput,
                          registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, registriesFuture) {
    override fun createRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput): RecipeProvider =
        Nlf2RecipeProvider0(registries, output)
    override fun getName(): String = "Nlf2RecipesProvider"

    private class Nlf2RecipeProvider0(registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(registries, output) {
        override fun buildRecipes() {
            val items = registries.lookupOrThrow(Registries.ITEM)

            shapeless(RecipeCategory.MISC, NineLifesItems.AMETHYST_STICK).requires(Items.DEBUG_STICK).requires(Items.AMETHYST_SHARD)
                .unlockedBy(getHasName(Items.DEBUG_STICK), inventoryTrigger(ItemPredicate.Builder.item().of(items, Items.DEBUG_STICK, Items.AMETHYST_SHARD)))
                .save(output, "amethyst_stick_from_debug_stick")
        }
    }
}
