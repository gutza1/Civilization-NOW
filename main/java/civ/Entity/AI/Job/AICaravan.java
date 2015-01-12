package civ.Entity.AI.Job;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import civ.Core.CivMod;
import civ.Entity.AI.AIHelper;
import civ.Item.InventoryManager;
import civ.Item.Document.LocationDocument;
import civ.TileEntity.TileEntityJobPost;

public class AICaravan extends AIWorker 
{
	//Static Score Calculators
	protected static double RouteScore(AICaravan ai, TileEntity from, TileEntity to)
	{
		EntityVillager ev = ai._myVillager;
		double dist1 = AIHelper.Distance(ev.posX, ev.posY, ev.posZ, from.xCoord, from.yCoord, from.zCoord);
		double dist2 = AIHelper.Distance(from.xCoord, from.yCoord, from.zCoord, to.xCoord, to.yCoord, to.zCoord);
		double payoff = 1.0;
		
		TileEntity startTE = from,//AIHelper.GetTileEntity(this._myVillager.worldObj, this._start),
				endTE = to;//AIHelper.GetTileEntity(this._myVillager.worldObj, this._end);
		IInventory startInv = null, endInv = null;
		if (IInventory.class.isInstance(startTE))
			startInv = (IInventory)startTE;
		if (IInventory.class.isInstance(endTE))
			endInv = (IInventory)endTE;
		
		if (startInv == null || endInv == null)
			return 0;
		
		//Find HighestConcentration
		int startHighIndex, endHighIndex, giftHigh = -1;
		ItemStack typeHigh = null;
		for (int i = 0; i < startInv.getSizeInventory(); i++)
		{
			ItemStack curItem = startInv.getStackInSlot(i);
			
			if (curItem == null)
				continue;			

			//int j = InventoryManager.NextMatchingInventoryStackIndex(curItem, endInv, 0);
			int countI = InventoryManager.TotalInventoryContained(curItem, startInv);
			int countJ = InventoryManager.TotalInventoryContained(curItem, endInv);
			
			int startInvCount = startInv.getSizeInventory(), endInvCount = endInv.getSizeInventory();
			int maxStackSize = curItem.getMaxStackSize();
			
			int maxSpaceI = startInvCount;
					//InventoryManager.TotalEmptySlots(startInv)
					//* maxStackSize;
			int maxSpaceJ = endInvCount;
					//InventoryManager.TotalEmptySlots(endInv)
					//* maxStackSize;
			
			double Iconc = (1.0 * countI);// / ((1 + maxSpaceI) * 1.0);
			double Jconc = (1.0 * countJ);// / ((1 + maxSpaceJ) * 1.0);
			double dif = (Iconc - Jconc) * .5; 
			
			if (Iconc <= Jconc)
				continue;
			
			int gift = (int)(dif * ai._maxEquilibriumRate);
			
			if (gift > giftHigh)
			{
				giftHigh = gift;
				//startHighIndex = i;
				//endHighIndex = ;
				typeHigh = curItem;
			}
		}
		
		return giftHigh / (dist1 + dist2);
	}
	protected static double[][] RouteScores(AICaravan ai, List<TileEntity> allInvs)
	{
		double[][] r = new double[allInvs.size()][];
		for (int i = 0; i < allInvs.size(); i++)
		{
			r[i] = new double[allInvs.size()];
			for (int j = 0; j < allInvs.size(); j++)
			{
				if (i == j)
					continue;				
				
				r[i][j] = AICaravan.RouteScore(ai, allInvs.get(i), allInvs.get(j));
			}
		}
		
		return r;
	}
	
	//Fields
	private Point3i _start, _end;
	private boolean _startCheck, _endCheck;
	protected int _stackPerTrip = 3;
	protected double _maxEquilibriumRate = .5;
	
	public AICaravan(EntityVillager ev, TileEntityJobPost jp)
	{
		super(ev, jp);
		
		this._acceptableWorkDistance = 3.5;
	}
	
	public void PerformTrade()
	{
		TileEntity startTE = AIHelper.GetTileEntity(this._myVillager.worldObj, this._start),
				endTE = AIHelper.GetTileEntity(this._myVillager.worldObj, this._end);
		IInventory startInv = null, endInv = null;
		if (IInventory.class.isInstance(startTE))
			startInv = (IInventory)startTE;
		if (IInventory.class.isInstance(endTE))
			endInv = (IInventory)endTE;
		
		if (startInv == null || endInv == null)
			return;
		
		//Find HighestConcentration
		for (int h = 0; h < this._stackPerTrip; h++)
		{
			int startHighIndex, endHighIndex, giftHigh = -1;
			ItemStack typeHigh = null;
			for (int i = 0; i < startInv.getSizeInventory(); i++)
			{
				ItemStack curItem = startInv.getStackInSlot(i);
				
				if (curItem == null)
					continue;			
	
				//int j = InventoryManager.NextMatchingInventoryStackIndex(curItem, endInv, 0);
				int countI = InventoryManager.TotalInventoryContained(curItem, startInv);
				int countJ = InventoryManager.TotalInventoryContained(curItem, endInv);
				
				int startInvCount = startInv.getSizeInventory(), endInvCount = endInv.getSizeInventory();
				int maxStackSize = curItem.getMaxStackSize();
				
				int maxSpaceI = startInvCount;
						//InventoryManager.TotalEmptySlots(startInv)
						//* maxStackSize;
				int maxSpaceJ = endInvCount;
						//InventoryManager.TotalEmptySlots(endInv)
						//* maxStackSize;
				
				double Iconc = (1.0 * countI);// / ((1 + maxSpaceI) * 1.0);
				double Jconc = (1.0 * countJ);// / ((1 + maxSpaceJ) * 1.0);
				double dif = (Iconc - Jconc) * .5; 
				
				if (Iconc <= Jconc)
					continue;
				
				int gift = (int)(dif * this._maxEquilibriumRate);
				
				if (gift > giftHigh)
				{
					giftHigh = gift;
					//startHighIndex = i;
					//endHighIndex = ;
					typeHigh = curItem;
				}
					
			}
			
			if (giftHigh <= 0)
				return;
			
			if (giftHigh > typeHigh.getMaxStackSize())
				giftHigh = typeHigh.getMaxStackSize();
				
			ItemStack giftStack = new ItemStack(typeHigh.getItem(), giftHigh);
			
			if (!InventoryManager.ContainsInventory(giftStack, startInv) || !InventoryManager.HasInventorySpaceFor(giftStack, endInv))
				return;
				
			InventoryManager.RemoveFromInventory(giftStack, startInv);
			InventoryManager.AddToInventory(giftStack, endInv);
			
			continue;
		}
		
		return;
	}

	@Override
	public Point3i NextWorkPosition() {

		List<Point3i> locs = this._myJobOwner.GetWorkSites(new Block[] { Blocks.chest });
		List<TileEntity> tes = new ArrayList<TileEntity>();
		for (int i = 0; i < locs.size(); i++)
		{
			Point3i p = locs.get(i);
			TileEntity te = this._myVillager.worldObj.getTileEntity(p.x, p.y, p.z);
			
			if (TileEntityChest.class.isInstance(te))
				tes.add(te);				
		}
		
		double[][] scrs = AICaravan.RouteScores(this, tes);
		
		int il = 0, jl = 0;
		double score = 0;
		for (int i = 0; i < scrs.length; i++)
			for (int j = 0; j < scrs[i].length; j++)
				if (scrs[i][j] > score)
				{
					score = scrs[i][j];
					il = i;
					jl = j;
				}
		
		//Eventually Handle (a = current loc, b = start, c = end)
		//trade (b->c) generates
		//check if nearbychest (a) to next destination (b) score > 0, if so, set to that trade (a->b), 
		//next iteration will yield original trade (b->c)
		
		this._start = AIHelper.GetLocation(tes.get(il));
		this._end = AIHelper.GetLocation(tes.get(jl));
		this._startCheck = false;
		this._endCheck = false;
		
		return this._start;
	}

	@Override
	public boolean DoWorkOnPosition()
	{

		Block b = AIHelper.GetBlock(this._myVillager.worldObj, this._myNav.GetLastPosition());
		
		if (b == null)
			return true;
		
		if (Block.isEqualTo(b, CivMod.JobPost))// || Block.isEqualTo(b,Blocks.chest))
			return true;
		
		if (this._start == null && this._end == null)
			return true;
		
		if (!this._startCheck &&
					AIHelper.IsWithinDistance(this._start, this._myVillager.posX, this._myVillager.posY, this._myVillager.posZ, 
					this._acceptableWorkDistance, false))
		{
			this._startCheck = true;
			this.SetMoveToLocation(this._end);
			
			return false;
		}

		if (this._startCheck && !this._endCheck)
		{
			if (AIHelper.IsWithinDistance(this._end, this._myVillager.posX, this._myVillager.posY, this._myVillager.posZ, 
					this._acceptableWorkDistance, false))
			{
				this._endCheck = true;
				
				//DO TRADE
				this.PerformTrade();
				
				return true;
			}
			else
				return false;
		}
		
		return true;
	}

}
