package civ.Entity.AI.Job;

import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import civ.Block.BlockManager;
import civ.Entity.AI.AIHelper;
import civ.Item.InventoryManager;
import civ.TileEntity.TileEntityJobPost;

public class AILumberJack extends AIGathering
{	
	protected static Block[] LeafBlocks = new Block[] { Blocks.leaves, Blocks.leaves2 };
	protected static int[] LeafMeta = new int[] { -1, -1 };

	protected static Block[] LogBlocks = new Block[] { Blocks.log, Blocks.log2 };
	protected static int[] LogMeta = new int[] { -1, -1 };
	
	public AILumberJack(EntityVillager ev, TileEntityJobPost jp)
	{
		super(ev, jp);	
		this._includeYInDistance = false;
		this._acceptableWorkDistance = 5;
		
		this._gatherBlocks = LogBlocks;
		this._gatherMeta = LogMeta;		

		this._workItemClass = ItemAxe.class;
	}
	
	//@Override
	public Point3i NextWorkPosition2() {
		
		Point3i loc = null;		
		Point3d p = AIHelper.GetLocation(this._myVillager);
		World w = this._myVillager.worldObj;
		
		if (this._lastBlockWorked != null)
		{
			if (BlockManager.IsMatch(w.getBlock(this._lastBlockWorked.x, this._lastBlockWorked.y - 1, this._lastBlockWorked.z),	-1, LogBlocks, LogMeta))
				loc = new Point3i(this._lastBlockWorked.x, this._lastBlockWorked.y - 1, this._lastBlockWorked.z);
			else					
				loc = BlockManager.GetNearbyBlockLocByType(w, this._lastBlockWorked.x, this._lastBlockWorked.y, this._lastBlockWorked.z, 1, 0, 1, LogBlocks);
		}
		
		if (loc == null)
			loc = BlockManager.GetRandomTopNeighbor(this._myVillager.worldObj, (int)p.x, (int)p.y, (int)p.z, 6, 20, 6, 
				LeafBlocks, LeafMeta);
		
		if (loc == null)
			return null;
		
		//Cycle downward until log is hit
		int dy = -1;
		Block b = this._myVillager.worldObj.getBlock(loc.x, loc.y + dy, loc.z);
		while (Block.isEqualTo(b, Blocks.air) || BlockManager.IsMatch(b, -1, LeafBlocks, LeafMeta))
		{
			dy--;
			b = this._myVillager.worldObj.getBlock(loc.x, loc.y + dy, loc.z);
		}
		
		if (!BlockManager.IsMatch(b, -1, LogBlocks, LogMeta))
			return null;
		
		loc.y += dy;
		
		return loc;
		
			/*
		Block b1 = this._myVillager.worldObj.getBlock((int)loc.x, (int)loc.y, (int)loc.z);
		Block b2 = this._myVillager.worldObj.getBlock((int)loc.x, (int)loc.y - 1, (int)loc.z);
		
		if (Block.isEqualTo(b1, Blocks.reeds) || Block.isEqualTo(b1, Blocks.cactus))
		{ 	
			if ((Block.isEqualTo(b2, Blocks.reeds) || Block.isEqualTo(b2, Blocks.cactus)))
				return loc;
			else
				return null;			
		}
		else if (Block.isEqualTo(b2, Blocks.farmland) ||
					Block.isEqualTo(b2, Blocks.dirt) || 
					Block.isEqualTo(b2, Blocks.sand))
			return loc;
		
		return null;*/
	}

	@Override
	public Point3i NextWorkPosition() {

		World w = this._myVillager.worldObj;
		
		//Check Last Work Position
		if (this._lastBlockWorked != null)
		{
			Block above = w.getBlock(this._lastBlockWorked.x, this._lastBlockWorked.y + 1, this._lastBlockWorked.z);
			int meta = w.getBlockMetadata(this._lastBlockWorked.x, this._lastBlockWorked.y + 1, this._lastBlockWorked.z);
			
			if (BlockManager.IsMatch(above, meta, LogBlocks, LogMeta))
			{
				this._blockToWork = new Point3i(this._lastBlockWorked.x, this._lastBlockWorked.y - 1, this._lastBlockWorked.z);
				return new Point3i(this._blockToWork.x, (int)this._myVillager.posY - 1, this._blockToWork.z);
			}
		}
		
		Point3d p = AIHelper.GetLocation(this._myVillager);
		Point3i loc = BlockManager.GetRandomTopNeighbor(w, (int)p.x, (int)p.y, (int)p.z, 6, 20, 6, 
				LeafBlocks, LeafMeta);
		
		if (loc == null)
			return null;
		
		int y = loc.y;
		Block b = w.getBlock(loc.x, y, loc.z);//, bprev = null;
		
		if (loc != null)
			while (b == Blocks.air || b == Blocks.log || b == Blocks.log2 || b == Blocks.leaves || b == Blocks.leaves2)
			{
				//bprev = b;
				y--;
				b = w.getBlock(loc.x, y, loc.z);
			}
		
		//if (b == Blocks.dirt ||)
			for (int i = 0; i < this.LogBlocks.length; i++)	
			{
				Point3i rp = new Point3i(loc.x, y + 1, loc.z);
				
				rp = BlockManager.GetNearbyBlockLocByType(w, loc.x, y + 1, loc.z, 7, 20, 6, this.LogBlocks);
				
				if (rp == null)
					return null;
				
				//Verify leaves at top
				Point3i tp = new Point3i(rp);
				Block tb = w.getBlock(tp.x, tp.y, tp.z);
				while (tb != Blocks.air)
				{
					tp.y++;
					
					if (tb == Blocks.leaves || tb == Blocks.leaves2)
						break;					

					tb = w.getBlock(tp.x, tp.y, tp.z);
				}
				
				if (tb == Blocks.air)
					return null;
				
				Block t = w.getBlock(rp.x, rp.y, rp.z);
				while (t == Blocks.log || t == Blocks.log2)
				{
					rp.y--;
					t = w.getBlock(rp.x, rp.y, rp.z);
				}
				
				rp.y++;
				
				if (BlockManager.IsMatch(w.getBlock(rp.x, rp.y, rp.z), w.getBlockMetadata(rp.x, rp.y, rp.z), 
						LogBlocks, LogMeta))
				{
					this._blockToWork = rp;
					return new Point3i(rp.x, (int)this._myVillager.posY + 1, rp.z);
				}
			}
		
		return null;
	}
	@Override
	public boolean DoWorkOnPosition() 
	{			
		Point3i p = this._blockToWork;//this._myNav.GetLastPosition();		
		
		Block b = null;
		int m = -1;
		if (p != null)
		{
			b = this._myVillager.worldObj.getBlock(p.x, p.y, p.z);
			m = this._myVillager.worldObj.getBlockMetadata(p.x, p.y, p.z);
		}
	
		if (!super.DoWorkOnPosition())
			return false;
		
		if (p == null || m == -1 || b == null)
			return true;
		
		int logIndex = -1;
		for (int i = 0; i < LogBlocks.length; i++)
			if (LogBlocks[i] == b)
				logIndex = i;
		
		if (logIndex == -1)
			return true;
		
		int sapM = -1;
		if (logIndex == 0)
			sapM = (m % 4);
		if (logIndex == 1)
			sapM = 4 + (m % 2);
		
		if (sapM == -1)
			return true;
		
		//Figure Replanting
		
		boolean replant = this._replantNoCost;
		ItemStack replStack = new ItemStack(Blocks.sapling, 1, sapM);
		if (this._replantWithCost && !this._replantNoCost && this._myJobOwner.GetRepository() != null && 
				InventoryManager.ContainsInventory(replStack, this._myJobOwner.GetRepository()))
		{
			/*if (drops.get(seedDropIndex).stackSize == 1)
				drops.remove(seedDropIndex);
			else
				drops.get(seedDropIndex).stackSize--;*/
			
			InventoryManager.RemoveFromInventory(replStack, this._myJobOwner.GetRepository());
			
			replant = true;
		}
		
		//Replant if necessesary
		if (replant && BlockManager.IsMatch(this._myVillager.worldObj.getBlock(p.x, p.y - 1, p.z), -1, 
				new Block[] { Blocks.grass, Blocks.dirt }, new int[] { -1, -1 }))
			this._myVillager.worldObj.setBlock(p.x, p.y, p.z, Blocks.sapling, sapM, 3);
		
		return true;
	}

}
