package civ.Entity.AI;

import java.util.Random;
import java.math.*;

import javax.vecmath.Point3i;

import net.minecraftforge.common.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.EntityCreature;
import net.minecraft.item.*;
import net.minecraft.util.*;

public abstract class AIBase extends EntityAIBase {

	protected boolean IsFinished = false;
	protected Point3i WorkingPosition = null;
	
	public Point3i GetWorkingPosition()
	{
		return this.WorkingPosition;
	}
	
	public boolean IsFinished()
	{
		return this.IsFinished;
	}
	public void SetFinished(boolean b)
	{
		this.IsFinished = b;
	}
	public void SetWorkingPosition(Point3i p)
	{
		/*if (this.WorkingPosition != null)
			if (
			this.WorkingPosition.x == p.x &&
			this.WorkingPosition.y == p.y &&
			this.WorkingPosition.z == p.z)
				{
					
				}*/
		
		this.resetTask();
		this.WorkingPosition = new Point3i(p);		
	}
	
	public AIBase()
	{
		super();
	}
	
	@Override
	public void resetTask()
	{
		super.resetTask();
		
		this.IsFinished = false;
		this.WorkingPosition = null;
	}
}
