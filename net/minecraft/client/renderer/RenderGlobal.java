package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.鬼Screen;
import net.minecraft.client.我的手艺;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.实体FX;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.projectile.实体WitherSkull;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Matrix4f;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.图像位置;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vector3d;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.optifine.CustomColors;
import net.optifine.CustomSky;
import net.optifine.DynamicLights;
import net.optifine.Lagometer;
import net.optifine.RandomEntities;
import net.optifine.SmartAnimations;
import net.optifine.model.BlockModelUtils;
import net.optifine.reflect.Reflector;
import net.optifine.render.ChunkVisibility;
import net.optifine.render.CloudRenderer;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import net.optifine.shaders.ShadowUtils;
import net.optifine.shaders.gui.鬼ShaderOptions;
import net.optifine.util.ChunkUtils;
import net.optifine.util.RenderChunkUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class RenderGlobal implements IWorldAccess, IResourceManagerReloadListener
{
    private static final Logger logger = LogManager.getLogger();
    private static final 图像位置 locationMoonPhasesPng = new 图像位置("textures/environment/moon_phases.png");
    private static final 图像位置 locationSunPng = new 图像位置("textures/environment/sun.png");
    private static final 图像位置 locationCloudsPng = new 图像位置("textures/environment/clouds.png");
    private static final 图像位置 locationEndSkyPng = new 图像位置("textures/environment/end_sky.png");
    private static final 图像位置 locationForcefieldPng = new 图像位置("textures/misc/forcefield.png");
    public final 我的手艺 mc;
    private final TextureManager renderEngine;
    private final RenderManager renderManager;
    private WorldClient theWorld;
    private Set<RenderChunk> chunksToUpdate = Sets.<RenderChunk>newLinkedHashSet();
    private List<RenderGlobal.ContainerLocalRenderInformation> renderInfos = Lists.<RenderGlobal.ContainerLocalRenderInformation>newArrayListWithCapacity(69696);
    private final Set<TileEntity> setTileEntities = Sets.<TileEntity>newHashSet();
    private ViewFrustum viewFrustum;
    private int starGLCallList = -1;
    private int glSkyList = -1;
    private int glSkyList2 = -1;
    private VertexFormat vertexBufferFormat;
    private VertexBuffer starVBO;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private int cloudTickCounter;
    public final Map<Integer, DestroyBlockProgress> damagedBlocks = Maps.<Integer, DestroyBlockProgress>newHashMap();
    private final Map<阻止位置, ISound> mapSoundPositions = Maps.<阻止位置, ISound>newHashMap();
    private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
    private Framebuffer entityOutlineFramebuffer;
    private ShaderGroup entityOutlineShader;
    private double frustumUpdatePosX = Double.MIN_VALUE;
    private double frustumUpdatePosY = Double.MIN_VALUE;
    private double frustumUpdatePosZ = Double.MIN_VALUE;
    private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
    private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
    private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
    private double lastViewEntityX = Double.MIN_VALUE;
    private double lastViewEntityY = Double.MIN_VALUE;
    private double lastViewEntityZ = Double.MIN_VALUE;
    private double lastViewEntityPitch = Double.MIN_VALUE;
    private double lastViewEntityYaw = Double.MIN_VALUE;
    private final ChunkRenderDispatcher renderDispatcher = new ChunkRenderDispatcher();
    private ChunkRenderContainer renderContainer;
    private int renderDistanceChunks = -1;
    private int renderEntitiesStartupCounter = 2;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    private boolean debugFixTerrainFrustum = false;
    private ClippingHelper debugFixedClippingHelper;
    private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
    private final Vector3d debugTerrainFrustumPosition = new Vector3d();
    private boolean vboEnabled = false;
    IRenderChunkFactory renderChunkFactory;
    private double prevRenderSortX;
    private double prevRenderSortY;
    private double prevRenderSortZ;
    public boolean displayListEntitiesDirty = true;
    private CloudRenderer cloudRenderer;
    public 实体 rendered实体;
    public Set chunksToResortTransparency = new LinkedHashSet();
    public Set chunksToUpdateForced = new LinkedHashSet();
    private Deque visibilityDeque = new ArrayDeque();
    private List renderInfosEntities = new ArrayList(1024);
    private List renderInfosTileEntities = new ArrayList(1024);
    private List renderInfosNormal = new ArrayList(1024);
    private List renderInfosEntitiesNormal = new ArrayList(1024);
    private List renderInfosTileEntitiesNormal = new ArrayList(1024);
    private List renderInfosShadow = new ArrayList(1024);
    private List renderInfosEntitiesShadow = new ArrayList(1024);
    private List renderInfosTileEntitiesShadow = new ArrayList(1024);
    private int renderDistance = 0;
    private int renderDistanceSq = 0;
    private static final Set SET_ALL_FACINGS = Collections.unmodifiableSet(new HashSet(Arrays.asList(EnumFacing.VALUES)));
    private int countTileEntitiesRendered;
    private IChunkProvider worldChunkProvider = null;
    private LongHashMap worldChunkProviderMap = null;
    private int countLoadedChunksPrev = 0;
    private RenderEnv renderEnv = new RenderEnv(Blocks.air.getDefaultState(), new 阻止位置(0, 0, 0));
    public boolean renderOverlayDamaged = false;
    public boolean renderOverlayEyes = false;
    private boolean firstWorldLoad = false;
    private static int renderEntitiesCounter = 0;

    public RenderGlobal(我的手艺 mcIn)
    {
        this.cloudRenderer = new CloudRenderer(mcIn);
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.renderEngine = mcIn.得到手感经理();
        this.renderEngine.绑定手感(locationForcefieldPng);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        光照状态经理.绑定手感(0);
        this.updateDestroyBlockIcons();
        this.vboEnabled = OpenGlHelper.useVbo();

        if (this.vboEnabled)
        {
            this.renderContainer = new VboRenderList();
            this.renderChunkFactory = new VboChunkFactory();
        }
        else
        {
            this.renderContainer = new RenderList();
            this.renderChunkFactory = new ListChunkFactory();
        }

        this.vertexBufferFormat = new VertexFormat();
        this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
        this.generateStars();
        this.generateSky();
        this.generateSky2();
    }

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.updateDestroyBlockIcons();
    }

    private void updateDestroyBlockIcons()
    {
        TextureMap texturemap = this.mc.getTextureMapBlocks();

        for (int i = 0; i < this.destroyBlockIcons.length; ++i)
        {
            this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
        }
    }

    public void makeEntityOutlineShader()
    {
        if (OpenGlHelper.shadersSupported)
        {
            if (ShaderLinkHelper.getStaticShaderLinkHelper() == null)
            {
                ShaderLinkHelper.setNewStaticShaderLinkHelper();
            }

            图像位置 resourcelocation = new 图像位置("shaders/post/entity_outline.json");

            try
            {
                this.entityOutlineShader = new ShaderGroup(this.mc.得到手感经理(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation);
                this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
                this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
            }
            catch (IOException ioexception)
            {
                logger.warn((String)("Failed to load shader: " + resourcelocation), (Throwable)ioexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
            catch (JsonSyntaxException jsonsyntaxexception)
            {
                logger.warn((String)("Failed to load shader: " + resourcelocation), (Throwable)jsonsyntaxexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
        }
        else
        {
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
        }
    }

    public void renderEntityOutlineFramebuffer()
    {
        if (this.isRenderEntityOutlines())
        {
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 771, 0, 1);
            this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
            光照状态经理.禁用混合品();
        }
    }

    protected boolean isRenderEntityOutlines()
    {
        return !Config.isFastRender() && !Config.isShaders() && !Config.isAntialiasing() ? this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.宇轩游玩者 != null && this.mc.宇轩游玩者.isSpectator() && this.mc.游戏一窝.keyBindSpectatorOutlines.键位绑定沿着() : false;
    }

    private void generateSky2()
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (this.sky2VBO != null)
        {
            this.sky2VBO.deleteGlBuffers();
        }

        if (this.glSkyList2 >= 0)
        {
            GLAllocation.deleteDisplayLists(this.glSkyList2);
            this.glSkyList2 = -1;
        }

        if (this.vboEnabled)
        {
            this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldrenderer, -16.0F, true);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.sky2VBO.bufferData(worldrenderer.getByteBuffer());
        }
        else
        {
            this.glSkyList2 = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
            this.renderSky(worldrenderer, -16.0F, true);
            tessellator.draw();
            GL11.glEndList();
        }
    }

    private void generateSky()
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (this.skyVBO != null)
        {
            this.skyVBO.deleteGlBuffers();
        }

        if (this.glSkyList >= 0)
        {
            GLAllocation.deleteDisplayLists(this.glSkyList);
            this.glSkyList = -1;
        }

        if (this.vboEnabled)
        {
            this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldrenderer, 16.0F, false);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.skyVBO.bufferData(worldrenderer.getByteBuffer());
        }
        else
        {
            this.glSkyList = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
            this.renderSky(worldrenderer, 16.0F, false);
            tessellator.draw();
            GL11.glEndList();
        }
    }

    private void renderSky(WorldRenderer worldRendererIn, float posY, boolean reverseX)
    {
        int i = 64;
        int j = 6;
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
        int k = (this.renderDistance / 64 + 1) * 64 + 64;

        for (int l = -k; l <= k; l += 64)
        {
            for (int i1 = -k; i1 <= k; i1 += 64)
            {
                float f = (float)l;
                float f1 = (float)(l + 64);

                if (reverseX)
                {
                    f1 = (float)l;
                    f = (float)(l + 64);
                }

                worldRendererIn.pos((double)f, (double)posY, (double)i1).endVertex();
                worldRendererIn.pos((double)f1, (double)posY, (double)i1).endVertex();
                worldRendererIn.pos((double)f1, (double)posY, (double)(i1 + 64)).endVertex();
                worldRendererIn.pos((double)f, (double)posY, (double)(i1 + 64)).endVertex();
            }
        }
    }

    private void generateStars()
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (this.starVBO != null)
        {
            this.starVBO.deleteGlBuffers();
        }

        if (this.starGLCallList >= 0)
        {
            GLAllocation.deleteDisplayLists(this.starGLCallList);
            this.starGLCallList = -1;
        }

        if (this.vboEnabled)
        {
            this.starVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderStars(worldrenderer);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.starVBO.bufferData(worldrenderer.getByteBuffer());
        }
        else
        {
            this.starGLCallList = GLAllocation.generateDisplayLists(1);
            光照状态经理.推黑客帝国();
            GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
            this.renderStars(worldrenderer);
            tessellator.draw();
            GL11.glEndList();
            光照状态经理.流行音乐黑客帝国();
        }
    }

    private void renderStars(WorldRenderer worldRendererIn)
    {
        Random random = new Random(10842L);
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);

        for (int i = 0; i < 1500; ++i)
        {
            double d0 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d3 = (double)(0.15F + random.nextFloat() * 0.1F);
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d4 < 1.0D && d4 > 0.01D)
            {
                d4 = 1.0D / Math.sqrt(d4);
                d0 = d0 * d4;
                d1 = d1 * d4;
                d2 = d2 * d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j)
                {
                    double d17 = 0.0D;
                    double d18 = (double)((j & 2) - 1) * d3;
                    double d19 = (double)((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0D;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    worldRendererIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }
    }

    public void setWorldAndLoadRenderers(WorldClient worldClientIn)
    {
        if (this.theWorld != null)
        {
            this.theWorld.removeWorldAccess(this);
        }

        this.frustumUpdatePosX = Double.MIN_VALUE;
        this.frustumUpdatePosY = Double.MIN_VALUE;
        this.frustumUpdatePosZ = Double.MIN_VALUE;
        this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
        this.renderManager.set(worldClientIn);
        this.theWorld = worldClientIn;

        if (Config.isDynamicLights())
        {
            DynamicLights.clear();
        }

        ChunkVisibility.reset();
        this.worldChunkProvider = null;
        this.worldChunkProviderMap = null;
        this.renderEnv.reset((IBlockState)null, (阻止位置)null);
        Shaders.checkWorldChanged(this.theWorld);

        if (worldClientIn != null)
        {
            worldClientIn.addWorldAccess(this);
            this.loadRenderers();
        }
        else
        {
            this.chunksToUpdate.clear();
            this.clearRenderInfos();

            if (this.viewFrustum != null)
            {
                this.viewFrustum.deleteGlResources();
            }

            this.viewFrustum = null;
        }
    }

    public void loadRenderers()
    {
        if (this.theWorld != null)
        {
            this.displayListEntitiesDirty = true;
            Blocks.leaves.setGraphicsLevel(Config.isTreesFancy());
            Blocks.leaves2.setGraphicsLevel(Config.isTreesFancy());
            BlockModelRenderer.updateAoLightValue();

            if (Config.isDynamicLights())
            {
                DynamicLights.clear();
            }

            SmartAnimations.update();
            this.renderDistanceChunks = this.mc.游戏一窝.renderDistanceChunks;
            this.renderDistance = this.renderDistanceChunks * 16;
            this.renderDistanceSq = this.renderDistance * this.renderDistance;
            boolean flag = this.vboEnabled;
            this.vboEnabled = OpenGlHelper.useVbo();

            if (flag && !this.vboEnabled)
            {
                this.renderContainer = new RenderList();
                this.renderChunkFactory = new ListChunkFactory();
            }
            else if (!flag && this.vboEnabled)
            {
                this.renderContainer = new VboRenderList();
                this.renderChunkFactory = new VboChunkFactory();
            }

            this.generateStars();
            this.generateSky();
            this.generateSky2();

            if (this.viewFrustum != null)
            {
                this.viewFrustum.deleteGlResources();
            }

            this.stopChunkUpdates();

            synchronized (this.setTileEntities)
            {
                this.setTileEntities.clear();
            }

            this.viewFrustum = new ViewFrustum(this.theWorld, this.mc.游戏一窝.renderDistanceChunks, this, this.renderChunkFactory);

            if (this.theWorld != null)
            {
                实体 实体 = this.mc.getRenderViewEntity();

                if (实体 != null)
                {
                    this.viewFrustum.updateChunkPositions(实体.X坐标, 实体.Z坐标);
                }
            }

            this.renderEntitiesStartupCounter = 2;
        }

        if (this.mc.宇轩游玩者 == null)
        {
            this.firstWorldLoad = true;
        }
    }

    protected void stopChunkUpdates()
    {
        this.chunksToUpdate.clear();
        this.renderDispatcher.stopChunkUpdates();
    }

    public void createBindEntityOutlineFbs(int width, int height)
    {
        if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null)
        {
            this.entityOutlineShader.createBindFramebuffers(width, height);
        }
    }

    public void renderEntities(实体 renderView实体, ICamera camera, float partialTicks)
    {
        int i = 0;

        if (Reflector.MinecraftForgeClient_getRenderPass.exists())
        {
            i = Reflector.callInt(Reflector.MinecraftForgeClient_getRenderPass, new Object[0]);
        }

        if (this.renderEntitiesStartupCounter > 0)
        {
            if (i > 0)
            {
                return;
            }

            --this.renderEntitiesStartupCounter;
        }
        else
        {
            double d0 = renderView实体.prevPosX + (renderView实体.X坐标 - renderView实体.prevPosX) * (double)partialTicks;
            double d1 = renderView实体.prevPosY + (renderView实体.Y坐标 - renderView实体.prevPosY) * (double)partialTicks;
            double d2 = renderView实体.prevPosZ + (renderView实体.Z坐标 - renderView实体.prevPosZ) * (double)partialTicks;
            this.theWorld.theProfiler.startSection("prepare");
            TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.得到手感经理(), this.mc.字体渲染员, this.mc.getRenderViewEntity(), partialTicks);
            this.renderManager.cacheActiveRenderInfo(this.theWorld, this.mc.字体渲染员, this.mc.getRenderViewEntity(), this.mc.pointed实体, this.mc.游戏一窝, partialTicks);
            ++renderEntitiesCounter;

            if (i == 0)
            {
                this.countEntitiesTotal = 0;
                this.countEntitiesRendered = 0;
                this.countEntitiesHidden = 0;
                this.countTileEntitiesRendered = 0;
            }

            实体 实体 = this.mc.getRenderViewEntity();
            double d3 = 实体.lastTickPosX + (实体.X坐标 - 实体.lastTickPosX) * (double)partialTicks;
            double d4 = 实体.lastTickPosY + (实体.Y坐标 - 实体.lastTickPosY) * (double)partialTicks;
            double d5 = 实体.lastTickPosZ + (实体.Z坐标 - 实体.lastTickPosZ) * (double)partialTicks;
            TileEntityRendererDispatcher.staticPlayerX = d3;
            TileEntityRendererDispatcher.staticPlayerY = d4;
            TileEntityRendererDispatcher.staticPlayerZ = d5;
            this.renderManager.setRenderPosition(d3, d4, d5);
            this.mc.entityRenderer.enableLightmap();
            this.theWorld.theProfiler.endStartSection("global");
            List<实体> list = this.theWorld.getLoadedEntityList();

            if (i == 0)
            {
                this.countEntitiesTotal = list.size();
            }

            if (Config.isFogOff() && this.mc.entityRenderer.fogStandard)
            {
                光照状态经理.disableFog();
            }

            boolean flag = Reflector.ForgeEntity_shouldRenderInPass.exists();
            boolean flag1 = Reflector.ForgeTileEntity_shouldRenderInPass.exists();

            for (int j = 0; j < this.theWorld.weatherEffects.size(); ++j)
            {
                实体 实体1 = (实体)this.theWorld.weatherEffects.get(j);

                if (!flag || Reflector.callBoolean(实体1, Reflector.ForgeEntity_shouldRenderInPass, new Object[] {Integer.valueOf(i)}))
                {
                    ++this.countEntitiesRendered;

                    if (实体1.isInRangeToRender3d(d0, d1, d2))
                    {
                        this.renderManager.renderEntitySimple(实体1, partialTicks);
                    }
                }
            }

            if (this.isRenderEntityOutlines())
            {
                光照状态经理.depthFunc(519);
                光照状态经理.disableFog();
                this.entityOutlineFramebuffer.framebufferClear();
                this.entityOutlineFramebuffer.bindFramebuffer(false);
                this.theWorld.theProfiler.endStartSection("entityOutlines");
                RenderHelper.disableStandardItemLighting();
                this.renderManager.setRenderOutlines(true);

                for (int k = 0; k < list.size(); ++k)
                {
                    实体 实体3 = (实体)list.get(k);
                    boolean flag2 = this.mc.getRenderViewEntity() instanceof 实体LivingBase && ((实体LivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                    boolean flag3 = 实体3.isInRangeToRender3d(d0, d1, d2) && (实体3.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(实体3.getEntityBoundingBox()) || 实体3.riddenBy实体 == this.mc.宇轩游玩者) && 实体3 instanceof 实体Player;

                    if ((实体3 != this.mc.getRenderViewEntity() || this.mc.游戏一窝.thirdPersonView != 0 || flag2) && flag3)
                    {
                        this.renderManager.renderEntitySimple(实体3, partialTicks);
                    }
                }

                this.renderManager.setRenderOutlines(false);
                RenderHelper.enableStandardItemLighting();
                光照状态经理.depthMask(false);
                this.entityOutlineShader.loadShaderGroup(partialTicks);
                光照状态经理.enableLighting();
                光照状态经理.depthMask(true);
                this.mc.getFramebuffer().bindFramebuffer(false);
                光照状态经理.enableFog();
                光照状态经理.启用混合品();
                光照状态经理.enableColorMaterial();
                光照状态经理.depthFunc(515);
                光照状态经理.启用纵深();
                光照状态经理.启用希腊字母表的第1个字母();
            }

            this.theWorld.theProfiler.endStartSection("entities");
            boolean flag6 = Config.isShaders();

            if (flag6)
            {
                Shaders.beginEntities();
            }

            RenderItemFrame.updateItemRenderDistance();
            boolean flag7 = this.mc.游戏一窝.fancyGraphics;
            this.mc.游戏一窝.fancyGraphics = Config.isDroppedItemsFancy();
            boolean flag8 = Shaders.isShadowPass && !this.mc.宇轩游玩者.isSpectator();
            label926:

            for (Object o : this.renderInfosEntities)
            {
                RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = (ContainerLocalRenderInformation) o;
                Chunk chunk = renderglobal$containerlocalrenderinformation.renderChunk.getChunk();
                ClassInheritanceMultiMap<实体> classinheritancemultimap = chunk.getEntityLists()[renderglobal$containerlocalrenderinformation.renderChunk.getPosition().getY() / 16];

                if (!classinheritancemultimap.isEmpty())
                {
                    Iterator iterator = classinheritancemultimap.iterator();

                    while (true)
                    {
                        实体 实体2;
                        boolean flag4;

                        while (true)
                        {
                            if (!iterator.hasNext())
                            {
                                continue label926;
                            }

                            实体2 = (实体)iterator.next();

                            if (!flag || Reflector.callBoolean(实体2, Reflector.ForgeEntity_shouldRenderInPass, new Object[] {Integer.valueOf(i)}))
                            {
                                flag4 = this.renderManager.shouldRender(实体2, camera, d0, d1, d2) || 实体2.riddenBy实体 == this.mc.宇轩游玩者;

                                if (!flag4)
                                {
                                    break;
                                }

                                boolean flag5 = this.mc.getRenderViewEntity() instanceof 实体LivingBase ? ((实体LivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping() : false;

                                if ((实体2 != this.mc.getRenderViewEntity() || flag8 || this.mc.游戏一窝.thirdPersonView != 0 || flag5) && (实体2.Y坐标 < 0.0D || 实体2.Y坐标 >= 256.0D || this.theWorld.isBlockLoaded(new 阻止位置(实体2))))
                                {
                                    ++this.countEntitiesRendered;
                                    this.rendered实体 = 实体2;

                                    if (flag6)
                                    {
                                        Shaders.nextEntity(实体2);
                                    }

                                    this.renderManager.renderEntitySimple(实体2, partialTicks);
                                    this.rendered实体 = null;
                                    break;
                                }
                            }
                        }

                        if (!flag4 && 实体2 instanceof 实体WitherSkull && (!flag || Reflector.callBoolean(实体2, Reflector.ForgeEntity_shouldRenderInPass, new Object[] {Integer.valueOf(i)})))
                        {
                            this.rendered实体 = 实体2;

                            if (flag6)
                            {
                                Shaders.nextEntity(实体2);
                            }

                            this.mc.getRenderManager().renderWitherSkull(实体2, partialTicks);
                            this.rendered实体 = null;
                        }
                    }
                }
            }

            this.mc.游戏一窝.fancyGraphics = flag7;

            if (flag6)
            {
                Shaders.endEntities();
                Shaders.beginBlockEntities();
            }

            this.theWorld.theProfiler.endStartSection("blockentities");
            RenderHelper.enableStandardItemLighting();

            if (Reflector.ForgeTileEntity_hasFastRenderer.exists())
            {
                TileEntityRendererDispatcher.instance.preDrawBatch();
            }

            TileEntitySignRenderer.updateTextRenderDistance();
            label1408:

            for (Object o : this.renderInfosTileEntities)
            {
                RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 = (ContainerLocalRenderInformation) o;
                List<TileEntity> list1 = renderglobal$containerlocalrenderinformation1.renderChunk.getCompiledChunk().getTileEntities();

                if (!list1.isEmpty())
                {
                    Iterator iterator1 = list1.iterator();

                    while (true)
                    {
                        TileEntity tileentity1;

                        while (true)
                        {
                            if (!iterator1.hasNext())
                            {
                                continue label1408;
                            }

                            tileentity1 = (TileEntity)iterator1.next();

                            if (!flag1)
                            {
                                break;
                            }

                            if (Reflector.callBoolean(tileentity1, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[] {Integer.valueOf(i)}))
                            {
                                AxisAlignedBB axisalignedbb1 = (AxisAlignedBB)Reflector.call(tileentity1, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);

                                if (axisalignedbb1 == null || camera.isBoundingBoxInFrustum(axisalignedbb1))
                                {
                                    break;
                                }
                            }
                        }

                        if (flag6)
                        {
                            Shaders.nextBlockEntity(tileentity1);
                        }

                        TileEntityRendererDispatcher.instance.renderTileEntity(tileentity1, partialTicks, -1);
                        ++this.countTileEntitiesRendered;
                    }
                }
            }

            synchronized (this.setTileEntities)
            {
                for (TileEntity tileentity : this.setTileEntities)
                {
                    if (!flag1 || Reflector.callBoolean(tileentity, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[] {Integer.valueOf(i)}))
                    {
                        if (flag6)
                        {
                            Shaders.nextBlockEntity(tileentity);
                        }

                        TileEntityRendererDispatcher.instance.renderTileEntity(tileentity, partialTicks, -1);
                    }
                }
            }

            if (Reflector.ForgeTileEntity_hasFastRenderer.exists())
            {
                TileEntityRendererDispatcher.instance.drawBatch(i);
            }

            this.renderOverlayDamaged = true;
            this.preRenderDamagedBlocks();

            for (DestroyBlockProgress destroyblockprogress : this.damagedBlocks.values())
            {
                阻止位置 blockpos = destroyblockprogress.getPosition();
                TileEntity tileentity2 = this.theWorld.getTileEntity(blockpos);

                if (tileentity2 instanceof TileEntityChest)
                {
                    TileEntityChest tileentitychest = (TileEntityChest)tileentity2;

                    if (tileentitychest.adjacentChestXNeg != null)
                    {
                        blockpos = blockpos.offset(EnumFacing.WEST);
                        tileentity2 = this.theWorld.getTileEntity(blockpos);
                    }
                    else if (tileentitychest.adjacentChestZNeg != null)
                    {
                        blockpos = blockpos.offset(EnumFacing.NORTH);
                        tileentity2 = this.theWorld.getTileEntity(blockpos);
                    }
                }

                Block block = this.theWorld.getBlockState(blockpos).getBlock();
                boolean flag9;

                if (flag1)
                {
                    flag9 = false;

                    if (tileentity2 != null && Reflector.callBoolean(tileentity2, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[] {Integer.valueOf(i)}) && Reflector.callBoolean(tileentity2, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]))
                    {
                        AxisAlignedBB axisalignedbb = (AxisAlignedBB)Reflector.call(tileentity2, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);

                        if (axisalignedbb != null)
                        {
                            flag9 = camera.isBoundingBoxInFrustum(axisalignedbb);
                        }
                    }
                }
                else
                {
                    flag9 = tileentity2 != null && (block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull);
                }

                if (flag9)
                {
                    if (flag6)
                    {
                        Shaders.nextBlockEntity(tileentity2);
                    }

                    TileEntityRendererDispatcher.instance.renderTileEntity(tileentity2, partialTicks, destroyblockprogress.getPartialBlockDamage());
                }
            }

            this.postRenderDamagedBlocks();
            this.renderOverlayDamaged = false;

            if (flag6)
            {
                Shaders.endBlockEntities();
            }

            --renderEntitiesCounter;
            this.mc.entityRenderer.disableLightmap();
            this.mc.mcProfiler.endSection();
        }
    }

    public String getDebugInfoRenders()
    {
        int i = this.viewFrustum.renderChunks.length;
        int j = 0;

        for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos)
        {
            CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk;

            if (compiledchunk != CompiledChunk.DUMMY && !compiledchunk.isEmpty())
            {
                ++j;
            }
        }

        return String.format("C: %d/%d %sD: %d, %s", new Object[] {Integer.valueOf(j), Integer.valueOf(i), this.mc.renderChunksMany ? "(s) " : "", Integer.valueOf(this.renderDistanceChunks), this.renderDispatcher.getDebugInfo()});
    }

    public String getDebugInfoEntities()
    {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered) + ", " + Config.getVersionDebug();
    }

    public void setupTerrain(实体 view实体, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator)
    {
        if (this.mc.游戏一窝.renderDistanceChunks != this.renderDistanceChunks)
        {
            this.loadRenderers();
        }

        this.theWorld.theProfiler.startSection("camera");
        double d0 = view实体.X坐标 - this.frustumUpdatePosX;
        double d1 = view实体.Y坐标 - this.frustumUpdatePosY;
        double d2 = view实体.Z坐标 - this.frustumUpdatePosZ;

        if (this.frustumUpdatePosChunkX != view实体.chunkCoordX || this.frustumUpdatePosChunkY != view实体.chunkCoordY || this.frustumUpdatePosChunkZ != view实体.chunkCoordZ || d0 * d0 + d1 * d1 + d2 * d2 > 16.0D)
        {
            this.frustumUpdatePosX = view实体.X坐标;
            this.frustumUpdatePosY = view实体.Y坐标;
            this.frustumUpdatePosZ = view实体.Z坐标;
            this.frustumUpdatePosChunkX = view实体.chunkCoordX;
            this.frustumUpdatePosChunkY = view实体.chunkCoordY;
            this.frustumUpdatePosChunkZ = view实体.chunkCoordZ;
            this.viewFrustum.updateChunkPositions(view实体.X坐标, view实体.Z坐标);
        }

        if (Config.isDynamicLights())
        {
            DynamicLights.update(this);
        }

        this.theWorld.theProfiler.endStartSection("renderlistcamera");
        double d3 = view实体.lastTickPosX + (view实体.X坐标 - view实体.lastTickPosX) * partialTicks;
        double d4 = view实体.lastTickPosY + (view实体.Y坐标 - view实体.lastTickPosY) * partialTicks;
        double d5 = view实体.lastTickPosZ + (view实体.Z坐标 - view实体.lastTickPosZ) * partialTicks;
        this.renderContainer.initialize(d3, d4, d5);
        this.theWorld.theProfiler.endStartSection("cull");

        if (this.debugFixedClippingHelper != null)
        {
            Frustum frustum = new Frustum(this.debugFixedClippingHelper);
            frustum.setPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
            camera = frustum;
        }

        this.mc.mcProfiler.endStartSection("culling");
        阻止位置 blockpos = new 阻止位置(d3, d4 + (double) view实体.getEyeHeight(), d5);
        RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos);
        new 阻止位置(MathHelper.floor_double(d3 / 16.0D) * 16, MathHelper.floor_double(d4 / 16.0D) * 16, MathHelper.floor_double(d5 / 16.0D) * 16);
        this.displayListEntitiesDirty = this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || view实体.X坐标 != this.lastViewEntityX || view实体.Y坐标 != this.lastViewEntityY || view实体.Z坐标 != this.lastViewEntityZ || (double) view实体.rotationPitch != this.lastViewEntityPitch || (double) view实体.旋转侧滑 != this.lastViewEntityYaw;
        this.lastViewEntityX = view实体.X坐标;
        this.lastViewEntityY = view实体.Y坐标;
        this.lastViewEntityZ = view实体.Z坐标;
        this.lastViewEntityPitch = (double) view实体.rotationPitch;
        this.lastViewEntityYaw = (double) view实体.旋转侧滑;
        boolean flag = this.debugFixedClippingHelper != null;
        this.mc.mcProfiler.endStartSection("update");
        Lagometer.timerVisibility.start();
        int i = this.getCountLoadedChunks();

        if (i != this.countLoadedChunksPrev)
        {
            this.countLoadedChunksPrev = i;
            this.displayListEntitiesDirty = true;
        }

        int j = 256;

        if (!ChunkVisibility.isFinished())
        {
            this.displayListEntitiesDirty = true;
        }

        if (!flag && this.displayListEntitiesDirty && Config.isIntegratedServerRunning())
        {
            j = ChunkVisibility.getMaxChunkY(this.theWorld, view实体, this.renderDistanceChunks);
        }

        RenderChunk renderchunk1 = this.viewFrustum.getRenderChunk(new 阻止位置(view实体.X坐标, view实体.Y坐标, view实体.Z坐标));

        if (Shaders.isShadowPass)
        {
            this.renderInfos = this.renderInfosShadow;
            this.renderInfosEntities = this.renderInfosEntitiesShadow;
            this.renderInfosTileEntities = this.renderInfosTileEntitiesShadow;

            if (!flag && this.displayListEntitiesDirty)
            {
                this.clearRenderInfos();

                if (renderchunk1 != null && renderchunk1.getPosition().getY() > j)
                {
                    this.renderInfosEntities.add(renderchunk1.getRenderInfo());
                }

                Iterator<RenderChunk> iterator = ShadowUtils.makeShadowChunkIterator(this.theWorld, partialTicks, view实体, this.renderDistanceChunks, this.viewFrustum);

                while (iterator.hasNext())
                {
                    RenderChunk renderchunk2 = (RenderChunk)iterator.next();

                    if (renderchunk2 != null && renderchunk2.getPosition().getY() <= j)
                    {
                        RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = renderchunk2.getRenderInfo();

                        if (!renderchunk2.compiledChunk.isEmpty() || renderchunk2.isNeedsUpdate())
                        {
                            this.renderInfos.add(renderglobal$containerlocalrenderinformation);
                        }

                        if (ChunkUtils.hasEntities(renderchunk2.getChunk()))
                        {
                            this.renderInfosEntities.add(renderglobal$containerlocalrenderinformation);
                        }

                        if (renderchunk2.getCompiledChunk().getTileEntities().size() > 0)
                        {
                            this.renderInfosTileEntities.add(renderglobal$containerlocalrenderinformation);
                        }
                    }
                }
            }
        }
        else
        {
            this.renderInfos = this.renderInfosNormal;
            this.renderInfosEntities = this.renderInfosEntitiesNormal;
            this.renderInfosTileEntities = this.renderInfosTileEntitiesNormal;
        }

        if (!flag && this.displayListEntitiesDirty && !Shaders.isShadowPass)
        {
            this.displayListEntitiesDirty = false;
            this.clearRenderInfos();
            this.visibilityDeque.clear();
            Deque deque = this.visibilityDeque;
            boolean flag1 = this.mc.renderChunksMany;

            if (renderchunk != null && renderchunk.getPosition().getY() <= j)
            {
                boolean flag2 = false;
                RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation4 = new RenderGlobal.ContainerLocalRenderInformation(renderchunk, (EnumFacing)null, 0);
                Set set1 = SET_ALL_FACINGS;

                if (set1.size() == 1)
                {
                    Vector3f vector3f = this.getViewVector(view实体, partialTicks);
                    EnumFacing enumfacing2 = EnumFacing.getFacingFromVector(vector3f.x, vector3f.y, vector3f.z).getOpposite();
                    set1.remove(enumfacing2);
                }

                if (set1.isEmpty())
                {
                    flag2 = true;
                }

                if (flag2 && !playerSpectator)
                {
                    this.renderInfos.add(renderglobal$containerlocalrenderinformation4);
                }
                else
                {
                    if (playerSpectator && this.theWorld.getBlockState(blockpos).getBlock().isOpaqueCube())
                    {
                        flag1 = false;
                    }

                    renderchunk.setFrameIndex(frameCount);
                    deque.add(renderglobal$containerlocalrenderinformation4);
                }
            }
            else
            {
                int j1 = blockpos.getY() > 0 ? Math.min(j, 248) : 8;

                if (renderchunk1 != null)
                {
                    this.renderInfosEntities.add(renderchunk1.getRenderInfo());
                }

                for (int k = -this.renderDistanceChunks; k <= this.renderDistanceChunks; ++k)
                {
                    for (int l = -this.renderDistanceChunks; l <= this.renderDistanceChunks; ++l)
                    {
                        RenderChunk renderchunk3 = this.viewFrustum.getRenderChunk(new 阻止位置((k << 4) + 8, j1, (l << 4) + 8));

                        if (renderchunk3 != null && renderchunk3.isBoundingBoxInFrustum((ICamera)camera, frameCount))
                        {
                            renderchunk3.setFrameIndex(frameCount);
                            RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 = renderchunk3.getRenderInfo();
                            renderglobal$containerlocalrenderinformation1.initialize((EnumFacing)null, 0);
                            deque.add(renderglobal$containerlocalrenderinformation1);
                        }
                    }
                }
            }

            this.mc.mcProfiler.startSection("iteration");
            boolean flag3 = Config.isFogOn();

            while (!deque.isEmpty())
            {
                RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation5 = (RenderGlobal.ContainerLocalRenderInformation)deque.poll();
                RenderChunk renderchunk6 = renderglobal$containerlocalrenderinformation5.renderChunk;
                EnumFacing enumfacing1 = renderglobal$containerlocalrenderinformation5.facing;
                CompiledChunk compiledchunk = renderchunk6.compiledChunk;

                if (!compiledchunk.isEmpty() || renderchunk6.isNeedsUpdate())
                {
                    this.renderInfos.add(renderglobal$containerlocalrenderinformation5);
                }

                if (ChunkUtils.hasEntities(renderchunk6.getChunk()))
                {
                    this.renderInfosEntities.add(renderglobal$containerlocalrenderinformation5);
                }

                if (compiledchunk.getTileEntities().size() > 0)
                {
                    this.renderInfosTileEntities.add(renderglobal$containerlocalrenderinformation5);
                }

                for (EnumFacing enumfacing : flag1 ? ChunkVisibility.getFacingsNotOpposite(renderglobal$containerlocalrenderinformation5.setFacing) : EnumFacing.VALUES)
                {
                    if (!flag1 || enumfacing1 == null || compiledchunk.isVisible(enumfacing1.getOpposite(), enumfacing))
                    {
                        RenderChunk renderchunk4 = this.getRenderChunkOffset(blockpos, renderchunk6, enumfacing, flag3, j);

                        if (renderchunk4 != null && renderchunk4.setFrameIndex(frameCount) && renderchunk4.isBoundingBoxInFrustum((ICamera)camera, frameCount))
                        {
                            int i1 = renderglobal$containerlocalrenderinformation5.setFacing | 1 << enumfacing.ordinal();
                            RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 = renderchunk4.getRenderInfo();
                            renderglobal$containerlocalrenderinformation2.initialize(enumfacing, i1);
                            deque.add(renderglobal$containerlocalrenderinformation2);
                        }
                    }
                }
            }

            this.mc.mcProfiler.endSection();
        }

        this.mc.mcProfiler.endStartSection("captureFrustum");

        if (this.debugFixTerrainFrustum)
        {
            this.fixTerrainFrustum(d3, d4, d5);
            this.debugFixTerrainFrustum = false;
        }

        Lagometer.timerVisibility.end();

        if (Shaders.isShadowPass)
        {
            Shaders.mcProfilerEndSection();
        }
        else
        {
            this.mc.mcProfiler.endStartSection("rebuildNear");
            this.renderDispatcher.clearChunkUpdates();
            Set<RenderChunk> set = this.chunksToUpdate;
            this.chunksToUpdate = Sets.<RenderChunk>newLinkedHashSet();
            Lagometer.timerChunkUpdate.start();

            for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 : this.renderInfos)
            {
                RenderChunk renderchunk5 = renderglobal$containerlocalrenderinformation3.renderChunk;

                if (renderchunk5.isNeedsUpdate() || set.contains(renderchunk5))
                {
                    this.displayListEntitiesDirty = true;
                    阻止位置 blockpos1 = renderchunk5.getPosition();
                    boolean flag4 = blockpos.distanceSq((double)(blockpos1.getX() + 8), (double)(blockpos1.getY() + 8), (double)(blockpos1.getZ() + 8)) < 768.0D;

                    if (!flag4)
                    {
                        this.chunksToUpdate.add(renderchunk5);
                    }
                    else if (!renderchunk5.isPlayerUpdate())
                    {
                        this.chunksToUpdateForced.add(renderchunk5);
                    }
                    else
                    {
                        this.mc.mcProfiler.startSection("build near");
                        this.renderDispatcher.updateChunkNow(renderchunk5);
                        renderchunk5.setNeedsUpdate(false);
                        this.mc.mcProfiler.endSection();
                    }
                }
            }

            Lagometer.timerChunkUpdate.end();
            this.chunksToUpdate.addAll(set);
            this.mc.mcProfiler.endSection();
        }
    }

    private boolean isPositionInRenderChunk(阻止位置 pos, RenderChunk renderChunkIn)
    {
        阻止位置 blockpos = renderChunkIn.getPosition();
        return MathHelper.abs_int(pos.getX() - blockpos.getX()) > 16 ? false : (MathHelper.abs_int(pos.getY() - blockpos.getY()) > 16 ? false : MathHelper.abs_int(pos.getZ() - blockpos.getZ()) <= 16);
    }

    private Set<EnumFacing> getVisibleFacings(阻止位置 pos)
    {
        VisGraph visgraph = new VisGraph();
        阻止位置 blockpos = new 阻止位置(pos.getX() >> 4 << 4, pos.getY() >> 4 << 4, pos.getZ() >> 4 << 4);
        Chunk chunk = this.theWorld.getChunkFromBlockCoords(blockpos);

        for (阻止位置.Mutable阻止位置 blockpos$mutableblockpos : 阻止位置.getAllInBoxMutable(blockpos, blockpos.add(15, 15, 15)))
        {
            if (chunk.getBlock(blockpos$mutableblockpos).isOpaqueCube())
            {
                visgraph.func_178606_a(blockpos$mutableblockpos);
            }
        }
        return visgraph.func_178609_b(pos);
    }

    private RenderChunk getRenderChunkOffset(阻止位置 p_getRenderChunkOffset_1_, RenderChunk p_getRenderChunkOffset_2_, EnumFacing p_getRenderChunkOffset_3_, boolean p_getRenderChunkOffset_4_, int p_getRenderChunkOffset_5_)
    {
        RenderChunk renderchunk = p_getRenderChunkOffset_2_.getRenderChunkNeighbour(p_getRenderChunkOffset_3_);

        if (renderchunk == null)
        {
            return null;
        }
        else if (renderchunk.getPosition().getY() > p_getRenderChunkOffset_5_)
        {
            return null;
        }
        else
        {
            if (p_getRenderChunkOffset_4_)
            {
                阻止位置 blockpos = renderchunk.getPosition();
                int i = p_getRenderChunkOffset_1_.getX() - blockpos.getX();
                int j = p_getRenderChunkOffset_1_.getZ() - blockpos.getZ();
                int k = i * i + j * j;

                if (k > this.renderDistanceSq)
                {
                    return null;
                }
            }

            return renderchunk;
        }
    }

    private void fixTerrainFrustum(double x, double y, double z)
    {
        this.debugFixedClippingHelper = new ClippingHelperImpl();
        ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
        Matrix4f matrix4f = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
        matrix4f.transpose();
        Matrix4f matrix4f1 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
        matrix4f1.transpose();
        Matrix4f matrix4f2 = new Matrix4f();
        Matrix4f.mul(matrix4f1, matrix4f, matrix4f2);
        matrix4f2.invert();
        this.debugTerrainFrustumPosition.x = x;
        this.debugTerrainFrustumPosition.y = y;
        this.debugTerrainFrustumPosition.z = z;
        this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
        this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
        this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < 8; ++i)
        {
            Matrix4f.transform(matrix4f2, this.debugTerrainMatrix[i], this.debugTerrainMatrix[i]);
            this.debugTerrainMatrix[i].x /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].y /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].z /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].w = 1.0F;
        }
    }

    protected Vector3f getViewVector(实体 实体In, double partialTicks)
    {
        float f = (float)((double) 实体In.prevRotationPitch + (double)(实体In.rotationPitch - 实体In.prevRotationPitch) * partialTicks);
        float f1 = (float)((double) 实体In.prevRotationYaw + (double)(实体In.旋转侧滑 - 实体In.prevRotationYaw) * partialTicks);

        if (我的手艺.得到我的手艺().游戏一窝.thirdPersonView == 2)
        {
            f += 180.0F;
        }

        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        return new Vector3f(f3 * f4, f5, f2 * f4);
    }

    public int renderBlockLayer(EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, 实体 实体In)
    {
        RenderHelper.disableStandardItemLighting();

        if (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT && !Shaders.isShadowPass)
        {
            this.mc.mcProfiler.startSection("translucent_sort");
            double d0 = 实体In.X坐标 - this.prevRenderSortX;
            double d1 = 实体In.Y坐标 - this.prevRenderSortY;
            double d2 = 实体In.Z坐标 - this.prevRenderSortZ;

            if (d0 * d0 + d1 * d1 + d2 * d2 > 1.0D)
            {
                this.prevRenderSortX = 实体In.X坐标;
                this.prevRenderSortY = 实体In.Y坐标;
                this.prevRenderSortZ = 实体In.Z坐标;
                int k = 0;
                this.chunksToResortTransparency.clear();

                for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos)
                {
                    if (renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) && k++ < 15)
                    {
                        this.chunksToResortTransparency.add(renderglobal$containerlocalrenderinformation.renderChunk);
                    }
                }
            }

            this.mc.mcProfiler.endSection();
        }

        this.mc.mcProfiler.startSection("filterempty");
        int l = 0;
        boolean flag = blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT;
        int i1 = flag ? this.renderInfos.size() - 1 : 0;
        int i = flag ? -1 : this.renderInfos.size();
        int j1 = flag ? -1 : 1;

        for (int j = i1; j != i; j += j1)
        {
            RenderChunk renderchunk = ((RenderGlobal.ContainerLocalRenderInformation)this.renderInfos.get(j)).renderChunk;

            if (!renderchunk.getCompiledChunk().isLayerEmpty(blockLayerIn))
            {
                ++l;
                this.renderContainer.addRenderChunk(renderchunk, blockLayerIn);
            }
        }

        if (l == 0)
        {
            this.mc.mcProfiler.endSection();
            return l;
        }
        else
        {
            if (Config.isFogOff() && this.mc.entityRenderer.fogStandard)
            {
                光照状态经理.disableFog();
            }

            this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
            this.renderBlockLayer(blockLayerIn);
            this.mc.mcProfiler.endSection();
            return l;
        }
    }

    @SuppressWarnings("incomplete-switch")
    private void renderBlockLayer(EnumWorldBlockLayer blockLayerIn)
    {
        this.mc.entityRenderer.enableLightmap();

        if (OpenGlHelper.useVbo())
        {
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        }

        if (Config.isShaders())
        {
            ShadersRender.preRenderChunkLayer(blockLayerIn);
        }

        this.renderContainer.renderChunkLayer(blockLayerIn);

        if (Config.isShaders())
        {
            ShadersRender.postRenderChunkLayer(blockLayerIn);
        }

        if (OpenGlHelper.useVbo())
        {
            for (VertexFormatElement vertexformatelement : DefaultVertexFormats.BLOCK.getElements())
            {
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                int i = vertexformatelement.getIndex();

                switch (vertexformatelement$enumusage)
                {
                    case POSITION:
                        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
                        break;

                    case UV:
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i);
                        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        break;

                    case COLOR:
                        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
                        光照状态经理.重设色彩();
                }
            }
        }

        this.mc.entityRenderer.disableLightmap();
    }

    private void cleanupDamagedBlocks(Iterator<DestroyBlockProgress> iteratorIn)
    {
        while (iteratorIn.hasNext())
        {
            DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)iteratorIn.next();
            int i = destroyblockprogress.getCreationCloudUpdateTick();

            if (this.cloudTickCounter - i > 400)
            {
                iteratorIn.remove();
            }
        }
    }

    public void updateClouds()
    {
        if (Config.isShaders())
        {
            if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(24))
            {
                鬼ShaderOptions guishaderoptions = new 鬼ShaderOptions((鬼Screen)null, Config.getGameSettings());
                Config.getMinecraft().displayGuiScreen(guishaderoptions);
            }

            if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(19))
            {
                Shaders.uninit();
                Shaders.loadShaderPack();
            }
        }

        ++this.cloudTickCounter;

        if (this.cloudTickCounter % 20 == 0)
        {
            this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
        }
    }

    private void renderSkyEnd()
    {
        if (Config.isSkyEnabled())
        {
            光照状态经理.disableFog();
            光照状态经理.禁用希腊字母表的第1个字母();
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            光照状态经理.depthMask(false);
            this.renderEngine.绑定手感(locationEndSkyPng);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

            for (int i = 0; i < 6; ++i)
            {
                光照状态经理.推黑客帝国();

                if (i == 1)
                {
                    光照状态经理.辐射(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 2)
                {
                    光照状态经理.辐射(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 3)
                {
                    光照状态经理.辐射(180.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 4)
                {
                    光照状态经理.辐射(90.0F, 0.0F, 0.0F, 1.0F);
                }

                if (i == 5)
                {
                    光照状态经理.辐射(-90.0F, 0.0F, 0.0F, 1.0F);
                }

                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int j = 40;
                int k = 40;
                int l = 40;

                if (Config.isCustomColors())
                {
                    Vec3 vec3 = new Vec3((double)j / 255.0D, (double)k / 255.0D, (double)l / 255.0D);
                    vec3 = CustomColors.getWorldSkyColor(vec3, this.theWorld, this.mc.getRenderViewEntity(), 0.0F);
                    j = (int)(vec3.xCoord * 255.0D);
                    k = (int)(vec3.yCoord * 255.0D);
                    l = (int)(vec3.zCoord * 255.0D);
                }

                worldrenderer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(j, k, l, 255).endVertex();
                worldrenderer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 16.0D).color(j, k, l, 255).endVertex();
                worldrenderer.pos(100.0D, -100.0D, 100.0D).tex(16.0D, 16.0D).color(j, k, l, 255).endVertex();
                worldrenderer.pos(100.0D, -100.0D, -100.0D).tex(16.0D, 0.0D).color(j, k, l, 255).endVertex();
                tessellator.draw();
                光照状态经理.流行音乐黑客帝国();
            }

            光照状态经理.depthMask(true);
            光照状态经理.启用手感();
            光照状态经理.启用希腊字母表的第1个字母();
            光照状态经理.禁用混合品();
        }
    }

    public void renderSky(float partialTicks, int pass)
    {
        if (Reflector.ForgeWorldProvider_getSkyRenderer.exists())
        {
            WorldProvider worldprovider = this.mc.宇轩の世界.provider;
            Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);

            if (object != null)
            {
                Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] {Float.valueOf(partialTicks), this.theWorld, this.mc});
                return;
            }
        }

        if (this.mc.宇轩の世界.provider.getDimensionId() == 1)
        {
            this.renderSkyEnd();
        }
        else if (this.mc.宇轩の世界.provider.isSurfaceWorld())
        {
            光照状态经理.禁用手感();
            boolean flag = Config.isShaders();

            if (flag)
            {
                Shaders.disableTexture2D();
            }

            Vec3 vec3 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
            vec3 = CustomColors.getSkyColor(vec3, this.mc.宇轩の世界, this.mc.getRenderViewEntity().X坐标, this.mc.getRenderViewEntity().Y坐标 + 1.0D, this.mc.getRenderViewEntity().Z坐标);

            if (flag)
            {
                Shaders.setSkyColor(vec3);
            }

            float f = (float)vec3.xCoord;
            float f1 = (float)vec3.yCoord;
            float f2 = (float)vec3.zCoord;

            if (pass != 2)
            {
                float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
                float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
                float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
                f = f3;
                f1 = f4;
                f2 = f5;
            }

            光照状态经理.色彩(f, f1, f2);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            光照状态经理.depthMask(false);
            光照状态经理.enableFog();

            if (flag)
            {
                Shaders.enableFog();
            }

            光照状态经理.色彩(f, f1, f2);

            if (flag)
            {
                Shaders.preSkyList();
            }

            if (Config.isSkyEnabled())
            {
                if (this.vboEnabled)
                {
                    this.skyVBO.bindBuffer();
                    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                    GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
                    this.skyVBO.drawArrays(7);
                    this.skyVBO.unbindBuffer();
                    GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
                }
                else
                {
                    光照状态经理.callList(this.glSkyList);
                }
            }

            光照状态经理.disableFog();

            if (flag)
            {
                Shaders.disableFog();
            }

            光照状态经理.禁用希腊字母表的第1个字母();
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            float[] afloat = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);

            if (afloat != null && Config.isSunMoonEnabled())
            {
                光照状态经理.禁用手感();

                if (flag)
                {
                    Shaders.disableTexture2D();
                }

                光照状态经理.shadeModel(7425);
                光照状态经理.推黑客帝国();
                光照状态经理.辐射(90.0F, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
                光照状态经理.辐射(90.0F, 0.0F, 0.0F, 1.0F);
                float f6 = afloat[0];
                float f7 = afloat[1];
                float f8 = afloat[2];

                if (pass != 2)
                {
                    float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
                    float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
                    float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
                    f6 = f9;
                    f7 = f10;
                    f8 = f11;
                }

                worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3]).endVertex();
                int j = 16;

                for (int l = 0; l <= 16; ++l)
                {
                    float f18 = (float)l * (float)Math.PI * 2.0F / 16.0F;
                    float f12 = MathHelper.sin(f18);
                    float f13 = MathHelper.cos(f18);
                    worldrenderer.pos((double)(f12 * 120.0F), (double)(f13 * 120.0F), (double)(-f13 * 40.0F * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
                }

                tessellator.draw();
                光照状态经理.流行音乐黑客帝国();
                光照状态经理.shadeModel(7424);
            }

            光照状态经理.启用手感();

            if (flag)
            {
                Shaders.enableTexture2D();
            }

            光照状态经理.tryBlendFuncSeparate(770, 1, 1, 0);
            光照状态经理.推黑客帝国();
            float f15 = 1.0F - this.theWorld.getRainStrength(partialTicks);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, f15);
            光照状态经理.辐射(-90.0F, 0.0F, 1.0F, 0.0F);
            CustomSky.renderSky(this.theWorld, this.renderEngine, partialTicks);

            if (flag)
            {
                Shaders.preCelestialRotate();
            }

            光照状态经理.辐射(this.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);

            if (flag)
            {
                Shaders.postCelestialRotate();
            }

            float f16 = 30.0F;

            if (Config.isSunTexture())
            {
                this.renderEngine.绑定手感(locationSunPng);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                worldrenderer.pos((double)(-f16), 100.0D, (double)(-f16)).tex(0.0D, 0.0D).endVertex();
                worldrenderer.pos((double)f16, 100.0D, (double)(-f16)).tex(1.0D, 0.0D).endVertex();
                worldrenderer.pos((double)f16, 100.0D, (double)f16).tex(1.0D, 1.0D).endVertex();
                worldrenderer.pos((double)(-f16), 100.0D, (double)f16).tex(0.0D, 1.0D).endVertex();
                tessellator.draw();
            }

            f16 = 20.0F;

            if (Config.isMoonTexture())
            {
                this.renderEngine.绑定手感(locationMoonPhasesPng);
                int i = this.theWorld.getMoonPhase();
                int k = i % 4;
                int i1 = i / 4 % 2;
                float f19 = (float)(k + 0) / 4.0F;
                float f21 = (float)(i1 + 0) / 2.0F;
                float f23 = (float)(k + 1) / 4.0F;
                float f14 = (float)(i1 + 1) / 2.0F;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                worldrenderer.pos((double)(-f16), -100.0D, (double)f16).tex((double)f23, (double)f14).endVertex();
                worldrenderer.pos((double)f16, -100.0D, (double)f16).tex((double)f19, (double)f14).endVertex();
                worldrenderer.pos((double)f16, -100.0D, (double)(-f16)).tex((double)f19, (double)f21).endVertex();
                worldrenderer.pos((double)(-f16), -100.0D, (double)(-f16)).tex((double)f23, (double)f21).endVertex();
                tessellator.draw();
            }

            光照状态经理.禁用手感();

            if (flag)
            {
                Shaders.disableTexture2D();
            }

            float f17 = this.theWorld.getStarBrightness(partialTicks) * f15;

            if (f17 > 0.0F && Config.isStarsEnabled() && !CustomSky.hasSkyLayers(this.theWorld))
            {
                光照状态经理.色彩(f17, f17, f17, f17);

                if (this.vboEnabled)
                {
                    this.starVBO.bindBuffer();
                    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                    GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
                    this.starVBO.drawArrays(7);
                    this.starVBO.unbindBuffer();
                    GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
                }
                else
                {
                    光照状态经理.callList(this.starGLCallList);
                }
            }

            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            光照状态经理.禁用混合品();
            光照状态经理.启用希腊字母表的第1个字母();
            光照状态经理.enableFog();

            if (flag)
            {
                Shaders.enableFog();
            }

            光照状态经理.流行音乐黑客帝国();
            光照状态经理.禁用手感();

            if (flag)
            {
                Shaders.disableTexture2D();
            }

            光照状态经理.色彩(0.0F, 0.0F, 0.0F);
            double d0 = this.mc.宇轩游玩者.getPositionEyes(partialTicks).yCoord - this.theWorld.getHorizon();

            if (d0 < 0.0D)
            {
                光照状态经理.推黑客帝国();
                光照状态经理.理解(0.0F, 12.0F, 0.0F);

                if (this.vboEnabled)
                {
                    this.sky2VBO.bindBuffer();
                    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                    GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
                    this.sky2VBO.drawArrays(7);
                    this.sky2VBO.unbindBuffer();
                    GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
                }
                else
                {
                    光照状态经理.callList(this.glSkyList2);
                }

                光照状态经理.流行音乐黑客帝国();
                float f20 = 1.0F;
                float f22 = -((float)(d0 + 65.0D));
                float f24 = -1.0F;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(-1.0D, (double)f22, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0D, (double)f22, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0D, (double)f22, -1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0D, (double)f22, -1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0D, (double)f22, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0D, (double)f22, -1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0D, (double)f22, -1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0D, (double)f22, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
            }

            if (this.theWorld.provider.isSkyColored())
            {
                光照状态经理.色彩(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
            }
            else
            {
                光照状态经理.色彩(f, f1, f2);
            }

            if (this.mc.游戏一窝.renderDistanceChunks <= 4)
            {
                光照状态经理.色彩(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue);
            }

            光照状态经理.推黑客帝国();
            光照状态经理.理解(0.0F, -((float)(d0 - 16.0D)), 0.0F);

            if (Config.isSkyEnabled())
            {
                if (this.vboEnabled)
                {
                    this.sky2VBO.bindBuffer();
                    光照状态经理.glEnableClientState(32884);
                    光照状态经理.glVertexPointer(3, 5126, 12, 0);
                    this.sky2VBO.drawArrays(7);
                    this.sky2VBO.unbindBuffer();
                    光照状态经理.glDisableClientState(32884);
                }
                else
                {
                    光照状态经理.callList(this.glSkyList2);
                }
            }

            光照状态经理.流行音乐黑客帝国();
            光照状态经理.启用手感();

            if (flag)
            {
                Shaders.enableTexture2D();
            }

            光照状态经理.depthMask(true);
        }
    }

    public void renderClouds(float partialTicks, int pass)
    {
        if (!Config.isCloudsOff())
        {
            if (Reflector.ForgeWorldProvider_getCloudRenderer.exists())
            {
                WorldProvider worldprovider = this.mc.宇轩の世界.provider;
                Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getCloudRenderer, new Object[0]);

                if (object != null)
                {
                    Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] {Float.valueOf(partialTicks), this.theWorld, this.mc});
                    return;
                }
            }

            if (this.mc.宇轩の世界.provider.isSurfaceWorld())
            {
                if (Config.isShaders())
                {
                    Shaders.beginClouds();
                }

                if (Config.isCloudsFancy())
                {
                    this.renderCloudsFancy(partialTicks, pass);
                }
                else
                {
                    float f9 = partialTicks;
                    partialTicks = 0.0F;
                    光照状态经理.disableCull();
                    float f10 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().Y坐标 - this.mc.getRenderViewEntity().lastTickPosY) * (double)partialTicks);
                    int i = 32;
                    int j = 8;
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    this.renderEngine.绑定手感(locationCloudsPng);
                    光照状态经理.启用混合品();
                    光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
                    Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
                    float f = (float)vec3.xCoord;
                    float f1 = (float)vec3.yCoord;
                    float f2 = (float)vec3.zCoord;
                    this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, f9, vec3);

                    if (this.cloudRenderer.shouldUpdateGlList())
                    {
                        this.cloudRenderer.startUpdateGlList();

                        if (pass != 2)
                        {
                            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
                            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
                            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
                            f = f3;
                            f1 = f4;
                            f2 = f5;
                        }

                        float f11 = 4.8828125E-4F;
                        double d2 = (double)((float)this.cloudTickCounter + partialTicks);
                        double d0 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().X坐标 - this.mc.getRenderViewEntity().prevPosX) * (double)partialTicks + d2 * 0.029999999329447746D;
                        double d1 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().Z坐标 - this.mc.getRenderViewEntity().prevPosZ) * (double)partialTicks;
                        int k = MathHelper.floor_double(d0 / 2048.0D);
                        int l = MathHelper.floor_double(d1 / 2048.0D);
                        d0 = d0 - (double)(k * 2048);
                        d1 = d1 - (double)(l * 2048);
                        float f6 = this.theWorld.provider.getCloudHeight() - f10 + 0.33F;
                        f6 = f6 + this.mc.游戏一窝.ofCloudsHeight * 128.0F;
                        float f7 = (float)(d0 * 4.8828125E-4D);
                        float f8 = (float)(d1 * 4.8828125E-4D);
                        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

                        for (int i1 = -256; i1 < 256; i1 += 32)
                        {
                            for (int j1 = -256; j1 < 256; j1 += 32)
                            {
                                worldrenderer.pos((double)(i1 + 0), (double)f6, (double)(j1 + 32)).tex((double)((float)(i1 + 0) * 4.8828125E-4F + f7), (double)((float)(j1 + 32) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
                                worldrenderer.pos((double)(i1 + 32), (double)f6, (double)(j1 + 32)).tex((double)((float)(i1 + 32) * 4.8828125E-4F + f7), (double)((float)(j1 + 32) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
                                worldrenderer.pos((double)(i1 + 32), (double)f6, (double)(j1 + 0)).tex((double)((float)(i1 + 32) * 4.8828125E-4F + f7), (double)((float)(j1 + 0) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
                                worldrenderer.pos((double)(i1 + 0), (double)f6, (double)(j1 + 0)).tex((double)((float)(i1 + 0) * 4.8828125E-4F + f7), (double)((float)(j1 + 0) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
                            }
                        }

                        tessellator.draw();
                        this.cloudRenderer.endUpdateGlList();
                    }

                    this.cloudRenderer.renderGlList();
                    光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
                    光照状态经理.禁用混合品();
                    光照状态经理.enableCull();
                }

                if (Config.isShaders())
                {
                    Shaders.endClouds();
                }
            }
        }
    }

    public boolean hasCloudFog(double x, double y, double z, float partialTicks)
    {
        return false;
    }

    private void renderCloudsFancy(float partialTicks, int pass)
    {
        partialTicks = 0.0F;
        光照状态经理.disableCull();
        float f = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().Y坐标 - this.mc.getRenderViewEntity().lastTickPosY) * (double)partialTicks);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f1 = 12.0F;
        float f2 = 4.0F;
        double d0 = (double)((float)this.cloudTickCounter + partialTicks);
        double d1 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().X坐标 - this.mc.getRenderViewEntity().prevPosX) * (double)partialTicks + d0 * 0.029999999329447746D) / 12.0D;
        double d2 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().Z坐标 - this.mc.getRenderViewEntity().prevPosZ) * (double)partialTicks) / 12.0D + 0.33000001311302185D;
        float f3 = this.theWorld.provider.getCloudHeight() - f + 0.33F;
        f3 = f3 + this.mc.游戏一窝.ofCloudsHeight * 128.0F;
        int i = MathHelper.floor_double(d1 / 2048.0D);
        int j = MathHelper.floor_double(d2 / 2048.0D);
        d1 = d1 - (double)(i * 2048);
        d2 = d2 - (double)(j * 2048);
        this.renderEngine.绑定手感(locationCloudsPng);
        光照状态经理.启用混合品();
        光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
        Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
        float f4 = (float)vec3.xCoord;
        float f5 = (float)vec3.yCoord;
        float f6 = (float)vec3.zCoord;
        this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, partialTicks, vec3);

        if (pass != 2)
        {
            float f7 = (f4 * 30.0F + f5 * 59.0F + f6 * 11.0F) / 100.0F;
            float f8 = (f4 * 30.0F + f5 * 70.0F) / 100.0F;
            float f9 = (f4 * 30.0F + f6 * 70.0F) / 100.0F;
            f4 = f7;
            f5 = f8;
            f6 = f9;
        }

        float f26 = f4 * 0.9F;
        float f27 = f5 * 0.9F;
        float f28 = f6 * 0.9F;
        float f10 = f4 * 0.7F;
        float f11 = f5 * 0.7F;
        float f12 = f6 * 0.7F;
        float f13 = f4 * 0.8F;
        float f14 = f5 * 0.8F;
        float f15 = f6 * 0.8F;
        float f16 = 0.00390625F;
        float f17 = (float)MathHelper.floor_double(d1) * 0.00390625F;
        float f18 = (float)MathHelper.floor_double(d2) * 0.00390625F;
        float f19 = (float)(d1 - (double)MathHelper.floor_double(d1));
        float f20 = (float)(d2 - (double)MathHelper.floor_double(d2));
        int k = 8;
        int l = 4;
        float f21 = 9.765625E-4F;
        光照状态经理.障眼物(12.0F, 1.0F, 12.0F);

        for (int i1 = 0; i1 < 2; ++i1)
        {
            if (i1 == 0)
            {
                光照状态经理.colorMask(false, false, false, false);
            }
            else
            {
                switch (pass)
                {
                    case 0:
                        光照状态经理.colorMask(false, true, true, true);
                        break;

                    case 1:
                        光照状态经理.colorMask(true, false, false, true);
                        break;

                    case 2:
                        光照状态经理.colorMask(true, true, true, true);
                }
            }

            this.cloudRenderer.renderGlList();
        }

        if (this.cloudRenderer.shouldUpdateGlList())
        {
            this.cloudRenderer.startUpdateGlList();

            for (int l1 = -3; l1 <= 4; ++l1)
            {
                for (int j1 = -3; j1 <= 4; ++j1)
                {
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                    float f22 = (float)(l1 * 8);
                    float f23 = (float)(j1 * 8);
                    float f24 = f22 - f19;
                    float f25 = f23 - f20;

                    if (f3 > -5.0F)
                    {
                        worldrenderer.pos((double)(f24 + 0.0F), (double)(f3 + 0.0F), (double)(f25 + 8.0F)).tex((double)((f22 + 0.0F) * 0.00390625F + f17), (double)((f23 + 8.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        worldrenderer.pos((double)(f24 + 8.0F), (double)(f3 + 0.0F), (double)(f25 + 8.0F)).tex((double)((f22 + 8.0F) * 0.00390625F + f17), (double)((f23 + 8.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        worldrenderer.pos((double)(f24 + 8.0F), (double)(f3 + 0.0F), (double)(f25 + 0.0F)).tex((double)((f22 + 8.0F) * 0.00390625F + f17), (double)((f23 + 0.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        worldrenderer.pos((double)(f24 + 0.0F), (double)(f3 + 0.0F), (double)(f25 + 0.0F)).tex((double)((f22 + 0.0F) * 0.00390625F + f17), (double)((f23 + 0.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    }

                    if (f3 <= 5.0F)
                    {
                        worldrenderer.pos((double)(f24 + 0.0F), (double)(f3 + 4.0F - 9.765625E-4F), (double)(f25 + 8.0F)).tex((double)((f22 + 0.0F) * 0.00390625F + f17), (double)((f23 + 8.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        worldrenderer.pos((double)(f24 + 8.0F), (double)(f3 + 4.0F - 9.765625E-4F), (double)(f25 + 8.0F)).tex((double)((f22 + 8.0F) * 0.00390625F + f17), (double)((f23 + 8.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        worldrenderer.pos((double)(f24 + 8.0F), (double)(f3 + 4.0F - 9.765625E-4F), (double)(f25 + 0.0F)).tex((double)((f22 + 8.0F) * 0.00390625F + f17), (double)((f23 + 0.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        worldrenderer.pos((double)(f24 + 0.0F), (double)(f3 + 4.0F - 9.765625E-4F), (double)(f25 + 0.0F)).tex((double)((f22 + 0.0F) * 0.00390625F + f17), (double)((f23 + 0.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                    }

                    if (l1 > -1)
                    {
                        for (int k1 = 0; k1 < 8; ++k1)
                        {
                            worldrenderer.pos((double)(f24 + (float)k1 + 0.0F), (double)(f3 + 0.0F), (double)(f25 + 8.0F)).tex((double)((f22 + (float)k1 + 0.5F) * 0.00390625F + f17), (double)((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            worldrenderer.pos((double)(f24 + (float)k1 + 0.0F), (double)(f3 + 4.0F), (double)(f25 + 8.0F)).tex((double)((f22 + (float)k1 + 0.5F) * 0.00390625F + f17), (double)((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            worldrenderer.pos((double)(f24 + (float)k1 + 0.0F), (double)(f3 + 4.0F), (double)(f25 + 0.0F)).tex((double)((f22 + (float)k1 + 0.5F) * 0.00390625F + f17), (double)((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            worldrenderer.pos((double)(f24 + (float)k1 + 0.0F), (double)(f3 + 0.0F), (double)(f25 + 0.0F)).tex((double)((f22 + (float)k1 + 0.5F) * 0.00390625F + f17), (double)((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                        }
                    }

                    if (l1 <= 1)
                    {
                        for (int i2 = 0; i2 < 8; ++i2)
                        {
                            worldrenderer.pos((double)(f24 + (float)i2 + 1.0F - 9.765625E-4F), (double)(f3 + 0.0F), (double)(f25 + 8.0F)).tex((double)((f22 + (float)i2 + 0.5F) * 0.00390625F + f17), (double)((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            worldrenderer.pos((double)(f24 + (float)i2 + 1.0F - 9.765625E-4F), (double)(f3 + 4.0F), (double)(f25 + 8.0F)).tex((double)((f22 + (float)i2 + 0.5F) * 0.00390625F + f17), (double)((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            worldrenderer.pos((double)(f24 + (float)i2 + 1.0F - 9.765625E-4F), (double)(f3 + 4.0F), (double)(f25 + 0.0F)).tex((double)((f22 + (float)i2 + 0.5F) * 0.00390625F + f17), (double)((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            worldrenderer.pos((double)(f24 + (float)i2 + 1.0F - 9.765625E-4F), (double)(f3 + 0.0F), (double)(f25 + 0.0F)).tex((double)((f22 + (float)i2 + 0.5F) * 0.00390625F + f17), (double)((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                        }
                    }

                    if (j1 > -1)
                    {
                        for (int j2 = 0; j2 < 8; ++j2)
                        {
                            worldrenderer.pos((double)(f24 + 0.0F), (double)(f3 + 4.0F), (double)(f25 + (float)j2 + 0.0F)).tex((double)((f22 + 0.0F) * 0.00390625F + f17), (double)((f23 + (float)j2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            worldrenderer.pos((double)(f24 + 8.0F), (double)(f3 + 4.0F), (double)(f25 + (float)j2 + 0.0F)).tex((double)((f22 + 8.0F) * 0.00390625F + f17), (double)((f23 + (float)j2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            worldrenderer.pos((double)(f24 + 8.0F), (double)(f3 + 0.0F), (double)(f25 + (float)j2 + 0.0F)).tex((double)((f22 + 8.0F) * 0.00390625F + f17), (double)((f23 + (float)j2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            worldrenderer.pos((double)(f24 + 0.0F), (double)(f3 + 0.0F), (double)(f25 + (float)j2 + 0.0F)).tex((double)((f22 + 0.0F) * 0.00390625F + f17), (double)((f23 + (float)j2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                        }
                    }

                    if (j1 <= 1)
                    {
                        for (int k2 = 0; k2 < 8; ++k2)
                        {
                            worldrenderer.pos((double)(f24 + 0.0F), (double)(f3 + 4.0F), (double)(f25 + (float)k2 + 1.0F - 9.765625E-4F)).tex((double)((f22 + 0.0F) * 0.00390625F + f17), (double)((f23 + (float)k2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            worldrenderer.pos((double)(f24 + 8.0F), (double)(f3 + 4.0F), (double)(f25 + (float)k2 + 1.0F - 9.765625E-4F)).tex((double)((f22 + 8.0F) * 0.00390625F + f17), (double)((f23 + (float)k2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            worldrenderer.pos((double)(f24 + 8.0F), (double)(f3 + 0.0F), (double)(f25 + (float)k2 + 1.0F - 9.765625E-4F)).tex((double)((f22 + 8.0F) * 0.00390625F + f17), (double)((f23 + (float)k2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            worldrenderer.pos((double)(f24 + 0.0F), (double)(f3 + 0.0F), (double)(f25 + (float)k2 + 1.0F - 9.765625E-4F)).tex((double)((f22 + 0.0F) * 0.00390625F + f17), (double)((f23 + (float)k2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                        }
                    }

                    tessellator.draw();
                }
            }

            this.cloudRenderer.endUpdateGlList();
        }

        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.禁用混合品();
        光照状态经理.enableCull();
    }

    public void updateChunks(long finishTimeNano)
    {
        finishTimeNano = (long)((double)finishTimeNano + 1.0E8D);
        this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano);

        if (this.chunksToUpdateForced.size() > 0)
        {
            Iterator iterator = this.chunksToUpdateForced.iterator();

            while (iterator.hasNext())
            {
                RenderChunk renderchunk = (RenderChunk)iterator.next();

                if (!this.renderDispatcher.updateChunkLater(renderchunk))
                {
                    break;
                }

                renderchunk.setNeedsUpdate(false);
                iterator.remove();
                this.chunksToUpdate.remove(renderchunk);
                this.chunksToResortTransparency.remove(renderchunk);
            }
        }

        if (this.chunksToResortTransparency.size() > 0)
        {
            Iterator iterator2 = this.chunksToResortTransparency.iterator();

            if (iterator2.hasNext())
            {
                RenderChunk renderchunk2 = (RenderChunk)iterator2.next();

                if (this.renderDispatcher.updateTransparencyLater(renderchunk2))
                {
                    iterator2.remove();
                }
            }
        }

        double d1 = 0.0D;
        int i = Config.getUpdatesPerFrame();

        if (!this.chunksToUpdate.isEmpty())
        {
            Iterator<RenderChunk> iterator1 = this.chunksToUpdate.iterator();

            while (iterator1.hasNext())
            {
                RenderChunk renderchunk1 = (RenderChunk)iterator1.next();
                boolean flag = renderchunk1.isChunkRegionEmpty();
                boolean flag1;

                if (flag)
                {
                    flag1 = this.renderDispatcher.updateChunkNow(renderchunk1);
                }
                else
                {
                    flag1 = this.renderDispatcher.updateChunkLater(renderchunk1);
                }

                if (!flag1)
                {
                    break;
                }

                renderchunk1.setNeedsUpdate(false);
                iterator1.remove();

                if (!flag)
                {
                    double d0 = 2.0D * RenderChunkUtils.getRelativeBufferSize(renderchunk1);
                    d1 += d0;

                    if (d1 > (double)i)
                    {
                        break;
                    }
                }
            }
        }
    }

    public void renderWorldBorder(实体 实体In, float partialTicks)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        WorldBorder worldborder = this.theWorld.getWorldBorder();
        double d0 = (double)(this.mc.游戏一窝.renderDistanceChunks * 16);

        if (实体In.X坐标 >= worldborder.maxX() - d0 || 实体In.X坐标 <= worldborder.minX() + d0 || 实体In.Z坐标 >= worldborder.maxZ() - d0 || 实体In.Z坐标 <= worldborder.minZ() + d0)
        {
            if (Config.isShaders())
            {
                Shaders.pushProgram();
                Shaders.useProgram(Shaders.ProgramTexturedLit);
            }

            double d1 = 1.0D - worldborder.getClosestDistance(实体In) / d0;
            d1 = Math.pow(d1, 4.0D);
            double d2 = 实体In.lastTickPosX + (实体In.X坐标 - 实体In.lastTickPosX) * (double)partialTicks;
            double d3 = 实体In.lastTickPosY + (实体In.Y坐标 - 实体In.lastTickPosY) * (double)partialTicks;
            double d4 = 实体In.lastTickPosZ + (实体In.Z坐标 - 实体In.lastTickPosZ) * (double)partialTicks;
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 1, 1, 0);
            this.renderEngine.绑定手感(locationForcefieldPng);
            光照状态经理.depthMask(false);
            光照状态经理.推黑客帝国();
            int i = worldborder.getStatus().getID();
            float f = (float)(i >> 16 & 255) / 255.0F;
            float f1 = (float)(i >> 8 & 255) / 255.0F;
            float f2 = (float)(i & 255) / 255.0F;
            光照状态经理.色彩(f, f1, f2, (float)d1);
            光照状态经理.doPolygonOffset(-3.0F, -3.0F);
            光照状态经理.enablePolygonOffset();
            光照状态经理.alphaFunc(516, 0.1F);
            光照状态经理.启用希腊字母表的第1个字母();
            光照状态经理.disableCull();
            float f3 = (float)(我的手艺.getSystemTime() % 3000L) / 3000.0F;
            float f4 = 0.0F;
            float f5 = 0.0F;
            float f6 = 128.0F;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.setTranslation(-d2, -d3, -d4);
            double d5 = Math.max((double)MathHelper.floor_double(d4 - d0), worldborder.minZ());
            double d6 = Math.min((double)MathHelper.ceiling_double_int(d4 + d0), worldborder.maxZ());

            if (d2 > worldborder.maxX() - d0)
            {
                float f7 = 0.0F;

                for (double d7 = d5; d7 < d6; f7 += 0.5F)
                {
                    double d8 = Math.min(1.0D, d6 - d7);
                    float f8 = (float)d8 * 0.5F;
                    worldrenderer.pos(worldborder.maxX(), 256.0D, d7).tex((double)(f3 + f7), (double)(f3 + 0.0F)).endVertex();
                    worldrenderer.pos(worldborder.maxX(), 256.0D, d7 + d8).tex((double)(f3 + f8 + f7), (double)(f3 + 0.0F)).endVertex();
                    worldrenderer.pos(worldborder.maxX(), 0.0D, d7 + d8).tex((double)(f3 + f8 + f7), (double)(f3 + 128.0F)).endVertex();
                    worldrenderer.pos(worldborder.maxX(), 0.0D, d7).tex((double)(f3 + f7), (double)(f3 + 128.0F)).endVertex();
                    ++d7;
                }
            }

            if (d2 < worldborder.minX() + d0)
            {
                float f9 = 0.0F;

                for (double d9 = d5; d9 < d6; f9 += 0.5F)
                {
                    double d12 = Math.min(1.0D, d6 - d9);
                    float f12 = (float)d12 * 0.5F;
                    worldrenderer.pos(worldborder.minX(), 256.0D, d9).tex((double)(f3 + f9), (double)(f3 + 0.0F)).endVertex();
                    worldrenderer.pos(worldborder.minX(), 256.0D, d9 + d12).tex((double)(f3 + f12 + f9), (double)(f3 + 0.0F)).endVertex();
                    worldrenderer.pos(worldborder.minX(), 0.0D, d9 + d12).tex((double)(f3 + f12 + f9), (double)(f3 + 128.0F)).endVertex();
                    worldrenderer.pos(worldborder.minX(), 0.0D, d9).tex((double)(f3 + f9), (double)(f3 + 128.0F)).endVertex();
                    ++d9;
                }
            }

            d5 = Math.max((double)MathHelper.floor_double(d2 - d0), worldborder.minX());
            d6 = Math.min((double)MathHelper.ceiling_double_int(d2 + d0), worldborder.maxX());

            if (d4 > worldborder.maxZ() - d0)
            {
                float f10 = 0.0F;

                for (double d10 = d5; d10 < d6; f10 += 0.5F)
                {
                    double d13 = Math.min(1.0D, d6 - d10);
                    float f13 = (float)d13 * 0.5F;
                    worldrenderer.pos(d10, 256.0D, worldborder.maxZ()).tex((double)(f3 + f10), (double)(f3 + 0.0F)).endVertex();
                    worldrenderer.pos(d10 + d13, 256.0D, worldborder.maxZ()).tex((double)(f3 + f13 + f10), (double)(f3 + 0.0F)).endVertex();
                    worldrenderer.pos(d10 + d13, 0.0D, worldborder.maxZ()).tex((double)(f3 + f13 + f10), (double)(f3 + 128.0F)).endVertex();
                    worldrenderer.pos(d10, 0.0D, worldborder.maxZ()).tex((double)(f3 + f10), (double)(f3 + 128.0F)).endVertex();
                    ++d10;
                }
            }

            if (d4 < worldborder.minZ() + d0)
            {
                float f11 = 0.0F;

                for (double d11 = d5; d11 < d6; f11 += 0.5F)
                {
                    double d14 = Math.min(1.0D, d6 - d11);
                    float f14 = (float)d14 * 0.5F;
                    worldrenderer.pos(d11, 256.0D, worldborder.minZ()).tex((double)(f3 + f11), (double)(f3 + 0.0F)).endVertex();
                    worldrenderer.pos(d11 + d14, 256.0D, worldborder.minZ()).tex((double)(f3 + f14 + f11), (double)(f3 + 0.0F)).endVertex();
                    worldrenderer.pos(d11 + d14, 0.0D, worldborder.minZ()).tex((double)(f3 + f14 + f11), (double)(f3 + 128.0F)).endVertex();
                    worldrenderer.pos(d11, 0.0D, worldborder.minZ()).tex((double)(f3 + f11), (double)(f3 + 128.0F)).endVertex();
                    ++d11;
                }
            }

            tessellator.draw();
            worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
            光照状态经理.enableCull();
            光照状态经理.禁用希腊字母表的第1个字母();
            光照状态经理.doPolygonOffset(0.0F, 0.0F);
            光照状态经理.disablePolygonOffset();
            光照状态经理.启用希腊字母表的第1个字母();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            光照状态经理.禁用混合品();
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.depthMask(true);

            if (Config.isShaders())
            {
                Shaders.popProgram();
            }
        }
    }

    private void preRenderDamagedBlocks()
    {
        光照状态经理.tryBlendFuncSeparate(774, 768, 1, 0);
        光照状态经理.启用混合品();
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 0.5F);
        光照状态经理.doPolygonOffset(-1.0F, -10.0F);
        光照状态经理.enablePolygonOffset();
        光照状态经理.alphaFunc(516, 0.1F);
        光照状态经理.启用希腊字母表的第1个字母();
        光照状态经理.推黑客帝国();

        if (Config.isShaders())
        {
            ShadersRender.beginBlockDamage();
        }
    }

    private void postRenderDamagedBlocks()
    {
        光照状态经理.禁用希腊字母表的第1个字母();
        光照状态经理.doPolygonOffset(0.0F, 0.0F);
        光照状态经理.disablePolygonOffset();
        光照状态经理.启用希腊字母表的第1个字母();
        光照状态经理.depthMask(true);
        光照状态经理.流行音乐黑客帝国();

        if (Config.isShaders())
        {
            ShadersRender.endBlockDamage();
        }
    }

    public void drawBlockDamageTexture(Tessellator tessellatorIn, WorldRenderer worldRendererIn, 实体 实体In, float partialTicks)
    {
        double d0 = 实体In.lastTickPosX + (实体In.X坐标 - 实体In.lastTickPosX) * (double)partialTicks;
        double d1 = 实体In.lastTickPosY + (实体In.Y坐标 - 实体In.lastTickPosY) * (double)partialTicks;
        double d2 = 实体In.lastTickPosZ + (实体In.Z坐标 - 实体In.lastTickPosZ) * (double)partialTicks;

        if (!this.damagedBlocks.isEmpty())
        {
            this.renderEngine.绑定手感(TextureMap.locationBlocksTexture);
            this.preRenderDamagedBlocks();
            worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
            worldRendererIn.setTranslation(-d0, -d1, -d2);
            worldRendererIn.noColor();
            Iterator<DestroyBlockProgress> iterator = this.damagedBlocks.values().iterator();

            while (iterator.hasNext())
            {
                DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)iterator.next();
                阻止位置 blockpos = destroyblockprogress.getPosition();
                double d3 = (double)blockpos.getX() - d0;
                double d4 = (double)blockpos.getY() - d1;
                double d5 = (double)blockpos.getZ() - d2;
                Block block = this.theWorld.getBlockState(blockpos).getBlock();
                boolean flag;

                if (Reflector.ForgeTileEntity_canRenderBreaking.exists())
                {
                    boolean flag1 = block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull;

                    if (!flag1)
                    {
                        TileEntity tileentity = this.theWorld.getTileEntity(blockpos);

                        if (tileentity != null)
                        {
                            flag1 = Reflector.callBoolean(tileentity, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]);
                        }
                    }

                    flag = !flag1;
                }
                else
                {
                    flag = !(block instanceof BlockChest) && !(block instanceof BlockEnderChest) && !(block instanceof BlockSign) && !(block instanceof BlockSkull);
                }

                if (flag)
                {
                    if (d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D)
                    {
                        iterator.remove();
                    }
                    else
                    {
                        IBlockState iblockstate = this.theWorld.getBlockState(blockpos);

                        if (iblockstate.getBlock().getMaterial() != Material.air)
                        {
                            int i = destroyblockprogress.getPartialBlockDamage();
                            TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];
                            BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
                            blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, this.theWorld);
                        }
                    }
                }
            }

            tessellatorIn.draw();
            worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
            this.postRenderDamagedBlocks();
        }
    }

    public void drawSelectionBox(实体Player player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks)
    {
        if (execute == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(770, 771, 1, 0);
            光照状态经理.色彩(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            光照状态经理.禁用手感();

            if (Config.isShaders())
            {
                Shaders.disableTexture2D();
            }

            光照状态经理.depthMask(false);
            float f = 0.002F;
            阻止位置 blockpos = movingObjectPositionIn.getBlockPos();
            Block block = this.theWorld.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(blockpos))
            {
                block.setBlockBoundsBasedOnState(this.theWorld, blockpos);
                double d0 = player.lastTickPosX + (player.X坐标 - player.lastTickPosX) * (double)partialTicks;
                double d1 = player.lastTickPosY + (player.Y坐标 - player.lastTickPosY) * (double)partialTicks;
                double d2 = player.lastTickPosZ + (player.Z坐标 - player.lastTickPosZ) * (double)partialTicks;
                AxisAlignedBB axisalignedbb = block.getSelectedBoundingBox(this.theWorld, blockpos);
                Block.EnumOffsetType block$enumoffsettype = block.getOffsetType();

                if (block$enumoffsettype != Block.EnumOffsetType.NONE)
                {
                    axisalignedbb = BlockModelUtils.getOffsetBoundingBox(axisalignedbb, block$enumoffsettype, blockpos);
                }

                drawSelectionBoundingBox(axisalignedbb.expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2));
            }

            光照状态经理.depthMask(true);
            光照状态经理.启用手感();

            if (Config.isShaders())
            {
                Shaders.enableTexture2D();
            }

            光照状态经理.禁用混合品();
        }
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB boundingBox, int red, int green, int blue, int alpha)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }

    private void markBlocksForUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        this.viewFrustum.markBlocksForUpdate(x1, y1, z1, x2, y2, z2);
    }

    public void markBlockForUpdate(阻止位置 pos)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }

    public void notifyLightSet(阻止位置 pos)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }

    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        this.markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
    }

    public void playRecord(String recordName, 阻止位置 阻止位置In)
    {
        ISound isound = (ISound)this.mapSoundPositions.get(阻止位置In);

        if (isound != null)
        {
            this.mc.getSoundHandler().stopSound(isound);
            this.mapSoundPositions.remove(阻止位置In);
        }

        if (recordName != null)
        {
            ItemRecord itemrecord = ItemRecord.getRecord(recordName);

            if (itemrecord != null)
            {
                this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordNameLocal());
            }

            PositionedSoundRecord positionedsoundrecord = PositionedSoundRecord.create(new 图像位置(recordName), (float) 阻止位置In.getX(), (float) 阻止位置In.getY(), (float) 阻止位置In.getZ());
            this.mapSoundPositions.put(阻止位置In, positionedsoundrecord);
            this.mc.getSoundHandler().playSound(positionedsoundrecord);
        }
    }

    public void playSound(String soundName, double x, double y, double z, float volume, float pitch)
    {
    }

    public void playSoundToNearExcept(实体Player except, String soundName, double x, double y, double z, float volume, float pitch)
    {
    }

    public void spawnParticle(int particleID, boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, double xOffset, double yOffset, double zOffset, int... parameters)
    {
        try
        {
            this.spawnEntityFX(particleID, ignoreRange, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, parameters);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
            crashreportcategory.addCrashSection("ID", Integer.valueOf(particleID));

            if (parameters != null)
            {
                crashreportcategory.addCrashSection("Parameters", parameters);
            }

            crashreportcategory.addCrashSectionCallable("Position", new Callable<String>()
            {
                public String call() throws Exception
                {
                    return CrashReportCategory.getCoordinateInfo(xCoord, yCoord, zCoord);
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    private void spawnParticle(EnumParticleTypes particleIn, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... parameters)
    {
        this.spawnParticle(particleIn.getParticleID(), particleIn.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, parameters);
    }

    private 实体FX spawnEntityFX(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... parameters)
    {
        if (this.mc != null && this.mc.getRenderViewEntity() != null && this.mc.effectRenderer != null)
        {
            int i = this.mc.游戏一窝.particleSetting;

            if (i == 1 && this.theWorld.rand.nextInt(3) == 0)
            {
                i = 2;
            }

            double d0 = this.mc.getRenderViewEntity().X坐标 - xCoord;
            double d1 = this.mc.getRenderViewEntity().Y坐标 - yCoord;
            double d2 = this.mc.getRenderViewEntity().Z坐标 - zCoord;

            if (particleID == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.PORTAL.getParticleID() && !Config.isPortalParticles())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava())
            {
                return null;
            }
            else if (particleID == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles())
            {
                return null;
            }
            else
            {
                if (!ignoreRange)
                {
                    double d3 = 256.0D;

                    if (particleID == EnumParticleTypes.CRIT.getParticleID())
                    {
                        d3 = 38416.0D;
                    }

                    if (d0 * d0 + d1 * d1 + d2 * d2 > d3)
                    {
                        return null;
                    }

                    if (i > 1)
                    {
                        return null;
                    }
                }

                实体FX entityfx = this.mc.effectRenderer.spawnEffectParticle(particleID, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, parameters);

                if (particleID == EnumParticleTypes.WATER_BUBBLE.getParticleID())
                {
                    CustomColors.updateWaterFX(entityfx, this.theWorld, xCoord, yCoord, zCoord, this.renderEnv);
                }

                if (particleID == EnumParticleTypes.WATER_SPLASH.getParticleID())
                {
                    CustomColors.updateWaterFX(entityfx, this.theWorld, xCoord, yCoord, zCoord, this.renderEnv);
                }

                if (particleID == EnumParticleTypes.WATER_DROP.getParticleID())
                {
                    CustomColors.updateWaterFX(entityfx, this.theWorld, xCoord, yCoord, zCoord, this.renderEnv);
                }

                if (particleID == EnumParticleTypes.TOWN_AURA.getParticleID())
                {
                    CustomColors.updateMyceliumFX(entityfx);
                }

                if (particleID == EnumParticleTypes.PORTAL.getParticleID())
                {
                    CustomColors.updatePortalFX(entityfx);
                }

                if (particleID == EnumParticleTypes.REDSTONE.getParticleID())
                {
                    CustomColors.updateReddustFX(entityfx, this.theWorld, xCoord, yCoord, zCoord);
                }

                return entityfx;
            }
        }
        else
        {
            return null;
        }
    }

    public void onEntityAdded(实体 实体In)
    {
        RandomEntities.entityLoaded(实体In, this.theWorld);

        if (Config.isDynamicLights())
        {
            DynamicLights.entityAdded(实体In, this);
        }
    }

    public void onEntityRemoved(实体 实体In)
    {
        RandomEntities.entityUnloaded(实体In, this.theWorld);

        if (Config.isDynamicLights())
        {
            DynamicLights.entityRemoved(实体In, this);
        }
    }

    public void deleteAllDisplayLists()
    {
    }

    public void broadcastSound(int soundID, 阻止位置 pos, int data)
    {
        switch (soundID)
        {
            case 1013:
            case 1018:
                if (this.mc.getRenderViewEntity() != null)
                {
                    double d0 = (double)pos.getX() - this.mc.getRenderViewEntity().X坐标;
                    double d1 = (double)pos.getY() - this.mc.getRenderViewEntity().Y坐标;
                    double d2 = (double)pos.getZ() - this.mc.getRenderViewEntity().Z坐标;
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    double d4 = this.mc.getRenderViewEntity().X坐标;
                    double d5 = this.mc.getRenderViewEntity().Y坐标;
                    double d6 = this.mc.getRenderViewEntity().Z坐标;

                    if (d3 > 0.0D)
                    {
                        d4 += d0 / d3 * 2.0D;
                        d5 += d1 / d3 * 2.0D;
                        d6 += d2 / d3 * 2.0D;
                    }

                    if (soundID == 1013)
                    {
                        this.theWorld.playSound(d4, d5, d6, "mob.wither.spawn", 1.0F, 1.0F, false);
                    }
                    else
                    {
                        this.theWorld.playSound(d4, d5, d6, "mob.enderdragon.end", 5.0F, 1.0F, false);
                    }
                }

            default:
        }
    }

    public void playAuxSFX(实体Player player, int sfxType, 阻止位置 阻止位置In, int data)
    {
        Random random = this.theWorld.rand;

        switch (sfxType)
        {
            case 1000:
                this.theWorld.playSoundAtPos(阻止位置In, "random.click", 1.0F, 1.0F, false);
                break;

            case 1001:
                this.theWorld.playSoundAtPos(阻止位置In, "random.click", 1.0F, 1.2F, false);
                break;

            case 1002:
                this.theWorld.playSoundAtPos(阻止位置In, "random.bow", 1.0F, 1.2F, false);
                break;

            case 1003:
                this.theWorld.playSoundAtPos(阻止位置In, "random.door_open", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1004:
                this.theWorld.playSoundAtPos(阻止位置In, "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
                break;

            case 1005:
                if (Item.getItemById(data) instanceof ItemRecord)
                {
                    this.theWorld.playRecord(阻止位置In, "records." + ((ItemRecord)Item.getItemById(data)).recordName);
                }
                else
                {
                    this.theWorld.playRecord(阻止位置In, (String)null);
                }

                break;

            case 1006:
                this.theWorld.playSoundAtPos(阻止位置In, "random.door_close", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1007:
                this.theWorld.playSoundAtPos(阻止位置In, "mob.ghast.charge", 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1008:
                this.theWorld.playSoundAtPos(阻止位置In, "mob.ghast.fireball", 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1009:
                this.theWorld.playSoundAtPos(阻止位置In, "mob.ghast.fireball", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1010:
                this.theWorld.playSoundAtPos(阻止位置In, "mob.zombie.wood", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1011:
                this.theWorld.playSoundAtPos(阻止位置In, "mob.zombie.metal", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1012:
                this.theWorld.playSoundAtPos(阻止位置In, "mob.zombie.woodbreak", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1014:
                this.theWorld.playSoundAtPos(阻止位置In, "mob.wither.shoot", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1015:
                this.theWorld.playSoundAtPos(阻止位置In, "mob.bat.takeoff", 0.05F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1016:
                this.theWorld.playSoundAtPos(阻止位置In, "mob.zombie.infect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1017:
                this.theWorld.playSoundAtPos(阻止位置In, "mob.zombie.unfect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1020:
                this.theWorld.playSoundAtPos(阻止位置In, "random.anvil_break", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1021:
                this.theWorld.playSoundAtPos(阻止位置In, "random.anvil_use", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1022:
                this.theWorld.playSoundAtPos(阻止位置In, "random.anvil_land", 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 2000:
                int i = data % 3 - 1;
                int j = data / 3 % 3 - 1;
                double d0 = (double) 阻止位置In.getX() + (double)i * 0.6D + 0.5D;
                double d1 = (double) 阻止位置In.getY() + 0.5D;
                double d2 = (double) 阻止位置In.getZ() + (double)j * 0.6D + 0.5D;

                for (int i1 = 0; i1 < 10; ++i1)
                {
                    double d15 = random.nextDouble() * 0.2D + 0.01D;
                    double d16 = d0 + (double)i * 0.01D + (random.nextDouble() - 0.5D) * (double)j * 0.5D;
                    double d17 = d1 + (random.nextDouble() - 0.5D) * 0.5D;
                    double d18 = d2 + (double)j * 0.01D + (random.nextDouble() - 0.5D) * (double)i * 0.5D;
                    double d19 = (double)i * d15 + random.nextGaussian() * 0.01D;
                    double d20 = -0.03D + random.nextGaussian() * 0.01D;
                    double d21 = (double)j * d15 + random.nextGaussian() * 0.01D;
                    this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d16, d17, d18, d19, d20, d21, new int[0]);
                }

                return;

            case 2001:
                Block block = Block.getBlockById(data & 4095);

                if (block.getMaterial() != Material.air)
                {
                    this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new 图像位置(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getFrequency() * 0.8F, (float) 阻止位置In.getX() + 0.5F, (float) 阻止位置In.getY() + 0.5F, (float) 阻止位置In.getZ() + 0.5F));
                }

                this.mc.effectRenderer.addBlockDestroyEffects(阻止位置In, block.getStateFromMeta(data >> 12 & 255));
                break;

            case 2002:
                double d3 = (double) 阻止位置In.getX();
                double d4 = (double) 阻止位置In.getY();
                double d5 = (double) 阻止位置In.getZ();

                for (int k = 0; k < 8; ++k)
                {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d3, d4, d5, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] {Item.getIdFromItem(Items.potionitem), data});
                }

                int j1 = Items.potionitem.getColorFromDamage(data);
                float f = (float)(j1 >> 16 & 255) / 255.0F;
                float f1 = (float)(j1 >> 8 & 255) / 255.0F;
                float f2 = (float)(j1 >> 0 & 255) / 255.0F;
                EnumParticleTypes enumparticletypes = EnumParticleTypes.SPELL;

                if (Items.potionitem.isEffectInstant(data))
                {
                    enumparticletypes = EnumParticleTypes.SPELL_INSTANT;
                }

                for (int k1 = 0; k1 < 100; ++k1)
                {
                    double d7 = random.nextDouble() * 4.0D;
                    double d9 = random.nextDouble() * Math.PI * 2.0D;
                    double d11 = Math.cos(d9) * d7;
                    double d23 = 0.01D + random.nextDouble() * 0.5D;
                    double d24 = Math.sin(d9) * d7;
                    实体FX entityfx = this.spawnEntityFX(enumparticletypes.getParticleID(), enumparticletypes.getShouldIgnoreRange(), d3 + d11 * 0.1D, d4 + 0.3D, d5 + d24 * 0.1D, d11, d23, d24, new int[0]);

                    if (entityfx != null)
                    {
                        float f3 = 0.75F + random.nextFloat() * 0.25F;
                        entityfx.setRBGColorF(f * f3, f1 * f3, f2 * f3);
                        entityfx.multiplyVelocity((float)d7);
                    }
                }

                this.theWorld.playSoundAtPos(阻止位置In, "game.potion.smash", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 2003:
                double d6 = (double) 阻止位置In.getX() + 0.5D;
                double d8 = (double) 阻止位置In.getY();
                double d10 = (double) 阻止位置In.getZ() + 0.5D;

                for (int l1 = 0; l1 < 8; ++l1)
                {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d6, d8, d10, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] {Item.getIdFromItem(Items.ender_eye)});
                }

                for (double d22 = 0.0D; d22 < (Math.PI * 2D); d22 += 0.15707963267948966D)
                {
                    this.spawnParticle(EnumParticleTypes.PORTAL, d6 + Math.cos(d22) * 5.0D, d8 - 0.4D, d10 + Math.sin(d22) * 5.0D, Math.cos(d22) * -5.0D, 0.0D, Math.sin(d22) * -5.0D, new int[0]);
                    this.spawnParticle(EnumParticleTypes.PORTAL, d6 + Math.cos(d22) * 5.0D, d8 - 0.4D, d10 + Math.sin(d22) * 5.0D, Math.cos(d22) * -7.0D, 0.0D, Math.sin(d22) * -7.0D, new int[0]);
                }

                return;

            case 2004:
                for (int l = 0; l < 20; ++l)
                {
                    double d12 = (double) 阻止位置In.getX() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
                    double d13 = (double) 阻止位置In.getY() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
                    double d14 = (double) 阻止位置In.getZ() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
                    this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d12, d13, d14, 0.0D, 0.0D, 0.0D, new int[0]);
                    this.theWorld.spawnParticle(EnumParticleTypes.FLAME, d12, d13, d14, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                return;

            case 2005:
                ItemDye.spawnBonemealParticles(this.theWorld, 阻止位置In, data);
        }
    }

    public void sendBlockBreakProgress(int breakerId, 阻止位置 pos, int progress)
    {
        if (progress >= 0 && progress < 10)
        {
            DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)this.damagedBlocks.get(Integer.valueOf(breakerId));

            if (destroyblockprogress == null || destroyblockprogress.getPosition().getX() != pos.getX() || destroyblockprogress.getPosition().getY() != pos.getY() || destroyblockprogress.getPosition().getZ() != pos.getZ())
            {
                destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
                this.damagedBlocks.put(Integer.valueOf(breakerId), destroyblockprogress);
            }

            destroyblockprogress.setPartialBlockDamage(progress);
            destroyblockprogress.setCloudUpdateTick(this.cloudTickCounter);
        }
        else
        {
            this.damagedBlocks.remove(Integer.valueOf(breakerId));
        }
    }

    public void setDisplayListEntitiesDirty()
    {
        this.displayListEntitiesDirty = true;
    }

    public boolean hasNoChunkUpdates()
    {
        return this.chunksToUpdate.isEmpty() && this.renderDispatcher.hasChunkUpdates();
    }

    public void resetClouds()
    {
        this.cloudRenderer.reset();
    }

    public int getCountRenderers()
    {
        return this.viewFrustum.renderChunks.length;
    }

    public int getCountActiveRenderers()
    {
        return this.renderInfos.size();
    }

    public int getCountEntitiesRendered()
    {
        return this.countEntitiesRendered;
    }

    public int getCountTileEntitiesRendered()
    {
        return this.countTileEntitiesRendered;
    }

    public int getCountLoadedChunks()
    {
        if (this.theWorld == null)
        {
            return 0;
        }
        else
        {
            IChunkProvider ichunkprovider = this.theWorld.getChunkProvider();

            if (ichunkprovider == null)
            {
                return 0;
            }
            else
            {
                if (ichunkprovider != this.worldChunkProvider)
                {
                    this.worldChunkProvider = ichunkprovider;
                    this.worldChunkProviderMap = (LongHashMap)Reflector.getFieldValue(ichunkprovider, Reflector.ChunkProviderClient_chunkMapping);
                }

                return this.worldChunkProviderMap == null ? 0 : this.worldChunkProviderMap.getNumHashElements();
            }
        }
    }

    public int getCountChunksToUpdate()
    {
        return this.chunksToUpdate.size();
    }

    public RenderChunk getRenderChunk(阻止位置 p_getRenderChunk_1_)
    {
        return this.viewFrustum.getRenderChunk(p_getRenderChunk_1_);
    }

    public WorldClient getWorld()
    {
        return this.theWorld;
    }

    private void clearRenderInfos()
    {
        if (renderEntitiesCounter > 0)
        {
            this.renderInfos = new ArrayList(this.renderInfos.size() + 16);
            this.renderInfosEntities = new ArrayList(this.renderInfosEntities.size() + 16);
            this.renderInfosTileEntities = new ArrayList(this.renderInfosTileEntities.size() + 16);
        }
        else
        {
            this.renderInfos.clear();
            this.renderInfosEntities.clear();
            this.renderInfosTileEntities.clear();
        }
    }

    public void onPlayerPositionSet()
    {
        if (this.firstWorldLoad)
        {
            this.loadRenderers();
            this.firstWorldLoad = false;
        }
    }

    public void pauseChunkUpdates()
    {
        if (this.renderDispatcher != null)
        {
            this.renderDispatcher.pauseChunkUpdates();
        }
    }

    public void resumeChunkUpdates()
    {
        if (this.renderDispatcher != null)
        {
            this.renderDispatcher.resumeChunkUpdates();
        }
    }

    public void updateTileEntities(Collection<TileEntity> tileEntitiesToRemove, Collection<TileEntity> tileEntitiesToAdd)
    {
        synchronized (this.setTileEntities)
        {
            this.setTileEntities.removeAll(tileEntitiesToRemove);
            this.setTileEntities.addAll(tileEntitiesToAdd);
        }
    }

    public static class ContainerLocalRenderInformation
    {
        final RenderChunk renderChunk;
        EnumFacing facing;
        int setFacing;

        public ContainerLocalRenderInformation(RenderChunk p_i2_1_, EnumFacing p_i2_2_, int p_i2_3_)
        {
            this.renderChunk = p_i2_1_;
            this.facing = p_i2_2_;
            this.setFacing = p_i2_3_;
        }

        public void setFacingBit(byte p_setFacingBit_1_, EnumFacing p_setFacingBit_2_)
        {
            this.setFacing = this.setFacing | p_setFacingBit_1_ | 1 << p_setFacingBit_2_.ordinal();
        }

        public boolean isFacingBit(EnumFacing p_isFacingBit_1_)
        {
            return (this.setFacing & 1 << p_isFacingBit_1_.ordinal()) > 0;
        }

        private void initialize(EnumFacing p_initialize_1_, int p_initialize_2_)
        {
            this.facing = p_initialize_1_;
            this.setFacing = p_initialize_2_;
        }
    }
}
