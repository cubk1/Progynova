package net.minecraft.client.gui;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import net.minecraft.client.我的手艺;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.枚举聊天格式;
import net.minecraft.util.图像位置;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry
{
    private static final Logger logger = LogManager.getLogger();
    private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
    private static final 图像位置 UNKNOWN_SERVER = new 图像位置("textures/misc/unknown_server.png");
    private static final 图像位置 SERVER_SELECTION_BUTTONS = new 图像位置("textures/gui/server_selection.png");
    private final 鬼Multiplayer owner;
    private final 我的手艺 mc;
    private final ServerData server;
    private final 图像位置 serverIcon;
    private String field_148299_g;
    private DynamicTexture field_148305_h;
    private long field_148298_f;

    protected ServerListEntryNormal(鬼Multiplayer p_i45048_1_, ServerData serverIn)
    {
        this.owner = p_i45048_1_;
        this.server = serverIn;
        this.mc = 我的手艺.得到我的手艺();
        this.serverIcon = new 图像位置("servers/" + serverIn.serverIP + "/icon");
        this.field_148305_h = (DynamicTexture)this.mc.得到手感经理().getTexture(this.serverIcon);
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
        if (!this.server.field_78841_f)
        {
            this.server.field_78841_f = true;
            this.server.pingToServer = -2L;
            this.server.serverMOTD = "";
            this.server.populationInfo = "";
            field_148302_b.submit(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        ServerListEntryNormal.this.owner.getOldServerPinger().ping(ServerListEntryNormal.this.server);
                    }
                    catch (UnknownHostException var2)
                    {
                        ServerListEntryNormal.this.server.pingToServer = -1L;
                        ServerListEntryNormal.this.server.serverMOTD = 枚举聊天格式.DARK_RED + "Can\'t resolve hostname";
                    }
                    catch (Exception var3)
                    {
                        ServerListEntryNormal.this.server.pingToServer = -1L;
                        ServerListEntryNormal.this.server.serverMOTD = 枚举聊天格式.DARK_RED + "Can\'t connect to server.";
                    }
                }
            });
        }

        boolean flag = this.server.version > 47;
        boolean flag1 = this.server.version < 47;
        boolean flag2 = flag || flag1;
        this.mc.字体渲染员.drawString(this.server.serverName, x + 32 + 3, y + 1, 16777215);
        List<String> list = this.mc.字体渲染员.listFormattedStringToWidth(this.server.serverMOTD, listWidth - 32 - 2);

        for (int i = 0; i < Math.min(list.size(), 2); ++i)
        {
            this.mc.字体渲染员.drawString((String)list.get(i), x + 32 + 3, y + 12 + this.mc.字体渲染员.FONT_HEIGHT * i, 8421504);
        }

        String s2 = flag2 ? 枚举聊天格式.DARK_RED + this.server.gameVersion : this.server.populationInfo;
        int j = this.mc.字体渲染员.getStringWidth(s2);
        this.mc.字体渲染员.drawString(s2, x + listWidth - j - 15 - 2, y + 1, 8421504);
        int k = 0;
        String s = null;
        int l;
        String s1;

        if (flag2)
        {
            l = 5;
            s1 = flag ? "Client out of date!" : "Server out of date!";
            s = this.server.playerList;
        }
        else if (this.server.field_78841_f && this.server.pingToServer != -2L)
        {
            if (this.server.pingToServer < 0L)
            {
                l = 5;
            }
            else if (this.server.pingToServer < 150L)
            {
                l = 0;
            }
            else if (this.server.pingToServer < 300L)
            {
                l = 1;
            }
            else if (this.server.pingToServer < 600L)
            {
                l = 2;
            }
            else if (this.server.pingToServer < 1000L)
            {
                l = 3;
            }
            else
            {
                l = 4;
            }

            if (this.server.pingToServer < 0L)
            {
                s1 = "(no connection)";
            }
            else
            {
                s1 = this.server.pingToServer + "ms";
                s = this.server.playerList;
            }
        }
        else
        {
            k = 1;
            l = (int)(我的手艺.getSystemTime() / 100L + (long)(slotIndex * 2) & 7L);

            if (l > 4)
            {
                l = 8 - l;
            }

            s1 = "Pinging...";
        }

        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.得到手感经理().绑定手感(鬼.icons);
        鬼.绘制模态矩形以自定义大小纹理(x + listWidth - 15, y, (float)(k * 10), (float)(176 + l * 8), 10, 8, 256.0F, 256.0F);

        if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.field_148299_g))
        {
            this.field_148299_g = this.server.getBase64EncodedIconData();
            this.prepareServerIcon();
            this.owner.getServerList().saveServerList();
        }

        if (this.field_148305_h != null)
        {
            this.drawTextureAt(x, y, this.serverIcon);
        }
        else
        {
            this.drawTextureAt(x, y, UNKNOWN_SERVER);
        }

        int i1 = mouseX - x;
        int j1 = mouseY - y;

        if (i1 >= listWidth - 15 && i1 <= listWidth - 5 && j1 >= 0 && j1 <= 8)
        {
            this.owner.setHoveringText(s1);
        }
        else if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8)
        {
            this.owner.setHoveringText(s);
        }

        if (this.mc.游戏一窝.touchscreen || isSelected)
        {
            this.mc.得到手感经理().绑定手感(SERVER_SELECTION_BUTTONS);
            鬼.drawRect(x, y, x + 32, y + 32, -1601138544);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            int k1 = mouseX - x;
            int l1 = mouseY - y;

            if (this.func_178013_b())
            {
                if (k1 < 32 && k1 > 16)
                {
                    鬼.绘制模态矩形以自定义大小纹理(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    鬼.绘制模态矩形以自定义大小纹理(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }

            if (this.owner.func_175392_a(this, slotIndex))
            {
                if (k1 < 16 && l1 < 16)
                {
                    鬼.绘制模态矩形以自定义大小纹理(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    鬼.绘制模态矩形以自定义大小纹理(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }

            if (this.owner.func_175394_b(this, slotIndex))
            {
                if (k1 < 16 && l1 > 16)
                {
                    鬼.绘制模态矩形以自定义大小纹理(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    鬼.绘制模态矩形以自定义大小纹理(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
        }
    }

    protected void drawTextureAt(int p_178012_1_, int p_178012_2_, 图像位置 p_178012_3_)
    {
        this.mc.得到手感经理().绑定手感(p_178012_3_);
        光照状态经理.启用混合品();
        鬼.绘制模态矩形以自定义大小纹理(p_178012_1_, p_178012_2_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        光照状态经理.禁用混合品();
    }

    private boolean func_178013_b()
    {
        return true;
    }

    private void prepareServerIcon()
    {
        if (this.server.getBase64EncodedIconData() == null)
        {
            this.mc.得到手感经理().deleteTexture(this.serverIcon);
            this.field_148305_h = null;
        }
        else
        {
            ByteBuf bytebuf = Unpooled.copiedBuffer((CharSequence)this.server.getBase64EncodedIconData(), Charsets.UTF_8);
            ByteBuf bytebuf1 = Base64.decode(bytebuf);
            BufferedImage bufferedimage;
            label101:
            {
                try
                {
                    bufferedimage = TextureUtil.readBufferedImage(new ByteBufInputStream(bytebuf1));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break label101;
                }
                catch (Throwable throwable)
                {
                    logger.error("Invalid icon for server " + this.server.serverName + " (" + this.server.serverIP + ")", throwable);
                    this.server.setBase64EncodedIconData((String)null);
                }
                finally
                {
                    bytebuf.release();
                    bytebuf1.release();
                }

                return;
            }

            if (this.field_148305_h == null)
            {
                this.field_148305_h = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.mc.得到手感经理().loadTexture(this.serverIcon, this.field_148305_h);
            }

            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.field_148305_h.getTextureData(), 0, bufferedimage.getWidth());
            this.field_148305_h.updateDynamicTexture();
        }
    }

    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        if (p_148278_5_ <= 32)
        {
            if (p_148278_5_ < 32 && p_148278_5_ > 16 && this.func_178013_b())
            {
                this.owner.selectServer(slotIndex);
                this.owner.connectToSelected();
                return true;
            }

            if (p_148278_5_ < 16 && p_148278_6_ < 16 && this.owner.func_175392_a(this, slotIndex))
            {
                this.owner.func_175391_a(this, slotIndex, 鬼Screen.isShiftKeyDown());
                return true;
            }

            if (p_148278_5_ < 16 && p_148278_6_ > 16 && this.owner.func_175394_b(this, slotIndex))
            {
                this.owner.func_175393_b(this, slotIndex, 鬼Screen.isShiftKeyDown());
                return true;
            }
        }

        this.owner.selectServer(slotIndex);

        if (我的手艺.getSystemTime() - this.field_148298_f < 250L)
        {
            this.owner.connectToSelected();
        }

        this.field_148298_f = 我的手艺.getSystemTime();
        return false;
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
    {
    }

    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }

    public ServerData getServerData()
    {
        return this.server;
    }
}
