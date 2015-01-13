package civ.Entity.AI.Job;

import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import civ.Block.BlockManager;
import civ.Entity.AI.AIHelper;
import civ.Item.InventoryManager;
import civ.TileEntity.TileEntityJobPost;

public class AIFarming extends AIGathering
{
	public AIFarming(EntityVillager ev, TileEntityJobPost jp) {
		super(ev, jp);
		
		this._gatherBlocks = new Block[] { Blocks.wheat, Blocks.potatoes, Blocks.carrots, Blocks.melon_block, Blocks.pumpkin,
				Blocks.reeds, Blocks.cactus }; 
		this._gatherMeta = new int[] { 7, 7, 7, 0, 0,
				0, 0 };
		this._replaceCost = new Item[] { Items.wheat_seeds, Items.potato, Items.carrot, null, null,
				Items.reeds, Item.getItemFromBlock(Blocks.cactus) };
		this._replaceMeta = new int[] { 0, 0, 0, 0, 0,
				0, 0 };
		
		this._workItemClass = ItemHoe.class;
	}
	
	@Override
	public Point3i NextWorkPosition() {
		
		Point3d p = AIHelper.GetLocation(this._myVillager);
		Point3i loc = BlockManager.GetRandomTopNeighbor(this._myVillager.worldObj, (int)p.x, (int)p.y, (int)p.z, 6, 4, 6, this._gatherBlocks, this._gatherMeta);
		
		if (loc == null)
			return null;
			
		Block b1 = this._myVillager.worldObj.getBlock((int)loc.x, (int)loc.y, (int)loc.z);
		Block b2 = this._myVillager.worldObj.getBlock((int)loc.x, (int)loc.y - 1, (int)loc.z);
		
		if (Block.isEqualTo(b1, Blocks.reeds) || Block.isEqualTo(b1, Blocks.cactus))
		{ 	
			if ((Block.isEqualTo(b2, Blocks.reeds) || Block.isEqualTo(b2, Blocks.cactus)))
			{
				this._blockToWork = loc;
				return loc;
			}
			else
				return null;			
		}
		else if (Block.isEqualTo(b2, Blocks.farmland) ||
					Block.isEqualTo(b2, Blocks.dirt) || 
					Block.isEqualTo(b2, Blocks.sand))
		{
			this._blockToWork = loc;
			return loc;
		}
		
		return null;
	}
	
	@Override
	public boolean DoWorkOnPosition() 
	{
		Point3i p = this._blockToWork;//this._myNav.GetLastPosition();
		Block b = this._myVillager.worldObj.getBlock(p.x, p.y, p.z);
		int m = this._myVillager.worldObj.getBlockMetadata(p.x, p.y, p.z);
		
		//Traditional grab drops and set to Air
		if (!super.DoWorkOnPosition())
			return false;
		
		//Check for seed
		ItemStack is = null;
		//int seedDropIndex = -1;
		int gatherIndex = -1;
		if (this._replantWithCost || this._replantNoCost)
			for (int i = 0; i < this._replaceCost.length; i++)
			{
				if (this._gatherBlocks[i] == b && this._gatherMeta[i] == m)
					gatherIndex = i;
				
				//for (int j = 0; j < drops.size(); j++)
				//	if (this._replaceCost[i] == drops.get(j).getItem())
				//		seedDropIndex = j;
			}
		
		if (gatherIndex == -1)
			return true;
		
		//Figure Replanting
		boolean replant = this._replantNoCost;
		ItemStack replStack = new ItemStack(this._replaceCost[gatherIndex], 1);
		if (this._replantWithCost && !this._replantNoCost && gatherIndex != -1 && this._myJobOwner.GetRepository() != null &&
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
		if (replant)
			this._myVillager.worldObj.setBlock(p.x, p.y, p.z, b, this._replaceMeta[gatherIndex], 3);
		
		return true;
	}

}
