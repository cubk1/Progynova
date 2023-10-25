package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.实体Minecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;
import net.minecraft.util.Vec3;

public class RenderMinecart<T extends 实体Minecart> extends Render<T>
{
    private static final 图像位置 minecartTextures = new 图像位置("textures/entity/minecart.png");
    protected ModelBase modelMinecart = new ModelMinecart();

    public RenderMinecart(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        this.bindEntityTexture(entity);
        long i = (long)entity.getEntityId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f = (((float)(i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f1 = (((float)(i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f2 = (((float)(i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        光照状态经理.理解(f, f1, f2);
        double d0 = entity.lastTickPosX + (entity.X坐标 - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.Y坐标 - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.Z坐标 - entity.lastTickPosZ) * (double)partialTicks;
        double d3 = 0.30000001192092896D;
        Vec3 vec3 = entity.func_70489_a(d0, d1, d2);
        float f3 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;

        if (vec3 != null)
        {
            Vec3 vec31 = entity.func_70495_a(d0, d1, d2, d3);
            Vec3 vec32 = entity.func_70495_a(d0, d1, d2, -d3);

            if (vec31 == null)
            {
                vec31 = vec3;
            }

            if (vec32 == null)
            {
                vec32 = vec3;
            }

            x += vec3.xCoord - d0;
            y += (vec31.yCoord + vec32.yCoord) / 2.0D - d1;
            z += vec3.zCoord - d2;
            Vec3 vec33 = vec32.addVector(-vec31.xCoord, -vec31.yCoord, -vec31.zCoord);

            if (vec33.lengthVector() != 0.0D)
            {
                vec33 = vec33.normalize();
                entityYaw = (float)(Math.atan2(vec33.zCoord, vec33.xCoord) * 180.0D / Math.PI);
                f3 = (float)(Math.atan(vec33.yCoord) * 73.0D);
            }
        }

        光照状态经理.理解((float)x, (float)y + 0.375F, (float)z);
        光照状态经理.辐射(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(-f3, 0.0F, 0.0F, 1.0F);
        float f5 = (float)entity.getRollingAmplitude() - partialTicks;
        float f6 = entity.getDamage() - partialTicks;

        if (f6 < 0.0F)
        {
            f6 = 0.0F;
        }

        if (f5 > 0.0F)
        {
            光照状态经理.辐射(MathHelper.sin(f5) * f5 * f6 / 10.0F * (float)entity.getRollingDirection(), 1.0F, 0.0F, 0.0F);
        }

        int j = entity.getDisplayTileOffset();
        IBlockState iblockstate = entity.getDisplayTile();

        if (iblockstate.getBlock().getRenderType() != -1)
        {
            光照状态经理.推黑客帝国();
            this.bindTexture(TextureMap.locationBlocksTexture);
            float f4 = 0.75F;
            光照状态经理.障眼物(f4, f4, f4);
            光照状态经理.理解(-0.5F, (float)(j - 8) / 16.0F, 0.5F);
            this.func_180560_a(entity, partialTicks, iblockstate);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindEntityTexture(entity);
        }

        光照状态经理.障眼物(-1.0F, -1.0F, 1.0F);
        this.modelMinecart.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(T entity)
    {
        return minecartTextures;
    }

    protected void func_180560_a(T minecart, float partialTicks, IBlockState state)
    {
        光照状态经理.推黑客帝国();
        我的手艺.得到我的手艺().getBlockRendererDispatcher().renderBlockBrightness(state, minecart.getBrightness(partialTicks));
        光照状态经理.流行音乐黑客帝国();
    }
}
