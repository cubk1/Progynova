package net.minecraft.client.renderer.entity.layers;

import java.util.Random;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;

public class LayerArrow implements LayerRenderer<EntityLivingBase>
{
    private final RendererLivingEntity field_177168_a;

    public LayerArrow(RendererLivingEntity p_i46124_1_)
    {
        this.field_177168_a = p_i46124_1_;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        int i = entitylivingbaseIn.getArrowCountInEntity();

        if (i > 0)
        {
            Entity entity = new EntityArrow(entitylivingbaseIn.worldObj, entitylivingbaseIn.posX, entitylivingbaseIn.posY, entitylivingbaseIn.posZ);
            Random random = new Random((long)entitylivingbaseIn.getEntityId());
            RenderHelper.disableStandardItemLighting();

            for (int j = 0; j < i; ++j)
            {
                光照状态经理.推黑客帝国();
                ModelRenderer modelrenderer = this.field_177168_a.getMainModel().getRandomModelBox(random);
                ModelBox modelbox = (ModelBox)modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
                modelrenderer.postRender(0.0625F);
                float f = random.nextFloat();
                float f1 = random.nextFloat();
                float f2 = random.nextFloat();
                float f3 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f) / 16.0F;
                float f4 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f1) / 16.0F;
                float f5 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f2) / 16.0F;
                光照状态经理.理解(f3, f4, f5);
                f = f * 2.0F - 1.0F;
                f1 = f1 * 2.0F - 1.0F;
                f2 = f2 * 2.0F - 1.0F;
                f = f * -1.0F;
                f1 = f1 * -1.0F;
                f2 = f2 * -1.0F;
                float f6 = MathHelper.sqrt_float(f * f + f2 * f2);
                entity.prevRotationYaw = entity.旋转侧滑 = (float)(Math.atan2((double)f, (double)f2) * 180.0D / Math.PI);
                entity.prevRotationPitch = entity.rotationPitch = (float)(Math.atan2((double)f1, (double)f6) * 180.0D / Math.PI);
                double d0 = 0.0D;
                double d1 = 0.0D;
                double d2 = 0.0D;
                this.field_177168_a.getRenderManager().renderEntityWithPosYaw(entity, d0, d1, d2, 0.0F, partialTicks);
                光照状态经理.流行音乐黑客帝国();
            }

            RenderHelper.enableStandardItemLighting();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
