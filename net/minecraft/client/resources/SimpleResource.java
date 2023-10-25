package net.minecraft.client.resources;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.图像位置;
import org.apache.commons.io.IOUtils;

public class SimpleResource implements IResource
{
    private final Map<String, IMetadataSection> mapMetadataSections = Maps.<String, IMetadataSection>newHashMap();
    private final String resourcePackName;
    private final 图像位置 sr图像位置;
    private final InputStream resourceInputStream;
    private final InputStream mcmetaInputStream;
    private final IMetadataSerializer srMetadataSerializer;
    private boolean mcmetaJsonChecked;
    private JsonObject mcmetaJson;

    public SimpleResource(String resourcePackNameIn, 图像位置 sr图像位置In, InputStream resourceInputStreamIn, InputStream mcmetaInputStreamIn, IMetadataSerializer srMetadataSerializerIn)
    {
        this.resourcePackName = resourcePackNameIn;
        this.sr图像位置 = sr图像位置In;
        this.resourceInputStream = resourceInputStreamIn;
        this.mcmetaInputStream = mcmetaInputStreamIn;
        this.srMetadataSerializer = srMetadataSerializerIn;
    }

    public 图像位置 getResourceLocation()
    {
        return this.sr图像位置;
    }

    public InputStream getInputStream()
    {
        return this.resourceInputStream;
    }

    public boolean hasMetadata()
    {
        return this.mcmetaInputStream != null;
    }

    public <T extends IMetadataSection> T getMetadata(String p_110526_1_)
    {
        if (!this.hasMetadata())
        {
            return (T)null;
        }
        else
        {
            if (this.mcmetaJson == null && !this.mcmetaJsonChecked)
            {
                this.mcmetaJsonChecked = true;
                BufferedReader bufferedreader = null;

                try
                {
                    bufferedreader = new BufferedReader(new InputStreamReader(this.mcmetaInputStream));
                    this.mcmetaJson = (new JsonParser()).parse((Reader)bufferedreader).getAsJsonObject();
                }
                finally
                {
                    IOUtils.closeQuietly((Reader)bufferedreader);
                }
            }

            T t = (T)this.mapMetadataSections.get(p_110526_1_);

            if (t == null)
            {
                t = this.srMetadataSerializer.parseMetadataSection(p_110526_1_, this.mcmetaJson);
            }

            return t;
        }
    }

    public String getResourcePackName()
    {
        return this.resourcePackName;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof SimpleResource))
        {
            return false;
        }
        else
        {
            SimpleResource simpleresource = (SimpleResource)p_equals_1_;

            if (this.sr图像位置 != null)
            {
                if (!this.sr图像位置.equals(simpleresource.sr图像位置))
                {
                    return false;
                }
            }
            else if (simpleresource.sr图像位置 != null)
            {
                return false;
            }

            if (this.resourcePackName != null)
            {
                if (!this.resourcePackName.equals(simpleresource.resourcePackName))
                {
                    return false;
                }
            }
            else if (simpleresource.resourcePackName != null)
            {
                return false;
            }

            return true;
        }
    }

    public int hashCode()
    {
        int i = this.resourcePackName != null ? this.resourcePackName.hashCode() : 0;
        i = 31 * i + (this.sr图像位置 != null ? this.sr图像位置.hashCode() : 0);
        return i;
    }
}
