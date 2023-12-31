package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.我的手艺;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.鬼Chat;
import net.minecraft.client.gui.鬼DownloadTerrain;
import net.minecraft.client.gui.鬼MainMenu;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.比例解析;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.实体ItemFrame;
import net.minecraft.entity.monster.实体Creeper;
import net.minecraft.entity.monster.实体Enderman;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.monster.实体Spider;
import net.minecraft.entity.passive.实体Animal;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.src.Config;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.图像位置;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.CustomColors;
import net.optifine.GlErrors;
import net.optifine.Lagometer;
import net.optifine.RandomEntities;
import net.optifine.gui.鬼ChatOF;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.reflect.ReflectorResolver;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import net.optifine.util.MemoryMonitor;
import net.optifine.util.TextureUtils;
import net.optifine.util.TimedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class EntityRenderer implements IResourceManagerReloadListener
{
    private static final Logger logger = LogManager.getLogger();
    private static final 图像位置 locationRainPng = new 图像位置("textures/environment/rain.png");
    private static final 图像位置 locationSnowPng = new 图像位置("textures/environment/snow.png");
    public static boolean anaglyphEnable;
    public static int anaglyphField;
    private 我的手艺 mc;
    private final IResourceManager resourceManager;
    private Random random = new Random();
    private float farPlaneDistance;
    public ItemRenderer itemRenderer;
    private final MapItemRenderer theMapItemRenderer;
    private int rendererUpdateCount;
    private 实体 pointed实体;
    private MouseFilter mouseFilterXAxis = new MouseFilter();
    private MouseFilter mouseFilterYAxis = new MouseFilter();
    private float thirdPersonDistance = 4.0F;
    private float thirdPersonDistanceTemp = 4.0F;
    private float smoothCamYaw;
    private float smoothCamPitch;
    private float smoothCamFilterX;
    private float smoothCamFilterY;
    private float smoothCamPartialTicks;
    private float fovModifierHand;
    private float fovModifierHandPrev;
    private float bossColorModifier;
    private float bossColorModifierPrev;
    private boolean cloudFog;
    private boolean renderHand = true;
    private boolean drawBlockOutline = true;
    private long prevFrameTime = 我的手艺.getSystemTime();
    private long renderEndNanoTime;
    private final DynamicTexture lightmapTexture;
    private final int[] lightmapColors;
    private final 图像位置 locationLightMap;
    private boolean lightmapUpdateNeeded;
    private float torchFlickerX;
    private float torchFlickerDX;
    private int rainSoundCounter;
    private float[] rainXCoords = new float[1024];
    private float[] rainYCoords = new float[1024];
    private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    public float fogColorRed;
    public float fogColorGreen;
    public float fogColorBlue;
    private float fogColor2;
    private float fogColor1;
    private int debugViewDirection = 0;
    private boolean debugView = false;
    private double cameraZoom = 1.0D;
    private double cameraYaw;
    private double cameraPitch;
    private ShaderGroup theShaderGroup;
    private static final 图像位置[] SHADER_图像位置s = new 图像位置[] {new 图像位置("shaders/post/notch.json"), new 图像位置("shaders/post/fxaa.json"), new 图像位置("shaders/post/art.json"), new 图像位置("shaders/post/bumpy.json"), new 图像位置("shaders/post/blobs2.json"), new 图像位置("shaders/post/pencil.json"), new 图像位置("shaders/post/color_convolve.json"), new 图像位置("shaders/post/deconverge.json"), new 图像位置("shaders/post/flip.json"), new 图像位置("shaders/post/invert.json"), new 图像位置("shaders/post/ntsc.json"), new 图像位置("shaders/post/outline.json"), new 图像位置("shaders/post/phosphor.json"), new 图像位置("shaders/post/scan_pincushion.json"), new 图像位置("shaders/post/sobel.json"), new 图像位置("shaders/post/bits.json"), new 图像位置("shaders/post/desaturate.json"), new 图像位置("shaders/post/green.json"), new 图像位置("shaders/post/blur.json"), new 图像位置("shaders/post/wobble.json"), new 图像位置("shaders/post/blobs.json"), new 图像位置("shaders/post/antialias.json"), new 图像位置("shaders/post/creeper.json"), new 图像位置("shaders/post/spider.json")};
    public static final int shaderCount = SHADER_图像位置s.length;
    private int shaderIndex;
    private boolean useShader;
    public int frameCount;
    private boolean initialized = false;
    private World updatedWorld = null;
    private boolean showDebugInfo = false;
    public boolean fogStandard = false;
    private float clipDistance = 128.0F;
    private long lastServerTime = 0L;
    private int lastServerTicks = 0;
    private int serverWaitTime = 0;
    private int serverWaitTimeCurrent = 0;
    private float avgServerTimeDiff = 0.0F;
    private float avgServerTickDiff = 0.0F;
    private ShaderGroup[] fxaaShaders = new ShaderGroup[10];
    private boolean loadVisibleChunks = false;

    public EntityRenderer(我的手艺 mcIn, IResourceManager resourceManagerIn)
    {
        this.shaderIndex = shaderCount;
        this.useShader = false;
        this.frameCount = 0;
        this.mc = mcIn;
        this.resourceManager = resourceManagerIn;
        this.itemRenderer = mcIn.getItemRenderer();
        this.theMapItemRenderer = new MapItemRenderer(mcIn.得到手感经理());
        this.lightmapTexture = new DynamicTexture(16, 16);
        this.locationLightMap = mcIn.得到手感经理().getDynamicTextureLocation("lightMap", this.lightmapTexture);
        this.lightmapColors = this.lightmapTexture.getTextureData();
        this.theShaderGroup = null;

        for (int i = 0; i < 32; ++i)
        {
            for (int j = 0; j < 32; ++j)
            {
                float f = (float)(j - 16);
                float f1 = (float)(i - 16);
                float f2 = MathHelper.sqrt_float(f * f + f1 * f1);
                this.rainXCoords[i << 5 | j] = -f1 / f2;
                this.rainYCoords[i << 5 | j] = f / f2;
            }
        }
    }

    public boolean isShaderActive()
    {
        return OpenGlHelper.shadersSupported && this.theShaderGroup != null;
    }

    public void stopUseShader()
    {
        if (this.theShaderGroup != null)
        {
            this.theShaderGroup.deleteShaderGroup();
        }

        this.theShaderGroup = null;
        this.shaderIndex = shaderCount;
    }

    public void switchUseShader()
    {
        this.useShader = !this.useShader;
    }

    public void loadEntityShader(实体 实体In)
    {
        if (OpenGlHelper.shadersSupported)
        {
            if (this.theShaderGroup != null)
            {
                this.theShaderGroup.deleteShaderGroup();
            }

            this.theShaderGroup = null;

            if (实体In instanceof 实体Creeper)
            {
                this.loadShader(new 图像位置("shaders/post/creeper.json"));
            }
            else if (实体In instanceof 实体Spider)
            {
                this.loadShader(new 图像位置("shaders/post/spider.json"));
            }
            else if (实体In instanceof 实体Enderman)
            {
                this.loadShader(new 图像位置("shaders/post/invert.json"));
            }
            else if (Reflector.ForgeHooksClient_loadEntityShader.exists())
            {
                Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, new Object[] {实体In, this});
            }
        }
    }

    public void activateNextShader()
    {
        if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof 实体Player)
        {
            if (this.theShaderGroup != null)
            {
                this.theShaderGroup.deleteShaderGroup();
            }

            this.shaderIndex = (this.shaderIndex + 1) % (SHADER_图像位置s.length + 1);

            if (this.shaderIndex != shaderCount)
            {
                this.loadShader(SHADER_图像位置s[this.shaderIndex]);
            }
            else
            {
                this.theShaderGroup = null;
            }
        }
    }

    private void loadShader(图像位置 图像位置In)
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            try
            {
                this.theShaderGroup = new ShaderGroup(this.mc.得到手感经理(), this.resourceManager, this.mc.getFramebuffer(), 图像位置In);
                this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
                this.useShader = true;
            }
            catch (IOException ioexception)
            {
                logger.warn((String)("Failed to load shader: " + 图像位置In), (Throwable)ioexception);
                this.shaderIndex = shaderCount;
                this.useShader = false;
            }
            catch (JsonSyntaxException jsonsyntaxexception)
            {
                logger.warn((String)("Failed to load shader: " + 图像位置In), (Throwable)jsonsyntaxexception);
                this.shaderIndex = shaderCount;
                this.useShader = false;
            }
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        if (this.theShaderGroup != null)
        {
            this.theShaderGroup.deleteShaderGroup();
        }

        this.theShaderGroup = null;

        if (this.shaderIndex != shaderCount)
        {
            this.loadShader(SHADER_图像位置s[this.shaderIndex]);
        }
        else
        {
            this.loadEntityShader(this.mc.getRenderViewEntity());
        }
    }

    public void updateRenderer()
    {
        if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null)
        {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }

        this.updateFovModifierHand();
        this.updateTorchFlicker();
        this.fogColor2 = this.fogColor1;
        this.thirdPersonDistanceTemp = this.thirdPersonDistance;

        if (this.mc.游戏一窝.smoothCamera)
        {
            float f = this.mc.游戏一窝.mouseSensitivity * 0.6F + 0.2F;
            float f1 = f * f * f * 8.0F;
            this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * f1);
            this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * f1);
            this.smoothCamPartialTicks = 0.0F;
            this.smoothCamYaw = 0.0F;
            this.smoothCamPitch = 0.0F;
        }
        else
        {
            this.smoothCamFilterX = 0.0F;
            this.smoothCamFilterY = 0.0F;
            this.mouseFilterXAxis.reset();
            this.mouseFilterYAxis.reset();
        }

        if (this.mc.getRenderViewEntity() == null)
        {
            this.mc.setRenderViewEntity(this.mc.宇轩游玩者);
        }

        实体 实体 = this.mc.getRenderViewEntity();
        double d2 = 实体.X坐标;
        double d0 = 实体.Y坐标 + (double) 实体.getEyeHeight();
        double d1 = 实体.Z坐标;
        float f2 = this.mc.宇轩の世界.getLightBrightness(new 阻止位置(d2, d0, d1));
        float f3 = (float)this.mc.游戏一窝.renderDistanceChunks / 16.0F;
        f3 = MathHelper.clamp_float(f3, 0.0F, 1.0F);
        float f4 = f2 * (1.0F - f3) + f3;
        this.fogColor1 += (f4 - this.fogColor1) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
        this.bossColorModifierPrev = this.bossColorModifier;

        if (BossStatus.hasColorModifier)
        {
            this.bossColorModifier += 0.05F;

            if (this.bossColorModifier > 1.0F)
            {
                this.bossColorModifier = 1.0F;
            }

            BossStatus.hasColorModifier = false;
        }
        else if (this.bossColorModifier > 0.0F)
        {
            this.bossColorModifier -= 0.0125F;
        }
    }

    public ShaderGroup getShaderGroup()
    {
        return this.theShaderGroup;
    }

    public void updateShaderGroupSize(int width, int height)
    {
        if (OpenGlHelper.shadersSupported)
        {
            if (this.theShaderGroup != null)
            {
                this.theShaderGroup.createBindFramebuffers(width, height);
            }

            this.mc.renderGlobal.createBindEntityOutlineFbs(width, height);
        }
    }

    public void getMouseOver(float partialTicks)
    {
        实体 实体 = this.mc.getRenderViewEntity();

        if (实体 != null && this.mc.宇轩の世界 != null)
        {
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointed实体 = null;
            double d0 = (double)this.mc.玩家控制者.getBlockReachDistance();
            this.mc.objectMouseOver = 实体.rayTrace(d0, partialTicks);
            double d1 = d0;
            Vec3 vec3 = 实体.getPositionEyes(partialTicks);
            boolean flag = false;
            int i = 3;

            if (this.mc.玩家控制者.extendedReach())
            {
                d0 = 6.0D;
                d1 = 6.0D;
            }
            else if (d0 > 3.0D)
            {
                flag = true;
            }

            if (this.mc.objectMouseOver != null)
            {
                d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
            }

            Vec3 vec31 = 实体.getLook(partialTicks);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            this.pointed实体 = null;
            Vec3 vec33 = null;
            float f = 1.0F;
            List<实体> list = this.mc.宇轩の世界.getEntitiesInAABBexcluding(实体, 实体.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<实体>()
            {
                public boolean apply(实体 p_apply_1_)
                {
                    return p_apply_1_.canBeCollidedWith();
                }
            }));
            double d2 = d1;

            for (int j = 0; j < list.size(); ++j)
            {
                实体 实体1 = (实体)list.get(j);
                float f1 = 实体1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = 实体1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3))
                {
                    if (d2 >= 0.0D)
                    {
                        this.pointed实体 = 实体1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                }
                else if (movingobjectposition != null)
                {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D)
                    {
                        boolean flag1 = false;

                        if (Reflector.ForgeEntity_canRiderInteract.exists())
                        {
                            flag1 = Reflector.callBoolean(实体1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }

                        if (!flag1 && 实体1 == 实体.riding实体)
                        {
                            if (d2 == 0.0D)
                            {
                                this.pointed实体 = 实体1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        }
                        else
                        {
                            this.pointed实体 = 实体1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (this.pointed实体 != null && flag && vec3.distanceTo(vec33) > 3.0D)
            {
                this.pointed实体 = null;
                this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing)null, new 阻止位置(vec33));
            }

            if (this.pointed实体 != null && (d2 < d1 || this.mc.objectMouseOver == null))
            {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointed实体, vec33);

                if (this.pointed实体 instanceof 实体LivingBase || this.pointed实体 instanceof 实体ItemFrame)
                {
                    this.mc.pointed实体 = this.pointed实体;
                }
            }

            this.mc.mcProfiler.endSection();
        }
    }

    private void updateFovModifierHand()
    {
        float f = 1.0F;

        if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer)
        {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.getRenderViewEntity();
            f = abstractclientplayer.getFovModifier();
        }

        this.fovModifierHandPrev = this.fovModifierHand;
        this.fovModifierHand += (f - this.fovModifierHand) * 0.5F;

        if (this.fovModifierHand > 1.5F)
        {
            this.fovModifierHand = 1.5F;
        }

        if (this.fovModifierHand < 0.1F)
        {
            this.fovModifierHand = 0.1F;
        }
    }

    private float getFOVModifier(float partialTicks, boolean useFOVSetting)
    {
        if (this.debugView)
        {
            return 90.0F;
        }
        else
        {
            实体 实体 = this.mc.getRenderViewEntity();
            float f = 70.0F;

            if (useFOVSetting)
            {
                f = this.mc.游戏一窝.fovSetting;

                if (Config.isDynamicFov())
                {
                    f *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
                }
            }

            boolean flag = false;

            if (this.mc.currentScreen == null)
            {
                GameSettings gamesettings = this.mc.游戏一窝;
                flag = GameSettings.isKeyDown(this.mc.游戏一窝.ofKeyBindZoom);
            }

            if (flag)
            {
                if (!Config.zoomMode)
                {
                    Config.zoomMode = true;
                    Config.zoomSmoothCamera = this.mc.游戏一窝.smoothCamera;
                    this.mc.游戏一窝.smoothCamera = true;
                    this.mc.renderGlobal.displayListEntitiesDirty = true;
                }

                if (Config.zoomMode)
                {
                    f /= 4.0F;
                }
            }
            else if (Config.zoomMode)
            {
                Config.zoomMode = false;
                this.mc.游戏一窝.smoothCamera = Config.zoomSmoothCamera;
                this.mouseFilterXAxis = new MouseFilter();
                this.mouseFilterYAxis = new MouseFilter();
                this.mc.renderGlobal.displayListEntitiesDirty = true;
            }

            if (实体 instanceof 实体LivingBase && ((实体LivingBase) 实体).getHealth() <= 0.0F)
            {
                float f1 = (float)((实体LivingBase) 实体).deathTime + partialTicks;
                f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
            }

            Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.宇轩の世界, 实体, partialTicks);

            if (block.getMaterial() == Material.water)
            {
                f = f * 60.0F / 70.0F;
            }

            return Reflector.ForgeHooksClient_getFOVModifier.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getFOVModifier, new Object[] {this, 实体, block, Float.valueOf(partialTicks), Float.valueOf(f)}): f;
        }
    }

    private void hurtCameraEffect(float partialTicks)
    {
        if (this.mc.getRenderViewEntity() instanceof 实体LivingBase)
        {
            实体LivingBase entitylivingbase = (实体LivingBase)this.mc.getRenderViewEntity();
            float f = (float)entitylivingbase.hurtTime - partialTicks;

            if (entitylivingbase.getHealth() <= 0.0F)
            {
                float f1 = (float)entitylivingbase.deathTime + partialTicks;
                光照状态经理.辐射(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
            }

            if (f < 0.0F)
            {
                return;
            }

            f = f / (float)entitylivingbase.maxHurtTime;
            f = MathHelper.sin(f * f * f * f * (float)Math.PI);
            float f2 = entitylivingbase.attackedAtYaw;
            光照状态经理.辐射(-f2, 0.0F, 1.0F, 0.0F);
            光照状态经理.辐射(-f * 14.0F, 0.0F, 0.0F, 1.0F);
            光照状态经理.辐射(f2, 0.0F, 1.0F, 0.0F);
        }
    }

    private void setupViewBobbing(float partialTicks)
    {
        if (this.mc.getRenderViewEntity() instanceof 实体Player)
        {
            实体Player entityplayer = (实体Player)this.mc.getRenderViewEntity();
            float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
            float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
            float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
            光照状态经理.理解(MathHelper.sin(f1 * (float)Math.PI) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2), 0.0F);
            光照状态经理.辐射(MathHelper.sin(f1 * (float)Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
            光照状态经理.辐射(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
            光照状态经理.辐射(f3, 1.0F, 0.0F, 0.0F);
        }
    }

    private void orientCamera(float partialTicks)
    {
        实体 实体 = this.mc.getRenderViewEntity();
        float f = 实体.getEyeHeight();
        double d0 = 实体.prevPosX + (实体.X坐标 - 实体.prevPosX) * (double)partialTicks;
        double d1 = 实体.prevPosY + (实体.Y坐标 - 实体.prevPosY) * (double)partialTicks + (double)f;
        double d2 = 实体.prevPosZ + (实体.Z坐标 - 实体.prevPosZ) * (double)partialTicks;

        if (实体 instanceof 实体LivingBase && ((实体LivingBase) 实体).isPlayerSleeping())
        {
            f = (float)((double)f + 1.0D);
            光照状态经理.理解(0.0F, 0.3F, 0.0F);

            if (!this.mc.游戏一窝.debugCamEnable)
            {
                阻止位置 blockpos = new 阻止位置(实体);
                IBlockState iblockstate = this.mc.宇轩の世界.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (Reflector.ForgeHooksClient_orientBedCamera.exists())
                {
                    Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, new Object[] {this.mc.宇轩の世界, blockpos, iblockstate, 实体});
                }
                else if (block == Blocks.bed)
                {
                    int j = ((EnumFacing)iblockstate.getValue(BlockBed.FACING)).getHorizontalIndex();
                    光照状态经理.辐射((float)(j * 90), 0.0F, 1.0F, 0.0F);
                }

                光照状态经理.辐射(实体.prevRotationYaw + (实体.旋转侧滑 - 实体.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
                光照状态经理.辐射(实体.prevRotationPitch + (实体.rotationPitch - 实体.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
            }
        }
        else if (this.mc.游戏一窝.thirdPersonView > 0)
        {
            double d3 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks);

            if (this.mc.游戏一窝.debugCamEnable)
            {
                光照状态经理.理解(0.0F, 0.0F, (float)(-d3));
            }
            else
            {
                float f1 = 实体.旋转侧滑;
                float f2 = 实体.rotationPitch;

                if (this.mc.游戏一窝.thirdPersonView == 2)
                {
                    f2 += 180.0F;
                }

                double d4 = (double)(-MathHelper.sin(f1 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI)) * d3;
                double d5 = (double)(MathHelper.cos(f1 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI)) * d3;
                double d6 = (double)(-MathHelper.sin(f2 / 180.0F * (float)Math.PI)) * d3;

                for (int i = 0; i < 8; ++i)
                {
                    float f3 = (float)((i & 1) * 2 - 1);
                    float f4 = (float)((i >> 1 & 1) * 2 - 1);
                    float f5 = (float)((i >> 2 & 1) * 2 - 1);
                    f3 = f3 * 0.1F;
                    f4 = f4 * 0.1F;
                    f5 = f5 * 0.1F;
                    MovingObjectPosition movingobjectposition = this.mc.宇轩の世界.rayTraceBlocks(new Vec3(d0 + (double)f3, d1 + (double)f4, d2 + (double)f5), new Vec3(d0 - d4 + (double)f3 + (double)f5, d1 - d6 + (double)f4, d2 - d5 + (double)f5));

                    if (movingobjectposition != null)
                    {
                        double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));

                        if (d7 < d3)
                        {
                            d3 = d7;
                        }
                    }
                }

                if (this.mc.游戏一窝.thirdPersonView == 2)
                {
                    光照状态经理.辐射(180.0F, 0.0F, 1.0F, 0.0F);
                }

                光照状态经理.辐射(实体.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(实体.旋转侧滑 - f1, 0.0F, 1.0F, 0.0F);
                光照状态经理.理解(0.0F, 0.0F, (float)(-d3));
                光照状态经理.辐射(f1 - 实体.旋转侧滑, 0.0F, 1.0F, 0.0F);
                光照状态经理.辐射(f2 - 实体.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        }
        else
        {
            光照状态经理.理解(0.0F, 0.0F, -0.1F);
        }

        if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists())
        {
            if (!this.mc.游戏一窝.debugCamEnable)
            {
                float f6 = 实体.prevRotationYaw + (实体.旋转侧滑 - 实体.prevRotationYaw) * partialTicks + 180.0F;
                float f7 = 实体.prevRotationPitch + (实体.rotationPitch - 实体.prevRotationPitch) * partialTicks;
                float f8 = 0.0F;

                if (实体 instanceof 实体Animal)
                {
                    实体Animal entityanimal1 = (实体Animal) 实体;
                    f6 = entityanimal1.prevRotationYawHead + (entityanimal1.rotationYawHead - entityanimal1.prevRotationYawHead) * partialTicks + 180.0F;
                }

                Block block1 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.宇轩の世界, 实体, partialTicks);
                Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_CameraSetup_Constructor, new Object[] {this, 实体, block1, Float.valueOf(partialTicks), Float.valueOf(f6), Float.valueOf(f7), Float.valueOf(f8)});
                Reflector.postForgeBusEvent(object);
                f8 = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_roll, f8);
                f7 = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_pitch, f7);
                f6 = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_yaw, f6);
                光照状态经理.辐射(f8, 0.0F, 0.0F, 1.0F);
                光照状态经理.辐射(f7, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(f6, 0.0F, 1.0F, 0.0F);
            }
        }
        else if (!this.mc.游戏一窝.debugCamEnable)
        {
            光照状态经理.辐射(实体.prevRotationPitch + (实体.rotationPitch - 实体.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);

            if (实体 instanceof 实体Animal)
            {
                实体Animal entityanimal = (实体Animal) 实体;
                光照状态经理.辐射(entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                光照状态经理.辐射(实体.prevRotationYaw + (实体.旋转侧滑 - 实体.prevRotationYaw) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
            }
        }

        光照状态经理.理解(0.0F, -f, 0.0F);
        d0 = 实体.prevPosX + (实体.X坐标 - 实体.prevPosX) * (double)partialTicks;
        d1 = 实体.prevPosY + (实体.Y坐标 - 实体.prevPosY) * (double)partialTicks + (double)f;
        d2 = 实体.prevPosZ + (实体.Z坐标 - 实体.prevPosZ) * (double)partialTicks;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
    }

    public void setupCameraTransform(float partialTicks, int pass)
    {
        this.farPlaneDistance = (float)(this.mc.游戏一窝.renderDistanceChunks * 16);

        if (Config.isFogFancy())
        {
            this.farPlaneDistance *= 0.95F;
        }

        if (Config.isFogFast())
        {
            this.farPlaneDistance *= 0.83F;
        }

        光照状态经理.matrixMode(5889);
        光照状态经理.loadIdentity();
        float f = 0.07F;

        if (this.mc.游戏一窝.anaglyph)
        {
            光照状态经理.理解((float)(-(pass * 2 - 1)) * f, 0.0F, 0.0F);
        }

        this.clipDistance = this.farPlaneDistance * 2.0F;

        if (this.clipDistance < 173.0F)
        {
            this.clipDistance = 173.0F;
        }

        if (this.cameraZoom != 1.0D)
        {
            光照状态经理.理解((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
            光照状态经理.障眼物(this.cameraZoom, this.cameraZoom, 1.0D);
        }

        Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
        光照状态经理.matrixMode(5888);
        光照状态经理.loadIdentity();

        if (this.mc.游戏一窝.anaglyph)
        {
            光照状态经理.理解((float)(pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        this.hurtCameraEffect(partialTicks);

        if (this.mc.游戏一窝.viewBobbing)
        {
            this.setupViewBobbing(partialTicks);
        }

        float f1 = this.mc.宇轩游玩者.prevTimeInPortal + (this.mc.宇轩游玩者.timeInPortal - this.mc.宇轩游玩者.prevTimeInPortal) * partialTicks;

        if (f1 > 0.0F)
        {
            int i = 20;

            if (this.mc.宇轩游玩者.isPotionActive(Potion.confusion))
            {
                i = 7;
            }

            float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
            f2 = f2 * f2;
            光照状态经理.辐射(((float)this.rendererUpdateCount + partialTicks) * (float)i, 0.0F, 1.0F, 1.0F);
            光照状态经理.障眼物(1.0F / f2, 1.0F, 1.0F);
            光照状态经理.辐射(-((float)this.rendererUpdateCount + partialTicks) * (float)i, 0.0F, 1.0F, 1.0F);
        }

        this.orientCamera(partialTicks);

        if (this.debugView)
        {
            switch (this.debugViewDirection)
            {
                case 0:
                    光照状态经理.辐射(90.0F, 0.0F, 1.0F, 0.0F);
                    break;

                case 1:
                    光照状态经理.辐射(180.0F, 0.0F, 1.0F, 0.0F);
                    break;

                case 2:
                    光照状态经理.辐射(-90.0F, 0.0F, 1.0F, 0.0F);
                    break;

                case 3:
                    光照状态经理.辐射(90.0F, 1.0F, 0.0F, 0.0F);
                    break;

                case 4:
                    光照状态经理.辐射(-90.0F, 1.0F, 0.0F, 0.0F);
            }
        }
    }

    private void renderHand(float partialTicks, int xOffset)
    {
        this.renderHand(partialTicks, xOffset, true, true, false);
    }

    public void renderHand(float p_renderHand_1_, int p_renderHand_2_, boolean p_renderHand_3_, boolean p_renderHand_4_, boolean p_renderHand_5_)
    {
        if (!this.debugView)
        {
            光照状态经理.matrixMode(5889);
            光照状态经理.loadIdentity();
            float f = 0.07F;

            if (this.mc.游戏一窝.anaglyph)
            {
                光照状态经理.理解((float)(-(p_renderHand_2_ * 2 - 1)) * f, 0.0F, 0.0F);
            }

            if (Config.isShaders())
            {
                Shaders.applyHandDepth();
            }

            Project.gluPerspective(this.getFOVModifier(p_renderHand_1_, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
            光照状态经理.matrixMode(5888);
            光照状态经理.loadIdentity();

            if (this.mc.游戏一窝.anaglyph)
            {
                光照状态经理.理解((float)(p_renderHand_2_ * 2 - 1) * 0.1F, 0.0F, 0.0F);
            }

            boolean flag = false;

            if (p_renderHand_3_)
            {
                光照状态经理.推黑客帝国();
                this.hurtCameraEffect(p_renderHand_1_);

                if (this.mc.游戏一窝.viewBobbing)
                {
                    this.setupViewBobbing(p_renderHand_1_);
                }

                flag = this.mc.getRenderViewEntity() instanceof 实体LivingBase && ((实体LivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                boolean flag1 = !ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, p_renderHand_1_, p_renderHand_2_);

                if (flag1 && this.mc.游戏一窝.thirdPersonView == 0 && !flag && !this.mc.游戏一窝.hideGUI && !this.mc.玩家控制者.isSpectator())
                {
                    this.enableLightmap();

                    if (Config.isShaders())
                    {
                        ShadersRender.renderItemFP(this.itemRenderer, p_renderHand_1_, p_renderHand_5_);
                    }
                    else
                    {
                        this.itemRenderer.renderItemInFirstPerson(p_renderHand_1_);
                    }

                    this.disableLightmap();
                }

                光照状态经理.流行音乐黑客帝国();
            }

            if (!p_renderHand_4_)
            {
                return;
            }

            this.disableLightmap();

            if (this.mc.游戏一窝.thirdPersonView == 0 && !flag)
            {
                this.itemRenderer.renderOverlays(p_renderHand_1_);
                this.hurtCameraEffect(p_renderHand_1_);
            }

            if (this.mc.游戏一窝.viewBobbing)
            {
                this.setupViewBobbing(p_renderHand_1_);
            }
        }
    }

    public void disableLightmap()
    {
        光照状态经理.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        光照状态经理.禁用手感();
        光照状态经理.setActiveTexture(OpenGlHelper.defaultTexUnit);

        if (Config.isShaders())
        {
            Shaders.disableLightmap();
        }
    }

    public void enableLightmap()
    {
        光照状态经理.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        光照状态经理.matrixMode(5890);
        光照状态经理.loadIdentity();
        float f = 0.00390625F;
        光照状态经理.障眼物(f, f, f);
        光照状态经理.理解(8.0F, 8.0F, 8.0F);
        光照状态经理.matrixMode(5888);
        this.mc.得到手感经理().绑定手感(this.locationLightMap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.启用手感();
        光照状态经理.setActiveTexture(OpenGlHelper.defaultTexUnit);

        if (Config.isShaders())
        {
            Shaders.enableLightmap();
        }
    }

    private void updateTorchFlicker()
    {
        this.torchFlickerDX = (float)((double)this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
        this.torchFlickerDX = (float)((double)this.torchFlickerDX * 0.9D);
        this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0F;
        this.lightmapUpdateNeeded = true;
    }

    private void updateLightmap(float partialTicks)
    {
        if (this.lightmapUpdateNeeded)
        {
            this.mc.mcProfiler.startSection("lightTex");
            World world = this.mc.宇轩の世界;

            if (world != null)
            {
                if (Config.isCustomColors() && CustomColors.updateLightmap(world, this.torchFlickerX, this.lightmapColors, this.mc.宇轩游玩者.isPotionActive(Potion.nightVision), partialTicks))
                {
                    this.lightmapTexture.updateDynamicTexture();
                    this.lightmapUpdateNeeded = false;
                    this.mc.mcProfiler.endSection();
                    return;
                }

                float f = world.getSunBrightness(1.0F);
                float f1 = f * 0.95F + 0.05F;

                for (int i = 0; i < 256; ++i)
                {
                    float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
                    float f3 = world.provider.getLightBrightnessTable()[i % 16] * (this.torchFlickerX * 0.1F + 1.5F);

                    if (world.getLastLightningBolt() > 0)
                    {
                        f2 = world.provider.getLightBrightnessTable()[i / 16];
                    }

                    float f4 = f2 * (f * 0.65F + 0.35F);
                    float f5 = f2 * (f * 0.65F + 0.35F);
                    float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
                    float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
                    float f8 = f4 + f3;
                    float f9 = f5 + f6;
                    float f10 = f2 + f7;
                    f8 = f8 * 0.96F + 0.03F;
                    f9 = f9 * 0.96F + 0.03F;
                    f10 = f10 * 0.96F + 0.03F;

                    if (this.bossColorModifier > 0.0F)
                    {
                        float f11 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
                        f8 = f8 * (1.0F - f11) + f8 * 0.7F * f11;
                        f9 = f9 * (1.0F - f11) + f9 * 0.6F * f11;
                        f10 = f10 * (1.0F - f11) + f10 * 0.6F * f11;
                    }

                    if (world.provider.getDimensionId() == 1)
                    {
                        f8 = 0.22F + f3 * 0.75F;
                        f9 = 0.28F + f6 * 0.75F;
                        f10 = 0.25F + f7 * 0.75F;
                    }

                    if (this.mc.宇轩游玩者.isPotionActive(Potion.nightVision))
                    {
                        float f15 = this.getNightVisionBrightness(this.mc.宇轩游玩者, partialTicks);
                        float f12 = 1.0F / f8;

                        if (f12 > 1.0F / f9)
                        {
                            f12 = 1.0F / f9;
                        }

                        if (f12 > 1.0F / f10)
                        {
                            f12 = 1.0F / f10;
                        }

                        f8 = f8 * (1.0F - f15) + f8 * f12 * f15;
                        f9 = f9 * (1.0F - f15) + f9 * f12 * f15;
                        f10 = f10 * (1.0F - f15) + f10 * f12 * f15;
                    }

                    if (f8 > 1.0F)
                    {
                        f8 = 1.0F;
                    }

                    if (f9 > 1.0F)
                    {
                        f9 = 1.0F;
                    }

                    if (f10 > 1.0F)
                    {
                        f10 = 1.0F;
                    }

                    float f16 = this.mc.游戏一窝.gammaSetting;
                    float f17 = 1.0F - f8;
                    float f13 = 1.0F - f9;
                    float f14 = 1.0F - f10;
                    f17 = 1.0F - f17 * f17 * f17 * f17;
                    f13 = 1.0F - f13 * f13 * f13 * f13;
                    f14 = 1.0F - f14 * f14 * f14 * f14;
                    f8 = f8 * (1.0F - f16) + f17 * f16;
                    f9 = f9 * (1.0F - f16) + f13 * f16;
                    f10 = f10 * (1.0F - f16) + f14 * f16;
                    f8 = f8 * 0.96F + 0.03F;
                    f9 = f9 * 0.96F + 0.03F;
                    f10 = f10 * 0.96F + 0.03F;

                    if (f8 > 1.0F)
                    {
                        f8 = 1.0F;
                    }

                    if (f9 > 1.0F)
                    {
                        f9 = 1.0F;
                    }

                    if (f10 > 1.0F)
                    {
                        f10 = 1.0F;
                    }

                    if (f8 < 0.0F)
                    {
                        f8 = 0.0F;
                    }

                    if (f9 < 0.0F)
                    {
                        f9 = 0.0F;
                    }

                    if (f10 < 0.0F)
                    {
                        f10 = 0.0F;
                    }

                    int j = 255;
                    int k = (int)(f8 * 255.0F);
                    int l = (int)(f9 * 255.0F);
                    int i1 = (int)(f10 * 255.0F);
                    this.lightmapColors[i] = j << 24 | k << 16 | l << 8 | i1;
                }

                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.mcProfiler.endSection();
            }
        }
    }

    public float getNightVisionBrightness(实体LivingBase entitylivingbaseIn, float partialTicks)
    {
        int i = entitylivingbaseIn.getActivePotionEffect(Potion.nightVision).getDuration();
        return i > 200 ? 1.0F : 0.7F + MathHelper.sin(((float)i - partialTicks) * (float)Math.PI * 0.2F) * 0.3F;
    }

    public void updateCameraAndRender(float partialTicks, long nanoTime)
    {
        Config.renderPartialTicks = partialTicks;
        this.frameInit();
        boolean flag = Display.isActive();

        if (!flag && this.mc.游戏一窝.pauseOnLostFocus && (!this.mc.游戏一窝.touchscreen || !Mouse.isButtonDown(1)))
        {
            if (我的手艺.getSystemTime() - this.prevFrameTime > 500L)
            {
                this.mc.displayInGameMenu();
            }
        }
        else
        {
            this.prevFrameTime = 我的手艺.getSystemTime();
        }

        this.mc.mcProfiler.startSection("mouse");

        if (flag && 我的手艺.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow())
        {
            Mouse.setGrabbed(false);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
            Mouse.setGrabbed(true);
        }

        if (this.mc.inGameHasFocus && flag)
        {
            this.mc.mouseHelper.mouseXYChange();
            float f = this.mc.游戏一窝.mouseSensitivity * 0.6F + 0.2F;
            float f1 = f * f * f * 8.0F;
            float f2 = (float)this.mc.mouseHelper.deltaX * f1;
            float f3 = (float)this.mc.mouseHelper.deltaY * f1;
            int i = 1;

            if (this.mc.游戏一窝.invertMouse)
            {
                i = -1;
            }

            if (this.mc.游戏一窝.smoothCamera)
            {
                this.smoothCamYaw += f2;
                this.smoothCamPitch += f3;
                float f4 = partialTicks - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = partialTicks;
                f2 = this.smoothCamFilterX * f4;
                f3 = this.smoothCamFilterY * f4;
                this.mc.宇轩游玩者.setAngles(f2, f3 * (float)i);
            }
            else
            {
                this.smoothCamYaw = 0.0F;
                this.smoothCamPitch = 0.0F;
                this.mc.宇轩游玩者.setAngles(f2, f3 * (float)i);
            }
        }

        this.mc.mcProfiler.endSection();

        if (!this.mc.skipRenderWorld)
        {
            anaglyphEnable = this.mc.游戏一窝.anaglyph;
            final 比例解析 scaledresolution = new 比例解析(this.mc);
            int i1 = scaledresolution.getScaledWidth();
            int j1 = scaledresolution.得到高度();
            final int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
            final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
            int i2 = this.mc.游戏一窝.limitFramerate;

            if (this.mc.宇轩の世界 != null)
            {
                this.mc.mcProfiler.startSection("level");
                int j = Math.min(我的手艺.getDebugFPS(), i2);
                j = Math.max(j, 60);
                long k = System.nanoTime() - nanoTime;
                long l = Math.max((long)(1000000000 / j / 4) - k, 0L);
                this.renderWorld(partialTicks, System.nanoTime() + l);

                if (OpenGlHelper.shadersSupported)
                {
                    this.mc.renderGlobal.renderEntityOutlineFramebuffer();

                    if (this.theShaderGroup != null && this.useShader)
                    {
                        光照状态经理.matrixMode(5890);
                        光照状态经理.推黑客帝国();
                        光照状态经理.loadIdentity();
                        this.theShaderGroup.loadShaderGroup(partialTicks);
                        光照状态经理.流行音乐黑客帝国();
                    }

                    this.mc.getFramebuffer().bindFramebuffer(true);
                }

                this.renderEndNanoTime = System.nanoTime();
                this.mc.mcProfiler.endStartSection("gui");

                if (!this.mc.游戏一窝.hideGUI || this.mc.currentScreen != null)
                {
                    光照状态经理.alphaFunc(516, 0.1F);
                    this.mc.ingameGUI.renderGameOverlay(partialTicks);

                    if (this.mc.游戏一窝.ofShowFps && !this.mc.游戏一窝.showDebugInfo)
                    {
                        Config.drawFps();
                    }

                    if (this.mc.游戏一窝.showDebugInfo)
                    {
                        Lagometer.showLagometer(scaledresolution);
                    }
                }

                this.mc.mcProfiler.endSection();
            }
            else
            {
                光照状态经理.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                光照状态经理.matrixMode(5889);
                光照状态经理.loadIdentity();
                光照状态经理.matrixMode(5888);
                光照状态经理.loadIdentity();
                this.setupOverlayRendering();
                this.renderEndNanoTime = System.nanoTime();
                TileEntityRendererDispatcher.instance.renderEngine = this.mc.得到手感经理();
                TileEntityRendererDispatcher.instance.fontRenderer = this.mc.字体渲染员;
            }

            if (this.mc.currentScreen != null)
            {
                光照状态经理.clear(256);

                try
                {
                    if (Reflector.ForgeHooksClient_drawScreen.exists())
                    {
                        Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, new Object[] {this.mc.currentScreen, Integer.valueOf(k1), Integer.valueOf(l1), Float.valueOf(partialTicks)});
                    }
                    else
                    {
                        this.mc.currentScreen.drawScreen(k1, l1, partialTicks);
                    }
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                    crashreportcategory.addCrashSectionCallable("Screen name", new Callable<String>()
                    {
                        public String call() throws Exception
                        {
                            return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    crashreportcategory.addCrashSectionCallable("Mouse location", new Callable<String>()
                    {
                        public String call() throws Exception
                        {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[] {Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY())});
                        }
                    });
                    crashreportcategory.addCrashSectionCallable("Screen size", new Callable<String>()
                    {
                        public String call() throws Exception
                        {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] {Integer.valueOf(scaledresolution.getScaledWidth()), Integer.valueOf(scaledresolution.得到高度()), Integer.valueOf(EntityRenderer.this.mc.displayWidth), Integer.valueOf(EntityRenderer.this.mc.displayHeight), Integer.valueOf(scaledresolution.getScaleFactor())});
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            }
        }

        this.frameFinish();
        this.waitForServerThread();
        MemoryMonitor.update();
        Lagometer.updateLagometer();

        if (this.mc.游戏一窝.ofProfiler)
        {
            this.mc.游戏一窝.showDebugProfilerChart = true;
        }
    }

    public void renderStreamIndicator(float partialTicks)
    {
        this.setupOverlayRendering();
        this.mc.ingameGUI.renderStreamIndicator(new 比例解析(this.mc));
    }

    private boolean isDrawBlockOutline()
    {
        if (!this.drawBlockOutline)
        {
            return false;
        }
        else
        {
            实体 实体 = this.mc.getRenderViewEntity();
            boolean flag = 实体 instanceof 实体Player && !this.mc.游戏一窝.hideGUI;

            if (flag && !((实体Player) 实体).capabilities.allowEdit)
            {
                ItemStack itemstack = ((实体Player) 实体).getCurrentEquippedItem();

                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    阻止位置 blockpos = this.mc.objectMouseOver.getBlockPos();
                    IBlockState iblockstate = this.mc.宇轩の世界.getBlockState(blockpos);
                    Block block = iblockstate.getBlock();

                    if (this.mc.玩家控制者.getCurrentGameType() == WorldSettings.GameType.SPECTATOR)
                    {
                        flag = ReflectorForge.blockHasTileEntity(iblockstate) && this.mc.宇轩の世界.getTileEntity(blockpos) instanceof IInventory;
                    }
                    else
                    {
                        flag = itemstack != null && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block));
                    }
                }
            }

            return flag;
        }
    }

    private void renderWorldDirections(float partialTicks)
    {
        if (this.mc.游戏一窝.showDebugInfo && !this.mc.游戏一窝.hideGUI && !this.mc.宇轩游玩者.hasReducedDebug() && !this.mc.游戏一窝.reducedDebugInfo)
        {
            实体 实体 = this.mc.getRenderViewEntity();
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glLineWidth(1.0F);
            光照状态经理.禁用手感();
            光照状态经理.depthMask(false);
            光照状态经理.推黑客帝国();
            光照状态经理.matrixMode(5888);
            光照状态经理.loadIdentity();
            this.orientCamera(partialTicks);
            光照状态经理.理解(0.0F, 实体.getEyeHeight(), 0.0F);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.005D, 1.0E-4D, 1.0E-4D), 255, 0, 0, 255);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 1.0E-4D, 0.005D), 0, 0, 255, 255);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 0.0033D, 1.0E-4D), 0, 255, 0, 255);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.depthMask(true);
            光照状态经理.启用手感();
            光照状态经理.禁用混合品();
        }
    }

    public void renderWorld(float partialTicks, long finishTimeNano)
    {
        this.updateLightmap(partialTicks);

        if (this.mc.getRenderViewEntity() == null)
        {
            this.mc.setRenderViewEntity(this.mc.宇轩游玩者);
        }

        this.getMouseOver(partialTicks);

        if (Config.isShaders())
        {
            Shaders.beginRender(this.mc, partialTicks, finishTimeNano);
        }

        光照状态经理.启用纵深();
        光照状态经理.启用希腊字母表的第1个字母();
        光照状态经理.alphaFunc(516, 0.1F);
        this.mc.mcProfiler.startSection("center");

        if (this.mc.游戏一窝.anaglyph)
        {
            anaglyphField = 0;
            光照状态经理.colorMask(false, true, true, false);
            this.renderWorldPass(0, partialTicks, finishTimeNano);
            anaglyphField = 1;
            光照状态经理.colorMask(true, false, false, false);
            this.renderWorldPass(1, partialTicks, finishTimeNano);
            光照状态经理.colorMask(true, true, true, false);
        }
        else
        {
            this.renderWorldPass(2, partialTicks, finishTimeNano);
        }

        this.mc.mcProfiler.endSection();
    }

    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano)
    {
        boolean flag = Config.isShaders();

        if (flag)
        {
            Shaders.beginRenderPass(pass, partialTicks, finishTimeNano);
        }

        RenderGlobal renderglobal = this.mc.renderGlobal;
        EffectRenderer effectrenderer = this.mc.effectRenderer;
        boolean flag1 = this.isDrawBlockOutline();
        光照状态经理.enableCull();
        this.mc.mcProfiler.endStartSection("clear");

        if (flag)
        {
            Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        }
        else
        {
            光照状态经理.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        }

        this.updateFogColor(partialTicks);
        光照状态经理.clear(16640);

        if (flag)
        {
            Shaders.clearRenderBuffer();
        }

        this.mc.mcProfiler.endStartSection("camera");
        this.setupCameraTransform(partialTicks, pass);

        if (flag)
        {
            Shaders.setCamera(partialTicks);
        }

        ActiveRenderInfo.updateRenderInfo(this.mc.宇轩游玩者, this.mc.游戏一窝.thirdPersonView == 2);
        this.mc.mcProfiler.endStartSection("frustum");
        ClippingHelper clippinghelper = ClippingHelperImpl.getInstance();
        this.mc.mcProfiler.endStartSection("culling");
        clippinghelper.disabled = Config.isShaders() && !Shaders.isFrustumCulling();
        ICamera icamera = new Frustum(clippinghelper);
        实体 实体 = this.mc.getRenderViewEntity();
        double d0 = 实体.lastTickPosX + (实体.X坐标 - 实体.lastTickPosX) * (double)partialTicks;
        double d1 = 实体.lastTickPosY + (实体.Y坐标 - 实体.lastTickPosY) * (double)partialTicks;
        double d2 = 实体.lastTickPosZ + (实体.Z坐标 - 实体.lastTickPosZ) * (double)partialTicks;

        if (flag)
        {
            ShadersRender.setFrustrumPosition(icamera, d0, d1, d2);
        }
        else
        {
            icamera.setPosition(d0, d1, d2);
        }

        if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass)
        {
            this.setupFog(-1, partialTicks);
            this.mc.mcProfiler.endStartSection("sky");
            光照状态经理.matrixMode(5889);
            光照状态经理.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
            光照状态经理.matrixMode(5888);

            if (flag)
            {
                Shaders.beginSky();
            }

            renderglobal.renderSky(partialTicks, pass);

            if (flag)
            {
                Shaders.endSky();
            }

            光照状态经理.matrixMode(5889);
            光照状态经理.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
            光照状态经理.matrixMode(5888);
        }
        else
        {
            光照状态经理.禁用混合品();
        }

        this.setupFog(0, partialTicks);
        光照状态经理.shadeModel(7425);

        if (实体.Y坐标 + (double) 实体.getEyeHeight() < 128.0D + (double)(this.mc.游戏一窝.ofCloudsHeight * 128.0F))
        {
            this.renderCloudsCheck(renderglobal, partialTicks, pass);
        }

        this.mc.mcProfiler.endStartSection("prepareterrain");
        this.setupFog(0, partialTicks);
        this.mc.得到手感经理().绑定手感(TextureMap.locationBlocksTexture);
        RenderHelper.disableStandardItemLighting();
        this.mc.mcProfiler.endStartSection("terrain_setup");
        this.checkLoadVisibleChunks(实体, partialTicks, icamera, this.mc.宇轩游玩者.isSpectator());

        if (flag)
        {
            ShadersRender.setupTerrain(renderglobal, 实体, (double)partialTicks, icamera, this.frameCount++, this.mc.宇轩游玩者.isSpectator());
        }
        else
        {
            renderglobal.setupTerrain(实体, (double)partialTicks, icamera, this.frameCount++, this.mc.宇轩游玩者.isSpectator());
        }

        if (pass == 0 || pass == 2)
        {
            this.mc.mcProfiler.endStartSection("updatechunks");
            Lagometer.timerChunkUpload.start();
            this.mc.renderGlobal.updateChunks(finishTimeNano);
            Lagometer.timerChunkUpload.end();
        }

        this.mc.mcProfiler.endStartSection("terrain");
        Lagometer.timerTerrain.start();

        if (this.mc.游戏一窝.ofSmoothFps && pass > 0)
        {
            this.mc.mcProfiler.endStartSection("finish");
            GL11.glFinish();
            this.mc.mcProfiler.endStartSection("terrain");
        }

        光照状态经理.matrixMode(5888);
        光照状态经理.推黑客帝国();
        光照状态经理.禁用希腊字母表的第1个字母();

        if (flag)
        {
            ShadersRender.beginTerrainSolid();
        }

        renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double)partialTicks, pass, 实体);
        光照状态经理.启用希腊字母表的第1个字母();

        if (flag)
        {
            ShadersRender.beginTerrainCutoutMipped();
        }

        this.mc.得到手感经理().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, this.mc.游戏一窝.mipmapLevels > 0);
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double)partialTicks, pass, 实体);
        this.mc.得到手感经理().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        this.mc.得到手感经理().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);

        if (flag)
        {
            ShadersRender.beginTerrainCutout();
        }

        renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double)partialTicks, pass, 实体);
        this.mc.得到手感经理().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();

        if (flag)
        {
            ShadersRender.endTerrain();
        }

        Lagometer.timerTerrain.end();
        光照状态经理.shadeModel(7424);
        光照状态经理.alphaFunc(516, 0.1F);

        if (!this.debugView)
        {
            光照状态经理.matrixMode(5888);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.推黑客帝国();
            RenderHelper.enableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("entities");

            if (Reflector.ForgeHooksClient_setRenderPass.exists())
            {
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] {Integer.valueOf(0)});
            }

            renderglobal.renderEntities(实体, icamera, partialTicks);

            if (Reflector.ForgeHooksClient_setRenderPass.exists())
            {
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] {Integer.valueOf(-1)});
            }

            RenderHelper.disableStandardItemLighting();
            this.disableLightmap();
            光照状态经理.matrixMode(5888);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.推黑客帝国();

            if (this.mc.objectMouseOver != null && 实体.isInsideOfMaterial(Material.water) && flag1)
            {
                实体Player entityplayer = (实体Player) 实体;
                光照状态经理.禁用希腊字母表的第1个字母();
                this.mc.mcProfiler.endStartSection("outline");
                renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
                光照状态经理.启用希腊字母表的第1个字母();
            }
        }

        光照状态经理.matrixMode(5888);
        光照状态经理.流行音乐黑客帝国();

        if (flag1 && this.mc.objectMouseOver != null && !实体.isInsideOfMaterial(Material.water))
        {
            实体Player entityplayer1 = (实体Player) 实体;
            光照状态经理.禁用希腊字母表的第1个字母();
            this.mc.mcProfiler.endStartSection("outline");

            if ((!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[] {renderglobal, entityplayer1, this.mc.objectMouseOver, Integer.valueOf(0), entityplayer1.getHeldItem(), Float.valueOf(partialTicks)})) && !this.mc.游戏一窝.hideGUI)
            {
                renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, partialTicks);
            }
            光照状态经理.启用希腊字母表的第1个字母();
        }

        if (!renderglobal.damagedBlocks.isEmpty())
        {
            this.mc.mcProfiler.endStartSection("destroyProgress");
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 1, 1, 0);
            this.mc.得到手感经理().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
            renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), 实体, partialTicks);
            this.mc.得到手感经理().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
            光照状态经理.禁用混合品();
        }

        光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        光照状态经理.禁用混合品();

        if (!this.debugView)
        {
            this.enableLightmap();
            this.mc.mcProfiler.endStartSection("litParticles");

            if (flag)
            {
                Shaders.beginLitParticles();
            }

            effectrenderer.renderLitParticles(实体, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, partialTicks);
            this.mc.mcProfiler.endStartSection("particles");

            if (flag)
            {
                Shaders.beginParticles();
            }

            effectrenderer.renderParticles(实体, partialTicks);

            if (flag)
            {
                Shaders.endParticles();
            }

            this.disableLightmap();
        }

        光照状态经理.depthMask(false);

        if (Config.isShaders())
        {
            光照状态经理.depthMask(Shaders.isRainDepth());
        }

        光照状态经理.enableCull();
        this.mc.mcProfiler.endStartSection("weather");

        if (flag)
        {
            Shaders.beginWeather();
        }

        this.renderRainSnow(partialTicks);

        if (flag)
        {
            Shaders.endWeather();
        }

        光照状态经理.depthMask(true);
        renderglobal.renderWorldBorder(实体, partialTicks);

        if (flag)
        {
            ShadersRender.renderHand0(this, partialTicks, pass);
            Shaders.preWater();
        }

        光照状态经理.禁用混合品();
        光照状态经理.enableCull();
        光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        光照状态经理.alphaFunc(516, 0.1F);
        this.setupFog(0, partialTicks);
        光照状态经理.启用混合品();
        光照状态经理.depthMask(false);
        this.mc.得到手感经理().绑定手感(TextureMap.locationBlocksTexture);
        光照状态经理.shadeModel(7425);
        this.mc.mcProfiler.endStartSection("translucent");

        if (flag)
        {
            Shaders.beginWater();
        }

        renderglobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double)partialTicks, pass, 实体);

        if (flag)
        {
            Shaders.endWater();
        }

        if (Reflector.ForgeHooksClient_setRenderPass.exists() && !this.debugView)
        {
            RenderHelper.enableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("entities");
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] {Integer.valueOf(1)});
            this.mc.renderGlobal.renderEntities(实体, icamera, partialTicks);
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] {Integer.valueOf(-1)});
            RenderHelper.disableStandardItemLighting();
        }

        光照状态经理.shadeModel(7424);
        光照状态经理.depthMask(true);
        光照状态经理.enableCull();
        光照状态经理.禁用混合品();
        光照状态经理.disableFog();

        if (实体.Y坐标 + (double) 实体.getEyeHeight() >= 128.0D + (double)(this.mc.游戏一窝.ofCloudsHeight * 128.0F))
        {
            this.mc.mcProfiler.endStartSection("aboveClouds");
            this.renderCloudsCheck(renderglobal, partialTicks, pass);
        }

        if (Reflector.ForgeHooksClient_dispatchRenderLast.exists())
        {
            this.mc.mcProfiler.endStartSection("forge_render_last");
            Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, new Object[] {renderglobal, Float.valueOf(partialTicks)});
        }

        this.mc.mcProfiler.endStartSection("hand");

        if (this.renderHand && !Shaders.isShadowPass)
        {
            if (flag)
            {
                ShadersRender.renderHand1(this, partialTicks, pass);
                Shaders.renderCompositeFinal();
            }

            光照状态经理.clear(256);

            if (flag)
            {
                ShadersRender.renderFPOverlay(this, partialTicks, pass);
            }
            else
            {
                this.renderHand(partialTicks, pass);
            }

            this.renderWorldDirections(partialTicks);
        }

        if (flag)
        {
            Shaders.endRender();
        }
    }

    private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass)
    {
        if (this.mc.游戏一窝.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(this.mc.游戏一窝))
        {
            this.mc.mcProfiler.endStartSection("clouds");
            光照状态经理.matrixMode(5889);
            光照状态经理.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance * 4.0F);
            光照状态经理.matrixMode(5888);
            光照状态经理.推黑客帝国();
            this.setupFog(0, partialTicks);
            renderGlobalIn.renderClouds(partialTicks, pass);
            光照状态经理.disableFog();
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.matrixMode(5889);
            光照状态经理.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
            光照状态经理.matrixMode(5888);
        }
    }

    private void addRainParticles()
    {
        float f = this.mc.宇轩の世界.getRainStrength(1.0F);

        if (!Config.isRainFancy())
        {
            f /= 2.0F;
        }

        if (f != 0.0F && Config.isRainSplash())
        {
            this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
            实体 实体 = this.mc.getRenderViewEntity();
            World world = this.mc.宇轩の世界;
            阻止位置 blockpos = new 阻止位置(实体);
            int i = 10;
            double d0 = 0.0D;
            double d1 = 0.0D;
            double d2 = 0.0D;
            int j = 0;
            int k = (int)(100.0F * f * f);

            if (this.mc.游戏一窝.particleSetting == 1)
            {
                k >>= 1;
            }
            else if (this.mc.游戏一窝.particleSetting == 2)
            {
                k = 0;
            }

            for (int l = 0; l < k; ++l)
            {
                阻止位置 blockpos1 = world.getPrecipitationHeight(blockpos.add(this.random.nextInt(i) - this.random.nextInt(i), 0, this.random.nextInt(i) - this.random.nextInt(i)));
                BiomeGenBase biomegenbase = world.getBiomeGenForCoords(blockpos1);
                阻止位置 blockpos2 = blockpos1.down();
                Block block = world.getBlockState(blockpos2).getBlock();

                if (blockpos1.getY() <= blockpos.getY() + i && blockpos1.getY() >= blockpos.getY() - i && biomegenbase.canRain() && biomegenbase.getFloatTemperature(blockpos1) >= 0.15F)
                {
                    double d3 = this.random.nextDouble();
                    double d4 = this.random.nextDouble();

                    if (block.getMaterial() == Material.lava)
                    {
                        this.mc.宇轩の世界.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)blockpos1.getX() + d3, (double)((float)blockpos1.getY() + 0.1F) - block.getBlockBoundsMinY(), (double)blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                    else if (block.getMaterial() != Material.air)
                    {
                        block.setBlockBoundsBasedOnState(world, blockpos2);
                        ++j;

                        if (this.random.nextInt(j) == 0)
                        {
                            d0 = (double)blockpos2.getX() + d3;
                            d1 = (double)((float)blockpos2.getY() + 0.1F) + block.getBlockBoundsMaxY() - 1.0D;
                            d2 = (double)blockpos2.getZ() + d4;
                        }

                        this.mc.宇轩の世界.spawnParticle(EnumParticleTypes.WATER_DROP, (double)blockpos2.getX() + d3, (double)((float)blockpos2.getY() + 0.1F) + block.getBlockBoundsMaxY(), (double)blockpos2.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                }
            }

            if (j > 0 && this.random.nextInt(3) < this.rainSoundCounter++)
            {
                this.rainSoundCounter = 0;

                if (d1 > (double)(blockpos.getY() + 1) && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor_float((float)blockpos.getY()))
                {
                    this.mc.宇轩の世界.playSound(d0, d1, d2, "ambient.weather.rain", 0.1F, 0.5F, false);
                }
                else
                {
                    this.mc.宇轩の世界.playSound(d0, d1, d2, "ambient.weather.rain", 0.2F, 1.0F, false);
                }
            }
        }
    }

    protected void renderRainSnow(float partialTicks)
    {
        if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists())
        {
            WorldProvider worldprovider = this.mc.宇轩の世界.provider;
            Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0]);

            if (object != null)
            {
                Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] {Float.valueOf(partialTicks), this.mc.宇轩の世界, this.mc});
                return;
            }
        }

        float f5 = this.mc.宇轩の世界.getRainStrength(partialTicks);

        if (f5 > 0.0F)
        {
            if (Config.isRainOff())
            {
                return;
            }

            this.enableLightmap();
            实体 实体 = this.mc.getRenderViewEntity();
            World world = this.mc.宇轩の世界;
            int i = MathHelper.floor_double(实体.X坐标);
            int j = MathHelper.floor_double(实体.Y坐标);
            int k = MathHelper.floor_double(实体.Z坐标);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            光照状态经理.disableCull();
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            光照状态经理.alphaFunc(516, 0.1F);
            double d0 = 实体.lastTickPosX + (实体.X坐标 - 实体.lastTickPosX) * (double)partialTicks;
            double d1 = 实体.lastTickPosY + (实体.Y坐标 - 实体.lastTickPosY) * (double)partialTicks;
            double d2 = 实体.lastTickPosZ + (实体.Z坐标 - 实体.lastTickPosZ) * (double)partialTicks;
            int l = MathHelper.floor_double(d1);
            int i1 = 5;

            if (Config.isRainFancy())
            {
                i1 = 10;
            }

            int j1 = -1;
            float f = (float)this.rendererUpdateCount + partialTicks;
            worldrenderer.setTranslation(-d0, -d1, -d2);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            阻止位置.Mutable阻止位置 blockpos$mutableblockpos = new 阻止位置.Mutable阻止位置();

            for (int k1 = k - i1; k1 <= k + i1; ++k1)
            {
                for (int l1 = i - i1; l1 <= i + i1; ++l1)
                {
                    int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                    double d3 = (double)this.rainXCoords[i2] * 0.5D;
                    double d4 = (double)this.rainYCoords[i2] * 0.5D;
                    blockpos$mutableblockpos.set(l1, 0, k1);
                    BiomeGenBase biomegenbase = world.getBiomeGenForCoords(blockpos$mutableblockpos);

                    if (biomegenbase.canRain() || biomegenbase.getEnableSnow())
                    {
                        int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                        int k2 = j - i1;
                        int l2 = j + i1;

                        if (k2 < j2)
                        {
                            k2 = j2;
                        }

                        if (l2 < j2)
                        {
                            l2 = j2;
                        }

                        int i3 = j2;

                        if (j2 < l)
                        {
                            i3 = l;
                        }

                        if (k2 != l2)
                        {
                            this.random.setSeed((long)(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
                            blockpos$mutableblockpos.set(l1, k2, k1);
                            float f1 = biomegenbase.getFloatTemperature(blockpos$mutableblockpos);

                            if (world.getWorldChunkManager().getTemperatureAtHeight(f1, j2) >= 0.15F)
                            {
                                if (j1 != 0)
                                {
                                    if (j1 >= 0)
                                    {
                                        tessellator.draw();
                                    }

                                    j1 = 0;
                                    this.mc.得到手感经理().绑定手感(locationRainPng);
                                    worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                double d5 = ((double)(this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + (double)partialTicks) / 32.0D * (3.0D + this.random.nextDouble());
                                double d6 = (double)((float)l1 + 0.5F) - 实体.X坐标;
                                double d7 = (double)((float)k1 + 0.5F) - 实体.Z坐标;
                                float f2 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / (float)i1;
                                float f3 = ((1.0F - f2 * f2) * 0.5F + 0.5F) * f5;
                                blockpos$mutableblockpos.set(l1, i3, k1);
                                int j3 = world.getCombinedLight(blockpos$mutableblockpos, 0);
                                int k3 = j3 >> 16 & 65535;
                                int l3 = j3 & 65535;
                                worldrenderer.pos((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
                                worldrenderer.pos((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
                                worldrenderer.pos((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
                                worldrenderer.pos((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
                            }
                            else
                            {
                                if (j1 != 1)
                                {
                                    if (j1 >= 0)
                                    {
                                        tessellator.draw();
                                    }

                                    j1 = 1;
                                    this.mc.得到手感经理().绑定手感(locationSnowPng);
                                    worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                double d8 = (double)(((float)(this.rendererUpdateCount & 511) + partialTicks) / 512.0F);
                                double d9 = this.random.nextDouble() + (double)f * 0.01D * (double)((float)this.random.nextGaussian());
                                double d10 = this.random.nextDouble() + (double)(f * (float)this.random.nextGaussian()) * 0.001D;
                                double d11 = (double)((float)l1 + 0.5F) - 实体.X坐标;
                                double d12 = (double)((float)k1 + 0.5F) - 实体.Z坐标;
                                float f6 = MathHelper.sqrt_double(d11 * d11 + d12 * d12) / (float)i1;
                                float f4 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f5;
                                blockpos$mutableblockpos.set(l1, i3, k1);
                                int i4 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
                                int j4 = i4 >> 16 & 65535;
                                int k4 = i4 & 65535;
                                worldrenderer.pos((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).tex(0.0D + d9, (double)k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(j4, k4).endVertex();
                                worldrenderer.pos((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).tex(1.0D + d9, (double)k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(j4, k4).endVertex();
                                worldrenderer.pos((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).tex(1.0D + d9, (double)l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(j4, k4).endVertex();
                                worldrenderer.pos((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).tex(0.0D + d9, (double)l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(j4, k4).endVertex();
                            }
                        }
                    }
                }
            }

            if (j1 >= 0)
            {
                tessellator.draw();
            }

            worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
            光照状态经理.enableCull();
            光照状态经理.禁用混合品();
            光照状态经理.alphaFunc(516, 0.1F);
            this.disableLightmap();
        }
    }

    public void setupOverlayRendering()
    {
        比例解析 scaledresolution = new 比例解析(this.mc);
        光照状态经理.clear(256);
        光照状态经理.matrixMode(5889);
        光照状态经理.loadIdentity();
        光照状态经理.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        光照状态经理.matrixMode(5888);
        光照状态经理.loadIdentity();
        光照状态经理.理解(0.0F, 0.0F, -2000.0F);
    }

    private void updateFogColor(float partialTicks)
    {
        World world = this.mc.宇轩の世界;
        实体 实体 = this.mc.getRenderViewEntity();
        float f = 0.25F + 0.75F * (float)this.mc.游戏一窝.renderDistanceChunks / 32.0F;
        f = 1.0F - (float)Math.pow((double)f, 0.25D);
        Vec3 vec3 = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
        vec3 = CustomColors.getWorldSkyColor(vec3, world, this.mc.getRenderViewEntity(), partialTicks);
        float f1 = (float)vec3.xCoord;
        float f2 = (float)vec3.yCoord;
        float f3 = (float)vec3.zCoord;
        Vec3 vec31 = world.getFogColor(partialTicks);
        vec31 = CustomColors.getWorldFogColor(vec31, world, this.mc.getRenderViewEntity(), partialTicks);
        this.fogColorRed = (float)vec31.xCoord;
        this.fogColorGreen = (float)vec31.yCoord;
        this.fogColorBlue = (float)vec31.zCoord;

        if (this.mc.游戏一窝.renderDistanceChunks >= 4)
        {
            double d0 = -1.0D;
            Vec3 vec32 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0F ? new Vec3(d0, 0.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
            float f5 = (float) 实体.getLook(partialTicks).dotProduct(vec32);

            if (f5 < 0.0F)
            {
                f5 = 0.0F;
            }

            if (f5 > 0.0F)
            {
                float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

                if (afloat != null)
                {
                    f5 = f5 * afloat[3];
                    this.fogColorRed = this.fogColorRed * (1.0F - f5) + afloat[0] * f5;
                    this.fogColorGreen = this.fogColorGreen * (1.0F - f5) + afloat[1] * f5;
                    this.fogColorBlue = this.fogColorBlue * (1.0F - f5) + afloat[2] * f5;
                }
            }
        }

        this.fogColorRed += (f1 - this.fogColorRed) * f;
        this.fogColorGreen += (f2 - this.fogColorGreen) * f;
        this.fogColorBlue += (f3 - this.fogColorBlue) * f;
        float f8 = world.getRainStrength(partialTicks);

        if (f8 > 0.0F)
        {
            float f4 = 1.0F - f8 * 0.5F;
            float f10 = 1.0F - f8 * 0.4F;
            this.fogColorRed *= f4;
            this.fogColorGreen *= f4;
            this.fogColorBlue *= f10;
        }

        float f9 = world.getThunderStrength(partialTicks);

        if (f9 > 0.0F)
        {
            float f11 = 1.0F - f9 * 0.5F;
            this.fogColorRed *= f11;
            this.fogColorGreen *= f11;
            this.fogColorBlue *= f11;
        }

        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.宇轩の世界, 实体, partialTicks);

        if (this.cloudFog)
        {
            Vec3 vec33 = world.getCloudColour(partialTicks);
            this.fogColorRed = (float)vec33.xCoord;
            this.fogColorGreen = (float)vec33.yCoord;
            this.fogColorBlue = (float)vec33.zCoord;
        }
        else if (block.getMaterial() == Material.water)
        {
            float f12 = (float)EnchantmentHelper.getRespiration(实体) * 0.2F;
            f12 = Config.limit(f12, 0.0F, 0.6F);

            if (实体 instanceof 实体LivingBase && ((实体LivingBase) 实体).isPotionActive(Potion.waterBreathing))
            {
                f12 = f12 * 0.3F + 0.6F;
            }

            this.fogColorRed = 0.02F + f12;
            this.fogColorGreen = 0.02F + f12;
            this.fogColorBlue = 0.2F + f12;
            Vec3 vec35 = CustomColors.getUnderwaterColor(this.mc.宇轩の世界, this.mc.getRenderViewEntity().X坐标, this.mc.getRenderViewEntity().Y坐标 + 1.0D, this.mc.getRenderViewEntity().Z坐标);

            if (vec35 != null)
            {
                this.fogColorRed = (float)vec35.xCoord;
                this.fogColorGreen = (float)vec35.yCoord;
                this.fogColorBlue = (float)vec35.zCoord;
            }
        }
        else if (block.getMaterial() == Material.lava)
        {
            this.fogColorRed = 0.6F;
            this.fogColorGreen = 0.1F;
            this.fogColorBlue = 0.0F;
            Vec3 vec34 = CustomColors.getUnderlavaColor(this.mc.宇轩の世界, this.mc.getRenderViewEntity().X坐标, this.mc.getRenderViewEntity().Y坐标 + 1.0D, this.mc.getRenderViewEntity().Z坐标);

            if (vec34 != null)
            {
                this.fogColorRed = (float)vec34.xCoord;
                this.fogColorGreen = (float)vec34.yCoord;
                this.fogColorBlue = (float)vec34.zCoord;
            }
        }

        float f13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
        this.fogColorRed *= f13;
        this.fogColorGreen *= f13;
        this.fogColorBlue *= f13;
        double d1 = (实体.lastTickPosY + (实体.Y坐标 - 实体.lastTickPosY) * (double)partialTicks) * world.provider.getVoidFogYFactor();

        if (实体 instanceof 实体LivingBase && ((实体LivingBase) 实体).isPotionActive(Potion.blindness))
        {
            int i = ((实体LivingBase) 实体).getActivePotionEffect(Potion.blindness).getDuration();

            if (i < 20)
            {
                d1 *= (double)(1.0F - (float)i / 20.0F);
            }
            else
            {
                d1 = 0.0D;
            }
        }

        if (d1 < 1.0D)
        {
            if (d1 < 0.0D)
            {
                d1 = 0.0D;
            }

            d1 = d1 * d1;
            this.fogColorRed = (float)((double)this.fogColorRed * d1);
            this.fogColorGreen = (float)((double)this.fogColorGreen * d1);
            this.fogColorBlue = (float)((double)this.fogColorBlue * d1);
        }

        if (this.bossColorModifier > 0.0F)
        {
            float f14 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
            this.fogColorRed = this.fogColorRed * (1.0F - f14) + this.fogColorRed * 0.7F * f14;
            this.fogColorGreen = this.fogColorGreen * (1.0F - f14) + this.fogColorGreen * 0.6F * f14;
            this.fogColorBlue = this.fogColorBlue * (1.0F - f14) + this.fogColorBlue * 0.6F * f14;
        }

        if (实体 instanceof 实体LivingBase && ((实体LivingBase) 实体).isPotionActive(Potion.nightVision))
        {
            float f15 = this.getNightVisionBrightness((实体LivingBase) 实体, partialTicks);
            float f6 = 1.0F / this.fogColorRed;

            if (f6 > 1.0F / this.fogColorGreen)
            {
                f6 = 1.0F / this.fogColorGreen;
            }

            if (f6 > 1.0F / this.fogColorBlue)
            {
                f6 = 1.0F / this.fogColorBlue;
            }

            if (Float.isInfinite(f6))
            {
                f6 = Math.nextAfter(f6, 0.0D);
            }

            this.fogColorRed = this.fogColorRed * (1.0F - f15) + this.fogColorRed * f6 * f15;
            this.fogColorGreen = this.fogColorGreen * (1.0F - f15) + this.fogColorGreen * f6 * f15;
            this.fogColorBlue = this.fogColorBlue * (1.0F - f15) + this.fogColorBlue * f6 * f15;
        }

        if (this.mc.游戏一窝.anaglyph)
        {
            float f16 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
            float f17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
            float f7 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
            this.fogColorRed = f16;
            this.fogColorGreen = f17;
            this.fogColorBlue = f7;
        }

        if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists())
        {
            Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, new Object[] {this, 实体, block, Float.valueOf(partialTicks), Float.valueOf(this.fogColorRed), Float.valueOf(this.fogColorGreen), Float.valueOf(this.fogColorBlue)});
            Reflector.postForgeBusEvent(object);
            this.fogColorRed = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_red, this.fogColorRed);
            this.fogColorGreen = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_green, this.fogColorGreen);
            this.fogColorBlue = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_blue, this.fogColorBlue);
        }

        Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
    }

    private void setupFog(int startCoords, float partialTicks)
    {
        this.fogStandard = false;
        实体 实体 = this.mc.getRenderViewEntity();
        boolean flag = false;

        if (实体 instanceof 实体Player)
        {
            flag = ((实体Player) 实体).capabilities.isCreativeMode;
        }

        GL11.glFog(GL11.GL_FOG_COLOR, (FloatBuffer)this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.宇轩の世界, 实体, partialTicks);
        float f = -1.0F;

        if (Reflector.ForgeHooksClient_getFogDensity.exists())
        {
            f = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, new Object[] {this, 实体, block, Float.valueOf(partialTicks), Float.valueOf(0.1F)});
        }

        if (f >= 0.0F)
        {
            光照状态经理.setFogDensity(f);
        }
        else if (实体 instanceof 实体LivingBase && ((实体LivingBase) 实体).isPotionActive(Potion.blindness))
        {
            float f4 = 5.0F;
            int i = ((实体LivingBase) 实体).getActivePotionEffect(Potion.blindness).getDuration();

            if (i < 20)
            {
                f4 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float)i / 20.0F);
            }

            光照状态经理.setFog(9729);

            if (startCoords == -1)
            {
                光照状态经理.setFogStart(0.0F);
                光照状态经理.setFogEnd(f4 * 0.8F);
            }
            else
            {
                光照状态经理.setFogStart(f4 * 0.25F);
                光照状态经理.setFogEnd(f4);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy())
            {
                GL11.glFogi(34138, 34139);
            }
        }
        else if (this.cloudFog)
        {
            光照状态经理.setFog(2048);
            光照状态经理.setFogDensity(0.1F);
        }
        else if (block.getMaterial() == Material.water)
        {
            光照状态经理.setFog(2048);
            float f1 = Config.isClearWater() ? 0.02F : 0.1F;

            if (实体 instanceof 实体LivingBase && ((实体LivingBase) 实体).isPotionActive(Potion.waterBreathing))
            {
                光照状态经理.setFogDensity(0.01F);
            }
            else
            {
                float f2 = 0.1F - (float)EnchantmentHelper.getRespiration(实体) * 0.03F;
                光照状态经理.setFogDensity(Config.limit(f2, 0.0F, f1));
            }
        }
        else if (block.getMaterial() == Material.lava)
        {
            光照状态经理.setFog(2048);
            光照状态经理.setFogDensity(2.0F);
        }
        else
        {
            float f3 = this.farPlaneDistance;
            this.fogStandard = true;
            光照状态经理.setFog(9729);

            if (startCoords == -1)
            {
                光照状态经理.setFogStart(0.0F);
                光照状态经理.setFogEnd(f3);
            }
            else
            {
                光照状态经理.setFogStart(f3 * Config.getFogStart());
                光照状态经理.setFogEnd(f3);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance)
            {
                if (Config.isFogFancy())
                {
                    GL11.glFogi(34138, 34139);
                }

                if (Config.isFogFast())
                {
                    GL11.glFogi(34138, 34140);
                }
            }

            if (this.mc.宇轩の世界.provider.doesXZShowFog((int) 实体.X坐标, (int) 实体.Z坐标))
            {
                光照状态经理.setFogStart(f3 * 0.05F);
                光照状态经理.setFogEnd(f3);
            }

            if (Reflector.ForgeHooksClient_onFogRender.exists())
            {
                Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, new Object[] {this, 实体, block, Float.valueOf(partialTicks), Integer.valueOf(startCoords), Float.valueOf(f3)});
            }
        }

        光照状态经理.enableColorMaterial();
        光照状态经理.enableFog();
        光照状态经理.colorMaterial(1028, 4608);
    }

    private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha)
    {
        if (Config.isShaders())
        {
            Shaders.setFogColor(red, green, blue);
        }

        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }

    public MapItemRenderer getMapItemRenderer()
    {
        return this.theMapItemRenderer;
    }

    private void waitForServerThread()
    {
        this.serverWaitTimeCurrent = 0;

        if (Config.isSmoothWorld() && Config.isSingleProcessor())
        {
            if (this.mc.isIntegratedServerRunning())
            {
                IntegratedServer integratedserver = this.mc.getIntegratedServer();

                if (integratedserver != null)
                {
                    boolean flag = this.mc.isGamePaused();

                    if (!flag && !(this.mc.currentScreen instanceof 鬼DownloadTerrain))
                    {
                        if (this.serverWaitTime > 0)
                        {
                            Lagometer.timerServer.start();
                            Config.sleep((long)this.serverWaitTime);
                            Lagometer.timerServer.end();
                            this.serverWaitTimeCurrent = this.serverWaitTime;
                        }

                        long i = System.nanoTime() / 1000000L;

                        if (this.lastServerTime != 0L && this.lastServerTicks != 0)
                        {
                            long j = i - this.lastServerTime;

                            if (j < 0L)
                            {
                                this.lastServerTime = i;
                                j = 0L;
                            }

                            if (j >= 50L)
                            {
                                this.lastServerTime = i;
                                int k = integratedserver.getTickCounter();
                                int l = k - this.lastServerTicks;

                                if (l < 0)
                                {
                                    this.lastServerTicks = k;
                                    l = 0;
                                }

                                if (l < 1 && this.serverWaitTime < 100)
                                {
                                    this.serverWaitTime += 2;
                                }

                                if (l > 1 && this.serverWaitTime > 0)
                                {
                                    --this.serverWaitTime;
                                }

                                this.lastServerTicks = k;
                            }
                        }
                        else
                        {
                            this.lastServerTime = i;
                            this.lastServerTicks = integratedserver.getTickCounter();
                            this.avgServerTickDiff = 1.0F;
                            this.avgServerTimeDiff = 50.0F;
                        }
                    }
                    else
                    {
                        if (this.mc.currentScreen instanceof 鬼DownloadTerrain)
                        {
                            Config.sleep(20L);
                        }

                        this.lastServerTime = 0L;
                        this.lastServerTicks = 0;
                    }
                }
            }
        }
        else
        {
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
        }
    }

    private void frameInit()
    {
        GlErrors.frameStart();

        if (!this.initialized)
        {
            ReflectorResolver.resolve();
            TextureUtils.registerResourceListener();

            if (Config.getBitsOs() == 64 && Config.getBitsJre() == 32)
            {
                Config.setNotify64BitJava(true);
            }

            this.initialized = true;
        }

        Config.checkDisplayMode();
        World world = this.mc.宇轩の世界;

        if (world != null)
        {
            if (Config.getNewRelease() != null)
            {
                String s = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
                String s1 = s + " " + Config.getNewRelease();
                交流组分文本 chatcomponenttext = new 交流组分文本(I18n.format("of.message.newVersion", new Object[] {"\u00a7n" + s1 + "\u00a7r"}));
                chatcomponenttext.setChatStyle((new ChatStyle()).setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://optifine.net/downloads")));
                this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext);
                Config.setNewRelease((String)null);
            }

            if (Config.isNotify64BitJava())
            {
                Config.setNotify64BitJava(false);
                交流组分文本 chatcomponenttext1 = new 交流组分文本(I18n.format("of.message.java64Bit", new Object[0]));
                this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext1);
            }
        }

        if (this.mc.currentScreen instanceof 鬼MainMenu)
        {
            this.updateMainMenu((鬼MainMenu)this.mc.currentScreen);
        }

        if (this.updatedWorld != world)
        {
            RandomEntities.worldChanged(this.updatedWorld, world);
            Config.updateThreadPriorities();
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
            this.updatedWorld = world;
        }

        if (!this.setFxaaShader(Shaders.configAntialiasingLevel))
        {
            Shaders.configAntialiasingLevel = 0;
        }

        if (this.mc.currentScreen != null && this.mc.currentScreen.getClass() == 鬼Chat.class)
        {
            this.mc.displayGuiScreen(new 鬼ChatOF((鬼Chat)this.mc.currentScreen));
        }
    }

    private void frameFinish()
    {
        if (this.mc.宇轩の世界 != null && Config.isShowGlErrors() && TimedEvent.isActive("CheckGlErrorFrameFinish", 10000L))
        {
            int i = 光照状态经理.glGetError();

            if (i != 0 && GlErrors.isEnabled(i))
            {
                String s = Config.getGlErrorString(i);
                交流组分文本 chatcomponenttext = new 交流组分文本(I18n.format("of.message.openglError", new Object[] {Integer.valueOf(i), s}));
                this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext);
            }
        }
    }

    private void updateMainMenu(鬼MainMenu p_updateMainMenu_1_)
    {
        try
        {
            String s = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int i = calendar.get(5);
            int j = calendar.get(2) + 1;

            if (i == 8 && j == 4)
            {
                s = "Happy birthday, OptiFine!";
            }

            if (i == 14 && j == 8)
            {
                s = "Happy birthday, sp614x!";
            }

            if (s == null)
            {
                return;
            }

            Reflector.setFieldValue(p_updateMainMenu_1_, Reflector.GuiMainMenu_splashText, s);
        }
        catch (Throwable var6)
        {
            ;
        }
    }

    public boolean setFxaaShader(int p_setFxaaShader_1_)
    {
        if (!OpenGlHelper.isFramebufferEnabled())
        {
            return false;
        }
        else if (this.theShaderGroup != null && this.theShaderGroup != this.fxaaShaders[2] && this.theShaderGroup != this.fxaaShaders[4])
        {
            return true;
        }
        else if (p_setFxaaShader_1_ != 2 && p_setFxaaShader_1_ != 4)
        {
            if (this.theShaderGroup == null)
            {
                return true;
            }
            else
            {
                this.theShaderGroup.deleteShaderGroup();
                this.theShaderGroup = null;
                return true;
            }
        }
        else if (this.theShaderGroup != null && this.theShaderGroup == this.fxaaShaders[p_setFxaaShader_1_])
        {
            return true;
        }
        else if (this.mc.宇轩の世界 == null)
        {
            return true;
        }
        else
        {
            this.loadShader(new 图像位置("shaders/post/fxaa_of_" + p_setFxaaShader_1_ + "x.json"));
            this.fxaaShaders[p_setFxaaShader_1_] = this.theShaderGroup;
            return this.useShader;
        }
    }

    private void checkLoadVisibleChunks(实体 p_checkLoadVisibleChunks_1_, float p_checkLoadVisibleChunks_2_, ICamera p_checkLoadVisibleChunks_3_, boolean p_checkLoadVisibleChunks_4_)
    {
        int i = 201435902;

        if (this.loadVisibleChunks)
        {
            this.loadVisibleChunks = false;
            this.loadAllVisibleChunks(p_checkLoadVisibleChunks_1_, (double)p_checkLoadVisibleChunks_2_, p_checkLoadVisibleChunks_3_, p_checkLoadVisibleChunks_4_);
            this.mc.ingameGUI.getChatGUI().deleteChatLine(i);
        }

        if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(38))
        {
            if (this.mc.currentScreen != null)
            {
                return;
            }

            this.loadVisibleChunks = true;
            交流组分文本 chatcomponenttext = new 交流组分文本(I18n.format("of.message.loadingVisibleChunks", new Object[0]));
            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(chatcomponenttext, i);
        }
    }

    private void loadAllVisibleChunks(实体 p_loadAllVisibleChunks_1_, double p_loadAllVisibleChunks_2_, ICamera p_loadAllVisibleChunks_4_, boolean p_loadAllVisibleChunks_5_)
    {
        int i = this.mc.游戏一窝.ofChunkUpdates;
        boolean flag = this.mc.游戏一窝.ofLazyChunkLoading;

        try
        {
            this.mc.游戏一窝.ofChunkUpdates = 1000;
            this.mc.游戏一窝.ofLazyChunkLoading = false;
            RenderGlobal renderglobal = Config.getRenderGlobal();
            int j = renderglobal.getCountLoadedChunks();
            long k = System.currentTimeMillis();
            Config.dbg("Loading visible chunks");
            long l = System.currentTimeMillis() + 5000L;
            int i1 = 0;
            boolean flag1 = false;

            while (true)
            {
                flag1 = false;

                for (int j1 = 0; j1 < 100; ++j1)
                {
                    renderglobal.displayListEntitiesDirty = true;
                    renderglobal.setupTerrain(p_loadAllVisibleChunks_1_, p_loadAllVisibleChunks_2_, p_loadAllVisibleChunks_4_, this.frameCount++, p_loadAllVisibleChunks_5_);

                    if (!renderglobal.hasNoChunkUpdates())
                    {
                        flag1 = true;
                    }

                    i1 = i1 + renderglobal.getCountChunksToUpdate();

                    while (!renderglobal.hasNoChunkUpdates())
                    {
                        renderglobal.updateChunks(System.nanoTime() + 1000000000L);
                    }

                    i1 = i1 - renderglobal.getCountChunksToUpdate();

                    if (!flag1)
                    {
                        break;
                    }
                }

                if (renderglobal.getCountLoadedChunks() != j)
                {
                    flag1 = true;
                    j = renderglobal.getCountLoadedChunks();
                }

                if (System.currentTimeMillis() > l)
                {
                    Config.log("Chunks loaded: " + i1);
                    l = System.currentTimeMillis() + 5000L;
                }

                if (!flag1)
                {
                    break;
                }
            }

            Config.log("Chunks loaded: " + i1);
            Config.log("Finished loading visible chunks");
            RenderChunk.renderChunksUpdated = 0;
        }
        finally
        {
            this.mc.游戏一窝.ofChunkUpdates = i;
            this.mc.游戏一窝.ofLazyChunkLoading = flag;
        }
    }
}
