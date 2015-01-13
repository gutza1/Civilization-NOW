package civ.TileEntity;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import com.sun.xml.internal.stream.Entity;

import civ.Block.BlockShopTable;
import civ.Core.CivMod;
import civ.Entity.AI.AIHelper;
import civ.Entity.AI.AIHelper.AIState;
import civ.Entity.AI.Job.AIBaker;
import civ.Entity.AI.Job.AICaravan;
import civ.Entity.AI.Job.AIFarming;
import civ.Entity.AI.Job.AILumberJack;
import civ.Entity.AI.Job.AIQuarry;
import civ.Entity.AI.Job.AIWorker;
import civ.Entity.AI.Job.IJobOwner;
import civ.Item.InventoryManager;
import civ.Item.Key;
import civ.Item.Document.IdentityDocument;
import civ.Item.Document.LocationDocument;
import civ.Merchant.MerchantTradeRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class TileEntityJobPost extends TileEntityPost implements IJobOwner
{
	//Private static fields
	private static int _AITradeSearchDelayTickWaitBase = 120, _inventorySize = 16,
			workerSearchXZ = 200, workerSearchY = 60;	
	public static boolean IsRepository(TileEntity te)
	{
		if (TileEntityChest.class.isInstance(te))
			return true;
		
		return false;
	}
	
	//Fields
	private int _tickCount = 0;
	protected int _startTime = 1000, _endTime = 10000;	    
	protected IInventory _repository = null;
	protected Item _jobType = null;
	protected List<AIWorker> _myWorkers = new ArrayList<AIWorker>();
	protected List<ItemStack> _myWorkSites = new ArrayList<ItemStack>();

	public boolean HasWorkRequirements()
	{
		if (this.chestContents[0] == null)
			return false;
		
		Item jobtype = this.chestContents[0].getItem();
		
		if (jobtype == null)
			return false;
		
		return true;		
	}	
	public Point3i GetRandomWorkSite(Item type)
	{
		if (this._myWorkSites.size() > 0)
		{
			
			ItemStack site = null;
			int wait = 0;
			while (wait < 10 && (site == null || !type.getClass().isInstance(site.getItem())))
			{
				site = this._myWorkSites.get(CivMod.RandomObj.nextInt(this._myWorkSites.size()));
				wait++;
			}
			
			if (site == null)
				return null;
			
			return LocationDocument.GetLocation(site);
		}
		
		
		return new Point3i(this.xCoord, this.yCoord, this.zCoord);
	}
	@Override
	public Point3i GetLocation() {
		return new Point3i(this.xCoord, this.yCoord, this.zCoord);
	}
	public List<Point3i> GetWorkSites(Item type)
	{
		List<Point3i> workSites = new ArrayList<Point3i>();
		for (int i = 0; i < this._myWorkSites.size(); i++)
			if (type.getClass().isInstance(AIHelper.GetTileEntity(this.worldObj, LocationDocument.GetLocation(this._myWorkSites.get(i)))))
				workSites.add(LocationDocument.GetLocation(this._myWorkSites.get(i)));		
		
		return workSites;
	}	
	public List<Point3i> GetWorkSites(Block[] types)
	{
		List<Point3i> workSites = new ArrayList<Point3i>();
		for (int i = 0; i < this._myWorkSites.size(); i++)
			for (int j = 0; j < types.length; j++)
			{
				Point3i loc = LocationDocument.GetLocation(this._myWorkSites.get(i));
				Block b = AIHelper.GetBlock(this.worldObj, loc);
				
				if (loc == null || b == null)
					continue;
								
				if (types[j].getClass().isInstance(b))
					workSites.add(LocationDocument.GetLocation(this._myWorkSites.get(i)));		
			}
		
		return workSites;
	}
	public Point3i GetRandomWorkSite(Block[] types) {
		List<Point3i> ps = this.GetWorkSites(types);
		return ps.get(CivMod.RandomObj.nextInt(ps.size()));
	}
	public List<ItemStack> GetWorkSites()
	{
		return this._myWorkSites;
	}
	public IInventory GetRepository()
	{
		return this._repository;
	}
	public ItemStack NextWorkItem()
	{
		for (int i = 0; i < this.chestContents.length; i++)
		{
			if (this.chestContents[i] == null || i == 0 || i == 8 || i == 7 || i == 15)
				continue;
			
			if (this._jobType == CivMod.FarmContract && ItemHoe.class.isInstance(this.chestContents[i].getItem()))
				return this.chestContents[i];
		}
		
		return null;
	}
	
	public boolean ContainsWorker(ItemStack identityDocument)
	{
		for (int i = 0; i < this._myWorkers.size(); i++)
		{
			EntityVillager ev1 = this._myWorkers.get(i).GetVillager();
			long vid = IdentityDocument.GetId(ev1), idDoc = IdentityDocument.GetId(identityDocument);
			
			if (vid == idDoc)
				return true;
		}
		
		return false;
	}
	public boolean IsBehaviorTime()
	{
		long currentTime = this.worldObj.getWorldTime() % 24000;
		return currentTime >= this._startTime && currentTime <= this._endTime;
	}	
	@Override
	public int getSizeInventory()
    {
        return TileEntityJobPost._inventorySize;
    } 
	
	public TileEntityJobPost()
	{
		super();
		
		this.blockType = CivMod.JobPost;	
		
		this.chestContents = new ItemStack[TileEntityJobPost._inventorySize];
	}    
   
	public void intakeWorkerProduct(List<ItemStack> items)
	{
		if (this._repository != null)
		{
			for (int i = 0; i < items.size(); i++)
				if (!InventoryManager.AddToInventory(items.get(i), this._repository))
					continue;
		}
		else
		{
			for (int i = 0; i < items.size(); i++)
				this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, 
						this.xCoord, this.yCoord + 1, this.zCoord, items.get(i)));
		}
						
		return;				
	}
	public void updateEntity()
	{ 	  		
  		if (this.worldObj.isRemote)
  			return;
  		
  		//Handle AI Tick Delay
  		if (this._tickCount > TileEntityJobPost._AITradeSearchDelayTickWaitBase)
  			this._tickCount = 20 - CivMod.RandomObj.nextInt(40);
  		else
  		{
  	  		this._tickCount++;
  			return;
  		}
  		
  		if (!this.IsBehaviorTime() || !this.updatePropertiesValid())
  		{
  			if (this._myWorkers.size() > 0)
  				this.unloadWorkers();
  			
  			return;
  		}
  		
  		//Verify job related properties are valid
  		//if (!this.updatePropertiesValid())
  		//	return;
  		
  		//Get Current Work Items
  		List<ItemStack> workSites = new ArrayList<ItemStack>();
  		List<ItemStack> newEmployees = new ArrayList<ItemStack>();
  		for (int i = 0; i < this.getSizeInventory(); i++)
  		{
  			if (this.chestContents[i] == null || i == 0 || i == 8 || i == 7 || i == 15)
  				continue;
  			
  			//Add all locations to work sites
  			if (LocationDocument.class.isInstance(this.chestContents[i].getItem()))
  				workSites.add(this.chestContents[i]);

  			//Add all employess who are not already part of AI to newEmp list
  			if (IdentityDocument.class.isInstance(this.chestContents[i].getItem())
  				&& !this.ContainsWorker(this.chestContents[i]))
  					newEmployees.add(this.chestContents[i]);  			
  		}
  		
  		//Iterate through new employees and send to random work sites with new AI
		for (int i = 0; i < newEmployees.size(); i++)
		{			
			EntityVillager ev = AIHelper.GetVillager(IdentityDocument.GetId(newEmployees.get(i)), this.worldObj, 
					this.xCoord, this.yCoord, this.zCoord, workerSearchXZ, workerSearchY);
			
			if (ev == null)
				continue;

			//Set Villager to base AI
			AIWorker newAI = null;
			AIHelper.SetAIState(ev, AIState.Base);
			
			//Set AI object
			if (this._jobType == CivMod.FarmContract)
				newAI = new AIFarming(ev, this);
			if (this._jobType == CivMod.LumberContract)
				newAI = new AILumberJack(ev, this);
			if (this._jobType == CivMod.QuarryContract)
				newAI = new AIQuarry(ev, this);
			if (this._jobType == CivMod.CaravanContract)
				newAI = new AICaravan(ev, this);
			if (this._jobType == CivMod.BakerContract)
				newAI = new AIBaker(ev, this);
			
			if (newAI == null)
				continue;
			
			//Add to Villager and Job Table
			ev.tasks.addTask(15, newAI);
			this._myWorkers.add(newAI);
		}  
		
		//Check new worksites
		this._myWorkSites.clear();
		for (int i = 0; i < workSites.size(); i++)
			if (LocationDocument.IsValidLocation(this.worldObj, workSites.get(i)))// && !this._myWorkSites.contains(workSites.get(i)))
				this._myWorkSites.add(workSites.get(i));
		
		/*
		for (int i = 0; i < this._myWorkSites.size(); i++)
			if (!LocationDocument.IsValidLocation(this.worldObj, this._myWorkSites.get(i)))
			{
				this._myWorkSites.remove(i);
				i--;
			}*/
	}
	
	public boolean updatePropertiesValid()
	{		
		if (!this.HasWorkRequirements())
			return false;
		
		this._jobType = this.chestContents[0].getItem();
  		
  		//Get Repository Information
  		Point3i repoLoc = LocationDocument.GetLocation(this.chestContents[8]);  
  		TileEntity te = null;
  		if (repoLoc != null)
  			te = this.worldObj.getTileEntity(repoLoc.x, repoLoc.y, repoLoc.z);
  		if (!TileEntityJobPost.IsRepository(te))
  		{
  			IInventory inv = this.GetNeighborChest();
  			
  			//if (inv == null)
  			//	return false;
  			
  			this._repository = inv;
  		}
  		else
  			this._repository = (IInventory)te;
  		
  		//If no Repository, Return
  		//if  (this._repository == null)
  		//	return false;  	
  		
  		return true;
	}
	public void unloadWorkers()
	{
		for (int i = 0; i < this._myWorkers.size(); i++)
			AIHelper.SetAIState(this._myWorkers.get(i).GetVillager(), AIState.Idle);
		this._myWorkers.clear();
	}



}
