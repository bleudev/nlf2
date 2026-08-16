package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import joptsimple.ArgumentAcceptingOptionSpec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.bleudev.nlf2.Nlf2ConstKt.DEV_ARG_USERNAME;

@Environment(EnvType.CLIENT)
@Mixin(Main.class)
public class MainMixin {
    @WrapOperation(method = "main", at = @At(value = "INVOKE", target = "Ljoptsimple/ArgumentAcceptingOptionSpec;defaultsTo(Ljava/lang/Object;[Ljava/lang/Object;)Ljoptsimple/ArgumentAcceptingOptionSpec;"))
    private static <V> ArgumentAcceptingOptionSpec<V> modifyArgDefaults(ArgumentAcceptingOptionSpec<V> instance, V value, V[] values, Operation<ArgumentAcceptingOptionSpec<V>> original) {
        if (instance.options().contains("username") && DEV_ARG_USERNAME != null) {
            return original.call(instance, DEV_ARG_USERNAME, new String[0]);
        }
        return original.call(instance, value, values);
    }
}
