package net.minecraft.entity;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.item.*;
import net.minecraft.entity.item.实体Item;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.player.实体PlayerMP;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.实体FishHook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.阻止位置;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry
{
    private static final Logger logger = LogManager.getLogger();
    public 实体 tracked实体;
    public int trackingDistanceThreshold;
    public int updateFrequency;
    public int encodedPosX;
    public int encodedPosY;
    public int encodedPosZ;
    public int encodedRotationYaw;
    public int encodedRotationPitch;
    public int lastHeadMotion;
    public double lastTrackedEntityMotionX;
    public double lastTrackedEntityMotionY;
    public double motionZ;
    public int updateCounter;
    private double lastTrackedEntityPosX;
    private double lastTrackedEntityPosY;
    private double lastTrackedEntityPosZ;
    private boolean firstUpdateDone;
    private boolean sendVelocityUpdates;
    private int ticksSinceLastForcedTeleport;
    private 实体 field_85178_v;
    private boolean ridingEntity;
    private boolean onGround;
    public boolean playerEntitiesUpdated;
    public Set<实体PlayerMP> trackingPlayers = Sets.<实体PlayerMP>newHashSet();

    public EntityTrackerEntry(实体 tracked实体In, int trackingDistanceThresholdIn, int updateFrequencyIn, boolean sendVelocityUpdatesIn)
    {
        this.tracked实体 = tracked实体In;
        this.trackingDistanceThreshold = trackingDistanceThresholdIn;
        this.updateFrequency = updateFrequencyIn;
        this.sendVelocityUpdates = sendVelocityUpdatesIn;
        this.encodedPosX = MathHelper.floor_double(tracked实体In.X坐标 * 32.0D);
        this.encodedPosY = MathHelper.floor_double(tracked实体In.Y坐标 * 32.0D);
        this.encodedPosZ = MathHelper.floor_double(tracked实体In.Z坐标 * 32.0D);
        this.encodedRotationYaw = MathHelper.floor_float(tracked实体In.旋转侧滑 * 256.0F / 360.0F);
        this.encodedRotationPitch = MathHelper.floor_float(tracked实体In.rotationPitch * 256.0F / 360.0F);
        this.lastHeadMotion = MathHelper.floor_float(tracked实体In.getRotationYawHead() * 256.0F / 360.0F);
        this.onGround = tracked实体In.onGround;
    }

    public boolean equals(Object p_equals_1_)
    {
        return p_equals_1_ instanceof EntityTrackerEntry ? ((EntityTrackerEntry)p_equals_1_).tracked实体.getEntityId() == this.tracked实体.getEntityId() : false;
    }

    public int hashCode()
    {
        return this.tracked实体.getEntityId();
    }

    public void updatePlayerList(List<实体Player> players)
    {
        this.playerEntitiesUpdated = false;

        if (!this.firstUpdateDone || this.tracked实体.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0D)
        {
            this.lastTrackedEntityPosX = this.tracked实体.X坐标;
            this.lastTrackedEntityPosY = this.tracked实体.Y坐标;
            this.lastTrackedEntityPosZ = this.tracked实体.Z坐标;
            this.firstUpdateDone = true;
            this.playerEntitiesUpdated = true;
            this.updatePlayerEntities(players);
        }

        if (this.field_85178_v != this.tracked实体.riding实体 || this.tracked实体.riding实体 != null && this.updateCounter % 60 == 0)
        {
            this.field_85178_v = this.tracked实体.riding实体;
            this.sendPacketToTrackedPlayers(new S1BPacketEntityAttach(0, this.tracked实体, this.tracked实体.riding实体));
        }

        if (this.tracked实体 instanceof 实体ItemFrame && this.updateCounter % 10 == 0)
        {
            实体ItemFrame entityitemframe = (实体ItemFrame)this.tracked实体;
            ItemStack itemstack = entityitemframe.getDisplayedItem();

            if (itemstack != null && itemstack.getItem() instanceof ItemMap)
            {
                MapData mapdata = Items.filled_map.getMapData(itemstack, this.tracked实体.worldObj);

                for (实体Player entityplayer : players)
                {
                    实体PlayerMP entityplayermp = (实体PlayerMP)entityplayer;
                    mapdata.updateVisiblePlayers(entityplayermp, itemstack);
                    Packet packet = Items.filled_map.createMapDataPacket(itemstack, this.tracked实体.worldObj, entityplayermp);

                    if (packet != null)
                    {
                        entityplayermp.playerNetServerHandler.sendPacket(packet);
                    }
                }
            }

            this.sendMetadataToAllAssociatedPlayers();
        }

        if (this.updateCounter % this.updateFrequency == 0 || this.tracked实体.isAirBorne || this.tracked实体.getDataWatcher().hasObjectChanged())
        {
            if (this.tracked实体.riding实体 == null)
            {
                ++this.ticksSinceLastForcedTeleport;
                int k = MathHelper.floor_double(this.tracked实体.X坐标 * 32.0D);
                int j1 = MathHelper.floor_double(this.tracked实体.Y坐标 * 32.0D);
                int k1 = MathHelper.floor_double(this.tracked实体.Z坐标 * 32.0D);
                int l1 = MathHelper.floor_float(this.tracked实体.旋转侧滑 * 256.0F / 360.0F);
                int i2 = MathHelper.floor_float(this.tracked实体.rotationPitch * 256.0F / 360.0F);
                int j2 = k - this.encodedPosX;
                int k2 = j1 - this.encodedPosY;
                int i = k1 - this.encodedPosZ;
                Packet packet1 = null;
                boolean flag = Math.abs(j2) >= 4 || Math.abs(k2) >= 4 || Math.abs(i) >= 4 || this.updateCounter % 60 == 0;
                boolean flag1 = Math.abs(l1 - this.encodedRotationYaw) >= 4 || Math.abs(i2 - this.encodedRotationPitch) >= 4;

                if (this.updateCounter > 0 || this.tracked实体 instanceof 实体Arrow)
                {
                    if (j2 >= -128 && j2 < 128 && k2 >= -128 && k2 < 128 && i >= -128 && i < 128 && this.ticksSinceLastForcedTeleport <= 400 && !this.ridingEntity && this.onGround == this.tracked实体.onGround)
                    {
                        if ((!flag || !flag1) && !(this.tracked实体 instanceof 实体Arrow))
                        {
                            if (flag)
                            {
                                packet1 = new S14PacketEntity.S15PacketEntityRelMove(this.tracked实体.getEntityId(), (byte)j2, (byte)k2, (byte)i, this.tracked实体.onGround);
                            }
                            else if (flag1)
                            {
                                packet1 = new S14PacketEntity.S16PacketEntityLook(this.tracked实体.getEntityId(), (byte)l1, (byte)i2, this.tracked实体.onGround);
                            }
                        }
                        else
                        {
                            packet1 = new S14PacketEntity.S17PacketEntityLookMove(this.tracked实体.getEntityId(), (byte)j2, (byte)k2, (byte)i, (byte)l1, (byte)i2, this.tracked实体.onGround);
                        }
                    }
                    else
                    {
                        this.onGround = this.tracked实体.onGround;
                        this.ticksSinceLastForcedTeleport = 0;
                        packet1 = new S18PacketEntityTeleport(this.tracked实体.getEntityId(), k, j1, k1, (byte)l1, (byte)i2, this.tracked实体.onGround);
                    }
                }

                if (this.sendVelocityUpdates)
                {
                    double d0 = this.tracked实体.通便X - this.lastTrackedEntityMotionX;
                    double d1 = this.tracked实体.motionY - this.lastTrackedEntityMotionY;
                    double d2 = this.tracked实体.通便Z - this.motionZ;
                    double d3 = 0.02D;
                    double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d4 > d3 * d3 || d4 > 0.0D && this.tracked实体.通便X == 0.0D && this.tracked实体.motionY == 0.0D && this.tracked实体.通便Z == 0.0D)
                    {
                        this.lastTrackedEntityMotionX = this.tracked实体.通便X;
                        this.lastTrackedEntityMotionY = this.tracked实体.motionY;
                        this.motionZ = this.tracked实体.通便Z;
                        this.sendPacketToTrackedPlayers(new S12PacketEntityVelocity(this.tracked实体.getEntityId(), this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.motionZ));
                    }
                }

                if (packet1 != null)
                {
                    this.sendPacketToTrackedPlayers(packet1);
                }

                this.sendMetadataToAllAssociatedPlayers();

                if (flag)
                {
                    this.encodedPosX = k;
                    this.encodedPosY = j1;
                    this.encodedPosZ = k1;
                }

                if (flag1)
                {
                    this.encodedRotationYaw = l1;
                    this.encodedRotationPitch = i2;
                }

                this.ridingEntity = false;
            }
            else
            {
                int j = MathHelper.floor_float(this.tracked实体.旋转侧滑 * 256.0F / 360.0F);
                int i1 = MathHelper.floor_float(this.tracked实体.rotationPitch * 256.0F / 360.0F);
                boolean flag2 = Math.abs(j - this.encodedRotationYaw) >= 4 || Math.abs(i1 - this.encodedRotationPitch) >= 4;

                if (flag2)
                {
                    this.sendPacketToTrackedPlayers(new S14PacketEntity.S16PacketEntityLook(this.tracked实体.getEntityId(), (byte)j, (byte)i1, this.tracked实体.onGround));
                    this.encodedRotationYaw = j;
                    this.encodedRotationPitch = i1;
                }

                this.encodedPosX = MathHelper.floor_double(this.tracked实体.X坐标 * 32.0D);
                this.encodedPosY = MathHelper.floor_double(this.tracked实体.Y坐标 * 32.0D);
                this.encodedPosZ = MathHelper.floor_double(this.tracked实体.Z坐标 * 32.0D);
                this.sendMetadataToAllAssociatedPlayers();
                this.ridingEntity = true;
            }

            int l = MathHelper.floor_float(this.tracked实体.getRotationYawHead() * 256.0F / 360.0F);

            if (Math.abs(l - this.lastHeadMotion) >= 4)
            {
                this.sendPacketToTrackedPlayers(new S19PacketEntityHeadLook(this.tracked实体, (byte)l));
                this.lastHeadMotion = l;
            }

            this.tracked实体.isAirBorne = false;
        }

        ++this.updateCounter;

        if (this.tracked实体.velocityChanged)
        {
            this.func_151261_b(new S12PacketEntityVelocity(this.tracked实体));
            this.tracked实体.velocityChanged = false;
        }
    }

    private void sendMetadataToAllAssociatedPlayers()
    {
        DataWatcher datawatcher = this.tracked实体.getDataWatcher();

        if (datawatcher.hasObjectChanged())
        {
            this.func_151261_b(new S1CPacketEntityMetadata(this.tracked实体.getEntityId(), datawatcher, false));
        }

        if (this.tracked实体 instanceof 实体LivingBase)
        {
            ServersideAttributeMap serversideattributemap = (ServersideAttributeMap)((实体LivingBase)this.tracked实体).getAttributeMap();
            Set<IAttributeInstance> set = serversideattributemap.getAttributeInstanceSet();

            if (!set.isEmpty())
            {
                this.func_151261_b(new S20PacketEntityProperties(this.tracked实体.getEntityId(), set));
            }

            set.clear();
        }
    }

    public void sendPacketToTrackedPlayers(Packet packetIn)
    {
        for (实体PlayerMP entityplayermp : this.trackingPlayers)
        {
            entityplayermp.playerNetServerHandler.sendPacket(packetIn);
        }
    }

    public void func_151261_b(Packet packetIn)
    {
        this.sendPacketToTrackedPlayers(packetIn);

        if (this.tracked实体 instanceof 实体PlayerMP)
        {
            ((实体PlayerMP)this.tracked实体).playerNetServerHandler.sendPacket(packetIn);
        }
    }

    public void sendDestroyEntityPacketToTrackedPlayers()
    {
        for (实体PlayerMP entityplayermp : this.trackingPlayers)
        {
            entityplayermp.removeEntity(this.tracked实体);
        }
    }

    public void removeFromTrackedPlayers(实体PlayerMP playerMP)
    {
        if (this.trackingPlayers.contains(playerMP))
        {
            playerMP.removeEntity(this.tracked实体);
            this.trackingPlayers.remove(playerMP);
        }
    }

    public void updatePlayerEntity(实体PlayerMP playerMP)
    {
        if (playerMP != this.tracked实体)
        {
            if (this.func_180233_c(playerMP))
            {
                if (!this.trackingPlayers.contains(playerMP) && (this.isPlayerWatchingThisChunk(playerMP) || this.tracked实体.forceSpawn))
                {
                    this.trackingPlayers.add(playerMP);
                    Packet packet = this.createSpawnPacket();
                    playerMP.playerNetServerHandler.sendPacket(packet);

                    if (!this.tracked实体.getDataWatcher().getIsBlank())
                    {
                        playerMP.playerNetServerHandler.sendPacket(new S1CPacketEntityMetadata(this.tracked实体.getEntityId(), this.tracked实体.getDataWatcher(), true));
                    }

                    NBTTagCompound nbttagcompound = this.tracked实体.getNBTTagCompound();

                    if (nbttagcompound != null)
                    {
                        playerMP.playerNetServerHandler.sendPacket(new S49PacketUpdateEntityNBT(this.tracked实体.getEntityId(), nbttagcompound));
                    }

                    if (this.tracked实体 instanceof 实体LivingBase)
                    {
                        ServersideAttributeMap serversideattributemap = (ServersideAttributeMap)((实体LivingBase)this.tracked实体).getAttributeMap();
                        Collection<IAttributeInstance> collection = serversideattributemap.getWatchedAttributes();

                        if (!collection.isEmpty())
                        {
                            playerMP.playerNetServerHandler.sendPacket(new S20PacketEntityProperties(this.tracked实体.getEntityId(), collection));
                        }
                    }

                    this.lastTrackedEntityMotionX = this.tracked实体.通便X;
                    this.lastTrackedEntityMotionY = this.tracked实体.motionY;
                    this.motionZ = this.tracked实体.通便Z;

                    if (this.sendVelocityUpdates && !(packet instanceof S0FPacketSpawnMob))
                    {
                        playerMP.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(this.tracked实体.getEntityId(), this.tracked实体.通便X, this.tracked实体.motionY, this.tracked实体.通便Z));
                    }

                    if (this.tracked实体.riding实体 != null)
                    {
                        playerMP.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this.tracked实体, this.tracked实体.riding实体));
                    }

                    if (this.tracked实体 instanceof 实体Living && ((实体Living)this.tracked实体).getLeashedToEntity() != null)
                    {
                        playerMP.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(1, this.tracked实体, ((实体Living)this.tracked实体).getLeashedToEntity()));
                    }

                    if (this.tracked实体 instanceof 实体LivingBase)
                    {
                        for (int i = 0; i < 5; ++i)
                        {
                            ItemStack itemstack = ((实体LivingBase)this.tracked实体).getEquipmentInSlot(i);

                            if (itemstack != null)
                            {
                                playerMP.playerNetServerHandler.sendPacket(new S04PacketEntityEquipment(this.tracked实体.getEntityId(), i, itemstack));
                            }
                        }
                    }

                    if (this.tracked实体 instanceof 实体Player)
                    {
                        实体Player entityplayer = (实体Player)this.tracked实体;

                        if (entityplayer.isPlayerSleeping())
                        {
                            playerMP.playerNetServerHandler.sendPacket(new S0APacketUseBed(entityplayer, new 阻止位置(this.tracked实体)));
                        }
                    }

                    if (this.tracked实体 instanceof 实体LivingBase)
                    {
                        实体LivingBase entitylivingbase = (实体LivingBase)this.tracked实体;

                        for (PotionEffect potioneffect : entitylivingbase.getActivePotionEffects())
                        {
                            playerMP.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.tracked实体.getEntityId(), potioneffect));
                        }
                    }
                }
            }
            else if (this.trackingPlayers.contains(playerMP))
            {
                this.trackingPlayers.remove(playerMP);
                playerMP.removeEntity(this.tracked实体);
            }
        }
    }

    public boolean func_180233_c(实体PlayerMP playerMP)
    {
        double d0 = playerMP.X坐标 - (double)(this.encodedPosX / 32);
        double d1 = playerMP.Z坐标 - (double)(this.encodedPosZ / 32);
        return d0 >= (double)(-this.trackingDistanceThreshold) && d0 <= (double)this.trackingDistanceThreshold && d1 >= (double)(-this.trackingDistanceThreshold) && d1 <= (double)this.trackingDistanceThreshold && this.tracked实体.isSpectatedByPlayer(playerMP);
    }

    private boolean isPlayerWatchingThisChunk(实体PlayerMP playerMP)
    {
        return playerMP.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(playerMP, this.tracked实体.chunkCoordX, this.tracked实体.chunkCoordZ);
    }

    public void updatePlayerEntities(List<实体Player> players)
    {
        for (int i = 0; i < players.size(); ++i)
        {
            this.updatePlayerEntity((实体PlayerMP)players.get(i));
        }
    }

    private Packet createSpawnPacket()
    {
        if (this.tracked实体.isDead)
        {
            logger.warn("Fetching addPacket for removed entity");
        }

        if (this.tracked实体 instanceof 实体Item)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 2, 1);
        }
        else if (this.tracked实体 instanceof 实体PlayerMP)
        {
            return new S0CPacketSpawnPlayer((实体Player)this.tracked实体);
        }
        else if (this.tracked实体 instanceof 实体Minecart)
        {
            实体Minecart entityminecart = (实体Minecart)this.tracked实体;
            return new S0EPacketSpawnObject(this.tracked实体, 10, entityminecart.getMinecartType().getNetworkID());
        }
        else if (this.tracked实体 instanceof 实体Boat)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 1);
        }
        else if (this.tracked实体 instanceof IAnimals)
        {
            this.lastHeadMotion = MathHelper.floor_float(this.tracked实体.getRotationYawHead() * 256.0F / 360.0F);
            return new S0FPacketSpawnMob((实体LivingBase)this.tracked实体);
        }
        else if (this.tracked实体 instanceof 实体FishHook)
        {
            实体 实体1 = ((实体FishHook)this.tracked实体).angler;
            return new S0EPacketSpawnObject(this.tracked实体, 90, 实体1 != null ? 实体1.getEntityId() : this.tracked实体.getEntityId());
        }
        else if (this.tracked实体 instanceof 实体Arrow)
        {
            实体 实体 = ((实体Arrow)this.tracked实体).shooting实体;
            return new S0EPacketSpawnObject(this.tracked实体, 60, 实体 != null ? 实体.getEntityId() : this.tracked实体.getEntityId());
        }
        else if (this.tracked实体 instanceof 实体Snowball)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 61);
        }
        else if (this.tracked实体 instanceof 实体Potion)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 73, ((实体Potion)this.tracked实体).getPotionDamage());
        }
        else if (this.tracked实体 instanceof 实体ExpBottle)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 75);
        }
        else if (this.tracked实体 instanceof 实体EnderPearl)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 65);
        }
        else if (this.tracked实体 instanceof 实体EnderEye)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 72);
        }
        else if (this.tracked实体 instanceof 实体FireworkRocket)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 76);
        }
        else if (this.tracked实体 instanceof 实体Fireball)
        {
            实体Fireball entityfireball = (实体Fireball)this.tracked实体;
            S0EPacketSpawnObject s0epacketspawnobject2 = null;
            int i = 63;

            if (this.tracked实体 instanceof 实体SmallFireball)
            {
                i = 64;
            }
            else if (this.tracked实体 instanceof 实体WitherSkull)
            {
                i = 66;
            }

            if (entityfireball.shootingEntity != null)
            {
                s0epacketspawnobject2 = new S0EPacketSpawnObject(this.tracked实体, i, ((实体Fireball)this.tracked实体).shootingEntity.getEntityId());
            }
            else
            {
                s0epacketspawnobject2 = new S0EPacketSpawnObject(this.tracked实体, i, 0);
            }

            s0epacketspawnobject2.setSpeedX((int)(entityfireball.accelerationX * 8000.0D));
            s0epacketspawnobject2.setSpeedY((int)(entityfireball.accelerationY * 8000.0D));
            s0epacketspawnobject2.setSpeedZ((int)(entityfireball.accelerationZ * 8000.0D));
            return s0epacketspawnobject2;
        }
        else if (this.tracked实体 instanceof 实体Egg)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 62);
        }
        else if (this.tracked实体 instanceof 实体TNTPrimed)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 50);
        }
        else if (this.tracked实体 instanceof 实体EnderCrystal)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 51);
        }
        else if (this.tracked实体 instanceof 实体FallingBlock)
        {
            实体FallingBlock entityfallingblock = (实体FallingBlock)this.tracked实体;
            return new S0EPacketSpawnObject(this.tracked实体, 70, Block.getStateId(entityfallingblock.getBlock()));
        }
        else if (this.tracked实体 instanceof 实体ArmorStand)
        {
            return new S0EPacketSpawnObject(this.tracked实体, 78);
        }
        else if (this.tracked实体 instanceof 实体Painting)
        {
            return new S10PacketSpawnPainting((实体Painting)this.tracked实体);
        }
        else if (this.tracked实体 instanceof 实体ItemFrame)
        {
            实体ItemFrame entityitemframe = (实体ItemFrame)this.tracked实体;
            S0EPacketSpawnObject s0epacketspawnobject1 = new S0EPacketSpawnObject(this.tracked实体, 71, entityitemframe.facingDirection.getHorizontalIndex());
            阻止位置 blockpos1 = entityitemframe.getHangingPosition();
            s0epacketspawnobject1.setX(MathHelper.floor_float((float)(blockpos1.getX() * 32)));
            s0epacketspawnobject1.setY(MathHelper.floor_float((float)(blockpos1.getY() * 32)));
            s0epacketspawnobject1.setZ(MathHelper.floor_float((float)(blockpos1.getZ() * 32)));
            return s0epacketspawnobject1;
        }
        else if (this.tracked实体 instanceof 实体LeashKnot)
        {
            实体LeashKnot entityleashknot = (实体LeashKnot)this.tracked实体;
            S0EPacketSpawnObject s0epacketspawnobject = new S0EPacketSpawnObject(this.tracked实体, 77);
            阻止位置 blockpos = entityleashknot.getHangingPosition();
            s0epacketspawnobject.setX(MathHelper.floor_float((float)(blockpos.getX() * 32)));
            s0epacketspawnobject.setY(MathHelper.floor_float((float)(blockpos.getY() * 32)));
            s0epacketspawnobject.setZ(MathHelper.floor_float((float)(blockpos.getZ() * 32)));
            return s0epacketspawnobject;
        }
        else if (this.tracked实体 instanceof 实体XPOrb)
        {
            return new S11PacketSpawnExperienceOrb((实体XPOrb)this.tracked实体);
        }
        else
        {
            throw new IllegalArgumentException("Don\'t know how to add " + this.tracked实体.getClass() + "!");
        }
    }

    public void removeTrackedPlayerSymmetric(实体PlayerMP playerMP)
    {
        if (this.trackingPlayers.contains(playerMP))
        {
            this.trackingPlayers.remove(playerMP);
            playerMP.removeEntity(this.tracked实体);
        }
    }
}
