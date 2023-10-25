package net.minecraft.block.state;

import com.google.common.base.Predicate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class BlockWorldState
{
    private final World world;
    private final 阻止位置 pos;
    private final boolean field_181628_c;
    private IBlockState state;
    private TileEntity tileEntity;
    private boolean tileEntityInitialized;

    public BlockWorldState(World worldIn, 阻止位置 posIn, boolean p_i46451_3_)
    {
        this.world = worldIn;
        this.pos = posIn;
        this.field_181628_c = p_i46451_3_;
    }

    public IBlockState getBlockState()
    {
        if (this.state == null && (this.field_181628_c || this.world.isBlockLoaded(this.pos)))
        {
            this.state = this.world.getBlockState(this.pos);
        }

        return this.state;
    }

    public TileEntity getTileEntity()
    {
        if (this.tileEntity == null && !this.tileEntityInitialized)
        {
            this.tileEntity = this.world.getTileEntity(this.pos);
            this.tileEntityInitialized = true;
        }

        return this.tileEntity;
    }

    public 阻止位置 getPos()
    {
        return this.pos;
    }

    public static Predicate<BlockWorldState> hasState(final Predicate<IBlockState> predicatesIn)
    {
        return new Predicate<BlockWorldState>()
        {
            public boolean apply(BlockWorldState p_apply_1_)
            {
                return p_apply_1_ != null && predicatesIn.apply(p_apply_1_.getBlockState());
            }
        };
    }
}
