package net.minecraft.entity.ai;

import net.minecraft.entity.实体Living;

public class EntityJumpHelper
{
    private 实体Living entity;
    protected boolean isJumping;

    public EntityJumpHelper(实体Living entityIn)
    {
        this.entity = entityIn;
    }

    public void setJumping()
    {
        this.isJumping = true;
    }

    public void doJump()
    {
        this.entity.setJumping(this.isJumping);
        this.isJumping = false;
    }
}
