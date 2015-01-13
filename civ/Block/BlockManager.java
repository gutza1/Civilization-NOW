package civ.Block;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;

import civ.Core.CivMod;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockManager {
	
	//Get Nearby Blocks within a given range and given types
	public static Block GetNearbyBlockByTypes(World w, int px, int py, int pz, Block typeAllowed)
	{
		return GetNearbyBlockByType(w, px, py, pz, 1, 1, 1, typeAllowed);
	}
	public static Block GetNearbyBlockByTypes(World w, int px, int py, int pz, Block[] typesAllowed)
	{
		return GetNearbyBlockByTypes(w, px, py, pz, 1, 1, 1, typesAllowed);
	}
	public static Block GetNearbyBlockByTypes(World w, int px, int py, int pz, int spread, Block[] typesAllowed)
	{
		return GetNearbyBlockByTypes(w, px, py, pz, spread, spread, spread, typesAllowed);
	}
	public static Block GetNearbyBlockByType(World w, int px, int py, int pz, int spread, Block typeAllowed)
	{
		return GetNearbyBlockByType(w, px, py, pz, spread, spread, spread, typeAllowed);
	}	
	public static Block GetNearbyBlockByTypes(World w, int px, int py, int pz, int XZspread, int Yspread, Block[] typesAllowed)
	{
		return GetNearbyBlockByTypes(w, px, py, pz, XZspread, Yspread, XZspread, typesAllowed);
	}	
	public static Block GetNearbyBlockByType(World w, int px, int py, int pz, int XZspread, int Yspread, Block typeAllowed)
	{
		return GetNearbyBlockByType(w, px, py, pz, XZspread, Yspread, XZspread, typeAllowed);
	}
	
	public static Block GetNearbyBlockByTypes(World w, int px, int py, int pz, int dx, int dy, int dz, Block[] typesAllowed, int[] metaAllowed)
	{
		for (int x = -1 * dx; x < dx + 1; x++)
			for (int y = -1 * dy; y < dy + 1; y++)
				for (int z = -1 * dz; z < dz + 1; z++)
					for (int i = 0; i < typesAllowed.length; i++)
					{
						if (x == px && y == py && z == pz)
							continue;
						
						Block b = w.getBlock(x + px, y + py, z + pz);
						if (Block.isEqualTo(typesAllowed[i], b) && w.getBlockMetadata(x + px, y + py, z + pz) == metaAllowed[i])
							return b;
					}	
		
		return null;
	}
	public static Block GetNearbyBlockByTypes(World w, int px, int py, int pz, int dx, int dy, int dz, Block[] typesAllowed)
	{
		for (int x = -1 * dx; x < dx + 1; x++)
			for (int y = -1 * dy; y < dy + 1; y++)
				for (int z = -1 * dz; z < dz + 1; z++)
					for (int i = 0; i < typesAllowed.length; i++)
					{
						if (x == px && y == py && z == pz)
							continue;
						
						Block b = w.getBlock(x + px, y + py, z + pz);
						if (Block.isEqualTo(typesAllowed[i], b))
							return b;
					}	
		
		return null;
	}
	public static Block GetNearbyBlockByType(World w, int px, int py, int pz, int dx, int dy, int dz, Block b)
	{
		for (int x = -1 * dx; x < dx + 1; x++)
			for (int y = -1 * dy; y < dy + 1; y++)
				for (int z = -1 * dz; z < dz + 1; z++)
				{
					if (x == px && y == py && z == pz)
						continue;
					
					Block tb = w.getBlock(x + px, y + py, z + pz);
					if (Block.isEqualTo(tb, b))
						return tb;
				}	
		
		return null;
	}
	public static Point3i GetNearbyBlockLocByType(World w, int px, int py, int pz, int dx, int dy, int dz, Block b)
	{
		for (int x = -1 * dx; x < dx + 1; x++)
			for (int y = -1 * dy; y < dy + 1; y++)
				for (int z = -1 * dz; z < dz + 1; z++)
				{
					if (x == px && y == py && z == pz)
						continue;
					
					Block tb = w.getBlock(x + px, y + py, z + pz);
					if (Block.isEqualTo(tb, b))
						return new Point3i(x + px, y + py, z + pz);
				}	
		
		return null;
	}
	public static Point3i GetNearbyBlockLocByType(World w, int px, int py, int pz, double dx, double dy, double dz, Block[] b)
	{
		for (int x = -1 * (int)dx; x < dx + 1; x++)
			for (int y = -1 * (int)dy; y < dy + 1; y++)
				for (int z = -1 * (int)dz; z < dz + 1; z++)
				{
					if (x == px && y == py && z == pz)
						continue;
					
					for (int i = 0; i < b.length; i++)
					{
						Block tb = w.getBlock(x + px, y + py, z + pz);
						if (Block.isEqualTo(tb, b[i]))
							return new Point3i(x + px, y + py, z + pz);
					}
				}	
		
		return null;
	}
	
	//Get Random Neighbors
	public static Point3i GetRandomNeighbor(World w, int x, int y, int z, int sx, int sy, int sz, Block[] types)
	{
		int nx = x + CivMod.RandomObj.nextInt(sx * 2 + 1) - sx;
		int ny = y + CivMod.RandomObj.nextInt(sy * 2 + 1) - sy;
		int nz = z + CivMod.RandomObj.nextInt(sz * 2 + 1) - sz;		
		
		Point3i rp = new Point3i(nx, ny, nz);
		
		for (int i = 0; i < types.length; i++)
		{
			if (Block.isEqualTo(w.getBlock(nx, ny, nz), types[i]))
					return rp;
		}
		
		return null;
	}	
	public static Point3i GetRandomTopNeighbor(World w, int x, int y, int z, int sx, int sy, int sz, Block[] types, int[] meta)
	{
		int nx = x + CivMod.RandomObj.nextInt(sx * 2 + 1) - sx;
		int nz = z + CivMod.RandomObj.nextInt(sz * 2 + 1) - sz;		
		
		for (int i = 0; i < types.length; i++)
		{
			int ny = w.getHeightValue(nx, nz);
			
			if (Math.abs(ny - y) > sy)
				continue;
			
			Block b = Blocks.air;
			while (b == Blocks.air)
			{
				b = w.getBlock(nx, ny, nz);
				
				if (ny <= 0)
					break;
				
				if (b == Blocks.air)
				{ 
					ny--;
					continue;
				}
				
				int m = w.getBlockMetadata(nx, ny, nz);
				if (Block.isEqualTo(b, types[i]) && (m == meta[i] || meta[i] == -1))
					return new Point3i(nx, ny, nz);;
			}
		}
		
		return null;
	}
	
	public static boolean Distance(Point3i pi, Point3d pd, double dist)
	{
		double dx = pi.x - pd.x;
		double dy = pi.y - pd.y;
		double dz = pi.z - pd.z;
		
		if (dx * dx + dy * dy + dz * dz <= dist * dist)
			return true;
		
		return false;
	}
	public static boolean IsMatch(Block b, int m, Block[] matches, int[] meta)
	{			
		for (int i = 0; i < matches.length; i++)
			if (Block.isEqualTo(b, matches[i]) && (m == meta[i] || m == -1 || meta[i] == -1))
				return true;
		
		return false;
	}
	public static boolean IsMatch(Block block, Block[] blocks) {
		
		for (int i = 0; i < blocks.length; i++)
			if (Block.isEqualTo(block, blocks[i]))
				return true;
		
		return false;
	}
	
	//Get Blocks Above (for transformations)
	public static boolean containsBlockAbove(World w, int x, int y, int z, int h, Block b)
	{
		for (int i = y + 1; i <= y + h; i++)
			if (Block.isEqualTo(b, w.getBlock(x, y, z)))
				return true;
		
		return false;
	}	
	//Get Blocks Above (for transformations)
	public static boolean containsBlocksAbove(World w, int x, int y, int z, int h, Block[] bs)
	{
		for (int i = y + 1; i <= y + h; i++)
			for (int j = 0; j < bs.length; j++)
				if (Block.isEqualTo(bs[j], w.getBlock(x, y, z)))
					return true;
		
		return false;
	}



}
