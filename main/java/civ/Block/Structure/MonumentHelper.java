package civ.Block.Structure;

import civ.Core.CivMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class MonumentHelper {

	public static int MonumentInternalHeightReq = 3;
	
	public static boolean isMonument(int h)
	{
		return h >= MonumentInternalHeightReq;
	}
	public static int[] isStructure(PlayerInteractEvent e) //out [height, value]
	{
		//Check starting one block below for base below, two blocks of air surrounding, blocks in order (base, value, reg * n, chisel, value)
		int dy = -1 , h = -1, val = -1;
		int[] r = new int[] { -1, -1 };
		boolean isMonument = true;
		boolean cont = true, checkTop = false, brk = false;
		while (cont)
		{
			brk = false;
			
			//Check area around
			for (int x = -2; x < 3; x++)
				for (int z = -2; z < 3; z++)
				{
					Block tb = e.entityPlayer.worldObj.getBlock(x + e.x, dy + e.y, z + e.z);
					int tbm = e.entityPlayer.worldObj.getBlockMetadata(x + e.x, dy + e.y, z + e.z);
					
					if (x == 0 && z == 0)
					{
						if (dy == -1 && !tb.isOpaqueCube())
							return r;
						
						if (dy == 0 && !((tb == Blocks.iron_block) || (tb == Blocks.gold_block) || (tb == Blocks.diamond_block))
							&& e.entityPlayer.worldObj.getTileEntity(x + e.x, dy + e.y, z + e.z) == null)
							return r;
						
						if (tb == Blocks.iron_block)
							val += 1;
						if (tb == Blocks.gold_block)
							val += 4;
						if (tb == Blocks.diamond_block)
							val += 7;
						
						if (dy == 1 && !((tb == Blocks.stonebrick && tbm == 3) || (tb == Blocks.sandstone && tbm == 1)))
							return r;
						
						if (dy == 1)
							h = 0;
						
						//Check middle
						if (dy > 1)
						{
							//If pre top block, check for toop
							if ((tb == Blocks.stonebrick && tbm == 3) || (tb == Blocks.sandstone && tbm == 1))
								checkTop = true;
							//otherwise verify correct interior block
							else	
							{
								if (tb == Blocks.stonebrick || tb == Blocks.sandstone)
									h++;
								else
									return r;
							}
						}
						
						if (checkTop)
						{
							Block tb2 = e.entityPlayer.worldObj.getBlock(x + e.x, dy + e.y + 1, z + e.z);
							if (((tb2 == Blocks.iron_block) || (tb2 == Blocks.gold_block) || (tb2 == Blocks.diamond_block)))
							{						
								if (tb2 == Blocks.iron_block)
									val += 1;
								if (tb2 == Blocks.gold_block)
									val += 4;
								if (tb2 == Blocks.diamond_block)
									val += 7;
								
								return new int[] { h, val };
							}
							else
								return r;							
						}							
						
						continue;
					}
					
					if (tb != Blocks.air)
						return r;
				}
			
			if (!cont)
				break;//return new int[] { h, val };
				
			dy++;
		}
		
		if (h >= MonumentInternalHeightReq)
			return new int[] { h, val };
		return r;
	}

	public static void CreateStructureTileEntity(int[] h, PlayerInteractEvent e)
	{
		if (h == null || e == null)
			return;
		
		if (h[0] <= 0 || h[1] <= 0)
			return;
		
		World w = e.entityPlayer.worldObj;
		TileEntity te = w.getTileEntity(e.x, e.y - 1, e.z);
		
		if (te == null)
		{
			/*
			TileEntityCultureGen tec = new TileEntityCultureGen();
			tec.blockType = w.getBlock(e.x, e.y, e.z);	
			tec.setWorldObj(w);
			w.addTileEntity(te);
			w.setTileEntity(e.x, e.y, e.z, te);	
			Chunk c = w.getChunkFromBlockCoords(e.x, e.z);
			c.addTileEntity(te);
			*/
			
			w.setBlock(e.x, e.y - 1, e.z, CivMod.MonumentBlock);
			//((TileEntityCultureGen)w.getTileEntity(e.x, e.y - 1, e.z)).SetCultureProps(e.entityPlayer, h[1], h[0]);
		}
		else
			return;
		
		return;
	}
}
