package net.minecraft.client.resources;

import net.minecraft.client.gui.鬼ScreenResourcePacks;

public class ResourcePackListEntryFound extends ResourcePackListEntry
{
    private final ResourcePackRepository.Entry field_148319_c;

    public ResourcePackListEntryFound(鬼ScreenResourcePacks resourcePacksGUIIn, ResourcePackRepository.Entry p_i45053_2_)
    {
        super(resourcePacksGUIIn);
        this.field_148319_c = p_i45053_2_;
    }

    protected void func_148313_c()
    {
        this.field_148319_c.bindTexturePackIcon(this.mc.得到手感经理());
    }

    protected int func_183019_a()
    {
        return this.field_148319_c.func_183027_f();
    }

    protected String func_148311_a()
    {
        return this.field_148319_c.getTexturePackDescription();
    }

    protected String func_148312_b()
    {
        return this.field_148319_c.getResourcePackName();
    }

    public ResourcePackRepository.Entry func_148318_i()
    {
        return this.field_148319_c;
    }
}
