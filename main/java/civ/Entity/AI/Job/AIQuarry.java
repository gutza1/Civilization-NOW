package civ.Entity.AI.Job;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import civ.Block.BlockManager;
import civ.Core.CivMod;
import civ.TileEntity.TileEntityJobPost;

public class AIQuarry extends AIGathering
{
	public int BlockOpenFaces(Point3i p)
	{
		World w = this._myVillager.worldObj;
		Block[] b = new Block[5];
		b[0] = w.getBlock(p.x, p.y + 1, p.z);
		b[1] = w.getBlock(p.x - 1, p.y, p.z);
		b[2] = w.getBlock(p.x + 1, p.y, p.z);
		b[3] = w.getBlock(p.x , p.y, p.z + 1);
		b[4] = w.getBlock(p.x , p.y, p.z - 1);
		
		int r = 0;
		for (int i = 0; i < 5; i++)
		{
			if (Block.isEqualTo(b[i], Blocks.air))
				r++;
		}
		
		return r;		
	}
	public Point3i NeighborQuarryPositions(Point3i startGround)
	{
		World w = this._myVillager.worldObj;
		
		Block b = w.getBlock(startGround.x, startGround.y, startGround.z);
		while (!Block.isEqualTo(b, Blocks.air) && !b.isOpaqueCube())
		{
			startGround.y++;
			b = w.getBlock(startGround.x, startGround.y + 1, startGround.z);
		}		
		
		int radSearch = 15;
		int maxSearch = radSearch * radSearch * 4;
		int index = 0, x = startGround.x, z = startGround.z, y = startGround.y + 1,
				xmax = 1, xmin = -1, zmax = 1, zmin = -1;	
		
		
		while (index < maxSearch)
		{
			for (int i = xmin; i < xmax; i++)
			{
				for (int j = zmin; j < zmax; j++)
				{
					if (!(i == xmin || i == xmax))
					{
						index++;	
						continue;
					}
					if (!(j == zmin || j == zmax))
					{
						index++;	
						continue;
					}
					
					Point3i p = new Point3i(i + x, y + 1, j + z);
					b = w.getBlock(i + x, y, j + z);
					Block above = w.getBlock(i + x, y + 1, j + z);
					int faces = this.BlockOpenFaces(p);
					
					if (!Block.isEqualTo(b, Blocks.air) && BlockManager.IsMatch(b, -1, this._gatherBlocks, this._gatherMeta))
					{
						if (faces >= 2)
							return p;
						else
							return this.NeighborQuarryPositions(p);
					}
				}
			}
			
			xmin--;
			xmax++;
			zmin--;
			zmax++;
					
			index++;
		}
		
		return null;
	}

	public AIQuarry(EntityVillager ev, TileEntityJobPost jp)
	{
		super(ev, jp);
		
		this._gatherBlocks = new Block[] { 
				Blocks.sand, 	   Blocks.grass,     Blocks.dirt,         Blocks.clay,           Blocks.gravel,
				Blocks.sandstone,  Blocks.stone,     Blocks.cobblestone,  Blocks.hardened_clay,  Blocks.stained_hardened_clay,
				Blocks.coal_ore,   Blocks.iron_ore,  Blocks.gold_ore,     Blocks.diamond_ore,    
				Blocks.lapis_ore,  Blocks.redstone_ore };
		
		this._gatherMeta = new int[] { 
				-1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1,
				-1, -1, -1, -1, 
				-1, -1 };
		
		this._includeYInDistance = false;
		this._acceptableWorkDistance = 5;
	}

	@Override
	public Point3i NextWorkPosition()
	{
		if (this._currentJobSite == null)
			return null;
		
		//return this.NeighborQuarryPositions(this._currentJobSite);
		
		World w = this._myVillager.worldObj;
		for (int attempts = 0; attempts < 20; attempts++)
		{
			int rx = CivMod.RandomObj.nextInt(9) - 4, rz = CivMod.RandomObj.nextInt(9) - 4;
			//Point3i p = new Point3i(rx + this._currentJobSite.x, 0, rz + this._currentJobSite.z);this._myVillager.posX
			Point3i p = new Point3i(rx + (int)this._myVillager.posX, 0, rz + (int)this._myVillager.posZ);
			p.y = w.getHeightValue(p.x, p.z) - 1;
			
			if (Math.abs(p.y - this._myVillager.posY) > 7 || p.y < this._currentJobSite.y)
				continue;
			
			Block b = w.getBlock(p.x, p.y, p.z);
			
			//Check Match
			if (!BlockManager.IsMatch(b, -1, this._gatherBlocks, this._gatherMeta))
				continue;
			
			//Check no surrounding block is higher than h
			boolean brk = false;
			for (int i = p.x - 1; i <= p.x + 1; i++)
			{
				for (int j = p.z - 1; j <= p.z + 1; j++)
				{
					if (w.getHeightValue(i, j) - 1 > p.y)
						brk = true;
					if (BlockManager.IsMatch(w.getBlock(i, p.y, j), new Block[] { Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava }))
						brk = true;
					
					if (brk)
						break;
				}
				if (brk)
					break;
			}
			
			if (brk)
				continue;
			
			//Check openFaces
			int openFaces = this.BlockOpenFaces(p);
			if (openFaces < 2)
				continue;			

			this._blockToWork = p;
			return new Point3i(p.x, (int)this._myVillager.posY + 1, p.z);
		}
		
		return null;
	}
	
}
