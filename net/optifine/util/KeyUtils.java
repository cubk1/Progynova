package net.optifine.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.settings.键入绑定;

public class KeyUtils
{
    public static void fixKeyConflicts(键入绑定[] keys, 键入绑定[] keysPrio)
    {
        Set<Integer> set = new HashSet();

        for (int i = 0; i < keysPrio.length; ++i)
        {
            键入绑定 keybinding = keysPrio[i];
            set.add(Integer.valueOf(keybinding.getKeyCode()));
        }

        Set<键入绑定> set1 = new HashSet(Arrays.asList(keys));
        set1.removeAll(Arrays.asList(keysPrio));

        for (键入绑定 keybinding1 : set1)
        {
            Integer integer = Integer.valueOf(keybinding1.getKeyCode());

            if (set.contains(integer))
            {
                keybinding1.setKeyCode(0);
            }
        }
    }
}
