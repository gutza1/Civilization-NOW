package civ.Entity.AI.Job;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import civ.Core.CivMod;
import civ.Entity.AI.AIHelper;
import civ.Entity.AI.AITravelToLocation;
import civ.Item.InventoryManager;
import civ.TileEntity.TileEntityJobPost;

public abstract class AIWorker extends EntityAIBase
{	
	public static enum WorkState { Pre, Idle, Search, Move, Work };
	public static final int MaxTickWait = 800;
	
	protected EntityVillager _myVillager = null;
	protected IJobOwner _myJobOwner = null;
	protected AITravelToLocation _myNav = null;
	protected Block[] _workSiteBlockTypes = new Block[] { Blocks.wall_sign, Blocks.standing_sign };
	protected Class _workItemClass = null;
	protected WorkState _myState = WorkState.Pre;
	
	//protected int[] StateTickDelay = new int[] { 100, 100, 100, 100, 200 };
	protected int[] StateTickDelay = new int[] { 20, 20, 20, 20, 50 };
	//protected int[] StateTickDelay = new int[] { 0, 0, 0, 0, 0 };
	protected double _acceptableWorkDistance = 2.0, _acceptableSiteDistance = 15;
	protected int _ticksInState = 0;
	protected boolean _includeYInDistance = true;
	protected Point3i _blockToWork = null, _lastBlockWorked = null, _currentJobSite = null;
	
	public boolean UseCurrentTool(Class toolClass)
	{
		ItemStack toolStack = InventoryManager.NextMatchingInventoryStack(toolClass, this._myJobOwner.GetRepository());		
		if (toolStack != null)
		{
			int uses = toolStack.getMaxDamage() - toolStack.getItemDamage();
			if (uses > 0)
				toolStack.attemptDamageItem(1, CivMod.RandomObj);
		
			if (toolStack.getMaxDamage() <= toolStack.getItemDamage())
				toolStack.stackSize = 0;
				
			if (uses > 0)
				return true;
			else
				return false;
		}
		
		return false;
	}
	
	public void SetIncludeYDist(boolean b)
	{
		this._includeYInDistance = b;
	}
	public void SetState(WorkState s)
	{
		this._myState = s;
		
		//Create a wait until activity can begin
		this._ticksInState = (this.StateTickDelay[s.ordinal()] * -1) - CivMod.RandomObj.nextInt(20);
	}	
	public EntityVillager GetVillager()
	{
		return this._myVillager;
	}
	public Point3i GetRandomWorkSite()
	{
		List<Point3i> ps = this._myJobOwner.GetWorkSites(this._workSiteBlockTypes);
		
		if (ps == null || ps.size() == 0)
			return this._myJobOwner.GetLocation();
				
		return ps.get(CivMod.RandomObj.nextInt(ps.size()));
	}

	public AIWorker(EntityVillager ev, IJobOwner te)
	{
		super();
		
		this._myVillager = ev;
		this._myJobOwner = te;
		
		//Create Navigator AI
		this._myNav = new AITravelToLocation(this._myVillager, .6f, null);
		
		//Add nav to entity		
		ev.tasks.addTask(7, this._myNav);
	}
	
	public void SetMoveToLocation(Point3i p)
	{
		this.resetTask();
		this._myNav.SetWorkingPosition(p);	
		this.SetState(WorkState.Move);	
	}

	public abstract Point3i NextWorkPosition();
	public abstract boolean DoWorkOnPosition();
		
	@Override
	public boolean shouldExecute() {

		if (this._myVillager.worldObj.isRemote)
			return false;
		
		if (!this._myJobOwner.IsBehaviorTime())
		{
			this._myNav.resetTask();
			return false;
		}
		
		if (TileEntityJobPost.class.isInstance(this._myJobOwner) && this._myJobOwner != null)
		{
			TileEntityJobPost te = (TileEntityJobPost)this._myJobOwner;
		
			if (this._myVillager.worldObj.getTileEntity(te.xCoord, te.yCoord, te.zCoord) != te)
			{			
				((TileEntityJobPost)this._myJobOwner).unloadWorkers();
				return false;
			}
		}
		
		return true;
	}	
	@Override
    public boolean continueExecuting()
    {
		if (this._myVillager.worldObj.isRemote)
			return false;
		
        if (!this.shouldExecute())
        	return false;        
        
        if (this._ticksInState < 0)
        {
        	this._ticksInState++;
        	return true;
        }
        
        //Designed in fly, not really a while loop. Just a good way to break out of a segment of code.
        int x = 0;
        while (true)
	    {
        	if (x > 2)
         		break;
        	
        	x++;
        	
	        if (this._myState == WorkState.Pre)
	        {
	    		this._currentJobSite = this.GetRandomWorkSite();
	    		this.SetMoveToLocation(this._currentJobSite);	
	        	break;
	        }
	        
	        if (this._myState == WorkState.Idle)
	        {
	        	this.SetState(WorkState.Search);
	        	break;
	        }
	        
	        //If searching
	        if (this._myState == WorkState.Search)
	        {
	        	Point3i nwp = this.NextWorkPosition();
	        	
	        	if (nwp != null && AIHelper.IsWithinDistance(this._currentJobSite, nwp, this._acceptableSiteDistance))
	        		this._myNav.SetWorkingPosition(nwp);
	        	
	        	if (this._ticksInState > AIWorker.MaxTickWait)
	        		this.SetState(WorkState.Pre); 
	        	
	        	if (this._myNav.GetWorkingPosition() != null)
	            	this.SetState(WorkState.Move);  
	        	
	        	break;
	        }
	        
	        if (!AIHelper.IsWithinDistance(this._currentJobSite, this._myVillager.posX, this._myVillager.posY, this._myVillager.posZ, this._acceptableSiteDistance, false))
	        	this.SetMoveToLocation(this._currentJobSite);
	        
	        /*if (this._myNav.GetWorkingPosition() == null && this._myState != WorkState.Search)
	        {
	        	this.SetState(WorkState.Search); 
	        	break;
	        }*/
	        
	        boolean pReached = this._myNav.PositionReached(this._includeYInDistance, this._acceptableWorkDistance);
	
	        //If ready to work but not at location, goto location
	        if (!pReached && this._myState == WorkState.Work)
	        {
	        	this.SetMoveToLocation(this._myNav.GetLastPosition());
	        	break;
	        }
	        
	        //If At location but not ready to work, set ready
	        if (pReached && this._myState != WorkState.Work)
	        {
	        	this.SetState(WorkState.Work);
	        	break;
	        }
		    
	        //If not at location, not moving, and has locationo, goto location
	        if (!pReached && this._myState != WorkState.Move && this._myNav.GetWorkingPosition() != null)	        
	        {
	        	this.SetState(WorkState.Move); 
	        	break;
	        }	        
	        
	        //If moving
	        if (this._myState == WorkState.Move)	        	
	        {
	        	//If no location exists to move to, move to last position
	        	if (this._myNav.GetWorkingPosition() == null)
		        	this.SetMoveToLocation(this._myNav.GetLastPosition());	
		        
	        	break;		        
	        }
	        
	        //If ready to work, do work
	        if (this._myState == WorkState.Work)
	        {
	        	if (this.DoWorkOnPosition())
	        		this.SetState(WorkState.Idle);  
	        	else
	        		this._ticksInState = 0;
	        	
	        	break;
	        }
	        	
	    }//EndWhile        
        
        if (this._ticksInState > AIWorker.MaxTickWait)
        {
        	/*if (this._lastLocation != null && AIHelper.IsWithinDistance(        	
        		this._lastLocation.x , this._lastLocation.y, this._lastLocation.z, 
        		this._myVillager.posX, this._myVillager.posY, this._myVillager.posZ, 
        		6, !this._includeYInDistance))
        		this.SetState(WorkState.Pre);
        	else*/
        		this.SetState(WorkState.Pre);
        		this._ticksInState = 0;
        }        

        this._ticksInState++;        
        return true;
    }
	@Override
    public void startExecuting() 
    {
    	this.resetTask();  
    }


}

