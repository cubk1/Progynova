package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.util.ReportedException;
import net.minecraft.util.图像位置;
import net.optifine.CustomGuis;
import net.optifine.EmissiveTextures;
import net.optifine.RandomEntities;
import net.optifine.shaders.ShadersTex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureManager implements ITickable, IResourceManagerReloadListener
{
    private static final Logger logger = LogManager.getLogger();
    private final Map<图像位置, ITextureObject> mapTextureObjects = Maps.<图像位置, ITextureObject>newHashMap();
    private final List<ITickable> listTickables = Lists.<ITickable>newArrayList();
    private final Map<String, Integer> mapTextureCounters = Maps.<String, Integer>newHashMap();
    private IResourceManager theResourceManager;
    private ITextureObject boundTexture;
    private 图像位置 boundTextureLocation;

    public TextureManager(IResourceManager resourceManager)
    {
        this.theResourceManager = resourceManager;
    }

    public void 绑定手感(图像位置 resource)
    {
        if (Config.isRandomEntities())
        {
            resource = RandomEntities.getTextureLocation(resource);
        }

        if (Config.isCustomGuis())
        {
            resource = CustomGuis.getTextureLocation(resource);
        }

        ITextureObject itextureobject = (ITextureObject)this.mapTextureObjects.get(resource);

        if (EmissiveTextures.isActive())
        {
            itextureobject = EmissiveTextures.getEmissiveTexture(itextureobject, this.mapTextureObjects);
        }

        if (itextureobject == null)
        {
            itextureobject = new SimpleTexture(resource);
            this.loadTexture(resource, itextureobject);
        }

        if (Config.isShaders())
        {
            ShadersTex.bindTexture(itextureobject);
        }
        else
        {
            TextureUtil.bindTexture(itextureobject.getGlTextureId());
        }

        this.boundTexture = itextureobject;
        this.boundTextureLocation = resource;
    }

    public boolean loadTickableTexture(图像位置 textureLocation, ITickableTextureObject textureObj)
    {
        if (this.loadTexture(textureLocation, textureObj))
        {
            this.listTickables.add(textureObj);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean loadTexture(图像位置 textureLocation, ITextureObject textureObj)
    {
        boolean flag = true;

        try
        {
            ((ITextureObject)textureObj).loadTexture(this.theResourceManager);
        }
        catch (IOException ioexception)
        {
            logger.warn((String)("Failed to load texture: " + textureLocation), (Throwable)ioexception);
            textureObj = TextureUtil.missingTexture;
            this.mapTextureObjects.put(textureLocation, (ITextureObject)textureObj);
            flag = false;
        }
        catch (Throwable throwable)
        {
            final ITextureObject textureObjf = textureObj;
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            crashreportcategory.addCrashSectionCallable("Texture object class", new Callable<String>()
            {
                public String call() throws Exception
                {
                    return textureObjf.getClass().getName();
                }
            });
            throw new ReportedException(crashreport);
        }

        this.mapTextureObjects.put(textureLocation, (ITextureObject)textureObj);
        return flag;
    }

    public ITextureObject getTexture(图像位置 textureLocation)
    {
        return (ITextureObject)this.mapTextureObjects.get(textureLocation);
    }

    public 图像位置 getDynamicTextureLocation(String name, DynamicTexture texture)
    {
        if (name.equals("logo"))
        {
            texture = Config.getMojangLogoTexture(texture);
        }

        Integer integer = (Integer)this.mapTextureCounters.get(name);

        if (integer == null)
        {
            integer = Integer.valueOf(1);
        }
        else
        {
            integer = Integer.valueOf(integer.intValue() + 1);
        }

        this.mapTextureCounters.put(name, integer);
        图像位置 resourcelocation = new 图像位置(String.format("dynamic/%s_%d", new Object[] {name, integer}));
        this.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }

    public void tick()
    {
        for (ITickable itickable : this.listTickables)
        {
            itickable.tick();
        }
    }

    public void deleteTexture(图像位置 textureLocation)
    {
        ITextureObject itextureobject = this.getTexture(textureLocation);

        if (itextureobject != null)
        {
            this.mapTextureObjects.remove(textureLocation);
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());
        Iterator iterator = this.mapTextureObjects.keySet().iterator();

        while (iterator.hasNext())
        {
            图像位置 resourcelocation = (图像位置)iterator.next();
            String s = resourcelocation.getResourcePath();

            if (s.startsWith("mcpatcher/") || s.startsWith("optifine/") || EmissiveTextures.isEmissive(resourcelocation))
            {
                ITextureObject itextureobject = (ITextureObject)this.mapTextureObjects.get(resourcelocation);

                if (itextureobject instanceof AbstractTexture)
                {
                    AbstractTexture abstracttexture = (AbstractTexture)itextureobject;
                    abstracttexture.deleteGlTexture();
                }

                iterator.remove();
            }
        }

        EmissiveTextures.update();

        for (Object o : new HashSet(this.mapTextureObjects.entrySet()))
        {
            Entry<图像位置, ITextureObject> entry = (Entry<图像位置, ITextureObject>) o;
            this.loadTexture((图像位置)entry.getKey(), (ITextureObject)entry.getValue());
        }
    }

    public void reloadBannerTextures()
    {
        for (Object o : new HashSet(this.mapTextureObjects.entrySet()))
        {
            Entry<图像位置, ITextureObject> entry = (Entry<图像位置, ITextureObject>) o;
            图像位置 resourcelocation = (图像位置)entry.getKey();
            ITextureObject itextureobject = (ITextureObject)entry.getValue();

            if (itextureobject instanceof LayeredColorMaskTexture)
            {
                this.loadTexture(resourcelocation, itextureobject);
            }
        }
    }

    public ITextureObject getBoundTexture()
    {
        return this.boundTexture;
    }

    public 图像位置 getBoundTextureLocation()
    {
        return this.boundTextureLocation;
    }
}
