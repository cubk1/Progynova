package net.minecraft.client.particle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.实体;
import net.minecraft.src.Config;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.图像位置;
import net.minecraft.world.World;
import net.optifine.reflect.Reflector;

public class EffectRenderer
{
    private static final 图像位置 particleTextures = new 图像位置("textures/particle/particles.png");
    protected World worldObj;
    private List<实体FX>[][] fxLayers = new List[4][];
    private List<实体ParticleEmitter> particleEmitters = Lists.<实体ParticleEmitter>newArrayList();
    private TextureManager renderer;
    private Random rand = new Random();
    private Map<Integer, IParticleFactory> particleTypes = Maps.<Integer, IParticleFactory>newHashMap();

    public EffectRenderer(World worldIn, TextureManager rendererIn)
    {
        this.worldObj = worldIn;
        this.renderer = rendererIn;

        for (int i = 0; i < 4; ++i)
        {
            this.fxLayers[i] = new List[2];

            for (int j = 0; j < 2; ++j)
            {
                this.fxLayers[i][j] = Lists.newArrayList();
            }
        }

        this.registerVanillaParticles();
    }

    private void registerVanillaParticles()
    {
        this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new 实体ExplodeFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new 实体BubbleFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new 实体SplashFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new 实体FishWakeFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new 实体RainFX.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new 实体SuspendFX.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new 实体AuraFX.Factory());
        this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new 实体Crit2FX.Factory());
        this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new 实体Crit2FX.MagicFactory());
        this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new 实体SmokeFX.Factory());
        this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new 实体CritFX.Factory());
        this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new 实体SpellParticleFX.Factory());
        this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new 实体SpellParticleFX.InstantFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new 实体SpellParticleFX.MobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new 实体SpellParticleFX.AmbientMobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new 实体SpellParticleFX.WitchFactory());
        this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new 实体DropParticleFX.WaterFactory());
        this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new 实体DropParticleFX.LavaFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new 实体HeartFX.AngryVillagerFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new 实体AuraFX.HappyVillagerFactory());
        this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new 实体AuraFX.Factory());
        this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new 实体NoteFX.Factory());
        this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new 实体PortalFX.Factory());
        this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new 实体EnchantmentTableParticleFX.EnchantmentTable());
        this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new 实体FlameFX.Factory());
        this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new 实体LavaFX.Factory());
        this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new 实体FootStepFX.Factory());
        this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new 实体CloudFX.Factory());
        this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new 实体ReddustFX.Factory());
        this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new 实体BreakingFX.SnowballFactory());
        this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new 实体SnowShovelFX.Factory());
        this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new 实体BreakingFX.SlimeFactory());
        this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new 实体HeartFX.Factory());
        this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
        this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new 实体BreakingFX.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new 实体DiggingFX.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new 实体BlockDustFX.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new 实体HugeExplodeFX.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new 实体LargeExplodeFX.Factory());
        this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new EntityFirework.Factory());
        this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new MobAppearance.Factory());
    }

    public void registerParticle(int id, IParticleFactory particleFactory)
    {
        this.particleTypes.put(Integer.valueOf(id), particleFactory);
    }

    public void emitParticleAtEntity(实体 实体In, EnumParticleTypes particleTypes)
    {
        this.particleEmitters.add(new 实体ParticleEmitter(this.worldObj, 实体In, particleTypes));
    }

    public 实体FX spawnEffectParticle(int particleId, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters)
    {
        IParticleFactory iparticlefactory = (IParticleFactory)this.particleTypes.get(Integer.valueOf(particleId));

        if (iparticlefactory != null)
        {
            实体FX entityfx = iparticlefactory.getEntityFX(particleId, this.worldObj, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);

            if (entityfx != null)
            {
                this.addEffect(entityfx);
                return entityfx;
            }
        }

        return null;
    }

    public void addEffect(实体FX effect)
    {
        if (effect != null)
        {
            if (!(effect instanceof EntityFirework.SparkFX) || Config.isFireworkParticles())
            {
                int i = effect.getFXLayer();
                int j = effect.getAlpha() != 1.0F ? 0 : 1;

                if (this.fxLayers[i][j].size() >= 4000)
                {
                    this.fxLayers[i][j].remove(0);
                }

                this.fxLayers[i][j].add(effect);
            }
        }
    }

    public void updateEffects()
    {
        for (int i = 0; i < 4; ++i)
        {
            this.updateEffectLayer(i);
        }

        List<实体ParticleEmitter> list = Lists.<实体ParticleEmitter>newArrayList();

        for (实体ParticleEmitter entityparticleemitter : this.particleEmitters)
        {
            entityparticleemitter.onUpdate();

            if (entityparticleemitter.isDead)
            {
                list.add(entityparticleemitter);
            }
        }

        this.particleEmitters.removeAll(list);
    }

    private void updateEffectLayer(int layer)
    {
        for (int i = 0; i < 2; ++i)
        {
            this.updateEffectAlphaLayer(this.fxLayers[layer][i]);
        }
    }

    private void updateEffectAlphaLayer(List<实体FX> entitiesFX)
    {
        List<实体FX> list = Lists.newArrayList();
        long i = System.currentTimeMillis();
        int j = entitiesFX.size();

        for (int k = 0; k < entitiesFX.size(); ++k)
        {
            实体FX entityfx = entitiesFX.get(k);
            this.tickParticle(entityfx);

            if (entityfx.isDead)
            {
                list.add(entityfx);
            }

            --j;

            if (System.currentTimeMillis() > i + 20L)
            {
                break;
            }
        }

        if (j > 0)
        {
            int l = j;

            for (Iterator iterator = entitiesFX.iterator(); iterator.hasNext() && l > 0; --l)
            {
                实体FX entityfx1 = (实体FX)iterator.next();
                entityfx1.setDead();
                iterator.remove();
            }
        }

        entitiesFX.removeAll(list);
    }

    private void tickParticle(final 实体FX particle)
    {
        try
        {
            particle.onUpdate();
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            final int i = particle.getFXLayer();
            crashreportcategory.addCrashSectionCallable("Particle", new Callable<String>()
            {
                public String call() throws Exception
                {
                    return particle.toString();
                }
            });
            crashreportcategory.addCrashSectionCallable("Particle Type", new Callable<String>()
            {
                public String call() throws Exception
                {
                    return i == 0 ? "MISC_TEXTURE" : (i == 1 ? "TERRAIN_TEXTURE" : (i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i));
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    public void renderParticles(实体 实体In, float partialTicks)
    {
        float f = ActiveRenderInfo.getRotationX();
        float f1 = ActiveRenderInfo.getRotationZ();
        float f2 = ActiveRenderInfo.getRotationYZ();
        float f3 = ActiveRenderInfo.getRotationXY();
        float f4 = ActiveRenderInfo.getRotationXZ();
        实体FX.interpPosX = 实体In.lastTickPosX + (实体In.X坐标 - 实体In.lastTickPosX) * (double)partialTicks;
        实体FX.interpPosY = 实体In.lastTickPosY + (实体In.Y坐标 - 实体In.lastTickPosY) * (double)partialTicks;
        实体FX.interpPosZ = 实体In.lastTickPosZ + (实体In.Z坐标 - 实体In.lastTickPosZ) * (double)partialTicks;
        光照状态经理.启用混合品();
        光照状态经理.正常工作(770, 771);
        光照状态经理.alphaFunc(516, 0.003921569F);
        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.worldObj, 实体In, partialTicks);
        boolean flag = block.getMaterial() == Material.water;

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                final int i_f = i;

                if (!this.fxLayers[i][j].isEmpty())
                {
                    switch (j)
                    {
                        case 0:
                            光照状态经理.depthMask(false);
                            break;

                        case 1:
                            光照状态经理.depthMask(true);
                    }

                    switch (i)
                    {
                        case 0:
                        default:
                            this.renderer.绑定手感(particleTextures);
                            break;

                        case 1:
                            this.renderer.绑定手感(TextureMap.locationBlocksTexture);
                    }

                    光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

                    for (int k = 0; k < this.fxLayers[i][j].size(); ++k)
                    {
                        final 实体FX entityfx = (实体FX)this.fxLayers[i][j].get(k);

                        try
                        {
                            if (flag || !(entityfx instanceof 实体SuspendFX))
                            {
                                entityfx.renderParticle(worldrenderer, 实体In, partialTicks, f, f4, f1, f2, f3);
                            }
                        }
                        catch (Throwable throwable)
                        {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                            crashreportcategory.addCrashSectionCallable("Particle", new Callable<String>()
                            {
                                public String call() throws Exception
                                {
                                    return entityfx.toString();
                                }
                            });
                            crashreportcategory.addCrashSectionCallable("Particle Type", new Callable<String>()
                            {
                                public String call() throws Exception
                                {
                                    return i_f == 0 ? "MISC_TEXTURE" : (i_f == 1 ? "TERRAIN_TEXTURE" : (i_f == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i_f));
                                }
                            });
                            throw new ReportedException(crashreport);
                        }
                    }

                    tessellator.draw();
                }
            }
        }

        光照状态经理.depthMask(true);
        光照状态经理.禁用混合品();
        光照状态经理.alphaFunc(516, 0.1F);
    }

    public void renderLitParticles(实体 实体In, float partialTick)
    {
        float f = 0.017453292F;
        float f1 = MathHelper.cos(实体In.旋转侧滑 * 0.017453292F);
        float f2 = MathHelper.sin(实体In.旋转侧滑 * 0.017453292F);
        float f3 = -f2 * MathHelper.sin(实体In.rotationPitch * 0.017453292F);
        float f4 = f1 * MathHelper.sin(实体In.rotationPitch * 0.017453292F);
        float f5 = MathHelper.cos(实体In.rotationPitch * 0.017453292F);

        for (int i = 0; i < 2; ++i)
        {
            List<实体FX> list = this.fxLayers[3][i];

            if (!list.isEmpty())
            {
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();

                for (int j = 0; j < list.size(); ++j)
                {
                    实体FX entityfx = (实体FX)list.get(j);
                    entityfx.renderParticle(worldrenderer, 实体In, partialTick, f1, f5, f2, f3, f4);
                }
            }
        }
    }

    public void clearEffects(World worldIn)
    {
        this.worldObj = worldIn;

        for (int i = 0; i < 4; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                this.fxLayers[i][j].clear();
            }
        }

        this.particleEmitters.clear();
    }

    public void addBlockDestroyEffects(阻止位置 pos, IBlockState state)
    {
        boolean flag;

        if (Reflector.ForgeBlock_addDestroyEffects.exists() && Reflector.ForgeBlock_isAir.exists())
        {
            Block block = state.getBlock();
            flag = !Reflector.callBoolean(block, Reflector.ForgeBlock_isAir, new Object[] {this.worldObj, pos}) && !Reflector.callBoolean(block, Reflector.ForgeBlock_addDestroyEffects, new Object[] {this.worldObj, pos, this});
        }
        else
        {
            flag = state.getBlock().getMaterial() != Material.air;
        }

        if (flag)
        {
            state = state.getBlock().getActualState(state, this.worldObj, pos);
            int l = 4;

            for (int i = 0; i < l; ++i)
            {
                for (int j = 0; j < l; ++j)
                {
                    for (int k = 0; k < l; ++k)
                    {
                        double d0 = (double)pos.getX() + ((double)i + 0.5D) / (double)l;
                        double d1 = (double)pos.getY() + ((double)j + 0.5D) / (double)l;
                        double d2 = (double)pos.getZ() + ((double)k + 0.5D) / (double)l;
                        this.addEffect((new 实体DiggingFX(this.worldObj, d0, d1, d2, d0 - (double)pos.getX() - 0.5D, d1 - (double)pos.getY() - 0.5D, d2 - (double)pos.getZ() - 0.5D, state)).setBlockPos(pos));
                    }
                }
            }
        }
    }

    public void addBlockHitEffects(阻止位置 pos, EnumFacing side)
    {
        IBlockState iblockstate = this.worldObj.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block.getRenderType() != -1)
        {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            double d0 = (double)i + this.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinX();
            double d1 = (double)j + this.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinY();
            double d2 = (double)k + this.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinZ();

            if (side == EnumFacing.DOWN)
            {
                d1 = (double)j + block.getBlockBoundsMinY() - (double)f;
            }

            if (side == EnumFacing.UP)
            {
                d1 = (double)j + block.getBlockBoundsMaxY() + (double)f;
            }

            if (side == EnumFacing.NORTH)
            {
                d2 = (double)k + block.getBlockBoundsMinZ() - (double)f;
            }

            if (side == EnumFacing.SOUTH)
            {
                d2 = (double)k + block.getBlockBoundsMaxZ() + (double)f;
            }

            if (side == EnumFacing.WEST)
            {
                d0 = (double)i + block.getBlockBoundsMinX() - (double)f;
            }

            if (side == EnumFacing.EAST)
            {
                d0 = (double)i + block.getBlockBoundsMaxX() + (double)f;
            }

            this.addEffect((new 实体DiggingFX(this.worldObj, d0, d1, d2, 0.0D, 0.0D, 0.0D, iblockstate)).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
        }
    }

    public void moveToAlphaLayer(实体FX effect)
    {
        this.moveToLayer(effect, 1, 0);
    }

    public void moveToNoAlphaLayer(实体FX effect)
    {
        this.moveToLayer(effect, 0, 1);
    }

    private void moveToLayer(实体FX effect, int layerFrom, int layerTo)
    {
        for (int i = 0; i < 4; ++i)
        {
            if (this.fxLayers[i][layerFrom].contains(effect))
            {
                this.fxLayers[i][layerFrom].remove(effect);
                this.fxLayers[i][layerTo].add(effect);
            }
        }
    }

    public String getStatistics()
    {
        int i = 0;

        for (int j = 0; j < 4; ++j)
        {
            for (int k = 0; k < 2; ++k)
            {
                i += this.fxLayers[j][k].size();
            }
        }

        return "" + i;
    }

    public void addBlockHitEffects(阻止位置 p_addBlockHitEffects_1_, MovingObjectPosition p_addBlockHitEffects_2_)
    {
        IBlockState iblockstate = this.worldObj.getBlockState(p_addBlockHitEffects_1_);

        if (iblockstate != null)
        {
            boolean flag = Reflector.callBoolean(iblockstate.getBlock(), Reflector.ForgeBlock_addHitEffects, new Object[] {this.worldObj, p_addBlockHitEffects_2_, this});

            if (iblockstate != null && !flag)
            {
                this.addBlockHitEffects(p_addBlockHitEffects_1_, p_addBlockHitEffects_2_.sideHit);
            }
        }
    }
}
