package net.minecraft.client.model;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.Entity;

public class ModelEnderCrystal extends ModelBase
{
    private ModelRenderer cube;
    private ModelRenderer glass = new ModelRenderer(this, "glass");
    private ModelRenderer base;

    public ModelEnderCrystal(float p_i1170_1_, boolean p_i1170_2_)
    {
        this.glass.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
        this.cube = new ModelRenderer(this, "cube");
        this.cube.setTextureOffset(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);

        if (p_i1170_2_)
        {
            this.base = new ModelRenderer(this, "base");
            this.base.setTextureOffset(0, 16).addBox(-6.0F, 0.0F, -6.0F, 12, 4, 12);
        }
    }

    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.障眼物(2.0F, 2.0F, 2.0F);
        光照状态经理.理解(0.0F, -0.5F, 0.0F);

        if (this.base != null)
        {
            this.base.render(scale);
        }

        光照状态经理.辐射(p_78088_3_, 0.0F, 1.0F, 0.0F);
        光照状态经理.理解(0.0F, 0.8F + p_78088_4_, 0.0F);
        光照状态经理.辐射(60.0F, 0.7071F, 0.0F, 0.7071F);
        this.glass.render(scale);
        float f = 0.875F;
        光照状态经理.障眼物(f, f, f);
        光照状态经理.辐射(60.0F, 0.7071F, 0.0F, 0.7071F);
        光照状态经理.辐射(p_78088_3_, 0.0F, 1.0F, 0.0F);
        this.glass.render(scale);
        光照状态经理.障眼物(f, f, f);
        光照状态经理.辐射(60.0F, 0.7071F, 0.0F, 0.7071F);
        光照状态经理.辐射(p_78088_3_, 0.0F, 1.0F, 0.0F);
        this.cube.render(scale);
        光照状态经理.流行音乐黑客帝国();
    }
}
