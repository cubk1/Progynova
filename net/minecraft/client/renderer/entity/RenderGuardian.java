package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.monster.实体Guardian;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.图像位置;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderGuardian extends RenderLiving<实体Guardian>
{
    private static final 图像位置 GUARDIAN_TEXTURE = new 图像位置("textures/entity/guardian.png");
    private static final 图像位置 GUARDIAN_ELDER_TEXTURE = new 图像位置("textures/entity/guardian_elder.png");
    private static final 图像位置 GUARDIAN_BEAM_TEXTURE = new 图像位置("textures/entity/guardian_beam.png");
    int field_177115_a;

    public RenderGuardian(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelGuardian(), 0.5F);
        this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
    }

    public boolean shouldRender(实体Guardian livingEntity, ICamera camera, double camX, double camY, double camZ)
    {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ))
        {
            return true;
        }
        else
        {
            if (livingEntity.hasTargetedEntity())
            {
                实体LivingBase entitylivingbase = livingEntity.getTargetedEntity();

                if (entitylivingbase != null)
                {
                    Vec3 vec3 = this.func_177110_a(entitylivingbase, (double)entitylivingbase.height * 0.5D, 1.0F);
                    Vec3 vec31 = this.func_177110_a(livingEntity, (double)livingEntity.getEyeHeight(), 1.0F);

                    if (camera.isBoundingBoxInFrustum(AxisAlignedBB.fromBounds(vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord, vec3.zCoord)))
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private Vec3 func_177110_a(实体LivingBase entityLivingBaseIn, double p_177110_2_, float p_177110_4_)
    {
        double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.X坐标 - entityLivingBaseIn.lastTickPosX) * (double)p_177110_4_;
        double d1 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.Y坐标 - entityLivingBaseIn.lastTickPosY) * (double)p_177110_4_;
        double d2 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.Z坐标 - entityLivingBaseIn.lastTickPosZ) * (double)p_177110_4_;
        return new Vec3(d0, d1, d2);
    }

    public void doRender(实体Guardian entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (this.field_177115_a != ((ModelGuardian)this.mainModel).func_178706_a())
        {
            this.mainModel = new ModelGuardian();
            this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        实体LivingBase entitylivingbase = entity.getTargetedEntity();

        if (entitylivingbase != null)
        {
            float f = entity.func_175477_p(partialTicks);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.bindTexture(GUARDIAN_BEAM_TEXTURE);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            光照状态经理.disableLighting();
            光照状态经理.disableCull();
            光照状态经理.禁用混合品();
            光照状态经理.depthMask(true);
            float f1 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f1, f1);
            光照状态经理.tryBlendFuncSeparate(770, 1, 1, 0);
            float f2 = (float)entity.worldObj.getTotalWorldTime() + partialTicks;
            float f3 = f2 * 0.5F % 1.0F;
            float f4 = entity.getEyeHeight();
            光照状态经理.推黑客帝国();
            光照状态经理.理解((float)x, (float)y + f4, (float)z);
            Vec3 vec3 = this.func_177110_a(entitylivingbase, (double)entitylivingbase.height * 0.5D, partialTicks);
            Vec3 vec31 = this.func_177110_a(entity, (double)f4, partialTicks);
            Vec3 vec32 = vec3.subtract(vec31);
            double d0 = vec32.lengthVector() + 1.0D;
            vec32 = vec32.normalize();
            float f5 = (float)Math.acos(vec32.yCoord);
            float f6 = (float)Math.atan2(vec32.zCoord, vec32.xCoord);
            光照状态经理.辐射((((float)Math.PI / 2F) + -f6) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            光照状态经理.辐射(f5 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            int i = 1;
            double d1 = (double)f2 * 0.05D * (1.0D - (double)(i & 1) * 2.5D);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            float f7 = f * f;
            int j = 64 + (int)(f7 * 240.0F);
            int k = 32 + (int)(f7 * 192.0F);
            int l = 128 - (int)(f7 * 64.0F);
            double d2 = (double)i * 0.2D;
            double d3 = d2 * 1.41D;
            double d4 = 0.0D + Math.cos(d1 + 2.356194490192345D) * d3;
            double d5 = 0.0D + Math.sin(d1 + 2.356194490192345D) * d3;
            double d6 = 0.0D + Math.cos(d1 + (Math.PI / 4D)) * d3;
            double d7 = 0.0D + Math.sin(d1 + (Math.PI / 4D)) * d3;
            double d8 = 0.0D + Math.cos(d1 + 3.9269908169872414D) * d3;
            double d9 = 0.0D + Math.sin(d1 + 3.9269908169872414D) * d3;
            double d10 = 0.0D + Math.cos(d1 + 5.497787143782138D) * d3;
            double d11 = 0.0D + Math.sin(d1 + 5.497787143782138D) * d3;
            double d12 = 0.0D + Math.cos(d1 + Math.PI) * d2;
            double d13 = 0.0D + Math.sin(d1 + Math.PI) * d2;
            double d14 = 0.0D + Math.cos(d1 + 0.0D) * d2;
            double d15 = 0.0D + Math.sin(d1 + 0.0D) * d2;
            double d16 = 0.0D + Math.cos(d1 + (Math.PI / 2D)) * d2;
            double d17 = 0.0D + Math.sin(d1 + (Math.PI / 2D)) * d2;
            double d18 = 0.0D + Math.cos(d1 + (Math.PI * 3D / 2D)) * d2;
            double d19 = 0.0D + Math.sin(d1 + (Math.PI * 3D / 2D)) * d2;
            double d20 = 0.0D;
            double d21 = 0.4999D;
            double d22 = (double)(-1.0F + f3);
            double d23 = d0 * (0.5D / d2) + d22;
            worldrenderer.pos(d12, d0, d13).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d12, 0.0D, d13).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d14, 0.0D, d15).tex(0.0D, d22).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d14, d0, d15).tex(0.0D, d23).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d16, d0, d17).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d16, 0.0D, d17).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d18, 0.0D, d19).tex(0.0D, d22).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d18, d0, d19).tex(0.0D, d23).color(j, k, l, 255).endVertex();
            double d24 = 0.0D;

            if (entity.已存在的刻度 % 2 == 0)
            {
                d24 = 0.5D;
            }

            worldrenderer.pos(d4, d0, d5).tex(0.5D, d24 + 0.5D).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d6, d0, d7).tex(1.0D, d24 + 0.5D).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d10, d0, d11).tex(1.0D, d24).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d8, d0, d9).tex(0.5D, d24).color(j, k, l, 255).endVertex();
            tessellator.draw();
            光照状态经理.流行音乐黑客帝国();
        }
    }

    protected void preRenderCallback(实体Guardian entitylivingbaseIn, float partialTickTime)
    {
        if (entitylivingbaseIn.isElder())
        {
            光照状态经理.障眼物(2.35F, 2.35F, 2.35F);
        }
    }

    protected 图像位置 getEntityTexture(实体Guardian entity)
    {
        return entity.isElder() ? GUARDIAN_ELDER_TEXTURE : GUARDIAN_TEXTURE;
    }
}
