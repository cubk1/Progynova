package net.minecraft.util;

import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn)
    {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState()
    {
        this.侧向移动 = 0.0F;
        this.向前移动 = 0.0F;

        if (this.gameSettings.键入绑定前.键位绑定沿着())
        {
            ++this.向前移动;
        }

        if (this.gameSettings.键入绑定后.键位绑定沿着())
        {
            --this.向前移动;
        }

        if (this.gameSettings.键入绑定左.键位绑定沿着())
        {
            ++this.侧向移动;
        }

        if (this.gameSettings.键入绑定右.键位绑定沿着())
        {
            --this.侧向移动;
        }

        this.跳跃 = this.gameSettings.keyBindJump.键位绑定沿着();
        this.蹲 = this.gameSettings.keyBindSneak.键位绑定沿着();

        if (this.蹲)
        {
            this.侧向移动 = (float)((double)this.侧向移动 * 0.3D);
            this.向前移动 = (float)((double)this.向前移动 * 0.3D);
        }
    }
}
