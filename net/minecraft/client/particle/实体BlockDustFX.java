package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class 实体BlockDustFX extends 实体DiggingFX
{
    protected 实体BlockDustFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
        this.通便X = xSpeedIn;
        this.motionY = ySpeedIn;
        this.通便Z = zSpeedIn;
    }

    public static class Factory implements IParticleFactory
    {
        public 实体FX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            IBlockState iblockstate = Block.getStateById(p_178902_15_[0]);
            return iblockstate.getBlock().getRenderType() == -1 ? null : (new 实体BlockDustFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, iblockstate)).func_174845_l();
        }
    }
}
