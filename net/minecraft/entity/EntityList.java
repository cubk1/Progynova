package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.ai.实体MinecartMobSpawner;
import net.minecraft.entity.boss.实体Dragon;
import net.minecraft.entity.boss.实体Wither;
import net.minecraft.entity.effect.实体LightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.item.实体EnderCrystal;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.实体MagmaCube;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.实体Bat;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.实体Arrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityList
{
    private static final Logger logger = LogManager.getLogger();
    private static final Map < String, Class <? extends 实体>> stringToClassMapping = Maps. < String, Class <? extends 实体>> newHashMap();
    private static final Map < Class <? extends 实体> , String > classToStringMapping = Maps. < Class <? extends 实体> , String > newHashMap();
    private static final Map < Integer, Class <? extends 实体>> idToClassMapping = Maps. < Integer, Class <? extends 实体>> newHashMap();
    private static final Map < Class <? extends 实体> , Integer > classToIDMapping = Maps. < Class <? extends 实体> , Integer > newHashMap();
    private static final Map<String, Integer> stringToIDMapping = Maps.<String, Integer>newHashMap();
    public static final Map<Integer, EntityList.EntityEggInfo> entityEggs = Maps.<Integer, EntityList.EntityEggInfo>newLinkedHashMap();

    private static void addMapping(Class <? extends 实体> entityClass, String entityName, int id)
    {
        if (stringToClassMapping.containsKey(entityName))
        {
            throw new IllegalArgumentException("ID is already registered: " + entityName);
        }
        else if (idToClassMapping.containsKey(Integer.valueOf(id)))
        {
            throw new IllegalArgumentException("ID is already registered: " + id);
        }
        else if (id == 0)
        {
            throw new IllegalArgumentException("Cannot register to reserved id: " + id);
        }
        else if (entityClass == null)
        {
            throw new IllegalArgumentException("Cannot register null clazz for id: " + id);
        }
        else
        {
            stringToClassMapping.put(entityName, entityClass);
            classToStringMapping.put(entityClass, entityName);
            idToClassMapping.put(Integer.valueOf(id), entityClass);
            classToIDMapping.put(entityClass, Integer.valueOf(id));
            stringToIDMapping.put(entityName, Integer.valueOf(id));
        }
    }

    private static void addMapping(Class <? extends 实体> entityClass, String entityName, int entityID, int baseColor, int spotColor)
    {
        addMapping(entityClass, entityName, entityID);
        entityEggs.put(Integer.valueOf(entityID), new EntityList.EntityEggInfo(entityID, baseColor, spotColor));
    }

    public static 实体 createEntityByName(String entityName, World worldIn)
    {
        实体 实体 = null;

        try
        {
            Class <? extends 实体> oclass = (Class)stringToClassMapping.get(entityName);

            if (oclass != null)
            {
                实体 = (实体)oclass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldIn});
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return 实体;
    }

    public static 实体 createEntityFromNBT(NBTTagCompound nbt, World worldIn)
    {
        实体 实体 = null;

        if ("Minecart".equals(nbt.getString("id")))
        {
            nbt.setString("id", 实体Minecart.EnumMinecartType.byNetworkID(nbt.getInteger("Type")).getName());
            nbt.removeTag("Type");
        }

        try
        {
            Class <? extends 实体> oclass = (Class)stringToClassMapping.get(nbt.getString("id"));

            if (oclass != null)
            {
                实体 = (实体)oclass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldIn});
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        if (实体 != null)
        {
            实体.readFromNBT(nbt);
        }
        else
        {
            logger.warn("Skipping Entity with id " + nbt.getString("id"));
        }

        return 实体;
    }

    public static 实体 createEntityByID(int entityID, World worldIn)
    {
        实体 实体 = null;

        try
        {
            Class <? extends 实体> oclass = getClassFromID(entityID);

            if (oclass != null)
            {
                实体 = (实体)oclass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldIn});
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        if (实体 == null)
        {
            logger.warn("Skipping Entity with id " + entityID);
        }

        return 实体;
    }

    public static int getEntityID(实体 实体In)
    {
        Integer integer = (Integer)classToIDMapping.get(实体In.getClass());
        return integer == null ? 0 : integer.intValue();
    }

    public static Class <? extends 实体> getClassFromID(int entityID)
    {
        return (Class)idToClassMapping.get(Integer.valueOf(entityID));
    }

    public static String getEntityString(实体 实体In)
    {
        return (String)classToStringMapping.get(实体In.getClass());
    }

    public static int getIDFromString(String entityName)
    {
        Integer integer = (Integer)stringToIDMapping.get(entityName);
        return integer == null ? 90 : integer.intValue();
    }

    public static String getStringFromID(int entityID)
    {
        return (String)classToStringMapping.get(getClassFromID(entityID));
    }

    public static void func_151514_a()
    {
    }

    public static List<String> getEntityNameList()
    {
        Set<String> set = stringToClassMapping.keySet();
        List<String> list = Lists.<String>newArrayList();

        for (String s : set)
        {
            Class <? extends 实体> oclass = (Class)stringToClassMapping.get(s);

            if ((oclass.getModifiers() & 1024) != 1024)
            {
                list.add(s);
            }
        }

        list.add("LightningBolt");
        return list;
    }

    public static boolean isStringEntityName(实体 实体In, String entityName)
    {
        String s = getEntityString(实体In);

        if (s == null && 实体In instanceof 实体Player)
        {
            s = "Player";
        }
        else if (s == null && 实体In instanceof 实体LightningBolt)
        {
            s = "LightningBolt";
        }

        return entityName.equals(s);
    }

    public static boolean isStringValidEntityName(String entityName)
    {
        return "Player".equals(entityName) || getEntityNameList().contains(entityName);
    }

    static
    {
        addMapping(实体Item.class, "Item", 1);
        addMapping(实体XPOrb.class, "XPOrb", 2);
        addMapping(实体Egg.class, "ThrownEgg", 7);
        addMapping(实体LeashKnot.class, "LeashKnot", 8);
        addMapping(实体Painting.class, "Painting", 9);
        addMapping(实体Arrow.class, "Arrow", 10);
        addMapping(实体Snowball.class, "Snowball", 11);
        addMapping(实体LargeFireball.class, "Fireball", 12);
        addMapping(实体SmallFireball.class, "SmallFireball", 13);
        addMapping(实体EnderPearl.class, "ThrownEnderpearl", 14);
        addMapping(实体EnderEye.class, "EyeOfEnderSignal", 15);
        addMapping(实体Potion.class, "ThrownPotion", 16);
        addMapping(实体ExpBottle.class, "ThrownExpBottle", 17);
        addMapping(实体ItemFrame.class, "ItemFrame", 18);
        addMapping(实体WitherSkull.class, "WitherSkull", 19);
        addMapping(实体TNTPrimed.class, "PrimedTnt", 20);
        addMapping(实体FallingBlock.class, "FallingSand", 21);
        addMapping(实体FireworkRocket.class, "FireworksRocketEntity", 22);
        addMapping(实体ArmorStand.class, "ArmorStand", 30);
        addMapping(实体Boat.class, "Boat", 41);
        addMapping(实体MinecartEmpty.class, 实体Minecart.EnumMinecartType.RIDEABLE.getName(), 42);
        addMapping(实体MinecartChest.class, 实体Minecart.EnumMinecartType.CHEST.getName(), 43);
        addMapping(实体MinecartFurnace.class, 实体Minecart.EnumMinecartType.FURNACE.getName(), 44);
        addMapping(实体MinecartTNT.class, 实体Minecart.EnumMinecartType.TNT.getName(), 45);
        addMapping(实体MinecartHopper.class, 实体Minecart.EnumMinecartType.HOPPER.getName(), 46);
        addMapping(实体MinecartMobSpawner.class, 实体Minecart.EnumMinecartType.SPAWNER.getName(), 47);
        addMapping(实体MinecartCommandBlock.class, 实体Minecart.EnumMinecartType.COMMAND_BLOCK.getName(), 40);
        addMapping(实体Living.class, "Mob", 48);
        addMapping(实体Mob.class, "Monster", 49);
        addMapping(实体Creeper.class, "Creeper", 50, 894731, 0);
        addMapping(实体Skeleton.class, "Skeleton", 51, 12698049, 4802889);
        addMapping(实体Spider.class, "Spider", 52, 3419431, 11013646);
        addMapping(实体GiantZombie.class, "Giant", 53);
        addMapping(实体Zombie.class, "Zombie", 54, 44975, 7969893);
        addMapping(实体Slime.class, "Slime", 55, 5349438, 8306542);
        addMapping(实体Ghast.class, "Ghast", 56, 16382457, 12369084);
        addMapping(实体PigZombie.class, "PigZombie", 57, 15373203, 5009705);
        addMapping(实体Enderman.class, "Enderman", 58, 1447446, 0);
        addMapping(实体CaveSpider.class, "CaveSpider", 59, 803406, 11013646);
        addMapping(实体Silverfish.class, "Silverfish", 60, 7237230, 3158064);
        addMapping(实体Blaze.class, "Blaze", 61, 16167425, 16775294);
        addMapping(实体MagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
        addMapping(实体Dragon.class, "EnderDragon", 63);
        addMapping(实体Wither.class, "WitherBoss", 64);
        addMapping(实体Bat.class, "Bat", 65, 4996656, 986895);
        addMapping(实体Witch.class, "Witch", 66, 3407872, 5349438);
        addMapping(实体Endermite.class, "Endermite", 67, 1447446, 7237230);
        addMapping(实体Guardian.class, "Guardian", 68, 5931634, 15826224);
        addMapping(实体Pig.class, "Pig", 90, 15771042, 14377823);
        addMapping(实体Sheep.class, "Sheep", 91, 15198183, 16758197);
        addMapping(实体Cow.class, "Cow", 92, 4470310, 10592673);
        addMapping(实体Chicken.class, "Chicken", 93, 10592673, 16711680);
        addMapping(实体Squid.class, "Squid", 94, 2243405, 7375001);
        addMapping(实体Wolf.class, "Wolf", 95, 14144467, 13545366);
        addMapping(实体Mooshroom.class, "MushroomCow", 96, 10489616, 12040119);
        addMapping(实体Snowman.class, "SnowMan", 97);
        addMapping(实体Ocelot.class, "Ozelot", 98, 15720061, 5653556);
        addMapping(实体IronGolem.class, "VillagerGolem", 99);
        addMapping(实体Horse.class, "EntityHorse", 100, 12623485, 15656192);
        addMapping(实体Rabbit.class, "Rabbit", 101, 10051392, 7555121);
        addMapping(实体Villager.class, "Villager", 120, 5651507, 12422002);
        addMapping(实体EnderCrystal.class, "EnderCrystal", 200);
    }

    public static class EntityEggInfo
    {
        public final int spawnedID;
        public final int primaryColor;
        public final int secondaryColor;
        public final StatBase field_151512_d;
        public final StatBase field_151513_e;

        public EntityEggInfo(int id, int baseColor, int spotColor)
        {
            this.spawnedID = id;
            this.primaryColor = baseColor;
            this.secondaryColor = spotColor;
            this.field_151512_d = StatList.getStatKillEntity(this);
            this.field_151513_e = StatList.getStatEntityKilledBy(this);
        }
    }
}
