package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.ai.实体MinecartMobSpawner;
import net.minecraft.entity.boss.实体Dragon;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.实体LeashKnot;
import net.minecraft.entity.boss.实体Wither;
import net.minecraft.entity.effect.实体LightningBolt;
import net.minecraft.entity.item.实体TNTPrimed;
import net.minecraft.entity.monster.实体Ghast;
import net.minecraft.entity.passive.实体Bat;
import net.minecraft.entity.projectile.实体Snowball;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.player.PlayerItemsLayer;
import net.optifine.reflect.Reflector;
import net.optifine.shaders.Shaders;

public class RenderManager
{
    private Map<Class, Render> entityRenderMap = Maps.newHashMap();
    private Map<String, RenderPlayer> skinMap = Maps.<String, RenderPlayer>newHashMap();
    private RenderPlayer playerRenderer;
    private FontRenderer textRenderer;
    private double renderPosX;
    private double renderPosY;
    private double renderPosZ;
    public TextureManager renderEngine;
    public World worldObj;
    public 实体 livingPlayer;
    public 实体 pointed实体;
    public float playerViewY;
    public float playerViewX;
    public GameSettings options;
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    private boolean renderOutlines = false;
    private boolean renderShadow = true;
    private boolean debugBoundingBox = false;
    public Render renderRender = null;

    public RenderManager(TextureManager renderEngineIn, RenderItem itemRendererIn)
    {
        this.renderEngine = renderEngineIn;
        this.entityRenderMap.put(实体CaveSpider.class, new RenderCaveSpider(this));
        this.entityRenderMap.put(实体Spider.class, new RenderSpider(this));
        this.entityRenderMap.put(实体Pig.class, new RenderPig(this, new ModelPig(), 0.7F));
        this.entityRenderMap.put(实体Sheep.class, new RenderSheep(this, new ModelSheep2(), 0.7F));
        this.entityRenderMap.put(实体Cow.class, new RenderCow(this, new ModelCow(), 0.7F));
        this.entityRenderMap.put(实体Mooshroom.class, new RenderMooshroom(this, new ModelCow(), 0.7F));
        this.entityRenderMap.put(实体Wolf.class, new RenderWolf(this, new ModelWolf(), 0.5F));
        this.entityRenderMap.put(实体Chicken.class, new RenderChicken(this, new ModelChicken(), 0.3F));
        this.entityRenderMap.put(实体Ocelot.class, new RenderOcelot(this, new ModelOcelot(), 0.4F));
        this.entityRenderMap.put(实体Rabbit.class, new RenderRabbit(this, new ModelRabbit(), 0.3F));
        this.entityRenderMap.put(实体Silverfish.class, new RenderSilverfish(this));
        this.entityRenderMap.put(实体Endermite.class, new RenderEndermite(this));
        this.entityRenderMap.put(实体Creeper.class, new RenderCreeper(this));
        this.entityRenderMap.put(实体Enderman.class, new RenderEnderman(this));
        this.entityRenderMap.put(实体Snowman.class, new RenderSnowMan(this));
        this.entityRenderMap.put(实体Skeleton.class, new RenderSkeleton(this));
        this.entityRenderMap.put(实体Witch.class, new RenderWitch(this));
        this.entityRenderMap.put(实体Blaze.class, new RenderBlaze(this));
        this.entityRenderMap.put(实体PigZombie.class, new RenderPigZombie(this));
        this.entityRenderMap.put(实体Zombie.class, new RenderZombie(this));
        this.entityRenderMap.put(实体Slime.class, new RenderSlime(this, new ModelSlime(16), 0.25F));
        this.entityRenderMap.put(实体MagmaCube.class, new RenderMagmaCube(this));
        this.entityRenderMap.put(实体GiantZombie.class, new RenderGiantZombie(this, new ModelZombie(), 0.5F, 6.0F));
        this.entityRenderMap.put(实体Ghast.class, new RenderGhast(this));
        this.entityRenderMap.put(实体Squid.class, new RenderSquid(this, new ModelSquid(), 0.7F));
        this.entityRenderMap.put(实体Villager.class, new RenderVillager(this));
        this.entityRenderMap.put(实体IronGolem.class, new RenderIronGolem(this));
        this.entityRenderMap.put(实体Bat.class, new RenderBat(this));
        this.entityRenderMap.put(实体Guardian.class, new RenderGuardian(this));
        this.entityRenderMap.put(实体Dragon.class, new RenderDragon(this));
        this.entityRenderMap.put(实体EnderCrystal.class, new RenderEnderCrystal(this));
        this.entityRenderMap.put(实体Wither.class, new RenderWither(this));
        this.entityRenderMap.put(实体.class, new RenderEntity(this));
        this.entityRenderMap.put(实体Painting.class, new RenderPainting(this));
        this.entityRenderMap.put(实体ItemFrame.class, new RenderItemFrame(this, itemRendererIn));
        this.entityRenderMap.put(实体LeashKnot.class, new RenderLeashKnot(this));
        this.entityRenderMap.put(实体Arrow.class, new RenderArrow(this));
        this.entityRenderMap.put(实体Snowball.class, new RenderSnowball(this, Items.snowball, itemRendererIn));
        this.entityRenderMap.put(实体EnderPearl.class, new RenderSnowball(this, Items.ender_pearl, itemRendererIn));
        this.entityRenderMap.put(实体EnderEye.class, new RenderSnowball(this, Items.ender_eye, itemRendererIn));
        this.entityRenderMap.put(实体Egg.class, new RenderSnowball(this, Items.egg, itemRendererIn));
        this.entityRenderMap.put(实体Potion.class, new RenderPotion(this, itemRendererIn));
        this.entityRenderMap.put(实体ExpBottle.class, new RenderSnowball(this, Items.experience_bottle, itemRendererIn));
        this.entityRenderMap.put(实体FireworkRocket.class, new RenderSnowball(this, Items.fireworks, itemRendererIn));
        this.entityRenderMap.put(实体LargeFireball.class, new RenderFireball(this, 2.0F));
        this.entityRenderMap.put(实体SmallFireball.class, new RenderFireball(this, 0.5F));
        this.entityRenderMap.put(实体WitherSkull.class, new RenderWitherSkull(this));
        this.entityRenderMap.put(实体Item.class, new RenderEntityItem(this, itemRendererIn));
        this.entityRenderMap.put(实体XPOrb.class, new RenderXPOrb(this));
        this.entityRenderMap.put(实体TNTPrimed.class, new RenderTNTPrimed(this));
        this.entityRenderMap.put(实体FallingBlock.class, new RenderFallingBlock(this));
        this.entityRenderMap.put(实体ArmorStand.class, new ArmorStandRenderer(this));
        this.entityRenderMap.put(实体MinecartTNT.class, new RenderTntMinecart(this));
        this.entityRenderMap.put(实体MinecartMobSpawner.class, new RenderMinecartMobSpawner(this));
        this.entityRenderMap.put(实体Minecart.class, new RenderMinecart(this));
        this.entityRenderMap.put(实体Boat.class, new RenderBoat(this));
        this.entityRenderMap.put(实体FishHook.class, new RenderFish(this));
        this.entityRenderMap.put(实体Horse.class, new RenderHorse(this, new ModelHorse(), 0.75F));
        this.entityRenderMap.put(实体LightningBolt.class, new RenderLightningBolt(this));
        this.playerRenderer = new RenderPlayer(this);
        this.skinMap.put("default", this.playerRenderer);
        this.skinMap.put("slim", new RenderPlayer(this, true));
        PlayerItemsLayer.register(this.skinMap);

        if (Reflector.RenderingRegistry_loadEntityRenderers.exists())
        {
            Reflector.call(Reflector.RenderingRegistry_loadEntityRenderers, new Object[] {this, this.entityRenderMap});
        }
    }

    public void setRenderPosition(double renderPosXIn, double renderPosYIn, double renderPosZIn)
    {
        this.renderPosX = renderPosXIn;
        this.renderPosY = renderPosYIn;
        this.renderPosZ = renderPosZIn;
    }

    public <T extends 实体> Render<T> getEntityClassRenderObject(Class <? extends 实体> entityClass)
    {
        Render <? extends 实体> render = (Render)this.entityRenderMap.get(entityClass);

        if (render == null && entityClass != 实体.class)
        {
            render = this.<实体>getEntityClassRenderObject((Class <? extends 实体>)entityClass.getSuperclass());
            this.entityRenderMap.put(entityClass, render);
        }

        return (Render<T>)render;
    }

    public <T extends 实体> Render<T> getEntityRenderObject(实体 实体In)
    {
        if (实体In instanceof AbstractClientPlayer)
        {
            String s = ((AbstractClientPlayer) 实体In).getSkinType();
            RenderPlayer renderplayer = (RenderPlayer)this.skinMap.get(s);
            return (Render<T>)(renderplayer != null ? renderplayer : this.playerRenderer);
        }
        else
        {
            return this.<T>getEntityClassRenderObject(实体In.getClass());
        }
    }

    public void cacheActiveRenderInfo(World worldIn, FontRenderer textRendererIn, 实体 livingPlayerIn, 实体 pointed实体In, GameSettings optionsIn, float partialTicks)
    {
        this.worldObj = worldIn;
        this.options = optionsIn;
        this.livingPlayer = livingPlayerIn;
        this.pointed实体 = pointed实体In;
        this.textRenderer = textRendererIn;

        if (livingPlayerIn instanceof 实体LivingBase && ((实体LivingBase)livingPlayerIn).isPlayerSleeping())
        {
            IBlockState iblockstate = worldIn.getBlockState(new 阻止位置(livingPlayerIn));
            Block block = iblockstate.getBlock();

            if (Reflector.callBoolean(block, Reflector.ForgeBlock_isBed, new Object[] {iblockstate, worldIn, new 阻止位置(livingPlayerIn), (实体LivingBase)livingPlayerIn}))
            {
                EnumFacing enumfacing = (EnumFacing)Reflector.call(block, Reflector.ForgeBlock_getBedDirection, new Object[] {iblockstate, worldIn, new 阻止位置(livingPlayerIn)});
                int i = enumfacing.getHorizontalIndex();
                this.playerViewY = (float)(i * 90 + 180);
                this.playerViewX = 0.0F;
            }
            else if (block == Blocks.bed)
            {
                int j = ((EnumFacing)iblockstate.getValue(BlockBed.FACING)).getHorizontalIndex();
                this.playerViewY = (float)(j * 90 + 180);
                this.playerViewX = 0.0F;
            }
        }
        else
        {
            this.playerViewY = livingPlayerIn.prevRotationYaw + (livingPlayerIn.旋转侧滑 - livingPlayerIn.prevRotationYaw) * partialTicks;
            this.playerViewX = livingPlayerIn.prevRotationPitch + (livingPlayerIn.rotationPitch - livingPlayerIn.prevRotationPitch) * partialTicks;
        }

        if (optionsIn.thirdPersonView == 2)
        {
            this.playerViewY += 180.0F;
        }

        this.viewerPosX = livingPlayerIn.lastTickPosX + (livingPlayerIn.X坐标 - livingPlayerIn.lastTickPosX) * (double)partialTicks;
        this.viewerPosY = livingPlayerIn.lastTickPosY + (livingPlayerIn.Y坐标 - livingPlayerIn.lastTickPosY) * (double)partialTicks;
        this.viewerPosZ = livingPlayerIn.lastTickPosZ + (livingPlayerIn.Z坐标 - livingPlayerIn.lastTickPosZ) * (double)partialTicks;
    }

    public void setPlayerViewY(float playerViewYIn)
    {
        this.playerViewY = playerViewYIn;
    }

    public boolean isRenderShadow()
    {
        return this.renderShadow;
    }

    public void setRenderShadow(boolean renderShadowIn)
    {
        this.renderShadow = renderShadowIn;
    }

    public void setDebugBoundingBox(boolean debugBoundingBoxIn)
    {
        this.debugBoundingBox = debugBoundingBoxIn;
    }

    public boolean isDebugBoundingBox()
    {
        return this.debugBoundingBox;
    }

    public boolean renderEntitySimple(实体 实体In, float partialTicks)
    {
        return this.renderEntityStatic(实体In, partialTicks, false);
    }

    public boolean shouldRender(实体 实体In, ICamera camera, double camX, double camY, double camZ)
    {
        Render<实体> render = this.<实体>getEntityRenderObject(实体In);
        return render != null && render.shouldRender(实体In, camera, camX, camY, camZ);
    }

    public boolean renderEntityStatic(实体 实体, float partialTicks, boolean hideDebugBox)
    {
        if (实体.已存在的刻度 == 0)
        {
            实体.lastTickPosX = 实体.X坐标;
            实体.lastTickPosY = 实体.Y坐标;
            实体.lastTickPosZ = 实体.Z坐标;
        }

        double d0 = 实体.lastTickPosX + (实体.X坐标 - 实体.lastTickPosX) * (double)partialTicks;
        double d1 = 实体.lastTickPosY + (实体.Y坐标 - 实体.lastTickPosY) * (double)partialTicks;
        double d2 = 实体.lastTickPosZ + (实体.Z坐标 - 实体.lastTickPosZ) * (double)partialTicks;
        float f = 实体.prevRotationYaw + (实体.旋转侧滑 - 实体.prevRotationYaw) * partialTicks;
        int i = 实体.getBrightnessForRender(partialTicks);

        if (实体.isBurning())
        {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        return this.doRenderEntity(实体, d0 - this.renderPosX, d1 - this.renderPosY, d2 - this.renderPosZ, f, partialTicks, hideDebugBox);
    }

    public void renderWitherSkull(实体 实体In, float partialTicks)
    {
        double d0 = 实体In.lastTickPosX + (实体In.X坐标 - 实体In.lastTickPosX) * (double)partialTicks;
        double d1 = 实体In.lastTickPosY + (实体In.Y坐标 - 实体In.lastTickPosY) * (double)partialTicks;
        double d2 = 实体In.lastTickPosZ + (实体In.Z坐标 - 实体In.lastTickPosZ) * (double)partialTicks;
        Render<实体> render = this.<实体>getEntityRenderObject(实体In);

        if (render != null && this.renderEngine != null)
        {
            int i = 实体In.getBrightnessForRender(partialTicks);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            render.renderName(实体In, d0 - this.renderPosX, d1 - this.renderPosY, d2 - this.renderPosZ);
        }
    }

    public boolean renderEntityWithPosYaw(实体 实体In, double x, double y, double z, float entityYaw, float partialTicks)
    {
        return this.doRenderEntity(实体In, x, y, z, entityYaw, partialTicks, false);
    }

    public boolean doRenderEntity(实体 实体, double x, double y, double z, float entityYaw, float partialTicks, boolean hideDebugBox)
    {
        Render<实体> render = null;

        try
        {
            render = this.<实体>getEntityRenderObject(实体);

            if (render != null && this.renderEngine != null)
            {
                try
                {
                    if (render instanceof RendererLivingEntity)
                    {
                        ((RendererLivingEntity)render).setRenderOutlines(this.renderOutlines);
                    }

                    if (CustomEntityModels.isActive())
                    {
                        this.renderRender = render;
                    }

                    render.doRender(实体, x, y, z, entityYaw, partialTicks);
                }
                catch (Throwable throwable2)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Rendering entity in world"));
                }

                try
                {
                    if (!this.renderOutlines)
                    {
                        render.doRenderShadowAndFire(实体, x, y, z, entityYaw, partialTicks);
                    }
                }
                catch (Throwable throwable1)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Post-rendering entity in world"));
                }

                if (this.debugBoundingBox && !实体.isInvisible() && !hideDebugBox)
                {
                    try
                    {
                        this.renderDebugBoundingBox(实体, x, y, z, entityYaw, partialTicks);
                    }
                    catch (Throwable throwable)
                    {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
                    }
                }
            }
            else if (this.renderEngine != null)
            {
                return false;
            }

            return true;
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            实体.addEntityCrashInfo(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addCrashSection("Assigned renderer", render);
            crashreportcategory1.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
            crashreportcategory1.addCrashSection("Rotation", Float.valueOf(entityYaw));
            crashreportcategory1.addCrashSection("Delta", Float.valueOf(partialTicks));
            throw new ReportedException(crashreport);
        }
    }

    private void renderDebugBoundingBox(实体 实体In, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (!Shaders.isShadowPass)
        {
            光照状态经理.depthMask(false);
            光照状态经理.禁用手感();
            光照状态经理.disableLighting();
            光照状态经理.disableCull();
            光照状态经理.禁用混合品();
            float f = 实体In.width / 2.0F;
            AxisAlignedBB axisalignedbb = 实体In.getEntityBoundingBox();
            AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX - 实体In.X坐标 + x, axisalignedbb.minY - 实体In.Y坐标 + y, axisalignedbb.minZ - 实体In.Z坐标 + z, axisalignedbb.maxX - 实体In.X坐标 + x, axisalignedbb.maxY - 实体In.Y坐标 + y, axisalignedbb.maxZ - 实体In.Z坐标 + z);
            RenderGlobal.drawOutlinedBoundingBox(axisalignedbb1, 255, 255, 255, 255);

            if (实体In instanceof 实体LivingBase)
            {
                float f1 = 0.01F;
                RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x - (double)f, y + (double) 实体In.getEyeHeight() - 0.009999999776482582D, z - (double)f, x + (double)f, y + (double) 实体In.getEyeHeight() + 0.009999999776482582D, z + (double)f), 255, 0, 0, 255);
            }

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            Vec3 vec3 = 实体In.getLook(partialTicks);
            worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(x, y + (double) 实体In.getEyeHeight(), z).color(0, 0, 255, 255).endVertex();
            worldrenderer.pos(x + vec3.xCoord * 2.0D, y + (double) 实体In.getEyeHeight() + vec3.yCoord * 2.0D, z + vec3.zCoord * 2.0D).color(0, 0, 255, 255).endVertex();
            tessellator.draw();
            光照状态经理.启用手感();
            光照状态经理.enableLighting();
            光照状态经理.enableCull();
            光照状态经理.禁用混合品();
            光照状态经理.depthMask(true);
        }
    }

    public void set(World worldIn)
    {
        this.worldObj = worldIn;
    }

    public double getDistanceToCamera(double x, double y, double z)
    {
        double d0 = x - this.viewerPosX;
        double d1 = y - this.viewerPosY;
        double d2 = z - this.viewerPosZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public FontRenderer getFontRenderer()
    {
        return this.textRenderer;
    }

    public void setRenderOutlines(boolean renderOutlinesIn)
    {
        this.renderOutlines = renderOutlinesIn;
    }

    public Map<Class, Render> getEntityRenderMap()
    {
        return this.entityRenderMap;
    }

    public void setEntityRenderMap(Map p_setEntityRenderMap_1_)
    {
        this.entityRenderMap = p_setEntityRenderMap_1_;
    }

    public Map<String, RenderPlayer> getSkinMap()
    {
        return Collections.<String, RenderPlayer>unmodifiableMap(this.skinMap);
    }
}
