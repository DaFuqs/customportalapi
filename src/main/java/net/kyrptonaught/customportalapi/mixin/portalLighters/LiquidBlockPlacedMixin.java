package net.kyrptonaught.customportalapi.mixin.portalLighters;

import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.kyrptonaught.customportalapi.portal.PortalPlacer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LiquidBlock.class)
public abstract class LiquidBlockPlacedMixin {

    @Inject(method = "onPlace", at = @At("HEAD"))
    public void fluidPlacedAttemptPortalLight(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (state.getFluidState().isSource())
            PortalPlacer.attemptPortalLight(world, pos, PortalIgnitionSource.FluidSource(state.getFluidState().getType()));
    }
}
