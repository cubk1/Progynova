package net.minecraft.world.chunk;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.阻止位置;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public interface IChunkProvider
{
    boolean chunkExists(int x, int z);

    Chunk provideChunk(int x, int z);

    Chunk provideChunk(阻止位置 阻止位置In);

    void populate(IChunkProvider chunkProvider, int x, int z);

    boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z);

    boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback);

    boolean unloadQueuedChunks();

    boolean canSave();

    String makeString();

    List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, 阻止位置 pos);

    阻止位置 getStrongholdGen(World worldIn, String structureName, 阻止位置 position);

    int getLoadedChunkCount();

    void recreateStructures(Chunk chunkIn, int x, int z);

    void saveExtraData();
}
