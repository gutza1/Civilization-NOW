package civ.Block.Structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import civ.Core.CivMod;
import civ.TileEntity.TileEntityStructure;

public abstract class BlockStructure extends BlockContainer
{
	public static boolean LayerSatisfies(World w, int x, int y, int z, boolean[][] toCheck, Block[] blocks)
	{
		for (int i = 0; i < toCheck.length; i++)
			for (int j = 0; j < toCheck[i].length; j++)
				if (toCheck[i][j])
				{
					boolean matchFound = false;
					
					if (blocks != null)
					{
						for (int k = 0; k < blocks.length; k++)
							if (Block.isEqualTo(blocks[k], w.getBlock(x + i, y, z + j)))
							{
								matchFound = true;
								break;
							}	
					}
					else
						if (!Block.isEqualTo(Blocks.air, w.getBlock(x + i, y, z + j)))
							matchFound = true;
					
					if (!matchFound)
						return false;
				}
		
		return true;
	}
	public static boolean LayerSatisfies(World w, int x, int y, int z, boolean[][] toCheck, Material[] mats)
	{
		for (int i = 0; i < toCheck.length; i++)
			for (int j = 0; j < toCheck[i].length; j++)
				if (toCheck[i][j])
				{
					boolean matchFound = false;
					
					for (int k = 0; k < mats.length; k++)
						if (w.getBlock(x + i, y, z + j).getMaterial() == mats[k])
						{
							matchFound = true;
							break;
						}	
					
					if (!matchFound)
						return false;
				}
		
		return true;
	}
	public static boolean LayerContainsNoTileEntity(World w, int x, int y, int z, int spread, Class c)
	{
		for (int ix = x - spread; ix <= x + spread; ix++)
			for (int iz = z - spread; iz <= z + spread; iz++)
			{
				TileEntity te = w.getTileEntity(ix, y, iz);
				
				if (te == null)
					continue;
				
				if (c == null)
				{ 
					if (te != null)
						return false;
					
					//continue;
				}
				
				if (c.isInstance(te))
					return false;					
			}
		
		return true;
	}
	
	protected abstract Block _GetBaseBlock();
	
    public BlockStructure(Material m)
    {
    	super(m);
    }
	
    @Override
    public void breakBlock(World w, int x, int y, int z, Block p_149749_5_, int p_149749_6_)
    {
    	TileEntity te = w.getTileEntity(x, y, z);

    	super.breakBlock(w, x, y, z, p_149749_5_, p_149749_6_);
    	
    	w.removeTileEntity(x, y, z);    	
    }
    
    public void HandleStructure(PlayerInteractEvent e)
    {   	
    	Block b = e.entity.worldObj.getBlock(e.x, e.y, e.z), bb = this._GetBaseBlock();
    	
    	boolean b1 = this._GetBaseBlock() != null, b2 = Block.isEqualTo(b, bb);
    	
    	if (!b2)
    		return;
    	
    	if (!this.hasBaseStructure(e))
    		return;    	

    	e.entity.worldObj.getBlock(e.x, e.y, e.z).breakBlock(e.entity.worldObj, e.x, e.y, e.z, null, 0);
		e.entity.worldObj.setBlock(e.x, e.y, e.z, this);
		TileEntity te = e.entity.worldObj.getTileEntity(e.x, e.y, e.z);		
		((TileEntityStructure)te).SetStructureProps(e.entityPlayer);
    }
    
    public abstract boolean hasBaseStructure(PlayerInteractEvent e);
}
