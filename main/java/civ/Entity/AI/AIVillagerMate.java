package civ.Entity.AI;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import civ.Entity.Manager.AnimalManager;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class AIVillagerMate extends EntityAIBase
{
    private EntityVillager theAnimal;
    World theWorld;
    private EntityVillager targetMate;
    /**
     * Delay preventing a baby from spawning immediately when two mate-able animals find each other.
     */
    int spawnBabyDelay;
    /** The speed the creature moves at during mating behavior. */
    double moveSpeed;
    private static final String __OBFID = "CL_00001578";

    public AIVillagerMate(EntityVillager par1EntityAnimal, double par2)
    {
        this.theAnimal = par1EntityAnimal;
        this.theWorld = par1EntityAnimal.worldObj;
        this.moveSpeed = par2;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.theAnimal.isMating())
        {
            return false;
        }
        else
        {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.targetMate.isEntityAlive() && this.targetMate.isMating() && this.spawnBabyDelay < 60;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
    	if (!this.theAnimal.isMating())
    		return;
    	
        this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float)this.theAnimal.getVerticalFaceSpeed());
        this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D)
        {
        	AnimalManager.Reproduce(this.theAnimal, this.targetMate);
            return;
        }
        
        return;
    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    private EntityVillager getNearbyMate()
    {
        float f = 8.0F;
        List list = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.boundingBox.expand((double)f, (double)f, (double)f));
        double d0 = Double.MAX_VALUE;
        EntityVillager entityanimal = null;
        Iterator iterator = list.iterator();

        while (iterator.hasNext())
        {
        	EntityVillager entityanimal1 = (EntityVillager)iterator.next();

            if (entityanimal1.isMating() && this.theAnimal.getDistanceSqToEntity(entityanimal1) < d0)
            {
                entityanimal = entityanimal1;
                d0 = this.theAnimal.getDistanceSqToEntity(entityanimal1);
            }
        }

        return entityanimal;
    }
}