package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.实体;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer<TileEntityEnchantmentTable>
{
    private static final 图像位置 TEXTURE_BOOK = new 图像位置("textures/entity/enchanting_table_book.png");
    private ModelBook field_147541_c = new ModelBook();

    public void renderTileEntityAt(TileEntityEnchantmentTable te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)x + 0.5F, (float)y + 0.75F, (float)z + 0.5F);
        float f = (float)te.tickCount + partialTicks;
        光照状态经理.理解(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F, 0.0F);
        float f1;

        for (f1 = te.bookRotation - te.bookRotationPrev; f1 >= (float)Math.PI; f1 -= ((float)Math.PI * 2F))
        {
            ;
        }

        while (f1 < -(float)Math.PI)
        {
            f1 += ((float)Math.PI * 2F);
        }

        float f2 = te.bookRotationPrev + f1 * partialTicks;
        光照状态经理.辐射(-f2 * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(80.0F, 0.0F, 0.0F, 1.0F);
        this.bindTexture(TEXTURE_BOOK);
        float f3 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.25F;
        float f4 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.75F;
        f3 = (f3 - (float)MathHelper.truncateDoubleToInt((double)f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float)MathHelper.truncateDoubleToInt((double)f4)) * 1.6F - 0.3F;

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        if (f4 < 0.0F)
        {
            f4 = 0.0F;
        }

        if (f3 > 1.0F)
        {
            f3 = 1.0F;
        }

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        float f5 = te.bookSpreadPrev + (te.bookSpread - te.bookSpreadPrev) * partialTicks;
        光照状态经理.enableCull();
        this.field_147541_c.render((实体)null, f, f3, f4, f5, 0.0F, 0.0625F);
        光照状态经理.流行音乐黑客帝国();
    }
}
