package com.omicron.coconut_mod;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = CoconutMod.MODID)
public class ModClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        System.out.println("renderer");
        event.registerEntityRenderer(CoconutMod.COCONUT_ENTITY.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    public static void onClient(FMLClientSetupEvent event)
    {
        ItemBlockRenderTypes.setRenderLayer(CoconutMod.PALM_TREE1.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CoconutMod.PALM_TREE2.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CoconutMod.PALM_TREE3.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CoconutMod.PALM_TREE4.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CoconutMod.PALM_TREE5.get(), RenderType.cutout());
    }

}
