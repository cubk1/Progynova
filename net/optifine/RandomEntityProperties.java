package net.optifine;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import net.minecraft.src.Config;
import net.minecraft.util.图像位置;
import net.optifine.config.ConnectedParser;

public class RandomEntityProperties
{
    public String name = null;
    public String basePath = null;
    public 图像位置[] 图像位置s = null;
    public RandomEntityRule[] rules = null;

    public RandomEntityProperties(String path, 图像位置[] variants)
    {
        ConnectedParser connectedparser = new ConnectedParser("RandomEntities");
        this.name = connectedparser.parseName(path);
        this.basePath = connectedparser.parseBasePath(path);
        this.图像位置s = variants;
    }

    public RandomEntityProperties(Properties props, String path, 图像位置 baseResLoc)
    {
        ConnectedParser connectedparser = new ConnectedParser("RandomEntities");
        this.name = connectedparser.parseName(path);
        this.basePath = connectedparser.parseBasePath(path);
        this.rules = this.parseRules(props, path, baseResLoc, connectedparser);
    }

    public 图像位置 getTextureLocation(图像位置 loc, IRandomEntity randomEntity)
    {
        if (this.rules != null)
        {
            for (int i = 0; i < this.rules.length; ++i)
            {
                RandomEntityRule randomentityrule = this.rules[i];

                if (randomentityrule.matches(randomEntity))
                {
                    return randomentityrule.getTextureLocation(loc, randomEntity.getId());
                }
            }
        }

        if (this.图像位置s != null)
        {
            int j = randomEntity.getId();
            int k = j % this.图像位置s.length;
            return this.图像位置s[k];
        }
        else
        {
            return loc;
        }
    }

    private RandomEntityRule[] parseRules(Properties props, String pathProps, 图像位置 baseResLoc, ConnectedParser cp)
    {
        List list = new ArrayList();
        int i = props.size();

        for (int j = 0; j < i; ++j)
        {
            int k = j + 1;
            String s = props.getProperty("textures." + k);

            if (s == null)
            {
                s = props.getProperty("skins." + k);
            }

            if (s != null)
            {
                RandomEntityRule randomentityrule = new RandomEntityRule(props, pathProps, baseResLoc, k, s, cp);

                if (randomentityrule.isValid(pathProps))
                {
                    list.add(randomentityrule);
                }
            }
        }

        RandomEntityRule[] arandomentityrule = (RandomEntityRule[])((RandomEntityRule[])list.toArray(new RandomEntityRule[list.size()]));
        return arandomentityrule;
    }

    public boolean isValid(String path)
    {
        if (this.图像位置s == null && this.rules == null)
        {
            Config.warn("No skins specified: " + path);
            return false;
        }
        else
        {
            if (this.rules != null)
            {
                for (int i = 0; i < this.rules.length; ++i)
                {
                    RandomEntityRule randomentityrule = this.rules[i];

                    if (!randomentityrule.isValid(path))
                    {
                        return false;
                    }
                }
            }

            if (this.图像位置s != null)
            {
                for (int j = 0; j < this.图像位置s.length; ++j)
                {
                    图像位置 resourcelocation = this.图像位置s[j];

                    if (!Config.hasResource(resourcelocation))
                    {
                        Config.warn("Texture not found: " + resourcelocation.getResourcePath());
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public boolean isDefault()
    {
        return this.rules != null ? false : this.图像位置s == null;
    }
}
