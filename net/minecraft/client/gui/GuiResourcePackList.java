package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.util.枚举聊天格式;

public abstract class GuiResourcePackList extends GuiListExtended
{
    protected final 我的手艺 mc;
    protected final List<ResourcePackListEntry> field_148204_l;

    public GuiResourcePackList(我的手艺 mcIn, int p_i45055_2_, int p_i45055_3_, List<ResourcePackListEntry> p_i45055_4_)
    {
        super(mcIn, p_i45055_2_, p_i45055_3_, 32, p_i45055_3_ - 55 + 4, 36);
        this.mc = mcIn;
        this.field_148204_l = p_i45055_4_;
        this.field_148163_i = false;
        this.setHasListHeader(true, (int)((float)mcIn.字体渲染员.FONT_HEIGHT * 1.5F));
    }

    protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_)
    {
        String s = 枚举聊天格式.UNDERLINE + "" + 枚举聊天格式.BOLD + this.getListHeader();
        this.mc.字体渲染员.drawString(s, p_148129_1_ + this.width / 2 - this.mc.字体渲染员.getStringWidth(s) / 2, Math.min(this.top + 3, p_148129_2_), 16777215);
    }

    protected abstract String getListHeader();

    public List<ResourcePackListEntry> getList()
    {
        return this.field_148204_l;
    }

    protected int getSize()
    {
        return this.getList().size();
    }

    public ResourcePackListEntry getListEntry(int index)
    {
        return (ResourcePackListEntry)this.getList().get(index);
    }

    public int getListWidth()
    {
        return this.width;
    }

    protected int getScrollBarX()
    {
        return this.right - 6;
    }
}
