package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.RegistrySimple;
import net.minecraft.util.图像位置;

public class SoundRegistry extends RegistrySimple<图像位置, SoundEventAccessorComposite>
{
    private Map<图像位置, SoundEventAccessorComposite> soundRegistry;

    protected Map<图像位置, SoundEventAccessorComposite> createUnderlyingMap()
    {
        this.soundRegistry = Maps.<图像位置, SoundEventAccessorComposite>newHashMap();
        return this.soundRegistry;
    }

    public void registerSound(SoundEventAccessorComposite p_148762_1_)
    {
        this.putObject(p_148762_1_.getSoundEventLocation(), p_148762_1_);
    }

    public void clearMap()
    {
        this.soundRegistry.clear();
    }
}
