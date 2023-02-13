package com.greencat.antimony.common.mixins;

import com.greencat.Antimony;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

@Mixin(LayerDeadmau5Head.class)
public class MixinLayerDeadmau5Head {
    private boolean getted = false;
    private DynamicTexture texture;
    private BufferedImage bufferedImage;
    @Final
    @Shadow
    private RenderPlayer playerRenderer;

    @Inject(method = "doRenderLayer*", at = @At("HEAD"))
    public void onRender(AbstractClientPlayer p_doRenderLayer_1_, float p_doRenderLayer_2_, float p_doRenderLayer_3_, float p_doRenderLayer_4_, float p_doRenderLayer_5_, float p_doRenderLayer_6_, float p_doRenderLayer_7_, float p_doRenderLayer_8_, CallbackInfo ci){
        try {
            if (p_doRenderLayer_1_.getName().equals(Antimony.GreenCatUserName) && p_doRenderLayer_1_.hasSkin() && !p_doRenderLayer_1_.isInvisible()) {
                bindTexture(new URL("https://gitee.com/origingreencat/antimony-decorate/raw/master/ears.jpg"));
                if (texture != null) {
                        float lvt_10_1_ = p_doRenderLayer_1_.prevRotationYaw + (p_doRenderLayer_1_.rotationYaw - p_doRenderLayer_1_.prevRotationYaw) * p_doRenderLayer_4_ - (p_doRenderLayer_1_.prevRenderYawOffset + (p_doRenderLayer_1_.renderYawOffset - p_doRenderLayer_1_.prevRenderYawOffset) * p_doRenderLayer_4_);
                        float lvt_11_1_ = p_doRenderLayer_1_.prevRotationPitch + (p_doRenderLayer_1_.rotationPitch - p_doRenderLayer_1_.prevRotationPitch) * p_doRenderLayer_4_;
                        GlStateManager.pushMatrix();
                        GlStateManager.color(1.0F,1.0F,1.0F);
                        GlStateManager.disableLighting();
                        GlStateManager.rotate(lvt_10_1_, 0.0F, 1.0F, 0.0F);
                        GlStateManager.rotate(lvt_11_1_, 1.0F, 0.0F, 0.0F);
                        GlStateManager.translate(0.375F, 0.0F, 0.0F);
                        GlStateManager.translate(0.0F, -0.375F, 0.0F);
                        GlStateManager.rotate(-lvt_11_1_, 1.0F, 0.0F, 0.0F);
                        GlStateManager.rotate(-lvt_10_1_, 0.0F, 1.0F, 0.0F);
                        float lvt_12_1_ = 1.3333334F;
                        GlStateManager.scale(lvt_12_1_, lvt_12_1_, lvt_12_1_);
                        Class<? extends ModelPlayer> playerModel = this.playerRenderer.getMainModel().getClass();
                        Method method = playerModel.getMethod("renderGreenCatEar",float.class);
                        method.invoke(this.playerRenderer.getMainModel(),0.0625F);
                        GlStateManager.enableLighting();
                        GlStateManager.popMatrix();
                }

            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void bindTexture(URL url) {
        if (!getted) {
            new Thread(() -> {
                try {
                    getted = true;
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla");
                    InputStream in = conn.getInputStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while( (len=in.read(buffer)) != -1 ){
                        outStream.write(buffer, 0, len);
                    }
                    in.close();
                    byte[] data = outStream.toByteArray();
                    File imageFile = new File(Antimony.AntimonyDirectory,"ears.jpg");
                    FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                    fileOutputStream.write(data);
                    fileOutputStream.close();
                    bufferedImage = ImageIO.read(new FileInputStream(imageFile));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        texture = new DynamicTexture(bufferedImage);
        GlStateManager.bindTexture(texture.getGlTextureId());
    }
}
