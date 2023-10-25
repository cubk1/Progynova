package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.实体;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.图像位置;

public class RenderSnowball<T extends 实体> extends Render<T>
{
    protected final Item field_177084_a;
    private final RenderItem field_177083_e;

    public RenderSnowball(RenderManager renderManagerIn, Item p_i46137_2_, RenderItem p_i46137_3_)
    {
        super(renderManagerIn);
        this.field_177084_a = p_i46137_2_;
        this.field_177083_e = p_i46137_3_;
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)x, (float)y, (float)z);
        光照状态经理.enableRescaleNormal();
        光照状态经理.障眼物(0.5F, 0.5F, 0.5F);
        光照状态经理.辐射(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        this.bindTexture(TextureMap.locationBlocksTexture);
        this.field_177083_e.renderItem(this.func_177082_d(entity), ItemCameraTransforms.TransformType.GROUND);
        光照状态经理.disableRescaleNormal();
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public ItemStack func_177082_d(T entityIn)
    {
        return new ItemStack(this.field_177084_a, 1, 0);
    }

    protected 图像位置 getEntityTexture(实体 实体)
    {
        return TextureMap.locationBlocksTexture;
    }
}
