package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.util.图像位置;

public interface IResourceManager
{
    Set<String> getResourceDomains();

    IResource getResource(图像位置 location) throws IOException;

    List<IResource> getAllResources(图像位置 location) throws IOException;
}
