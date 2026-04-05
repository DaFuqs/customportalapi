package net.kyrptonaught.customportalapi.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Portal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.PortalProcessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class GuiMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private int lastColor = -1;

    @WrapOperation(method = "extractPortalOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;white(F)I"))
    public int changeColor(float alpha, Operation<Integer> original) {
        isCustomPortal(minecraft.player);
        return lastColor >= 0 ? ARGB.color(ARGB.as8BitChannel(alpha), lastColor) : original.call(alpha);
    }

    @ModifyArg(method = "extractPortalOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockStateModelSet;getParticleMaterial(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/sprite/Material$Baked;"))
    public BlockState renderCustomPortalOverlay(BlockState blockState) {
        return lastColor >= 0 ? CustomPortalsMod.portalBlock.defaultBlockState() : blockState;
    }


    @Unique
    private void isCustomPortal(LocalPlayer player) {
        PortalProcessor portalManager = player.portalProcess;
        Portal portalBlock = portalManager != null && portalManager.isInsidePortalThisTick() ? ((PortalProcessorAccessor) portalManager).getPortal() : null;
        BlockPos portalPos = portalManager != null && portalManager.isInsidePortalThisTick() ? ((PortalProcessorAccessor) portalManager).getEntryPosition() : null;

        if (portalBlock == null) {
            return;
        }

        if (portalBlock instanceof CustomPortalBlock) {
            PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(((CustomPortalBlock) portalBlock).getPortalBase(player.level(), portalPos));
            if (link != null) {
                lastColor = link.colorID;
                return;
            }
        }

        lastColor = -1;
    }
}
