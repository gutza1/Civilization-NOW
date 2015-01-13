package civ.Block.Structure;

import javax.vecmath.Point3i;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import civ.Block.BlockJobPost;
import civ.TileEntity.TileEntityGranary;
import civ.TileEntity.TileEntityJobPost;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BlockGranary extends BlockStructure
{	
	//Icon Fields
    @SideOnly(Side.CLIENT)
    private IIcon field_150035_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_150034_b;
    
	@Override
	protected Block _GetBaseBlock() {
		return Blocks.cobblestone;
	}    
	
	//Gets the block's texture. Args: side, meta
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.field_150035_a : 
        	(p_149691_1_ == 0 ? Blocks.cobblestone.getBlockTextureFromSide(p_149691_1_) : this.field_150034_b);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName());
        this.field_150035_a = p_149651_1_.registerIcon(this.getTextureName());
        this.field_150034_b = p_149651_1_.registerIcon("civmod:granary");
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityGranary();
	}
	
	public BlockGranary()
    {
    	//new BlockCompressed().setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setBlockName("blockIron").setBlockTextureName("iron_block"));
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeStone).setBlockName("granary").setBlockTextureName("cobblestone");
    }

	@Override
	public boolean hasBaseStructure(PlayerInteractEvent e) {

		//Check for x or z wall orientation
		boolean hasXWall = false, hasZWall = false;
		World w = e.entity.worldObj;
		
		if (this.isWallType(w.getBlock(e.x - 1, e.y, e.z)) && this.isWallType(w.getBlock(e.x + 1, e.y, e.z)))
			hasXWall = true;
		
		if (this.isWallType(w.getBlock(e.x, e.y, e.z - 1)) && this.isWallType(w.getBlock(e.x, e.y, e.z + 1)))
			hasZWall = true;
		
		if ((!hasXWall && !hasZWall) || (hasXWall & hasZWall))
			return false;
		
		boolean[][] b = new boolean[5][];
		b[0] = new boolean[] { false, true, true, true, false };
		b[1] = new boolean[] { true, false, false, false, true };
		b[2] = new boolean[] { true, false, false, false, true };
		b[3] = new boolean[] { true, false, false, false, true };
		b[4] = new boolean[] { false, true, true, true, false };
		
		Point3i p = null;
		Material[] mats = new Material[] { Material.rock, Material.wood };
		if (hasXWall)
		{
			if (BlockStructure.LayerSatisfies(w, e.x - 2, e.y, e.z, b, mats))
				p = new Point3i(e.x - 2, e.y, e.z);

			if (BlockStructure.LayerSatisfies(w, e.x - 2, e.y, e.z - 4, b, mats))
				p = new Point3i(e.x - 2, e.y, e.z - 4);	
		}
		if (hasZWall)
		{
			if (BlockStructure.LayerSatisfies(w, e.x, e.y, e.z - 2, b, mats))
				p = new Point3i(e.x, e.y, e.z - 2);

			if (BlockStructure.LayerSatisfies(w, e.x - 4, e.y, e.z - 2, b, mats))
				p = new Point3i(e.x - 4, e.y, e.z - 2);	
		}
		
		if (p == null)
			return false;		

		//Check for foundation
		boolean[][] bf = new boolean[3][];
		bf[0] = new boolean[] { true, true, true };
		bf[1] = new boolean[] { true, true, true };
		bf[2] = new boolean[] { true, true, true };
		
		if (!BlockStructure.LayerSatisfies(w, p.x + 1, p.y - 1, p.z + 1, b, (Block[])null))
			return false;
		
		if (!BlockStructure.LayerContainsNoTileEntity(w, p.x, p.y, p.z, 5, TileEntityGranary.class))
			return false;
		
		int height = 0;
		
		while (BlockStructure.LayerSatisfies(w, p.x, p.y + height, p.z, b, mats))
			height++;
			
		if (height > 0)
			return true;
		
		return false;
	}
	
	protected boolean isWallType(Block b)
	{
		if (b.getMaterial() == Material.rock || b.getMaterial() == Material.wood)
			return true;
		
		return false;
	}
}
