package net.minecraft.client.audio;

import java.util.Random;

import net.minecraft.client.我的手艺;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class MusicTicker implements ITickable
{
    private final Random rand = new Random();
    private final 我的手艺 mc;
    private ISound currentMusic;
    private int timeUntilNextMusic = 100;

    public MusicTicker(我的手艺 mcIn)
    {
        this.mc = mcIn;
    }

    public void update()
    {
        MusicTicker.MusicType musicticker$musictype = this.mc.getAmbientMusicType();

        if (this.currentMusic != null)
        {
            if (!musicticker$musictype.getMusicLocation().equals(this.currentMusic.getSoundLocation()))
            {
                this.mc.getSoundHandler().stopSound(this.currentMusic);
                this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, musicticker$musictype.getMinDelay() / 2);
            }

            if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic))
            {
                this.currentMusic = null;
                this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, musicticker$musictype.getMinDelay(), musicticker$musictype.getMaxDelay()), this.timeUntilNextMusic);
            }
        }

        if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0)
        {
            this.func_181558_a(musicticker$musictype);
        }
    }

    public void func_181558_a(MusicTicker.MusicType p_181558_1_)
    {
        this.currentMusic = PositionedSoundRecord.create(p_181558_1_.getMusicLocation());
        this.mc.getSoundHandler().playSound(this.currentMusic);
        this.timeUntilNextMusic = Integer.MAX_VALUE;
    }

    public void func_181557_a()
    {
        if (this.currentMusic != null)
        {
            this.mc.getSoundHandler().stopSound(this.currentMusic);
            this.currentMusic = null;
            this.timeUntilNextMusic = 0;
        }
    }

    public static enum MusicType
    {
        MENU(new 图像位置("minecraft:music.menu"), 20, 600),
        GAME(new 图像位置("minecraft:music.game"), 12000, 24000),
        CREATIVE(new 图像位置("minecraft:music.game.creative"), 1200, 3600),
        CREDITS(new 图像位置("minecraft:music.game.end.credits"), Integer.MAX_VALUE, Integer.MAX_VALUE),
        NETHER(new 图像位置("minecraft:music.game.nether"), 1200, 3600),
        END_BOSS(new 图像位置("minecraft:music.game.end.dragon"), 0, 0),
        END(new 图像位置("minecraft:music.game.end"), 6000, 24000);

        private final 图像位置 musicLocation;
        private final int minDelay;
        private final int maxDelay;

        private MusicType(图像位置 location, int minDelayIn, int maxDelayIn)
        {
            this.musicLocation = location;
            this.minDelay = minDelayIn;
            this.maxDelay = maxDelayIn;
        }

        public 图像位置 getMusicLocation()
        {
            return this.musicLocation;
        }

        public int getMinDelay()
        {
            return this.minDelay;
        }

        public int getMaxDelay()
        {
            return this.maxDelay;
        }
    }
}
