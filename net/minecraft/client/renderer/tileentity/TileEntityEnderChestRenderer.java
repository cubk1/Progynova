package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.图像位置;

public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer<TileEntityEnderChest>
{
    private static final 图像位置 ENDER_CHEST_TEXTURE = new 图像位置("textures/entity/chest/ender.png");
    private ModelChest field_147521_c = new ModelChest();

    public void renderTileEntityAt(TileEntityEnderChest te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        int i = 0;

        if (te.hasWorldObj())
        {
            i = te.getBlockMetadata();
        }

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            光照状态经理.matrixMode(5890);
            光照状态经理.推黑客帝国();
            光照状态经理.障眼物(4.0F, 4.0F, 1.0F);
            光照状态经理.理解(0.0625F, 0.0625F, 0.0625F);
            光照状态经理.matrixMode(5888);
        }
        else
        {
            this.bindTexture(ENDER_CHEST_TEXTURE);
        }

        光照状态经理.推黑客帝国();
        光照状态经理.enableRescaleNormal();
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.理解((float)x, (float)y + 1.0F, (float)z + 1.0F);
        光照状态经理.障眼物(1.0F, -1.0F, -1.0F);
        光照状态经理.理解(0.5F, 0.5F, 0.5F);
        int j = 0;

        if (i == 2)
        {
            j = 180;
        }

        if (i == 3)
        {
            j = 0;
        }

        if (i == 4)
        {
            j = 90;
        }

        if (i == 5)
        {
            j = -90;
        }

        光照状态经理.辐射((float)j, 0.0F, 1.0F, 0.0F);
        光照状态经理.理解(-0.5F, -0.5F, -0.5F);
        float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        this.field_147521_c.chestLid.rotateAngleX = -(f * (float)Math.PI / 2.0F);
        this.field_147521_c.renderAll();
        光照状态经理.disableRescaleNormal();
        光照状态经理.流行音乐黑客帝国();
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0)
        {
            光照状态经理.matrixMode(5890);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.matrixMode(5888);
        }
    }
}
