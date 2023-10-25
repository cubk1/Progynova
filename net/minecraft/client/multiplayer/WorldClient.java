package net.minecraft.client.multiplayer;

import com.google.common.collect.Sets;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.我的手艺;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityFirework;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.item.实体Minecart;
import net.minecraft.entity.实体;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.src.Config;
import net.minecraft.util.阻止位置;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.图像位置;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.CustomGuis;
import net.optifine.DynamicLights;
import net.optifine.override.PlayerControllerOF;
import net.optifine.reflect.Reflector;

public class WorldClient extends World
{
    private NetHandlerPlayClient sendQueue;
    private ChunkProviderClient clientChunkProvider;
    private final Set<实体> 实体List = Sets.<实体>newHashSet();
    private final Set<实体> 实体SpawnQueue = Sets.<实体>newHashSet();
    private final 我的手艺 mc = 我的手艺.得到我的手艺();
    private final Set<ChunkCoordIntPair> previousActiveChunkSet = Sets.<ChunkCoordIntPair>newHashSet();
    private boolean playerUpdate = false;

    public WorldClient(NetHandlerPlayClient netHandler, WorldSettings settings, int dimension, EnumDifficulty difficulty, Profiler profilerIn)
    {
        super(new SaveHandlerMP(), new WorldInfo(settings, "MpServer"), WorldProvider.getProviderForDimension(dimension), profilerIn, true);
        this.sendQueue = netHandler;
        this.getWorldInfo().setDifficulty(difficulty);
        this.provider.registerWorld(this);
        this.setSpawnPoint(new 阻止位置(8, 64, 8));
        this.chunkProvider = this.createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, new Object[] {this});

        if (this.mc.玩家控制者 != null && this.mc.玩家控制者.getClass() == PlayerControllerMP.class)
        {
            this.mc.玩家控制者 = new PlayerControllerOF(this.mc, netHandler);
            CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.mc.玩家控制者);
        }
    }

    public void tick()
    {
        super.tick();
        this.setTotalWorldTime(this.getTotalWorldTime() + 1L);

        if (this.getGameRules().getBoolean("doDaylightCycle"))
        {
            this.setWorldTime(this.getWorldTime() + 1L);
        }

        this.theProfiler.startSection("reEntryProcessing");

        for (int i = 0; i < 10 && !this.实体SpawnQueue.isEmpty(); ++i)
        {
            实体 实体 = (实体)this.实体SpawnQueue.iterator().next();
            this.实体SpawnQueue.remove(实体);

            if (!this.loaded实体List.contains(实体))
            {
                this.spawnEntityInWorld(实体);
            }
        }

        this.theProfiler.endStartSection("chunkCache");
        this.clientChunkProvider.unloadQueuedChunks();
        this.theProfiler.endStartSection("blocks");
        this.updateBlocks();
        this.theProfiler.endSection();
    }

    public void invalidateBlockReceiveRegion(int x1, int y1, int z1, int x2, int y2, int z2)
    {
    }

    protected IChunkProvider createChunkProvider()
    {
        this.clientChunkProvider = new ChunkProviderClient(this);
        return this.clientChunkProvider;
    }

    protected void updateBlocks()
    {
        super.updateBlocks();
        this.previousActiveChunkSet.retainAll(this.activeChunkSet);

        if (this.previousActiveChunkSet.size() == this.activeChunkSet.size())
        {
            this.previousActiveChunkSet.clear();
        }

        int i = 0;

        for (ChunkCoordIntPair chunkcoordintpair : this.activeChunkSet)
        {
            if (!this.previousActiveChunkSet.contains(chunkcoordintpair))
            {
                int j = chunkcoordintpair.chunkXPos * 16;
                int k = chunkcoordintpair.chunkZPos * 16;
                this.theProfiler.startSection("getChunk");
                Chunk chunk = this.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
                this.playMoodSoundAndCheckLight(j, k, chunk);
                this.theProfiler.endSection();
                this.previousActiveChunkSet.add(chunkcoordintpair);
                ++i;

                if (i >= 10)
                {
                    return;
                }
            }
        }
    }

    public void doPreChunk(int chuncX, int chuncZ, boolean loadChunk)
    {
        if (loadChunk)
        {
            this.clientChunkProvider.loadChunk(chuncX, chuncZ);
        }
        else
        {
            this.clientChunkProvider.unloadChunk(chuncX, chuncZ);
        }

        if (!loadChunk)
        {
            this.markBlockRangeForRenderUpdate(chuncX * 16, 0, chuncZ * 16, chuncX * 16 + 15, 256, chuncZ * 16 + 15);
        }
    }

    public boolean spawnEntityInWorld(实体 实体In)
    {
        boolean flag = super.spawnEntityInWorld(实体In);
        this.实体List.add(实体In);

        if (!flag)
        {
            this.实体SpawnQueue.add(实体In);
        }
        else if (实体In instanceof 实体Minecart)
        {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecart((实体Minecart) 实体In));
        }

        return flag;
    }

    public void removeEntity(实体 实体In)
    {
        super.removeEntity(实体In);
        this.实体List.remove(实体In);
    }

    protected void onEntityAdded(实体 实体In)
    {
        super.onEntityAdded(实体In);

        if (this.实体SpawnQueue.contains(实体In))
        {
            this.实体SpawnQueue.remove(实体In);
        }
    }

    protected void onEntityRemoved(实体 实体In)
    {
        super.onEntityRemoved(实体In);
        boolean flag = false;

        if (this.实体List.contains(实体In))
        {
            if (实体In.isEntityAlive())
            {
                this.实体SpawnQueue.add(实体In);
                flag = true;
            }
            else
            {
                this.实体List.remove(实体In);
            }
        }
    }

    public void addEntityToWorld(int entityID, 实体 实体ToSpawn)
    {
        实体 实体 = this.getEntityByID(entityID);

        if (实体 != null)
        {
            this.removeEntity(实体);
        }

        this.实体List.add(实体ToSpawn);
        实体ToSpawn.setEntityId(entityID);

        if (!this.spawnEntityInWorld(实体ToSpawn))
        {
            this.实体SpawnQueue.add(实体ToSpawn);
        }

        this.entitiesById.addKey(entityID, 实体ToSpawn);
    }

    public 实体 getEntityByID(int id)
    {
        return (实体)(id == this.mc.宇轩游玩者.getEntityId() ? this.mc.宇轩游玩者 : super.getEntityByID(id));
    }

    public 实体 removeEntityFromWorld(int entityID)
    {
        实体 实体 = (实体)this.entitiesById.removeObject(entityID);

        if (实体 != null)
        {
            this.实体List.remove(实体);
            this.removeEntity(实体);
        }

        return 实体;
    }

    public boolean invalidateRegionAndSetBlock(阻止位置 pos, IBlockState state)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        this.invalidateBlockReceiveRegion(i, j, k, i, j, k);
        return super.setBlockState(pos, state, 3);
    }

    public void sendQuittingDisconnectingPacket()
    {
        this.sendQueue.getNetworkManager().closeChannel(new 交流组分文本("Quitting"));
    }

    protected void updateWeather()
    {
    }

    protected int getRenderDistanceChunks()
    {
        return this.mc.游戏一窝.renderDistanceChunks;
    }

    public void doVoidFogParticles(int posX, int posY, int posZ)
    {
        int i = 16;
        Random random = new Random();
        ItemStack itemstack = this.mc.宇轩游玩者.getHeldItem();
        boolean flag = this.mc.玩家控制者.getCurrentGameType() == WorldSettings.GameType.CREATIVE && itemstack != null && Block.getBlockFromItem(itemstack.getItem()) == Blocks.barrier;
        阻止位置.Mutable阻止位置 blockpos$mutableblockpos = new 阻止位置.Mutable阻止位置();

        for (int j = 0; j < 1000; ++j)
        {
            int k = posX + this.rand.nextInt(i) - this.rand.nextInt(i);
            int l = posY + this.rand.nextInt(i) - this.rand.nextInt(i);
            int i1 = posZ + this.rand.nextInt(i) - this.rand.nextInt(i);
            blockpos$mutableblockpos.set(k, l, i1);
            IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos);
            iblockstate.getBlock().randomDisplayTick(this, blockpos$mutableblockpos, iblockstate, random);

            if (flag && iblockstate.getBlock() == Blocks.barrier)
            {
                this.spawnParticle(EnumParticleTypes.BARRIER, (double)((float)k + 0.5F), (double)((float)l + 0.5F), (double)((float)i1 + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }

    public void removeAllEntities()
    {
        this.loaded实体List.removeAll(this.unloaded实体List);

        for (int i = 0; i < this.unloaded实体List.size(); ++i)
        {
            实体 实体 = (实体)this.unloaded实体List.get(i);
            int j = 实体.chunkCoordX;
            int k = 实体.chunkCoordZ;

            if (实体.addedToChunk && this.isChunkLoaded(j, k, true))
            {
                this.getChunkFromChunkCoords(j, k).removeEntity(实体);
            }
        }

        for (int l = 0; l < this.unloaded实体List.size(); ++l)
        {
            this.onEntityRemoved((实体)this.unloaded实体List.get(l));
        }

        this.unloaded实体List.clear();

        for (int i1 = 0; i1 < this.loaded实体List.size(); ++i1)
        {
            实体 实体1 = (实体)this.loaded实体List.get(i1);

            if (实体1.riding实体 != null)
            {
                if (!实体1.riding实体.isDead && 实体1.riding实体.riddenBy实体 == 实体1)
                {
                    continue;
                }

                实体1.riding实体.riddenBy实体 = null;
                实体1.riding实体 = null;
            }

            if (实体1.isDead)
            {
                int j1 = 实体1.chunkCoordX;
                int k1 = 实体1.chunkCoordZ;

                if (实体1.addedToChunk && this.isChunkLoaded(j1, k1, true))
                {
                    this.getChunkFromChunkCoords(j1, k1).removeEntity(实体1);
                }

                this.loaded实体List.remove(i1--);
                this.onEntityRemoved(实体1);
            }
        }
    }

    public CrashReportCategory addWorldInfoToCrashReport(CrashReport report)
    {
        CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(report);
        crashreportcategory.addCrashSectionCallable("Forced entities", new Callable<String>()
        {
            public String call()
            {
                return WorldClient.this.实体List.size() + " total; " + WorldClient.this.实体List.toString();
            }
        });
        crashreportcategory.addCrashSectionCallable("Retry entities", new Callable<String>()
        {
            public String call()
            {
                return WorldClient.this.实体SpawnQueue.size() + " total; " + WorldClient.this.实体SpawnQueue.toString();
            }
        });
        crashreportcategory.addCrashSectionCallable("Server brand", new Callable<String>()
        {
            public String call() throws Exception
            {
                return WorldClient.this.mc.宇轩游玩者.getClientBrand();
            }
        });
        crashreportcategory.addCrashSectionCallable("Server type", new Callable<String>()
        {
            public String call() throws Exception
            {
                return WorldClient.this.mc.getIntegratedServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
            }
        });
        return crashreportcategory;
    }

    public void playSoundAtPos(阻止位置 pos, String soundName, float volume, float pitch, boolean distanceDelay)
    {
        this.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, soundName, volume, pitch, distanceDelay);
    }

    public void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay)
    {
        double d0 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
        PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(new 图像位置(soundName), volume, pitch, (float)x, (float)y, (float)z);

        if (distanceDelay && d0 > 100.0D)
        {
            double d1 = Math.sqrt(d0) / 40.0D;
            this.mc.getSoundHandler().playDelayedSound(positionedsoundrecord, (int)(d1 * 20.0D));
        }
        else
        {
            this.mc.getSoundHandler().playSound(positionedsoundrecord);
        }
    }

    public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, NBTTagCompound compund)
    {
        this.mc.effectRenderer.addEffect(new EntityFirework.StarterFX(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compund));
    }

    public void setWorldScoreboard(Scoreboard scoreboardIn)
    {
        this.worldScoreboard = scoreboardIn;
    }

    public void setWorldTime(long time)
    {
        if (time < 0L)
        {
            time = -time;
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
        }
        else
        {
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
        }

        super.setWorldTime(time);
    }

    public int getCombinedLight(阻止位置 pos, int lightValue)
    {
        int i = super.getCombinedLight(pos, lightValue);

        if (Config.isDynamicLights())
        {
            i = DynamicLights.getCombinedLight(pos, i);
        }

        return i;
    }

    public boolean setBlockState(阻止位置 pos, IBlockState newState, int flags)
    {
        this.playerUpdate = this.isPlayerActing();
        boolean flag = super.setBlockState(pos, newState, flags);
        this.playerUpdate = false;
        return flag;
    }

    private boolean isPlayerActing()
    {
        if (this.mc.玩家控制者 instanceof PlayerControllerOF)
        {
            PlayerControllerOF playercontrollerof = (PlayerControllerOF)this.mc.玩家控制者;
            return playercontrollerof.isActing();
        }
        else
        {
            return false;
        }
    }

    public boolean isPlayerUpdate()
    {
        return this.playerUpdate;
    }
}
