package com.greencat.antimony.common.function;

import com.greencat.antimony.common.mixins.RenderManagerAccessor;
import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.utils.RotationUtils;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.util.Vector;

import static java.lang.Math.*;
import static net.minecraft.realms.RealmsMth.sqrt;

public class Projectiles {
    Minecraft mc = Minecraft.getMinecraft();
    public Projectiles() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void RenderWorldLastEvent(RenderWorldLastEvent event) {
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer.getHeldItem() == null || !FunctionManager.getStatus("Projectiles")){
            return;
        }
        EntityPlayerSP thePlayer = mc.thePlayer;
        WorldClient theWorld = mc.theWorld;
        ItemStack heldItem = thePlayer.getHeldItem();
        Item item = heldItem.getItem();
        RenderManager renderManager = mc.getRenderManager();
        boolean isBow = false;
        float motionFactor = 1.5F;
        float motionSlowdown = 0.99F;
        float gravity;
        float size;
        if (item instanceof ItemBow) {
            if (!thePlayer.isUsingItem())
                return;

            isBow = true;
            gravity = 0.05F;
            size = 0.3F;

            float power = thePlayer.getItemInUseDuration() / 20f;
            power = (power * power + power * 2F) / 3F;
            if (power < 0.1F)
                return;

            if (power > 1F)
                power = 1F;

            motionFactor = power * 3F;
        } else if (item instanceof ItemFishingRod) {
            gravity = 0.04F;
            size = 0.25F;
            motionSlowdown = 0.92F;
        } else if (item instanceof ItemPotion && ItemPotion.isSplash(mc.thePlayer.getHeldItem().getItemDamage())) {
            gravity = 0.05F;
            size = 0.25F;
            motionFactor = 0.5F;
        } else {
            if (!(item instanceof ItemSnowball) && !(item instanceof ItemEnderPearl) && !(item instanceof ItemEgg))
            return;
            gravity = 0.03F;
            size = 0.25F;
        }

        float yaw = RotationUtils.targetRotation != null ?
            RotationUtils.targetRotation.getYaw()
        :
            thePlayer.rotationYaw;

        float pitch = RotationUtils.targetRotation != null ?
            RotationUtils.targetRotation.getPitch()
        :
            thePlayer.rotationPitch;

        float yawRadians = yaw / 180f * (float)Math.PI;
        float pitchRadians = pitch / 180f * (float)Math.PI;


        float posX = (float) (((RenderManagerAccessor)renderManager).getRenderPosX() - cos(yawRadians) * 0.16F);
        float posY = (float) (((RenderManagerAccessor)renderManager).getRenderPosY() + thePlayer.eyeHeight - 0.10000000149011612);
        float posZ = (float) (((RenderManagerAccessor)renderManager).getRenderPosZ() - sin(yawRadians) * 0.16F);


        float motionX = (float) (-sin(yawRadians) * cos(pitchRadians) * (isBow ? 1.0 : 0.4));
        float motionY = (float) (-sin((pitch + ((item instanceof ItemPotion && ItemPotion.isSplash(mc.thePlayer.getHeldItem().getItemDamage())) ? -20 : 0))/ 180f * 3.1415927f) * (isBow ? 1.0 : 0.4));
        float motionZ = (float) (cos(yawRadians) * cos(pitchRadians) * (isBow ? 1.0 : 0.4));

        float distance = sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);

        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        motionX *= motionFactor;
        motionY *= motionFactor;
        motionZ *= motionFactor;


        MovingObjectPosition landingPosition = null;
        boolean hasLanded = false;
        boolean hitEntity = false;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();


        GL11.glDepthMask(false);
        Utils.enableGlCap(GL11.GL_BLEND, GL11.GL_LINE_SMOOTH);
        Utils.disableGlCap(GL11.GL_DEPTH_TEST, GL11.GL_ALPHA_TEST, GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        Utils.glColor(new Color(192,168,255,255));
        GL11.glLineWidth(2f);

        worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);

        while (!hasLanded && posY > 0.0) {
            Vec3 posBefore = new Vec3(posX, posY, posZ);
            Vec3 posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);


            landingPosition = theWorld.rayTraceBlocks(posBefore, posAfter, false,
                    true, false);


            posBefore = new Vec3(posX, posY, posZ);
            posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);

            if (landingPosition != null) {
                hasLanded = true;
                posAfter = new Vec3(landingPosition.hitVec.xCoord, landingPosition.hitVec.yCoord, landingPosition.hitVec.zCoord);
            }


            AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size,
                    posY + size, posZ + size).addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0);

            int chunkMinX = (int) floor((arrowBox.minX - 2.0) / 16.0);
            int chunkMaxX = (int) floor((arrowBox.maxX + 2.0) / 16.0);
            int chunkMinZ = (int) floor((arrowBox.minZ - 2.0) / 16.0);
            int chunkMaxZ = (int) floor((arrowBox.maxZ + 2.0) / 16.0);

            Vector<Entity> collidedEntities = new Vector<Entity>();

            for (int x = chunkMinX; x < chunkMaxX;x++)
            for (int z = chunkMinZ;z < chunkMaxZ;z++)
            theWorld.getChunkFromChunkCoords(x, z)
                    .getEntitiesWithinAABBForEntity(thePlayer, arrowBox, collidedEntities, null);

            for (Entity possibleEntity : collidedEntities) {
                if (possibleEntity.canBeCollidedWith() && possibleEntity != thePlayer) {
                    AxisAlignedBB possibleEntityBoundingBox = possibleEntity.getEntityBoundingBox()
                            .expand(size, size, size);
                    if(possibleEntityBoundingBox.calculateIntercept(posBefore, posAfter) != null) {
                        MovingObjectPosition possibleEntityLanding = possibleEntityBoundingBox
                                .calculateIntercept(posBefore, posAfter);
                        hitEntity = true;
                        hasLanded = true;
                        landingPosition = possibleEntityLanding;
                    }
                }
            }


            posX += motionX;
            posY += motionY;
            posZ += motionZ;



            if (mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().getMaterial() == Material.water) {
                motionX *= 0.6;
                motionY *= 0.6;
                motionZ *= 0.6;
            } else {
                motionX *= motionSlowdown;
                motionY *= motionSlowdown;
                motionZ *= motionSlowdown;
            }

            motionY -= gravity;


            worldRenderer.pos(posX - ((RenderManagerAccessor)renderManager).getRenderPosX(), posY - ((RenderManagerAccessor)renderManager).getRenderPosY(),
                    posZ - ((RenderManagerAccessor)renderManager).getRenderPosZ()).endVertex();
        }


        tessellator.draw();
        GL11.glTranslated(posX - ((RenderManagerAccessor)renderManager).getRenderPosX(), posY - ((RenderManagerAccessor)renderManager).getRenderPosY(),
                posZ - ((RenderManagerAccessor)renderManager).getRenderPosZ());

        if (landingPosition != null) {

            switch (landingPosition.sideHit.ordinal()) {
                case 0 :
                    GL11.glRotatef(90F, 0F, 0F, 1F);
                    break;
                case 2 :
                    GL11.glRotatef(90F, 1F, 0F, 0F);
            }

            if (hitEntity)
                Utils.glColor(new Color(255, 0, 0, 150));
        }

        GL11.glRotatef(-90F, 1F, 0F, 0F);

        Cylinder cylinder = new Cylinder();
        cylinder.setDrawStyle(GLU.GLU_LINE);
        cylinder.draw(0.2F, 0F, 0F, 60, 1);

        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        Utils.resetCaps();
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }
}
