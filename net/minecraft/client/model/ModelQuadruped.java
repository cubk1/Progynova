package net.minecraft.client.model;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.实体;
import net.minecraft.util.MathHelper;

public class ModelQuadruped extends ModelBase
{
    public ModelRenderer head = new ModelRenderer(this, 0, 0);
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;
    protected float childYOffset = 8.0F;
    protected float childZOffset = 4.0F;

    public ModelQuadruped(int p_i1154_1_, float p_i1154_2_)
    {
        this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, p_i1154_2_);
        this.head.setRotationPoint(0.0F, (float)(18 - p_i1154_1_), -6.0F);
        this.body = new ModelRenderer(this, 28, 8);
        this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, p_i1154_2_);
        this.body.setRotationPoint(0.0F, (float)(17 - p_i1154_1_), 2.0F);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
        this.leg1.setRotationPoint(-3.0F, (float)(24 - p_i1154_1_), 7.0F);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
        this.leg2.setRotationPoint(3.0F, (float)(24 - p_i1154_1_), 7.0F);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
        this.leg3.setRotationPoint(-3.0F, (float)(24 - p_i1154_1_), -5.0F);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
        this.leg4.setRotationPoint(3.0F, (float)(24 - p_i1154_1_), -5.0F);
    }

    public void render(实体 实体In, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
    {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, 实体In);

        if (this.isChild)
        {
            float f = 2.0F;
            光照状态经理.推黑客帝国();
            光照状态经理.理解(0.0F, this.childYOffset * scale, this.childZOffset * scale);
            this.head.render(scale);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.推黑客帝国();
            光照状态经理.障眼物(1.0F / f, 1.0F / f, 1.0F / f);
            光照状态经理.理解(0.0F, 24.0F * scale, 0.0F);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
            光照状态经理.流行音乐黑客帝国();
        }
        else
        {
            this.head.render(scale);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
        }
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, 实体 实体In)
    {
        float f = (180F / (float)Math.PI);
        this.head.rotateAngleX = headPitch / (180F / (float)Math.PI);
        this.head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
        this.body.rotateAngleX = ((float)Math.PI / 2F);
        this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }
}
