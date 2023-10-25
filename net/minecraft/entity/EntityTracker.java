package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.boss.实体Dragon;
import net.minecraft.entity.boss.实体Wither;
import net.minecraft.entity.item.*;
import net.minecraft.entity.item.实体Item;
import net.minecraft.entity.passive.实体Bat;
import net.minecraft.entity.passive.实体Squid;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.实体PlayerMP;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.实体FishHook;
import net.minecraft.network.Packet;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTracker
{
    private static final Logger logger = LogManager.getLogger();
    private final WorldServer theWorld;
    private Set<EntityTrackerEntry> trackedEntities = Sets.<EntityTrackerEntry>newHashSet();
    private IntHashMap<EntityTrackerEntry> trackedEntityHashTable = new IntHashMap();
    private int maxTrackingDistanceThreshold;

    public EntityTracker(WorldServer theWorldIn)
    {
        this.theWorld = theWorldIn;
        this.maxTrackingDistanceThreshold = theWorldIn.getMinecraftServer().getConfigurationManager().getEntityViewDistance();
    }

    public void trackEntity(实体 实体In)
    {
        if (实体In instanceof 实体PlayerMP)
        {
            this.trackEntity(实体In, 512, 2);
            实体PlayerMP entityplayermp = (实体PlayerMP) 实体In;

            for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
            {
                if (entitytrackerentry.tracked实体 != entityplayermp)
                {
                    entitytrackerentry.updatePlayerEntity(entityplayermp);
                }
            }
        }
        else if (实体In instanceof 实体FishHook)
        {
            this.addEntityToTracker(实体In, 64, 5, true);
        }
        else if (实体In instanceof 实体Arrow)
        {
            this.addEntityToTracker(实体In, 64, 20, false);
        }
        else if (实体In instanceof 实体SmallFireball)
        {
            this.addEntityToTracker(实体In, 64, 10, false);
        }
        else if (实体In instanceof 实体Fireball)
        {
            this.addEntityToTracker(实体In, 64, 10, false);
        }
        else if (实体In instanceof 实体Snowball)
        {
            this.addEntityToTracker(实体In, 64, 10, true);
        }
        else if (实体In instanceof 实体EnderPearl)
        {
            this.addEntityToTracker(实体In, 64, 10, true);
        }
        else if (实体In instanceof 实体EnderEye)
        {
            this.addEntityToTracker(实体In, 64, 4, true);
        }
        else if (实体In instanceof 实体Egg)
        {
            this.addEntityToTracker(实体In, 64, 10, true);
        }
        else if (实体In instanceof 实体Potion)
        {
            this.addEntityToTracker(实体In, 64, 10, true);
        }
        else if (实体In instanceof 实体ExpBottle)
        {
            this.addEntityToTracker(实体In, 64, 10, true);
        }
        else if (实体In instanceof 实体FireworkRocket)
        {
            this.addEntityToTracker(实体In, 64, 10, true);
        }
        else if (实体In instanceof 实体Item)
        {
            this.addEntityToTracker(实体In, 64, 20, true);
        }
        else if (实体In instanceof 实体Minecart)
        {
            this.addEntityToTracker(实体In, 80, 3, true);
        }
        else if (实体In instanceof 实体Boat)
        {
            this.addEntityToTracker(实体In, 80, 3, true);
        }
        else if (实体In instanceof 实体Squid)
        {
            this.addEntityToTracker(实体In, 64, 3, true);
        }
        else if (实体In instanceof 实体Wither)
        {
            this.addEntityToTracker(实体In, 80, 3, false);
        }
        else if (实体In instanceof 实体Bat)
        {
            this.addEntityToTracker(实体In, 80, 3, false);
        }
        else if (实体In instanceof 实体Dragon)
        {
            this.addEntityToTracker(实体In, 160, 3, true);
        }
        else if (实体In instanceof IAnimals)
        {
            this.addEntityToTracker(实体In, 80, 3, true);
        }
        else if (实体In instanceof 实体TNTPrimed)
        {
            this.addEntityToTracker(实体In, 160, 10, true);
        }
        else if (实体In instanceof 实体FallingBlock)
        {
            this.addEntityToTracker(实体In, 160, 20, true);
        }
        else if (实体In instanceof 实体Hanging)
        {
            this.addEntityToTracker(实体In, 160, Integer.MAX_VALUE, false);
        }
        else if (实体In instanceof 实体ArmorStand)
        {
            this.addEntityToTracker(实体In, 160, 3, true);
        }
        else if (实体In instanceof 实体XPOrb)
        {
            this.addEntityToTracker(实体In, 160, 20, true);
        }
        else if (实体In instanceof 实体EnderCrystal)
        {
            this.addEntityToTracker(实体In, 256, Integer.MAX_VALUE, false);
        }
    }

    public void trackEntity(实体 实体In, int trackingRange, int updateFrequency)
    {
        this.addEntityToTracker(实体In, trackingRange, updateFrequency, false);
    }

    public void addEntityToTracker(实体 实体In, int trackingRange, final int updateFrequency, boolean sendVelocityUpdates)
    {
        if (trackingRange > this.maxTrackingDistanceThreshold)
        {
            trackingRange = this.maxTrackingDistanceThreshold;
        }

        try
        {
            if (this.trackedEntityHashTable.containsItem(实体In.getEntityId()))
            {
                throw new IllegalStateException("Entity is already tracked!");
            }

            EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(实体In, trackingRange, updateFrequency, sendVelocityUpdates);
            this.trackedEntities.add(entitytrackerentry);
            this.trackedEntityHashTable.addKey(实体In.getEntityId(), entitytrackerentry);
            entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity To Track");
            crashreportcategory.addCrashSection("Tracking range", trackingRange + " blocks");
            crashreportcategory.addCrashSectionCallable("Update interval", new Callable<String>()
            {
                public String call() throws Exception
                {
                    String s = "Once per " + updateFrequency + " ticks";

                    if (updateFrequency == Integer.MAX_VALUE)
                    {
                        s = "Maximum (" + s + ")";
                    }

                    return s;
                }
            });
            实体In.addEntityCrashInfo(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Entity That Is Already Tracked");
            ((EntityTrackerEntry)this.trackedEntityHashTable.lookup(实体In.getEntityId())).tracked实体.addEntityCrashInfo(crashreportcategory1);

            try
            {
                throw new ReportedException(crashreport);
            }
            catch (ReportedException reportedexception)
            {
                logger.error((String)"\"Silently\" catching entity tracking error.", (Throwable)reportedexception);
            }
        }
    }

    public void untrackEntity(实体 实体In)
    {
        if (实体In instanceof 实体PlayerMP)
        {
            实体PlayerMP entityplayermp = (实体PlayerMP) 实体In;

            for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
            {
                entitytrackerentry.removeFromTrackedPlayers(entityplayermp);
            }
        }

        EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry)this.trackedEntityHashTable.removeObject(实体In.getEntityId());

        if (entitytrackerentry1 != null)
        {
            this.trackedEntities.remove(entitytrackerentry1);
            entitytrackerentry1.sendDestroyEntityPacketToTrackedPlayers();
        }
    }

    public void updateTrackedEntities()
    {
        List<实体PlayerMP> list = Lists.<实体PlayerMP>newArrayList();

        for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
        {
            entitytrackerentry.updatePlayerList(this.theWorld.playerEntities);

            if (entitytrackerentry.playerEntitiesUpdated && entitytrackerentry.tracked实体 instanceof 实体PlayerMP)
            {
                list.add((实体PlayerMP)entitytrackerentry.tracked实体);
            }
        }

        for (int i = 0; i < ((List)list).size(); ++i)
        {
            实体PlayerMP entityplayermp = (实体PlayerMP)list.get(i);

            for (EntityTrackerEntry entitytrackerentry1 : this.trackedEntities)
            {
                if (entitytrackerentry1.tracked实体 != entityplayermp)
                {
                    entitytrackerentry1.updatePlayerEntity(entityplayermp);
                }
            }
        }
    }

    public void func_180245_a(实体PlayerMP p_180245_1_)
    {
        for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
        {
            if (entitytrackerentry.tracked实体 == p_180245_1_)
            {
                entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
            }
            else
            {
                entitytrackerentry.updatePlayerEntity(p_180245_1_);
            }
        }
    }

    public void sendToAllTrackingEntity(实体 实体In, Packet p_151247_2_)
    {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(实体In.getEntityId());

        if (entitytrackerentry != null)
        {
            entitytrackerentry.sendPacketToTrackedPlayers(p_151247_2_);
        }
    }

    public void func_151248_b(实体 实体In, Packet p_151248_2_)
    {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(实体In.getEntityId());

        if (entitytrackerentry != null)
        {
            entitytrackerentry.func_151261_b(p_151248_2_);
        }
    }

    public void removePlayerFromTrackers(实体PlayerMP p_72787_1_)
    {
        for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
        {
            entitytrackerentry.removeTrackedPlayerSymmetric(p_72787_1_);
        }
    }

    public void func_85172_a(实体PlayerMP p_85172_1_, Chunk p_85172_2_)
    {
        for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
        {
            if (entitytrackerentry.tracked实体 != p_85172_1_ && entitytrackerentry.tracked实体.chunkCoordX == p_85172_2_.xPosition && entitytrackerentry.tracked实体.chunkCoordZ == p_85172_2_.zPosition)
            {
                entitytrackerentry.updatePlayerEntity(p_85172_1_);
            }
        }
    }
}
