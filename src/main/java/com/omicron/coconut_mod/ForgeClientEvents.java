package com.omicron.coconut_mod;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CoconutMod.MODID, value = Dist.CLIENT)
public class ForgeClientEvents {

    public static boolean show = false;

    private static final ResourceLocation PANORAMA_OVERLAY = new ResourceLocation(CoconutMod.MODID, "textures/gui/bars.png");
    @SubscribeEvent
    public static void overlay(RenderGameOverlayEvent.Post event)
    {
        if(event.getType() == RenderGameOverlayEvent.ElementType.CHAT)
        {
            ResourceLocation MINECRAFT_EDITION = new ResourceLocation("textures/gui/title/edition.png");
            //System.out.println("render");
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableBlend();
            RenderSystem.setShaderTexture(0, PANORAMA_OVERLAY);
            int j = event.getWindow().getWidth() / 2 - 137 - 50 - 300;
            int x = event.getWindow().getGuiScaledWidth() / 2;
            int y = event.getWindow().getGuiScaledHeight() / 2;
            int x1 = 192;
            int y1 = 8;

            int x2 = x / 30;
            int y2 = event.getWindow().getGuiScaledHeight() - y / 15 - 8;

            int x3 = (int) (x / (30 * 0.6)) + 5;
            int y3 = (int) event.getWindow().getGuiScaledHeight() * 2 - y + 16;
            GuiComponent.blit(event.getMatrixStack(), x2, y2, x1, y1, 0.0F, 4.0F, 32, 2, 32, 32);
            GuiComponent.blit(event.getMatrixStack(), x2, y2, (int) ((x1) * (Minecraft.getInstance().player.getHealth() / Minecraft.getInstance().player.getMaxHealth())), y1, 0.0F, 0.0F, 32, 2, 32, 32);
            PoseStack stack = event.getMatrixStack();
            stack.pushPose();
            stack.scale(0.6f, 0.6f, 0.6f);
            Minecraft.getInstance().font.draw(stack, "HP: " + String.valueOf(String.format("%.02f",Minecraft.getInstance().player.getMaxHealth())) + "/" + String.valueOf(String.format("%.02f",Minecraft.getInstance().player.getHealth())), x3, y3, 	0xFFFFFF);
            stack.popPose();
            if(show) {
                stack = event.getMatrixStack();
                stack.pushPose();
                stack.scale(3.0f, 3.0f, 3.0f);
                renderGuiItem(new ItemStack(CoconutMod.COCONUT.get()), (int) (event.getWindow().getGuiScaledWidth() - 50), event.getWindow().getGuiScaledHeight() - y / 2, stack);
                stack.popPose();
                stack.pushPose();
                stack.scale(0.6f, 0.6f, 0.6f);
                Minecraft.getInstance().font.draw(stack, "Coconuts bag: " + Minecraft.getInstance().player.getMainHandItem().getCount() + "/10", (float) (event.getWindow().getGuiScaledWidth() / 0.6 - 115), event.getWindow().getGuiScaledHeight() * 2 - y, 0xFFFFFF);
                stack.popPose();
                //System.out.println(event.getWindow().getGuiScale());
            }
        }
    }

    @SubscribeEvent
    public static void registerClient(RegisterClientCommandsEvent event)
    {
        event.getDispatcher().register(
                Commands.literal("showcoconutbag").requires((player) -> {
                    return player.hasPermission(2);
                }).executes((context -> {
                    show = true;
                    return 1;
                }))
        );

        event.getDispatcher().register(
                Commands.literal("hidecoconutbag").requires((player) -> {
                    return player.hasPermission(2);
                }).executes((context -> {
                    show = false;
                    return 1;
                }))
        );
    }

    public static void renderGuiItem(ItemStack pStack, int pX, int pY, PoseStack stack) {
        renderGuiItem(pStack, pX, pY, Minecraft.getInstance().getItemRenderer().getModel(pStack, (Level)null, (LivingEntity)null, 0), stack);
    }

    public static void renderGuiItem(ItemStack pStack, int pX, int pY, BakedModel pBakedModel, PoseStack stack) {
        Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double)pX, (double)pY, (double)(100.0F + Minecraft.getInstance().getItemRenderer().blitOffset));
        posestack.translate(8.0D, 8.0D, 0.0D);
        posestack.scale(1.0F, -1.0F, 1.0F);
        posestack.scale(16.0F, 16.0F, 16.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !pBakedModel.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }

        Minecraft.getInstance().getItemRenderer().render(pStack, ItemTransforms.TransformType.GUI, false, stack, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, pBakedModel);
        multibuffersource$buffersource.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) {
            Lighting.setupFor3DItems();
        }

        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
