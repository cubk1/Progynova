package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.图像位置;

public class RenderBiped<T extends EntityLiving> extends RenderLiving<T>
{
    private static final 图像位置 DEFAULT_RES_LOC = new 图像位置("textures/entity/steve.png");
    protected ModelBiped modelBipedMain;
    protected float field_77070_b;

    public RenderBiped(RenderManager renderManagerIn, ModelBiped modelBipedIn, float shadowSize)
    {
        this(renderManagerIn, modelBipedIn, shadowSize, 1.0F);
        this.addLayer(new LayerHeldItem(this));
    }

    public RenderBiped(RenderManager renderManagerIn, ModelBiped modelBipedIn, float shadowSize, float p_i46169_4_)
    {
        super(renderManagerIn, modelBipedIn, shadowSize);
        this.modelBipedMain = modelBipedIn;
        this.field_77070_b = p_i46169_4_;
        this.addLayer(new LayerCustomHead(modelBipedIn.bipedHead));
    }

    protected 图像位置 getEntityTexture(T entity)
    {
        return DEFAULT_RES_LOC;
    }

    public void transformHeldFull3DItemLayer()
    {
        光照状态经理.理解(0.0F, 0.1875F, 0.0F);
    }
}
