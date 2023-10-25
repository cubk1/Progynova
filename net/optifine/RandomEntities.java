package net.optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.passive.实体Villager;
import net.minecraft.entity.实体;
import net.minecraft.entity.passive.实体Horse;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.图像位置;
import net.minecraft.world.World;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorRaw;
import net.optifine.util.IntegratedServerUtils;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.ResUtils;
import net.optifine.util.StrUtils;

public class RandomEntities
{
    private static Map<String, RandomEntityProperties> mapProperties = new HashMap();
    private static boolean active = false;
    private static RenderGlobal renderGlobal;
    private static RandomEntity randomEntity = new RandomEntity();
    private static TileEntityRendererDispatcher tileEntityRendererDispatcher;
    private static RandomTileEntity randomTileEntity = new RandomTileEntity();
    private static boolean working = false;
    public static final String SUFFIX_PNG = ".png";
    public static final String SUFFIX_PROPERTIES = ".properties";
    public static final String PREFIX_TEXTURES_ENTITY = "textures/entity/";
    public static final String PREFIX_TEXTURES_PAINTING = "textures/painting/";
    public static final String PREFIX_TEXTURES = "textures/";
    public static final String PREFIX_OPTIFINE_RANDOM = "optifine/random/";
    public static final String PREFIX_MCPATCHER_MOB = "mcpatcher/mob/";
    private static final String[] DEPENDANT_SUFFIXES = new String[] {"_armor", "_eyes", "_exploding", "_shooting", "_fur", "_eyes", "_invulnerable", "_angry", "_tame", "_collar"};
    private static final String PREFIX_DYNAMIC_TEXTURE_HORSE = "horse/";
    private static final String[] HORSE_TEXTURES = (String[])((String[])ReflectorRaw.getFieldValue((Object)null, 实体Horse.class, String[].class, 2));
    private static final String[] HORSE_TEXTURES_ABBR = (String[])((String[])ReflectorRaw.getFieldValue((Object)null, 实体Horse.class, String[].class, 3));

    public static void entityLoaded(实体 实体, World world)
    {
        if (world != null)
        {
            DataWatcher datawatcher = 实体.getDataWatcher();
            datawatcher.spawnPosition = 实体.getPosition();
            datawatcher.spawnBiome = world.getBiomeGenForCoords(datawatcher.spawnPosition);
            UUID uuid = 实体.getUniqueID();

            if (实体 instanceof 实体Villager)
            {
                updateEntityVillager(uuid, (实体Villager) 实体);
            }
        }
    }

    public static void entityUnloaded(实体 实体, World world)
    {
    }

    private static void updateEntityVillager(UUID uuid, 实体Villager ev)
    {
        实体 实体 = IntegratedServerUtils.getEntity(uuid);

        if (实体 instanceof 实体Villager)
        {
            实体Villager entityvillager = (实体Villager) 实体;
            int i = entityvillager.getProfession();
            ev.setProfession(i);
            int j = Reflector.getFieldValueInt(entityvillager, Reflector.EntityVillager_careerId, 0);
            Reflector.setFieldValueInt(ev, Reflector.EntityVillager_careerId, j);
            int k = Reflector.getFieldValueInt(entityvillager, Reflector.EntityVillager_careerLevel, 0);
            Reflector.setFieldValueInt(ev, Reflector.EntityVillager_careerLevel, k);
        }
    }

    public static void worldChanged(World oldWorld, World newWorld)
    {
        if (newWorld != null)
        {
            List list = newWorld.getLoadedEntityList();

            for (int i = 0; i < list.size(); ++i)
            {
                实体 实体 = (实体)list.get(i);
                entityLoaded(实体, newWorld);
            }
        }

        randomEntity.setEntity((实体)null);
        randomTileEntity.setTileEntity((TileEntity)null);
    }

    public static 图像位置 getTextureLocation(图像位置 loc)
    {
        if (!active)
        {
            return loc;
        }
        else if (working)
        {
            return loc;
        }
        else
        {
            图像位置 name;

            try
            {
                working = true;
                IRandomEntity irandomentity = getRandomEntityRendered();

                if (irandomentity != null)
                {
                    String s = loc.getResourcePath();

                    if (s.startsWith("horse/"))
                    {
                        s = getHorseTexturePath(s, "horse/".length());
                    }

                    if (!s.startsWith("textures/entity/") && !s.startsWith("textures/painting/"))
                    {
                        图像位置 resourcelocation2 = loc;
                        return resourcelocation2;
                    }

                    RandomEntityProperties randomentityproperties = (RandomEntityProperties)mapProperties.get(s);

                    if (randomentityproperties == null)
                    {
                        图像位置 resourcelocation3 = loc;
                        return resourcelocation3;
                    }

                    图像位置 resourcelocation1 = randomentityproperties.getTextureLocation(loc, irandomentity);
                    return resourcelocation1;
                }

                name = loc;
            }
            finally
            {
                working = false;
            }

            return name;
        }
    }

    private static String getHorseTexturePath(String path, int pos)
    {
        if (HORSE_TEXTURES != null && HORSE_TEXTURES_ABBR != null)
        {
            for (int i = 0; i < HORSE_TEXTURES_ABBR.length; ++i)
            {
                String s = HORSE_TEXTURES_ABBR[i];

                if (path.startsWith(s, pos))
                {
                    return HORSE_TEXTURES[i];
                }
            }

            return path;
        }
        else
        {
            return path;
        }
    }

    private static IRandomEntity getRandomEntityRendered()
    {
        if (renderGlobal.rendered实体 != null)
        {
            randomEntity.setEntity(renderGlobal.rendered实体);
            return randomEntity;
        }
        else
        {
            if (tileEntityRendererDispatcher.tileEntityRendered != null)
            {
                TileEntity tileentity = tileEntityRendererDispatcher.tileEntityRendered;

                if (tileentity.getWorld() != null)
                {
                    randomTileEntity.setTileEntity(tileentity);
                    return randomTileEntity;
                }
            }

            return null;
        }
    }

    private static RandomEntityProperties makeProperties(图像位置 loc, boolean mcpatcher)
    {
        String s = loc.getResourcePath();
        图像位置 resourcelocation = getLocationProperties(loc, mcpatcher);

        if (resourcelocation != null)
        {
            RandomEntityProperties randomentityproperties = parseProperties(resourcelocation, loc);

            if (randomentityproperties != null)
            {
                return randomentityproperties;
            }
        }

        图像位置[] aresourcelocation = getLocationsVariants(loc, mcpatcher);
        return aresourcelocation == null ? null : new RandomEntityProperties(s, aresourcelocation);
    }

    private static RandomEntityProperties parseProperties(图像位置 propLoc, 图像位置 resLoc)
    {
        try
        {
            String s = propLoc.getResourcePath();
            dbg(resLoc.getResourcePath() + ", properties: " + s);
            InputStream inputstream = Config.getResourceStream(propLoc);

            if (inputstream == null)
            {
                warn("Properties not found: " + s);
                return null;
            }
            else
            {
                Properties properties = new PropertiesOrdered();
                properties.load(inputstream);
                inputstream.close();
                RandomEntityProperties randomentityproperties = new RandomEntityProperties(properties, s, resLoc);
                return !randomentityproperties.isValid(s) ? null : randomentityproperties;
            }
        }
        catch (FileNotFoundException var6)
        {
            warn("File not found: " + resLoc.getResourcePath());
            return null;
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
            return null;
        }
    }

    private static 图像位置 getLocationProperties(图像位置 loc, boolean mcpatcher)
    {
        图像位置 resourcelocation = getLocationRandom(loc, mcpatcher);

        if (resourcelocation == null)
        {
            return null;
        }
        else
        {
            String s = resourcelocation.getResourceDomain();
            String s1 = resourcelocation.getResourcePath();
            String s2 = StrUtils.removeSuffix(s1, ".png");
            String s3 = s2 + ".properties";
            图像位置 resourcelocation1 = new 图像位置(s, s3);

            if (Config.hasResource(resourcelocation1))
            {
                return resourcelocation1;
            }
            else
            {
                String s4 = getParentTexturePath(s2);

                if (s4 == null)
                {
                    return null;
                }
                else
                {
                    图像位置 resourcelocation2 = new 图像位置(s, s4 + ".properties");
                    return Config.hasResource(resourcelocation2) ? resourcelocation2 : null;
                }
            }
        }
    }

    protected static 图像位置 getLocationRandom(图像位置 loc, boolean mcpatcher)
    {
        String s = loc.getResourceDomain();
        String s1 = loc.getResourcePath();
        String s2 = "textures/";
        String s3 = "optifine/random/";

        if (mcpatcher)
        {
            s2 = "textures/entity/";
            s3 = "mcpatcher/mob/";
        }

        if (!s1.startsWith(s2))
        {
            return null;
        }
        else
        {
            String s4 = StrUtils.replacePrefix(s1, s2, s3);
            return new 图像位置(s, s4);
        }
    }

    private static String getPathBase(String pathRandom)
    {
        return pathRandom.startsWith("optifine/random/") ? StrUtils.replacePrefix(pathRandom, "optifine/random/", "textures/") : (pathRandom.startsWith("mcpatcher/mob/") ? StrUtils.replacePrefix(pathRandom, "mcpatcher/mob/", "textures/entity/") : null);
    }

    protected static 图像位置 getLocationIndexed(图像位置 loc, int index)
    {
        if (loc == null)
        {
            return null;
        }
        else
        {
            String s = loc.getResourcePath();
            int i = s.lastIndexOf(46);

            if (i < 0)
            {
                return null;
            }
            else
            {
                String s1 = s.substring(0, i);
                String s2 = s.substring(i);
                String s3 = s1 + index + s2;
                图像位置 resourcelocation = new 图像位置(loc.getResourceDomain(), s3);
                return resourcelocation;
            }
        }
    }

    private static String getParentTexturePath(String path)
    {
        for (int i = 0; i < DEPENDANT_SUFFIXES.length; ++i)
        {
            String s = DEPENDANT_SUFFIXES[i];

            if (path.endsWith(s))
            {
                String s1 = StrUtils.removeSuffix(path, s);
                return s1;
            }
        }

        return null;
    }

    private static 图像位置[] getLocationsVariants(图像位置 loc, boolean mcpatcher)
    {
        List list = new ArrayList();
        list.add(loc);
        图像位置 resourcelocation = getLocationRandom(loc, mcpatcher);

        if (resourcelocation == null)
        {
            return null;
        }
        else
        {
            for (int i = 1; i < ((List)list).size() + 10; ++i)
            {
                int j = i + 1;
                图像位置 resourcelocation1 = getLocationIndexed(resourcelocation, j);

                if (Config.hasResource(resourcelocation1))
                {
                    list.add(resourcelocation1);
                }
            }

            if (list.size() <= 1)
            {
                return null;
            }
            else
            {
                图像位置[] aresourcelocation = (图像位置[])((图像位置[])list.toArray(new 图像位置[list.size()]));
                dbg(loc.getResourcePath() + ", variants: " + aresourcelocation.length);
                return aresourcelocation;
            }
        }
    }

    public static void update()
    {
        mapProperties.clear();
        active = false;

        if (Config.isRandomEntities())
        {
            initialize();
        }
    }

    private static void initialize()
    {
        renderGlobal = Config.getRenderGlobal();
        tileEntityRendererDispatcher = TileEntityRendererDispatcher.instance;
        String[] astring = new String[] {"optifine/random/", "mcpatcher/mob/"};
        String[] astring1 = new String[] {".png", ".properties"};
        String[] astring2 = ResUtils.collectFiles(astring, astring1);
        Set set = new HashSet();

        for (int i = 0; i < astring2.length; ++i)
        {
            String s = astring2[i];
            s = StrUtils.removeSuffix(s, astring1);
            s = StrUtils.trimTrailing(s, "0123456789");
            s = s + ".png";
            String s1 = getPathBase(s);

            if (!set.contains(s1))
            {
                set.add(s1);
                图像位置 resourcelocation = new 图像位置(s1);

                if (Config.hasResource(resourcelocation))
                {
                    RandomEntityProperties randomentityproperties = (RandomEntityProperties)mapProperties.get(s1);

                    if (randomentityproperties == null)
                    {
                        randomentityproperties = makeProperties(resourcelocation, false);

                        if (randomentityproperties == null)
                        {
                            randomentityproperties = makeProperties(resourcelocation, true);
                        }

                        if (randomentityproperties != null)
                        {
                            mapProperties.put(s1, randomentityproperties);
                        }
                    }
                }
            }
        }

        active = !mapProperties.isEmpty();
    }

    public static void dbg(String str)
    {
        Config.dbg("RandomEntities: " + str);
    }

    public static void warn(String str)
    {
        Config.warn("RandomEntities: " + str);
    }
}
