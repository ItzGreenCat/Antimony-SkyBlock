package com.greencat.test;

import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Screenshot {
    Framebuffer buffer = Minecraft.getMinecraft().getFramebuffer();
    Utils utils = new Utils();
    public void CreateScreenshot(int scaling) throws Exception {
        int randomnumber = new Random().nextInt(114514);
        ScreenShotHelper.saveScreenshot(new File(System.getProperty("user.dir")),"huge-" + randomnumber + "-" + scaling + "x"+ ".png",Minecraft.getMinecraft().displayWidth * 20, Minecraft.getMinecraft().displayHeight * 20,buffer);
        Thread.sleep(1000);
        if(Minecraft.getMinecraft().displayWidth * scaling > 34560) {
            utils.print("图片过大");
        } else {
            this.ProcessingImage(System.getProperty("user.dir") + "\\screenshots\\huge-" + randomnumber + "-" + scaling + "x" + ".png", Minecraft.getMinecraft().displayWidth * scaling, Minecraft.getMinecraft().displayHeight * scaling,Minecraft.getMinecraft().displayWidth,Minecraft.getMinecraft().displayHeight);
        }

    }
    public void ProcessingImage(String src, int w, int h,int originalw,int originalh) throws Exception {

        double wr=0,hr=0;
        File srcFile = new File(src);
        File destFile = new File(src);

        BufferedImage bufImg = ImageIO.read(srcFile);

        wr=w*1.0/bufImg.getWidth();
        hr=h*1.0 / bufImg.getHeight();

        Graphics2D g2d = bufImg.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.drawImage(bufImg.getScaledInstance(originalw,originalh,bufImg.SCALE_SMOOTH),0,0,null);

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Image Itemp = ato.filter(bufImg, null);
        try {
            ImageIO.write((BufferedImage) Itemp,src.substring(src.lastIndexOf(".")+1), destFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        utils.print("截图已保存至:" + src);
    }

}
