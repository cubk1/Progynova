package net.minecraft.client.model;

import net.minecraft.entity.passive.实体Sheep;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;

public class ModelSheep2 extends ModelQuadruped
{
    private float headRotationAngleX;

    public ModelSheep2()
    {
        super(12, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-3.0F, -4.0F, -6.0F, 6, 6, 8, 0.0F);
        this.head.setRotationPoint(0.0F, 6.0F, -8.0F);
        this.body = new ModelRenderer(this, 28, 8);
        this.body.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
    }

    public void setLivingAnimations(实体LivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
    {
        super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
        this.head.rotationPointY = 6.0F + ((实体Sheep)entitylivingbaseIn).getHeadRotationPointY(partialTickTime) * 9.0F;
        this.headRotationAngleX = ((实体Sheep)entitylivingbaseIn).getHeadRotationAngleX(partialTickTime);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, 实体 实体In)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, 实体In);
        this.head.rotateAngleX = this.headRotationAngleX;
    }
}
