package net.minecraft.client.renderer.tileentity;

import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.图像位置;

public class TileEntityChestRenderer extends TileEntitySpecialRenderer<TileEntityChest>
{
    private static final 图像位置 textureTrappedDouble = new 图像位置("textures/entity/chest/trapped_double.png");
    private static final 图像位置 textureChristmasDouble = new 图像位置("textures/entity/chest/christmas_double.png");
    private static final 图像位置 textureNormalDouble = new 图像位置("textures/entity/chest/normal_double.png");
    private static final 图像位置 textureTrapped = new 图像位置("textures/entity/chest/trapped.png");
    private static final 图像位置 textureChristmas = new 图像位置("textures/entity/chest/christmas.png");
    private static final 图像位置 textureNormal = new 图像位置("textures/entity/chest/normal.png");
    private ModelChest simpleChest = new ModelChest();
    private ModelChest largeChest = new ModelLargeChest();
    private boolean isChristmas;

    public TileEntityChestRenderer()
    {
        Calendar calendar = Calendar.getInstance();

        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26)
        {
            this.isChristmas = true;
        }
    }

    public void renderTileEntityAt(TileEntityChest te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        光照状态经理.启用纵深();
        光照状态经理.depthFunc(515);
        光照状态经理.depthMask(true);
        int i;

        if (!te.hasWorldObj())
        {
            i = 0;
        }
        else
        {
            Block block = te.getBlockType();
            i = te.getBlockMetadata();

            if (block instanceof BlockChest && i == 0)
            {
                ((BlockChest)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }

            te.checkForAdjacentChests();
        }

        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null)
        {
            ModelChest modelchest;

            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null)
            {
                modelchest = this.simpleChest;

                if (destroyStage >= 0)
                {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    光照状态经理.matrixMode(5890);
                    光照状态经理.推黑客帝国();
                    光照状态经理.障眼物(4.0F, 4.0F, 1.0F);
                    光照状态经理.理解(0.0625F, 0.0625F, 0.0625F);
                    光照状态经理.matrixMode(5888);
                }
                else if (this.isChristmas)
                {
                    this.bindTexture(textureChristmas);
                }
                else if (te.getChestType() == 1)
                {
                    this.bindTexture(textureTrapped);
                }
                else
                {
                    this.bindTexture(textureNormal);
                }
            }
            else
            {
                modelchest = this.largeChest;

                if (destroyStage >= 0)
                {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    光照状态经理.matrixMode(5890);
                    光照状态经理.推黑客帝国();
                    光照状态经理.障眼物(8.0F, 4.0F, 1.0F);
                    光照状态经理.理解(0.0625F, 0.0625F, 0.0625F);
                    光照状态经理.matrixMode(5888);
                }
                else if (this.isChristmas)
                {
                    this.bindTexture(textureChristmasDouble);
                }
                else if (te.getChestType() == 1)
                {
                    this.bindTexture(textureTrappedDouble);
                }
                else
                {
                    this.bindTexture(textureNormalDouble);
                }
            }

            光照状态经理.推黑客帝国();
            光照状态经理.enableRescaleNormal();

            if (destroyStage < 0)
            {
                光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            }

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

            if (i == 2 && te.adjacentChestXPos != null)
            {
                光照状态经理.理解(1.0F, 0.0F, 0.0F);
            }

            if (i == 5 && te.adjacentChestZPos != null)
            {
                光照状态经理.理解(0.0F, 0.0F, -1.0F);
            }

            光照状态经理.辐射((float)j, 0.0F, 1.0F, 0.0F);
            光照状态经理.理解(-0.5F, -0.5F, -0.5F);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

            if (te.adjacentChestZNeg != null)
            {
                float f1 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;

                if (f1 > f)
                {
                    f = f1;
                }
            }

            if (te.adjacentChestXNeg != null)
            {
                float f2 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;

                if (f2 > f)
                {
                    f = f2;
                }
            }

            f = 1.0F - f;
            f = 1.0F - f * f * f;
            modelchest.chestLid.rotateAngleX = -(f * (float)Math.PI / 2.0F);
            modelchest.renderAll();
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
}
