package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.实体;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals
{
    Predicate<实体> mobSelector = new Predicate<实体>()
    {
        public boolean apply(实体 p_apply_1_)
        {
            return p_apply_1_ instanceof IMob;
        }
    };
    Predicate<实体> VISIBLE_MOB_SELECTOR = new Predicate<实体>()
    {
        public boolean apply(实体 p_apply_1_)
        {
            return p_apply_1_ instanceof IMob && !p_apply_1_.isInvisible();
        }
    };
}
