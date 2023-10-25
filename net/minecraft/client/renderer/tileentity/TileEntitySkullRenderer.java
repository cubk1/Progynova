package net.minecraft.client.renderer.tileentity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.图像位置;

public class TileEntitySkullRenderer extends TileEntitySpecialRenderer<TileEntitySkull>
{
    private static final 图像位置 SKELETON_TEXTURES = new 图像位置("textures/entity/skeleton/skeleton.png");
    private static final 图像位置 WITHER_SKELETON_TEXTURES = new 图像位置("textures/entity/skeleton/wither_skeleton.png");
    private static final 图像位置 ZOMBIE_TEXTURES = new 图像位置("textures/entity/zombie/zombie.png");
    private static final 图像位置 CREEPER_TEXTURES = new 图像位置("textures/entity/creeper/creeper.png");
    public static TileEntitySkullRenderer instance;
    private final ModelSkeletonHead skeletonHead = new ModelSkeletonHead(0, 0, 64, 32);
    private final ModelSkeletonHead humanoidHead = new ModelHumanoidHead();

    public void renderTileEntityAt(TileEntitySkull te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        EnumFacing enumfacing = EnumFacing.getFront(te.getBlockMetadata() & 7);
        this.renderSkull((float)x, (float)y, (float)z, enumfacing, (float)(te.getSkullRotation() * 360) / 16.0F, te.getSkullType(), te.getPlayerProfile(), destroyStage);
    }

    public void setRendererDispatcher(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super.setRendererDispatcher(rendererDispatcherIn);
        instance = this;
    }

    public void renderSkull(float p_180543_1_, float p_180543_2_, float p_180543_3_, EnumFacing p_180543_4_, float p_180543_5_, int p_180543_6_, GameProfile p_180543_7_, int p_180543_8_)
    {
        ModelBase modelbase = this.skeletonHead;

        if (p_180543_8_ >= 0)
        {
            this.bindTexture(DESTROY_STAGES[p_180543_8_]);
            光照状态经理.matrixMode(5890);
            光照状态经理.推黑客帝国();
            光照状态经理.障眼物(4.0F, 2.0F, 1.0F);
            光照状态经理.理解(0.0625F, 0.0625F, 0.0625F);
            光照状态经理.matrixMode(5888);
        }
        else
        {
            switch (p_180543_6_)
            {
                case 0:
                default:
                    this.bindTexture(SKELETON_TEXTURES);
                    break;

                case 1:
                    this.bindTexture(WITHER_SKELETON_TEXTURES);
                    break;

                case 2:
                    this.bindTexture(ZOMBIE_TEXTURES);
                    modelbase = this.humanoidHead;
                    break;

                case 3:
                    modelbase = this.humanoidHead;
                    图像位置 resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();

                    if (p_180543_7_ != null)
                    {
                        我的手艺 宇轩的世界 = 我的手艺.得到我的手艺();
                        Map<Type, MinecraftProfileTexture> map = 宇轩的世界.getSkinManager().loadSkinFromCache(p_180543_7_);

                        if (map.containsKey(Type.SKIN))
                        {
                            resourcelocation = 宇轩的世界.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
                        }
                        else
                        {
                            UUID uuid = EntityPlayer.getUUID(p_180543_7_);
                            resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
                        }
                    }

                    this.bindTexture(resourcelocation);
                    break;

                case 4:
                    this.bindTexture(CREEPER_TEXTURES);
            }
        }

        光照状态经理.推黑客帝国();
        光照状态经理.disableCull();

        if (p_180543_4_ != EnumFacing.UP)
        {
            switch (p_180543_4_)
            {
                case NORTH:
                    光照状态经理.理解(p_180543_1_ + 0.5F, p_180543_2_ + 0.25F, p_180543_3_ + 0.74F);
                    break;

                case SOUTH:
                    光照状态经理.理解(p_180543_1_ + 0.5F, p_180543_2_ + 0.25F, p_180543_3_ + 0.26F);
                    p_180543_5_ = 180.0F;
                    break;

                case WEST:
                    光照状态经理.理解(p_180543_1_ + 0.74F, p_180543_2_ + 0.25F, p_180543_3_ + 0.5F);
                    p_180543_5_ = 270.0F;
                    break;

                case EAST:
                default:
                    光照状态经理.理解(p_180543_1_ + 0.26F, p_180543_2_ + 0.25F, p_180543_3_ + 0.5F);
                    p_180543_5_ = 90.0F;
            }
        }
        else
        {
            光照状态经理.理解(p_180543_1_ + 0.5F, p_180543_2_, p_180543_3_ + 0.5F);
        }

        float f = 0.0625F;
        光照状态经理.enableRescaleNormal();
        光照状态经理.障眼物(-1.0F, -1.0F, 1.0F);
        光照状态经理.启用希腊字母表的第1个字母();
        modelbase.render((Entity)null, 0.0F, 0.0F, 0.0F, p_180543_5_, 0.0F, f);
        光照状态经理.流行音乐黑客帝国();

        if (p_180543_8_ >= 0)
        {
            光照状态经理.matrixMode(5890);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.matrixMode(5888);
        }
    }
}
