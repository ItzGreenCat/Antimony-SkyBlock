package com.greencat.antimony.common.mixins;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

import static com.greencat.antimony.core.config.ConfigInterface.get;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Shadow
    private float prevEquippedProgress;
    @Shadow
    private float equippedProgress;
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void rotateArroundXAndY(float angle, float angleY);

    @Shadow
    protected abstract void setLightMapFromPlayer(AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void rotateWithPlayerRotations(EntityPlayerSP entityPlayerSP, float partialTicks);

    @Shadow
    protected abstract void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress);

    @Shadow
    protected abstract void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks);

    @Shadow
    protected abstract void doBlockTransformations();

    @Shadow
    protected abstract void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void doItemUsedTransformations(float swingProgress);

    @Shadow
    public abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);

    @Shadow
    protected abstract void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress);

    /**
     * WHY I NEED TO WRITE THAT STUPID COMMIT
     */
    @Overwrite
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        doItemRenderGLScale();
    }

    /**
     * WHY I NEED TO WRITE THAT STUPID COMMIT
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        if(FunctionManager.getStatus("Animation")) {
            float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
            EntityPlayerSP abstractclientplayer = mc.thePlayer;
            float f1 = abstractclientplayer.getSwingProgress(partialTicks);
            float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
            float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
            this.rotateArroundXAndY(f2, f3);
            this.setLightMapFromPlayer(abstractclientplayer);
            this.rotateWithPlayerRotations(abstractclientplayer, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();

            if (this.itemToRender != null) {
                final boolean displayBlocking = Minecraft.getMinecraft().thePlayer.isBlocking();
                if (this.itemToRender.getItem() instanceof ItemMap) {
                    this.renderItemMap(abstractclientplayer, f2, f, f1);
                } else if ((abstractclientplayer.isUsingItem() || (mc.gameSettings.keyBindUseItem.isKeyDown() && (Boolean) get("Animation", "anythingBlock"))) || ((itemToRender.getItem() instanceof ItemSword || (Boolean) get("Animation", "anythingBlock")) && displayBlocking)) {
                    switch (displayBlocking || (Boolean) get("Animation", "anythingBlock") ? EnumAction.BLOCK : this.itemToRender.getItemUseAction()) {
                        case NONE:
                            this.transformFirstPersonItem(f, 0.0F);
                            break;
                        case EAT:
                        case DRINK:
                            this.performDrinking(abstractclientplayer, partialTicks);
                            this.transformFirstPersonItem(f, f1);
                            break;
                        case BLOCK:
                            GL11.glTranslated((Double) get("Animation", "translateX"), (Double) get("Animation", "translateY"), (Double) get("Animation", "translateZ"));
                            GlStateManager.rotate((float) (double) get("Animation", "rotateX"), 1.0F, 0.0F, 0.0F);
                            GlStateManager.rotate((float) (double) get("Animation", "rotateY"), 0.0F, 1.0F, 0.0F);
                            GlStateManager.rotate((float) (double) get("Animation", "rotateZ"), 0.0F, 0.0F, 1.0F);
                            switch ((Integer) get("Animation", "blockMode")) {
                                case 0: {
                                    transformFirstPersonItem(f, f1);
                                    doBlockTransformations();
                                    break;
                                }
                                case 1: {
                                    transformFirstPersonItem(f1, 0.0F);
                                    doBlockTransformations();
                                    break;
                                }
                                case 2: {
                                    avatar(f1);
                                    doBlockTransformations();
                                    break;
                                }
                                case 3: {
                                    etb(f, f1);
                                    doBlockTransformations();
                                    break;
                                }
                                case 4: {
                                    transformFirstPersonItem(f, 0.83F);
                                    float f4 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.83F);
                                    GlStateManager.translate(-0.5F, 0.2F, 0.2F);
                                    GlStateManager.rotate(-f4 * 0.0F, 0.0F, 0.0F, 0.0F);
                                    GlStateManager.rotate(-f4 * 43.0F, 58.0F, 23.0F, 45.0F);
                                    doBlockTransformations();
                                    break;
                                }
                                case 5: {
                                    push(f1);
                                    doBlockTransformations();
                                    break;
                                }
                                case 6: {
                                    transformFirstPersonItem(f1, f1);
                                    doBlockTransformations();
                                    GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
                                    break;
                                }
                                case 7: {
                                    jello(f1);
                                    doBlockTransformations();
                                    break;
                                }
                                case 8: {
                                    slide(f1);
                                    doBlockTransformations();
                                    break;
                                }
                                case 9: {
                                    transformFirstPersonItem(0.2F, f1);
                                    doBlockTransformations();
                                    break;
                                }
                                case 10: {
                                    transformFirstPersonItem(f / 2.0F, 0.0F);
                                    GlStateManager.rotate(-MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F) * 40.0F / 2.0F, MathHelper.sqrt_float(f1) / 2.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-MathHelper.sqrt_float(f1) * 30.0F, 1.0F, MathHelper.sqrt_float(f1) / 2.0F, -0.0F);
                                    doBlockTransformations();
                                    break;
                                }
                                case 11: {
                                    continuity(f1);
                                    doBlockTransformations();
                                    break;
                                }
                                case 12: {
                                    GL11.glTranslated(-0.1, 0.15, 0.0);
                                    this.transformFirstPersonItem(f / 0.15f, f1);
                                    final float rot = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927f);
                                    GlStateManager.rotate(rot * 30.0f, 2.0f, -rot, 9.0f);
                                    GlStateManager.rotate(rot * 35.0f, 1.0f, -rot, -0.0f);
                                    this.doBlockTransformations();
                                    break;
                                }
                                case 13: {
                                    this.transformFirstPersonItem(0.0f, 0.0f);
                                    this.doBlockTransformations();
                                    final int alpha = (int) Math.min(255L, ((System.currentTimeMillis() % 255L > 127L) ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : (System.currentTimeMillis() % 255L)) * 2L);
                                    GlStateManager.translate(0.3f, -0.0f, 0.4f);
                                    GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.translate(0.0f, 0.5f, 0.0f);
                                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.translate(0.6f, 0.5f, 0.0f);
                                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate(-10.0f, 1.0f, 0.0f, -1.0f);
                                    GlStateManager.rotate(abstractclientplayer.isSwingInProgress ? (-alpha / 5.0f) : 1.0f, 1.0f, -0.0f, 1.0f);
                                    break;
                                }
                                case 14: {
                                    transformFirstPersonItem(f1 != 0 ? Math.max(1 - (f1 * 2), 0) * 0.7F : 0, 1F);
                                    doBlockTransformations();
                                    break;
                                }
                                case 15: {
                                    transformFirstPersonItem(0F, 0F);
                                    doBlockTransformations();
                                    break;
                                }
                                case 16: {
                                    rotateSword(f1);
                                    break;
                                }
                                case 17: {
                                    doItemRenderGLTranslate();
                                    GlStateManager.translate(0.0F, f * -0.6F, 0.0F);
                                    GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                                    doItemRenderGLScale();
                                    doBlockTransformations();
                                    break;
                                }
                                case 18: {
                                    doItemRenderGLTranslate();
                                    GlStateManager.translate(0.0F, f * -0.6F, 0.0F);
                                    GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                                    float var11 = MathHelper.sin(f1 * f1 * 3.1415927F);
                                    float var12 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.rotate(var11 * 0.0F, 0.0F, 1.0F, 0.0F);
                                    GlStateManager.rotate(var12 * 0.0F, 0.0F, 0.0F, 1.0F);
                                    GlStateManager.rotate(var12 * -40.0F + 10F, 1.0F, 0.0F, 0.0F);
                                    doItemRenderGLScale();
                                    doBlockTransformations();
                                    break;
                                }
                                case 19: {
                                    float var9 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GL11.glTranslated(-0.04D, 0.0D, 0.0D);
                                    this.transformFirstPersonItem(f / 2.5F, 0.0f);
                                    GlStateManager.rotate(-var9 * 0.0F / 2.0F, var9 / 2.0F, 1.0F, 4.0F);
                                    GlStateManager.rotate(-var9 * 120.0F, 1.0F, var9 / 3.0F, -0.0F);
                                    GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                                    GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
                                    GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
                                    GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
                                    break;
                                }
                                case 20: {
                                    Random random = new Random();
                                    float var9 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GL11.glTranslated(-0.04D, 0.0D, 0.0D);
                                    this.transformFirstPersonItem(f / 2.5F, 0.0f);
                                    GlStateManager.rotate(-var9 * 0.0F / 2.0F, var9 / 2.0F, 1.0F, 4.0F);
                                    GlStateManager.rotate(-var9 * 120.0F, 1.0F, var9 / 3.0F, -0.0F);
                                    GlStateManager.translate(random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
                                    GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
                                    GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
                                    GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
                                    break;
                                }

                            }
                            break;
                        case BOW:
                            this.transformFirstPersonItem(f, f1);
                            this.doBowTransformations(partialTicks, abstractclientplayer);
                    }
                } else {
                    if (!(Boolean) get("Animation", "swingItem"))
                        this.doItemUsedTransformations(f1);
                    this.transformFirstPersonItem(f, f1);
                }

                this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
            } else if (!abstractclientplayer.isInvisible()) {
                this.renderPlayerArm(abstractclientplayer, f, f1);
            }

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
        } else {
            float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
            EntityPlayerSP abstractclientplayer = this.mc.thePlayer;
            float f1 = abstractclientplayer.getSwingProgress(partialTicks);
            float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
            float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
            this.rotateArroundXAndY(f2, f3);
            this.setLightMapFromPlayer(abstractclientplayer);
            this.rotateWithPlayerRotations(abstractclientplayer, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            if (this.itemToRender != null) {
                if (this.itemToRender.getItem() instanceof ItemMap) {
                    this.renderItemMap(abstractclientplayer, f2, f, f1);
                } else if (abstractclientplayer.getItemInUseCount() > 0) {
                    EnumAction enumaction = this.itemToRender.getItemUseAction();
                    switch(enumaction) {
                        case NONE:
                            this.transformFirstPersonItem(f, 0.0F);
                            break;
                        case EAT:
                        case DRINK:
                            this.performDrinking(abstractclientplayer, partialTicks);
                            this.transformFirstPersonItem(f, 0.0F);
                            break;
                        case BLOCK:
                            this.transformFirstPersonItem(f, 0.0F);
                            this.doBlockTransformations();
                            break;
                        case BOW:
                            this.transformFirstPersonItem(f, 0.0F);
                            this.doBowTransformations(partialTicks, abstractclientplayer);
                    }
                } else {
                    this.doItemUsedTransformations(f1);
                    this.transformFirstPersonItem(f, f1);
                }

                this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
            } else if (!abstractclientplayer.isInvisible()) {
                this.renderPlayerArm(abstractclientplayer, f, f1);
            }

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
        }
    }

    private void doItemRenderGLTranslate() {
        GlStateManager.translate((Double)get("Animation","posX"),(Double)get("Animation","posY"), (Double)get("Animation","posZ"));
    }

    private void doItemRenderGLScale() {
        GlStateManager.scale((Double)get("Animation","scale"),(Double)get("Animation","scale"),(Double)get("Animation","scale"));
    }
    private void avatar(float swingProgress) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
        float f2 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f2 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f2 * -40.0F, 1.0F, 0.0F, 0.0F);
        doItemRenderGLScale();
    }

    private void slide(float var9) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var11 = MathHelper.sin(var9 * var9 * 3.1415927F);
        float var12 = MathHelper.sin(MathHelper.sqrt_float(var9) * 3.1415927F);
        GlStateManager.rotate(var11 * 0.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var12 * 0.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var12 * -40.0F, 1.0F, 0.0F, 0.0F);
        doItemRenderGLScale();
    }

    private void rotateSword(float f1) {
        genCustom();
        doBlockTransformations();
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(MathHelper.sqrt_float(f1) * 10.0F * 40.0F, 1.0F, -0.0F, 2.0F);
    }

    private void genCustom() {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, (float) 0.0 * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin((float) 0.0 * (float) 0.0 * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float((float) 0.0) * 3.1415927F);
        GlStateManager.rotate(var3 * -34.0F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(var4 * -20.7F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(var4 * -68.6F, 1.3F, 0.1F, 0.2F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }


    private void jello(float var12) {
        doItemRenderGLTranslate();
        GlStateManager.rotate(48.57F, 0.0F, 0.24F, 0.14F);
        float var13 = MathHelper.sin(var12 * var12 * 3.1415927F);
        float var14 = MathHelper.sin(MathHelper.sqrt_float(var12) * 3.1415927F);
        GlStateManager.rotate(var13 * -35.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var14 * 0.0F, 0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var14 * 20.0F, 1.0F, 1.0F, 1.0F);
        doItemRenderGLScale();
    }

    private void continuity(float var10) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var12 = -MathHelper.sin(var10 * var10 * 3.1415927F);
        float var13 = MathHelper.cos(MathHelper.sqrt_float(var10) * 3.1415927F);
        float var14 = MathHelper.abs(MathHelper.sqrt_float((float) 0.1) * 3.1415927F);
        GlStateManager.rotate(var12 * var14 * 30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var13 * 0.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var13 * 20.0F, 1.0F, 0.0F, 0.0F);
        doItemRenderGLScale();
    }
    private void etb(float equipProgress, float swingProgress) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
        GlStateManager.rotate(var3 * -34.0F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(var4 * -20.7F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(var4 * -68.6F, 1.3F, 0.1F, 0.2F);
        doItemRenderGLScale();
    }

    private void push(float idc) {
        doItemRenderGLTranslate();
        GlStateManager.translate(0.0F, (float) 0.1 * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(idc * idc * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(idc) * 3.1415927F);
        GlStateManager.rotate(var3 * -10.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(var4 * -10.0F, 1.0F, 1.0F, 1.0F);
        doItemRenderGLScale();
    }
}
