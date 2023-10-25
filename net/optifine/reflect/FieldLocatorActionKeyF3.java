package net.optifine.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.我的手艺;
import net.minecraft.src.Config;

public class FieldLocatorActionKeyF3 implements IFieldLocator
{
    public Field getField()
    {
        Class oclass = 我的手艺.class;
        Field field = this.getFieldRenderChunksMany();

        if (field == null)
        {
            Config.log("(Reflector) Field not present: " + oclass.getName() + ".actionKeyF3 (field renderChunksMany not found)");
            return null;
        }
        else
        {
            Field field1 = ReflectorRaw.getFieldAfter(我的手艺.class, field, Boolean.TYPE, 0);

            if (field1 == null)
            {
                Config.log("(Reflector) Field not present: " + oclass.getName() + ".actionKeyF3");
                return null;
            }
            else
            {
                return field1;
            }
        }
    }

    private Field getFieldRenderChunksMany()
    {
        我的手艺 宇轩的世界 = 我的手艺.得到我的手艺();
        boolean flag = 宇轩的世界.renderChunksMany;
        Field[] afield = 我的手艺.class.getDeclaredFields();
        宇轩的世界.renderChunksMany = true;
        Field[] afield1 = ReflectorRaw.getFields(宇轩的世界, afield, Boolean.TYPE, Boolean.TRUE);
        宇轩的世界.renderChunksMany = false;
        Field[] afield2 = ReflectorRaw.getFields(宇轩的世界, afield, Boolean.TYPE, Boolean.FALSE);
        宇轩的世界.renderChunksMany = flag;
        Set<Field> set = new HashSet(Arrays.asList(afield1));
        Set<Field> set1 = new HashSet(Arrays.asList(afield2));
        Set<Field> set2 = new HashSet(set);
        set2.retainAll(set1);
        Field[] afield3 = (Field[])((Field[])set2.toArray(new Field[set2.size()]));
        return afield3.length != 1 ? null : afield3[0];
    }
}
