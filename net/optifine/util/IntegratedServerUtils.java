package net.optifine.util;

import java.util.UUID;

import net.minecraft.client.我的手艺;
import net.minecraft.entity.实体;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class IntegratedServerUtils
{
    public static WorldServer getWorldServer()
    {
        我的手艺 宇轩的世界 = Config.getMinecraft();
        World world = 宇轩的世界.宇轩の世界;

        if (world == null)
        {
            return null;
        }
        else if (!宇轩的世界.isIntegratedServerRunning())
        {
            return null;
        }
        else
        {
            IntegratedServer integratedserver = 宇轩的世界.getIntegratedServer();

            if (integratedserver == null)
            {
                return null;
            }
            else
            {
                WorldProvider worldprovider = world.provider;

                if (worldprovider == null)
                {
                    return null;
                }
                else
                {
                    int i = worldprovider.getDimensionId();

                    try
                    {
                        WorldServer worldserver = integratedserver.worldServerForDimension(i);
                        return worldserver;
                    }
                    catch (NullPointerException var6)
                    {
                        return null;
                    }
                }
            }
        }
    }

    public static 实体 getEntity(UUID uuid)
    {
        WorldServer worldserver = getWorldServer();

        if (worldserver == null)
        {
            return null;
        }
        else
        {
            实体 实体 = worldserver.getEntityFromUuid(uuid);
            return 实体;
        }
    }

    public static TileEntity getTileEntity(阻止位置 pos)
    {
        WorldServer worldserver = getWorldServer();

        if (worldserver == null)
        {
            return null;
        }
        else
        {
            Chunk chunk = worldserver.getChunkProvider().provideChunk(pos.getX() >> 4, pos.getZ() >> 4);

            if (chunk == null)
            {
                return null;
            }
            else
            {
                TileEntity tileentity = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                return tileentity;
            }
        }
    }
}
