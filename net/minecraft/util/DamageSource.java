package net.minecraft.util;

import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.projectile.实体Arrow;
import net.minecraft.entity.projectile.实体Fireball;
import net.minecraft.world.Explosion;

public class DamageSource
{
    public static DamageSource inFire = (new DamageSource("inFire")).setFireDamage();
    public static DamageSource lightningBolt = new DamageSource("lightningBolt");
    public static DamageSource onFire = (new DamageSource("onFire")).setDamageBypassesArmor().setFireDamage();
    public static DamageSource lava = (new DamageSource("lava")).setFireDamage();
    public static DamageSource inWall = (new DamageSource("inWall")).setDamageBypassesArmor();
    public static DamageSource drown = (new DamageSource("drown")).setDamageBypassesArmor();
    public static DamageSource starve = (new DamageSource("starve")).setDamageBypassesArmor().setDamageIsAbsolute();
    public static DamageSource cactus = new DamageSource("cactus");
    public static DamageSource fall = (new DamageSource("fall")).setDamageBypassesArmor();
    public static DamageSource outOfWorld = (new DamageSource("outOfWorld")).setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    public static DamageSource generic = (new DamageSource("generic")).setDamageBypassesArmor();
    public static DamageSource magic = (new DamageSource("magic")).setDamageBypassesArmor().setMagicDamage();
    public static DamageSource wither = (new DamageSource("wither")).setDamageBypassesArmor();
    public static DamageSource anvil = new DamageSource("anvil");
    public static DamageSource fallingBlock = new DamageSource("fallingBlock");
    private boolean isUnblockable;
    private boolean isDamageAllowedInCreativeMode;
    private boolean damageIsAbsolute;
    private float hungerDamage = 0.3F;
    private boolean fireDamage;
    private boolean projectile;
    private boolean difficultyScaled;
    private boolean magicDamage;
    private boolean explosion;
    public String damageType;

    public static DamageSource causeMobDamage(实体LivingBase mob)
    {
        return new EntityDamageSource("mob", mob);
    }

    public static DamageSource causePlayerDamage(实体Player player)
    {
        return new EntityDamageSource("player", player);
    }

    public static DamageSource causeArrowDamage(实体Arrow arrow, 实体 indirect实体In)
    {
        return (new EntityDamageSourceIndirect("arrow", arrow, indirect实体In)).setProjectile();
    }

    public static DamageSource causeFireballDamage(实体Fireball fireball, 实体 indirect实体In)
    {
        return indirect实体In == null ? (new EntityDamageSourceIndirect("onFire", fireball, fireball)).setFireDamage().setProjectile() : (new EntityDamageSourceIndirect("fireball", fireball, indirect实体In)).setFireDamage().setProjectile();
    }

    public static DamageSource causeThrownDamage(实体 source, 实体 indirect实体In)
    {
        return (new EntityDamageSourceIndirect("thrown", source, indirect实体In)).setProjectile();
    }

    public static DamageSource causeIndirectMagicDamage(实体 source, 实体 indirect实体In)
    {
        return (new EntityDamageSourceIndirect("indirectMagic", source, indirect实体In)).setDamageBypassesArmor().setMagicDamage();
    }

    public static DamageSource causeThornsDamage(实体 source)
    {
        return (new EntityDamageSource("thorns", source)).setIsThornsDamage().setMagicDamage();
    }

    public static DamageSource setExplosionSource(Explosion explosionIn)
    {
        return explosionIn != null && explosionIn.getExplosivePlacedBy() != null ? (new EntityDamageSource("explosion.player", explosionIn.getExplosivePlacedBy())).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
    }

    public boolean isProjectile()
    {
        return this.projectile;
    }

    public DamageSource setProjectile()
    {
        this.projectile = true;
        return this;
    }

    public boolean isExplosion()
    {
        return this.explosion;
    }

    public DamageSource setExplosion()
    {
        this.explosion = true;
        return this;
    }

    public boolean isUnblockable()
    {
        return this.isUnblockable;
    }

    public float getHungerDamage()
    {
        return this.hungerDamage;
    }

    public boolean canHarmInCreative()
    {
        return this.isDamageAllowedInCreativeMode;
    }

    public boolean isDamageAbsolute()
    {
        return this.damageIsAbsolute;
    }

    protected DamageSource(String damageTypeIn)
    {
        this.damageType = damageTypeIn;
    }

    public 实体 getSourceOfDamage()
    {
        return this.getEntity();
    }

    public 实体 getEntity()
    {
        return null;
    }

    protected DamageSource setDamageBypassesArmor()
    {
        this.isUnblockable = true;
        this.hungerDamage = 0.0F;
        return this;
    }

    protected DamageSource setDamageAllowedInCreativeMode()
    {
        this.isDamageAllowedInCreativeMode = true;
        return this;
    }

    protected DamageSource setDamageIsAbsolute()
    {
        this.damageIsAbsolute = true;
        this.hungerDamage = 0.0F;
        return this;
    }

    protected DamageSource setFireDamage()
    {
        this.fireDamage = true;
        return this;
    }

    public IChatComponent getDeathMessage(实体LivingBase entityLivingBaseIn)
    {
        实体LivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        return entitylivingbase != null && StatCollector.canTranslate(s1) ? new ChatComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName()}): new ChatComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName()});
    }

    public boolean isFireDamage()
    {
        return this.fireDamage;
    }

    public String getDamageType()
    {
        return this.damageType;
    }

    public DamageSource setDifficultyScaled()
    {
        this.difficultyScaled = true;
        return this;
    }

    public boolean isDifficultyScaled()
    {
        return this.difficultyScaled;
    }

    public boolean isMagicDamage()
    {
        return this.magicDamage;
    }

    public DamageSource setMagicDamage()
    {
        this.magicDamage = true;
        return this;
    }

    public boolean isCreativePlayer()
    {
        实体 实体 = this.getEntity();
        return 实体 instanceof 实体Player && ((实体Player) 实体).capabilities.isCreativeMode;
    }
}
