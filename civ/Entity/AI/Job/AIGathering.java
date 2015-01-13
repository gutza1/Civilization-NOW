package civ.Entity.AI.Job;

import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import civ.Block.BlockManager;
import civ.Entity.AI.AIHelper;
import civ.TileEntity.TileEntityJobPost;

public abstract class AIGathering extends AIWorker
{
	protected Block[] _gatherBlocks = null;
	protected int[] _gatherMeta = null;
	protected Item[] _replaceCost = null;
	protected int[] _replaceMeta = null;
	
	protected boolean _replantWithCost = true, _replantNoCost = false;
	
	public AIGathering(EntityVillager ev, TileEntityJobPost jp) 
	{
		super(ev, jp);

	}
	
	@Override
	public boolean DoWorkOnPosition()
	{
		if (this._blockToWork == null)
			return true;
		
		//Get Variables
		Point3i p = this._blockToWork;
		this._blockToWork = null;
		this._lastBlockWorked = null;
		Block b = this._myVillager.worldObj.getBlock(p.x, p.y, p.z);
		int m = this._myVillager.worldObj.getBlockMetadata(p.x, p.y, p.z);
		
		//Verify Gather Match
		/*boolean cont = false;
		for (int i = 0; i < this._gatherBlocks.length; i++)
			if (this._gatherBlocks[i] == b && this._gatherMeta[i] == m)
			{
				cont = true;
				break;
			}
		
		if (!cont)
			return true;*/
		if (!BlockManager.IsMatch(b, m, this._gatherBlocks, this._gatherMeta))
			return true;
		
		//Get Block Drops
		List<ItemStack> drops = b.getDrops(this._myVillager.worldObj, p.x, p.y, p.z, m, 1);
		
		//Set block to air in world		
		boolean toolUse = this.UseCurrentTool(this._workItemClass);
		this._myVillager.worldObj.setBlock(p.x, p.y, p.z, Blocks.air);
		this._lastBlockWorked = p;
		
		if (this._myJobOwner != null)
			this._myJobOwner.intakeWorkerProduct(drops);
			
		return true;
	}

}
