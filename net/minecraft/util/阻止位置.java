package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.entity.实体;

public class 阻止位置 extends Vec3i
{
    public static final 阻止位置 ORIGIN = new 阻止位置(0, 0, 0);
    private static final int NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    public 阻止位置(int x, int y, int z)
    {
        super(x, y, z);
    }

    public 阻止位置(double x, double y, double z)
    {
        super(x, y, z);
    }

    public 阻止位置(实体 source)
    {
        this(source.X坐标, source.Y坐标, source.Z坐标);
    }

    public 阻止位置(Vec3 source)
    {
        this(source.xCoord, source.yCoord, source.zCoord);
    }

    public 阻止位置(Vec3i source)
    {
        this(source.getX(), source.getY(), source.getZ());
    }

    public 阻止位置 add(double x, double y, double z)
    {
        return x == 0.0D && y == 0.0D && z == 0.0D ? this : new 阻止位置((double)this.getX() + x, (double)this.getY() + y, (double)this.getZ() + z);
    }

    public 阻止位置 add(int x, int y, int z)
    {
        return x == 0 && y == 0 && z == 0 ? this : new 阻止位置(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public 阻止位置 add(Vec3i vec)
    {
        return vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0 ? this : new 阻止位置(this.getX() + vec.getX(), this.getY() + vec.getY(), this.getZ() + vec.getZ());
    }

    public 阻止位置 subtract(Vec3i vec)
    {
        return vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0 ? this : new 阻止位置(this.getX() - vec.getX(), this.getY() - vec.getY(), this.getZ() - vec.getZ());
    }

    public 阻止位置 up()
    {
        return this.up(1);
    }

    public 阻止位置 up(int n)
    {
        return this.offset(EnumFacing.UP, n);
    }

    public 阻止位置 down()
    {
        return this.down(1);
    }

    public 阻止位置 down(int n)
    {
        return this.offset(EnumFacing.DOWN, n);
    }

    public 阻止位置 north()
    {
        return this.north(1);
    }

    public 阻止位置 north(int n)
    {
        return this.offset(EnumFacing.NORTH, n);
    }

    public 阻止位置 south()
    {
        return this.south(1);
    }

    public 阻止位置 south(int n)
    {
        return this.offset(EnumFacing.SOUTH, n);
    }

    public 阻止位置 west()
    {
        return this.west(1);
    }

    public 阻止位置 west(int n)
    {
        return this.offset(EnumFacing.WEST, n);
    }

    public 阻止位置 east()
    {
        return this.east(1);
    }

    public 阻止位置 east(int n)
    {
        return this.offset(EnumFacing.EAST, n);
    }

    public 阻止位置 offset(EnumFacing facing)
    {
        return this.offset(facing, 1);
    }

    public 阻止位置 offset(EnumFacing facing, int n)
    {
        return n == 0 ? this : new 阻止位置(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
    }

    public 阻止位置 crossProduct(Vec3i vec)
    {
        return new 阻止位置(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    public long toLong()
    {
        return ((long)this.getX() & X_MASK) << X_SHIFT | ((long)this.getY() & Y_MASK) << Y_SHIFT | ((long)this.getZ() & Z_MASK) << 0;
    }

    public static 阻止位置 fromLong(long serialized)
    {
        int i = (int)(serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
        int j = (int)(serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
        int k = (int)(serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
        return new 阻止位置(i, j, k);
    }

    public static Iterable<阻止位置> getAllInBox(阻止位置 from, 阻止位置 to)
    {
        final 阻止位置 blockpos = new 阻止位置(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final 阻止位置 blockpos1 = new 阻止位置(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable<阻止位置>()
        {
            public Iterator<阻止位置> iterator()
            {
                return new AbstractIterator<阻止位置>()
                {
                    private 阻止位置 lastReturned = null;
                    protected 阻止位置 computeNext()
                    {
                        if (this.lastReturned == null)
                        {
                            this.lastReturned = blockpos;
                            return this.lastReturned;
                        }
                        else if (this.lastReturned.equals(blockpos1))
                        {
                            return (阻止位置)this.endOfData();
                        }
                        else
                        {
                            int i = this.lastReturned.getX();
                            int j = this.lastReturned.getY();
                            int k = this.lastReturned.getZ();

                            if (i < blockpos1.getX())
                            {
                                ++i;
                            }
                            else if (j < blockpos1.getY())
                            {
                                i = blockpos.getX();
                                ++j;
                            }
                            else if (k < blockpos1.getZ())
                            {
                                i = blockpos.getX();
                                j = blockpos.getY();
                                ++k;
                            }

                            this.lastReturned = new 阻止位置(i, j, k);
                            return this.lastReturned;
                        }
                    }
                };
            }
        };
    }

    public static Iterable<Mutable阻止位置> getAllInBoxMutable(阻止位置 from, 阻止位置 to)
    {
        final 阻止位置 blockpos = new 阻止位置(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final 阻止位置 blockpos1 = new 阻止位置(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable<Mutable阻止位置>()
        {
            public Iterator<Mutable阻止位置> iterator()
            {
                return new AbstractIterator<Mutable阻止位置>()
                {
                    private Mutable阻止位置 theBlockPos = null;
                    protected Mutable阻止位置 computeNext()
                    {
                        if (this.theBlockPos == null)
                        {
                            this.theBlockPos = new Mutable阻止位置(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                            return this.theBlockPos;
                        }
                        else if (this.theBlockPos.equals(blockpos1))
                        {
                            return (Mutable阻止位置)this.endOfData();
                        }
                        else
                        {
                            int i = this.theBlockPos.getX();
                            int j = this.theBlockPos.getY();
                            int k = this.theBlockPos.getZ();

                            if (i < blockpos1.getX())
                            {
                                ++i;
                            }
                            else if (j < blockpos1.getY())
                            {
                                i = blockpos.getX();
                                ++j;
                            }
                            else if (k < blockpos1.getZ())
                            {
                                i = blockpos.getX();
                                j = blockpos.getY();
                                ++k;
                            }

                            this.theBlockPos.x = i;
                            this.theBlockPos.y = j;
                            this.theBlockPos.z = k;
                            return this.theBlockPos;
                        }
                    }
                };
            }
        };
    }

    public static final class Mutable阻止位置 extends 阻止位置
    {
        private int x;
        private int y;
        private int z;

        public Mutable阻止位置()
        {
            this(0, 0, 0);
        }

        public Mutable阻止位置(int x_, int y_, int z_)
        {
            super(0, 0, 0);
            this.x = x_;
            this.y = y_;
            this.z = z_;
        }

        public int getX()
        {
            return this.x;
        }

        public int getY()
        {
            return this.y;
        }

        public int getZ()
        {
            return this.z;
        }

        public Mutable阻止位置 set(int xIn, int yIn, int zIn)
        {
            this.x = xIn;
            this.y = yIn;
            this.z = zIn;
            return this;
        }
    }
}
