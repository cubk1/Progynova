package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.实体EnderCrystal;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class RenderEnderCrystal extends Render<实体EnderCrystal>
{
    private static final 图像位置 enderCrystalTextures = new 图像位置("textures/entity/endercrystal/endercrystal.png");
    private ModelBase modelEnderCrystal = new ModelEnderCrystal(0.0F, true);

    public RenderEnderCrystal(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    public void doRender(实体EnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        float f = (float)entity.innerRotation + partialTicks;
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)x, (float)y, (float)z);
        this.bindTexture(enderCrystalTextures);
        float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
        f1 = f1 * f1 + f1;
        this.modelEnderCrystal.render(entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(实体EnderCrystal entity)
    {
        return enderCrystalTextures;
    }
}
