package net.optifine.render;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.实体;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class CloudRenderer
{
    private 我的手艺 mc;
    private boolean updated = false;
    private boolean renderFancy = false;
    int cloudTickCounter;
    private Vec3 cloudColor;
    float partialTicks;
    private boolean updateRenderFancy = false;
    private int updateCloudTickCounter = 0;
    private Vec3 updateCloudColor = new Vec3(-1.0D, -1.0D, -1.0D);
    private double updatePlayerX = 0.0D;
    private double updatePlayerY = 0.0D;
    private double updatePlayerZ = 0.0D;
    private int glListClouds = -1;

    public CloudRenderer(我的手艺 mc)
    {
        this.mc = mc;
        this.glListClouds = GLAllocation.generateDisplayLists(1);
    }

    public void prepareToRender(boolean renderFancy, int cloudTickCounter, float partialTicks, Vec3 cloudColor)
    {
        this.renderFancy = renderFancy;
        this.cloudTickCounter = cloudTickCounter;
        this.partialTicks = partialTicks;
        this.cloudColor = cloudColor;
    }

    public boolean shouldUpdateGlList()
    {
        if (!this.updated)
        {
            return true;
        }
        else if (this.renderFancy != this.updateRenderFancy)
        {
            return true;
        }
        else if (this.cloudTickCounter >= this.updateCloudTickCounter + 20)
        {
            return true;
        }
        else if (Math.abs(this.cloudColor.xCoord - this.updateCloudColor.xCoord) > 0.003D)
        {
            return true;
        }
        else if (Math.abs(this.cloudColor.yCoord - this.updateCloudColor.yCoord) > 0.003D)
        {
            return true;
        }
        else if (Math.abs(this.cloudColor.zCoord - this.updateCloudColor.zCoord) > 0.003D)
        {
            return true;
        }
        else
        {
            实体 实体 = this.mc.getRenderViewEntity();
            boolean flag = this.updatePlayerY + (double) 实体.getEyeHeight() < 128.0D + (double)(this.mc.游戏一窝.ofCloudsHeight * 128.0F);
            boolean flag1 = 实体.prevPosY + (double) 实体.getEyeHeight() < 128.0D + (double)(this.mc.游戏一窝.ofCloudsHeight * 128.0F);
            return flag1 != flag;
        }
    }

    public void startUpdateGlList()
    {
        GL11.glNewList(this.glListClouds, GL11.GL_COMPILE);
    }

    public void endUpdateGlList()
    {
        GL11.glEndList();
        this.updateRenderFancy = this.renderFancy;
        this.updateCloudTickCounter = this.cloudTickCounter;
        this.updateCloudColor = this.cloudColor;
        this.updatePlayerX = this.mc.getRenderViewEntity().prevPosX;
        this.updatePlayerY = this.mc.getRenderViewEntity().prevPosY;
        this.updatePlayerZ = this.mc.getRenderViewEntity().prevPosZ;
        this.updated = true;
        光照状态经理.重设色彩();
    }

    public void renderGlList()
    {
        实体 实体 = this.mc.getRenderViewEntity();
        double d0 = 实体.prevPosX + (实体.X坐标 - 实体.prevPosX) * (double)this.partialTicks;
        double d1 = 实体.prevPosY + (实体.Y坐标 - 实体.prevPosY) * (double)this.partialTicks;
        double d2 = 实体.prevPosZ + (实体.Z坐标 - 实体.prevPosZ) * (double)this.partialTicks;
        double d3 = (double)((float)(this.cloudTickCounter - this.updateCloudTickCounter) + this.partialTicks);
        float f = (float)(d0 - this.updatePlayerX + d3 * 0.03D);
        float f1 = (float)(d1 - this.updatePlayerY);
        float f2 = (float)(d2 - this.updatePlayerZ);
        光照状态经理.推黑客帝国();

        if (this.renderFancy)
        {
            光照状态经理.理解(-f / 12.0F, -f1, -f2 / 12.0F);
        }
        else
        {
            光照状态经理.理解(-f, -f1, -f2);
        }

        光照状态经理.callList(this.glListClouds);
        光照状态经理.流行音乐黑客帝国();
        光照状态经理.重设色彩();
    }

    public void reset()
    {
        this.updated = false;
    }
}
