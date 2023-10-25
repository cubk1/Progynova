package net.minecraft.client.resources;

import java.io.InputStream;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.图像位置;

public interface IResource
{
    图像位置 getResourceLocation();

    InputStream getInputStream();

    boolean hasMetadata();

    <T extends IMetadataSection> T getMetadata(String p_110526_1_);

    String getResourcePackName();
}
