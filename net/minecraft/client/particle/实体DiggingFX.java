package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.实体;
import net.minecraft.init.Blocks;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class 实体DiggingFX extends 实体FX
{
    private IBlockState sourceState;
    private 阻止位置 sourcePos;

    protected 实体DiggingFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.sourceState = state;
        this.setParticleIcon(我的手艺.得到我的手艺().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
        this.particleGravity = state.getBlock().blockParticleGravity;
        this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
        this.particleScale /= 2.0F;
    }

    public 实体DiggingFX setBlockPos(阻止位置 pos)
    {
        this.sourcePos = pos;

        if (this.sourceState.getBlock() == Blocks.grass)
        {
            return this;
        }
        else
        {
            int i = this.sourceState.getBlock().colorMultiplier(this.worldObj, pos);
            this.particleRed *= (float)(i >> 16 & 255) / 255.0F;
            this.particleGreen *= (float)(i >> 8 & 255) / 255.0F;
            this.particleBlue *= (float)(i & 255) / 255.0F;
            return this;
        }
    }

    public 实体DiggingFX func_174845_l()
    {
        this.sourcePos = new 阻止位置(this.X坐标, this.Y坐标, this.Z坐标);
        Block block = this.sourceState.getBlock();

        if (block == Blocks.grass)
        {
            return this;
        }
        else
        {
            int i = block.getRenderColor(this.sourceState);
            this.particleRed *= (float)(i >> 16 & 255) / 255.0F;
            this.particleGreen *= (float)(i >> 8 & 255) / 255.0F;
            this.particleBlue *= (float)(i & 255) / 255.0F;
            return this;
        }
    }

    public int getFXLayer()
    {
        return 1;
    }

    public void renderParticle(WorldRenderer worldRendererIn, 实体 实体In, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
        float f1 = f + 0.015609375F;
        float f2 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
        float f3 = f2 + 0.015609375F;
        float f4 = 0.1F * this.particleScale;

        if (this.particleIcon != null)
        {
            f = this.particleIcon.getInterpolatedU((double)(this.particleTextureJitterX / 4.0F * 16.0F));
            f1 = this.particleIcon.getInterpolatedU((double)((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
            f2 = this.particleIcon.getInterpolatedV((double)(this.particleTextureJitterY / 4.0F * 16.0F));
            f3 = this.particleIcon.getInterpolatedV((double)((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
        }

        float f5 = (float)(this.prevPosX + (this.X坐标 - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.Y坐标 - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.Z坐标 - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        worldRendererIn.pos((double)(f5 - rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 - rotationYZ * f4 - rotationXZ * f4)).tex((double)f, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 - rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 - rotationYZ * f4 + rotationXZ * f4)).tex((double)f, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 + rotationYZ * f4 + rotationXZ * f4)).tex((double)f1, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 + rotationYZ * f4 - rotationXZ * f4)).tex((double)f1, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
    }

    public int getBrightnessForRender(float partialTicks)
    {
        int i = super.getBrightnessForRender(partialTicks);
        int j = 0;

        if (this.worldObj.isBlockLoaded(this.sourcePos))
        {
            j = this.worldObj.getCombinedLight(this.sourcePos, 0);
        }

        return i == 0 ? j : i;
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return (new 实体DiggingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Block.getStateById(p_178902_15_[0]))).func_174845_l();
        }
    }
}
