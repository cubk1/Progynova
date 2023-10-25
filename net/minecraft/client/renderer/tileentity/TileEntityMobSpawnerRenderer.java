package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.实体;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer<TileEntityMobSpawner>
{
    public void renderTileEntityAt(TileEntityMobSpawner te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)x + 0.5F, (float)y, (float)z + 0.5F);
        renderMob(te.getSpawnerBaseLogic(), x, y, z, partialTicks);
        光照状态经理.流行音乐黑客帝国();
    }

    public static void renderMob(MobSpawnerBaseLogic mobSpawnerLogic, double posX, double posY, double posZ, float partialTicks)
    {
        实体 实体 = mobSpawnerLogic.func_180612_a(mobSpawnerLogic.getSpawnerWorld());

        if (实体 != null)
        {
            float f = 0.4375F;
            光照状态经理.理解(0.0F, 0.4F, 0.0F);
            光照状态经理.辐射((float)(mobSpawnerLogic.getPrevMobRotation() + (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * (double)partialTicks) * 10.0F, 0.0F, 1.0F, 0.0F);
            光照状态经理.辐射(-30.0F, 1.0F, 0.0F, 0.0F);
            光照状态经理.理解(0.0F, -0.4F, 0.0F);
            光照状态经理.障眼物(f, f, f);
            实体.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
            我的手艺.得到我的手艺().getRenderManager().renderEntityWithPosYaw(实体, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        }
    }
}
