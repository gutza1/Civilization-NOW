package civ.Entity.AI;

import java.util.Random;
import java.math.*;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraftforge.common.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.item.*;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.*;

public class AITravelToLocation extends AIBase
{
	//private fields
    private EntityVillager entity;
    private int xPosition;
    private int yPosition;
    private int zPosition;
    private double speed;   
    
	public void SetFinished(boolean b)
	{
		this.IsFinished = b;

		if (this.WorkingPosition == null)
			this.SetWorkingPosition(new Point3i(this.xPosition, this.yPosition, this.zPosition));
	}
    public boolean PositionReached(boolean includeY, double dist)
    {
    	//if (this.WorkingPosition == null)
    	//	return false;
    	
    	double r1 = dist * dist;
    	double r2x = entity.posX - this.xPosition, r2z = entity.posZ - this.zPosition;
    	//double r2x = entity.posX - this.WorkingPosition.x, r2z = entity.posZ - this.WorkingPosition.z;
    	double r2 = r2x * r2x + r2z * r2z;
    	
    	if (includeY)
    	{
    		double r2y = entity.posY - this.yPosition;
    		//double r2y = entity.posY - this.WorkingPosition.y;
    		r2 += r2y * r2y;
    	}
    	
    	return r2 <= r1;    	
    }
    public Point3i GetLastPosition()
    {
    	if (this.WorkingPosition != null)
    		return this.WorkingPosition;
    	
    	return new Point3i(this.xPosition, this.yPosition, this.zPosition);
    }
    @Override
    public void SetWorkingPosition(Point3i p)
    {
    	super.SetWorkingPosition(p);
    	this.xPosition = p.x;;
    	this.yPosition = p.y;
    	this.zPosition = p.z;
    }

    public AITravelToLocation(EntityVillager villager, double speed, Point3i travelPoint)
    {
        this.entity = villager;
        this.speed = speed;
        this.WorkingPosition = travelPoint;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {  	    	
        if (this.WorkingPosition == null || this.IsFinished)
        {
            return false;
        }
        else
        {
            this.xPosition = this.WorkingPosition.x;
            this.yPosition = this.WorkingPosition.y;
            this.zPosition = this.WorkingPosition.z;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        boolean returnVal = !this.entity.getNavigator().noPath();
        
        if (!returnVal)// || AIHelper.Distance(this.WorkingPosition, 
        //		new Point3d(this.entity.posX, this.entity.posY, this.entity.posZ), 1.5))
        {
        	//this.WorkingPosition = null;
        	this.IsFinished = true;
        }
        
        return returnVal;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
    	PathNavigate n = this.entity.getNavigator();
    	//PathEntity pe = new PathEntity(null)
    	//n.setPath(par1PathEntity, par2)
        if (!n.tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed))
        {
        	//this.worldObj.getEntityPathToXYZ(this.theEntity, MathHelper.floor_double(par1), (int)par3, MathHelper.floor_double(par5), 
        	//		this.getPathSearchRange(), this.canPassOpenWoodenDoors, this.canPassClosedWoodenDoors, this.avoidsWater, this.canSwim);

        	PathEntity pe = this.entity.worldObj.getEntityPathToXYZ(this.entity, 
        		MathHelper.floor_double(this.xPosition), (int)this.yPosition, MathHelper.floor_double(this.zPosition),  
        		200, true, true, true, false);
        	
        	n.setPath(pe, this.speed);
        	
        	return;
        }
        return;
    }
}






















