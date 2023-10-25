package net.optifine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.item.实体Item;
import net.minecraft.entity.item.实体TNTPrimed;
import net.minecraft.entity.monster.实体Creeper;
import net.minecraft.entity.monster.实体MagmaCube;
import net.minecraft.entity.projectile.实体Fireball;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.monster.实体Blaze;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.util.阻止位置;
import net.minecraft.util.图像位置;
import net.minecraft.world.World;
import net.optifine.config.ConnectedParser;
import net.optifine.config.EntityClassLocator;
import net.optifine.config.IObjectLocator;
import net.optifine.config.ItemLocator;
import net.optifine.reflect.ReflectorForge;
import net.optifine.util.PropertiesOrdered;

public class DynamicLights
{
    private static DynamicLightsMap mapDynamicLights = new DynamicLightsMap();
    private static Map<Class, Integer> mapEntityLightLevels = new HashMap();
    private static Map<Item, Integer> mapItemLightLevels = new HashMap();
    private static long timeUpdateMs = 0L;
    private static final double MAX_DIST = 7.5D;
    private static final double MAX_DIST_SQ = 56.25D;
    private static final int LIGHT_LEVEL_MAX = 15;
    private static final int LIGHT_LEVEL_FIRE = 15;
    private static final int LIGHT_LEVEL_BLAZE = 10;
    private static final int LIGHT_LEVEL_MAGMA_CUBE = 8;
    private static final int LIGHT_LEVEL_MAGMA_CUBE_CORE = 13;
    private static final int LIGHT_LEVEL_GLOWSTONE_DUST = 8;
    private static final int LIGHT_LEVEL_PRISMARINE_CRYSTALS = 8;
    private static boolean initialized;

    public static void entityAdded(实体 实体In, RenderGlobal renderGlobal)
    {
    }

    public static void entityRemoved(实体 实体In, RenderGlobal renderGlobal)
    {
        synchronized (mapDynamicLights)
        {
            DynamicLight dynamiclight = mapDynamicLights.remove(实体In.getEntityId());

            if (dynamiclight != null)
            {
                dynamiclight.updateLitChunks(renderGlobal);
            }
        }
    }

    public static void update(RenderGlobal renderGlobal)
    {
        long i = System.currentTimeMillis();

        if (i >= timeUpdateMs + 50L)
        {
            timeUpdateMs = i;

            if (!initialized)
            {
                initialize();
            }

            synchronized (mapDynamicLights)
            {
                updateMapDynamicLights(renderGlobal);

                if (mapDynamicLights.size() > 0)
                {
                    List<DynamicLight> list = mapDynamicLights.valueList();

                    for (int j = 0; j < list.size(); ++j)
                    {
                        DynamicLight dynamiclight = (DynamicLight)list.get(j);
                        dynamiclight.update(renderGlobal);
                    }
                }
            }
        }
    }

    private static void initialize()
    {
        initialized = true;
        mapEntityLightLevels.clear();
        mapItemLightLevels.clear();
        String[] astring = ReflectorForge.getForgeModIds();

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];

            try
            {
                图像位置 resourcelocation = new 图像位置(s, "optifine/dynamic_lights.properties");
                InputStream inputstream = Config.getResourceStream(resourcelocation);
                loadModConfiguration(inputstream, resourcelocation.toString(), s);
            }
            catch (IOException var5)
            {
                ;
            }
        }

        if (mapEntityLightLevels.size() > 0)
        {
            Config.dbg("DynamicLights entities: " + mapEntityLightLevels.size());
        }

        if (mapItemLightLevels.size() > 0)
        {
            Config.dbg("DynamicLights items: " + mapItemLightLevels.size());
        }
    }

    private static void loadModConfiguration(InputStream in, String path, String modId)
    {
        if (in != null)
        {
            try
            {
                Properties properties = new PropertiesOrdered();
                properties.load(in);
                in.close();
                Config.dbg("DynamicLights: Parsing " + path);
                ConnectedParser connectedparser = new ConnectedParser("DynamicLights");
                loadModLightLevels(properties.getProperty("entities"), mapEntityLightLevels, new EntityClassLocator(), connectedparser, path, modId);
                loadModLightLevels(properties.getProperty("items"), mapItemLightLevels, new ItemLocator(), connectedparser, path, modId);
            }
            catch (IOException var5)
            {
                Config.warn("DynamicLights: Error reading " + path);
            }
        }
    }

    private static void loadModLightLevels(String prop, Map mapLightLevels, IObjectLocator ol, ConnectedParser cp, String path, String modId)
    {
        if (prop != null)
        {
            String[] astring = Config.tokenize(prop, " ");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                String[] astring1 = Config.tokenize(s, ":");

                if (astring1.length != 2)
                {
                    cp.warn("Invalid entry: " + s + ", in:" + path);
                }
                else
                {
                    String s1 = astring1[0];
                    String s2 = astring1[1];
                    String s3 = modId + ":" + s1;
                    图像位置 resourcelocation = new 图像位置(s3);
                    Object object = ol.getObject(resourcelocation);

                    if (object == null)
                    {
                        cp.warn("Object not found: " + s3);
                    }
                    else
                    {
                        int j = cp.parseInt(s2, -1);

                        if (j >= 0 && j <= 15)
                        {
                            mapLightLevels.put(object, new Integer(j));
                        }
                        else
                        {
                            cp.warn("Invalid light level: " + s);
                        }
                    }
                }
            }
        }
    }

    private static void updateMapDynamicLights(RenderGlobal renderGlobal)
    {
        World world = renderGlobal.getWorld();

        if (world != null)
        {
            for (实体 实体 : world.getLoadedEntityList())
            {
                int i = getLightLevel(实体);

                if (i > 0)
                {
                    int j = 实体.getEntityId();
                    DynamicLight dynamiclight = mapDynamicLights.get(j);

                    if (dynamiclight == null)
                    {
                        dynamiclight = new DynamicLight(实体);
                        mapDynamicLights.put(j, dynamiclight);
                    }
                }
                else
                {
                    int k = 实体.getEntityId();
                    DynamicLight dynamiclight1 = mapDynamicLights.remove(k);

                    if (dynamiclight1 != null)
                    {
                        dynamiclight1.updateLitChunks(renderGlobal);
                    }
                }
            }
        }
    }

    public static int getCombinedLight(阻止位置 pos, int combinedLight)
    {
        double d0 = getLightLevel(pos);
        combinedLight = getCombinedLight(d0, combinedLight);
        return combinedLight;
    }

    public static int getCombinedLight(实体 实体, int combinedLight)
    {
        double d0 = (double)getLightLevel(实体);
        combinedLight = getCombinedLight(d0, combinedLight);
        return combinedLight;
    }

    public static int getCombinedLight(double lightPlayer, int combinedLight)
    {
        if (lightPlayer > 0.0D)
        {
            int i = (int)(lightPlayer * 16.0D);
            int j = combinedLight & 255;

            if (i > j)
            {
                combinedLight = combinedLight & -256;
                combinedLight = combinedLight | i;
            }
        }

        return combinedLight;
    }

    public static double getLightLevel(阻止位置 pos)
    {
        double d0 = 0.0D;

        synchronized (mapDynamicLights)
        {
            List<DynamicLight> list = mapDynamicLights.valueList();
            int i = list.size();

            for (int j = 0; j < i; ++j)
            {
                DynamicLight dynamiclight = (DynamicLight)list.get(j);
                int k = dynamiclight.getLastLightLevel();

                if (k > 0)
                {
                    double d1 = dynamiclight.getLastPosX();
                    double d2 = dynamiclight.getLastPosY();
                    double d3 = dynamiclight.getLastPosZ();
                    double d4 = (double)pos.getX() - d1;
                    double d5 = (double)pos.getY() - d2;
                    double d6 = (double)pos.getZ() - d3;
                    double d7 = d4 * d4 + d5 * d5 + d6 * d6;

                    if (dynamiclight.isUnderwater() && !Config.isClearWater())
                    {
                        k = Config.limit(k - 2, 0, 15);
                        d7 *= 2.0D;
                    }

                    if (d7 <= 56.25D)
                    {
                        double d8 = Math.sqrt(d7);
                        double d9 = 1.0D - d8 / 7.5D;
                        double d10 = d9 * (double)k;

                        if (d10 > d0)
                        {
                            d0 = d10;
                        }
                    }
                }
            }
        }

        double d11 = Config.limit(d0, 0.0D, 15.0D);
        return d11;
    }

    public static int getLightLevel(ItemStack itemStack)
    {
        if (itemStack == null)
        {
            return 0;
        }
        else
        {
            Item item = itemStack.getItem();

            if (item instanceof ItemBlock)
            {
                ItemBlock itemblock = (ItemBlock)item;
                Block block = itemblock.getBlock();

                if (block != null)
                {
                    return block.getLightValue();
                }
            }

            if (item == Items.lava_bucket)
            {
                return Blocks.lava.getLightValue();
            }
            else if (item != Items.blaze_rod && item != Items.blaze_powder)
            {
                if (item == Items.glowstone_dust)
                {
                    return 8;
                }
                else if (item == Items.prismarine_crystals)
                {
                    return 8;
                }
                else if (item == Items.magma_cream)
                {
                    return 8;
                }
                else if (item == Items.nether_star)
                {
                    return Blocks.beacon.getLightValue() / 2;
                }
                else
                {
                    if (!mapItemLightLevels.isEmpty())
                    {
                        Integer integer = (Integer)mapItemLightLevels.get(item);

                        if (integer != null)
                        {
                            return integer.intValue();
                        }
                    }

                    return 0;
                }
            }
            else
            {
                return 10;
            }
        }
    }

    public static int getLightLevel(实体 实体)
    {
        if (实体 == Config.getMinecraft().getRenderViewEntity() && !Config.isDynamicHandLight())
        {
            return 0;
        }
        else
        {
            if (实体 instanceof 实体Player)
            {
                实体Player entityplayer = (实体Player) 实体;

                if (entityplayer.isSpectator())
                {
                    return 0;
                }
            }

            if (实体.isBurning())
            {
                return 15;
            }
            else
            {
                if (!mapEntityLightLevels.isEmpty())
                {
                    Integer integer = (Integer)mapEntityLightLevels.get(实体.getClass());

                    if (integer != null)
                    {
                        return integer.intValue();
                    }
                }

                if (实体 instanceof 实体Fireball)
                {
                    return 15;
                }
                else if (实体 instanceof 实体TNTPrimed)
                {
                    return 15;
                }
                else if (实体 instanceof 实体Blaze)
                {
                    实体Blaze entityblaze = (实体Blaze) 实体;
                    return entityblaze.func_70845_n() ? 15 : 10;
                }
                else if (实体 instanceof 实体MagmaCube)
                {
                    实体MagmaCube entitymagmacube = (实体MagmaCube) 实体;
                    return (double)entitymagmacube.squishFactor > 0.6D ? 13 : 8;
                }
                else
                {
                    if (实体 instanceof 实体Creeper)
                    {
                        实体Creeper entitycreeper = (实体Creeper) 实体;

                        if ((double)entitycreeper.getCreeperFlashIntensity(0.0F) > 0.001D)
                        {
                            return 15;
                        }
                    }

                    if (实体 instanceof 实体LivingBase)
                    {
                        实体LivingBase entitylivingbase = (实体LivingBase) 实体;
                        ItemStack itemstack2 = entitylivingbase.getHeldItem();
                        int i = getLightLevel(itemstack2);
                        ItemStack itemstack1 = entitylivingbase.getEquipmentInSlot(4);
                        int j = getLightLevel(itemstack1);
                        return Math.max(i, j);
                    }
                    else if (实体 instanceof 实体Item)
                    {
                        实体Item entityitem = (实体Item) 实体;
                        ItemStack itemstack = getItemStack(entityitem);
                        return getLightLevel(itemstack);
                    }
                    else
                    {
                        return 0;
                    }
                }
            }
        }
    }

    public static void removeLights(RenderGlobal renderGlobal)
    {
        synchronized (mapDynamicLights)
        {
            List<DynamicLight> list = mapDynamicLights.valueList();

            for (int i = 0; i < list.size(); ++i)
            {
                DynamicLight dynamiclight = (DynamicLight)list.get(i);
                dynamiclight.updateLitChunks(renderGlobal);
            }

            mapDynamicLights.clear();
        }
    }

    public static void clear()
    {
        synchronized (mapDynamicLights)
        {
            mapDynamicLights.clear();
        }
    }

    public static int getCount()
    {
        synchronized (mapDynamicLights)
        {
            return mapDynamicLights.size();
        }
    }

    public static ItemStack getItemStack(实体Item entityItem)
    {
        ItemStack itemstack = entityItem.getDataWatcher().getWatchableObjectItemStack(10);
        return itemstack;
    }
}
