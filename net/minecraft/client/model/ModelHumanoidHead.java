package net.minecraft.client.model;

import net.minecraft.entity.实体;

public class ModelHumanoidHead extends ModelSkeletonHead
{
    private final ModelRenderer head = new ModelRenderer(this, 32, 0);

    public ModelHumanoidHead()
    {
        super(0, 0, 64, 64);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.25F);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void render(实体 实体In, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
    {
        super.render(实体In, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        this.head.render(scale);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, 实体 实体In)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, 实体In);
        this.head.rotateAngleY = this.skeletonHead.rotateAngleY;
        this.head.rotateAngleX = this.skeletonHead.rotateAngleX;
    }
}
