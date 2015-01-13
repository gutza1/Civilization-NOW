package civ.Block.Structure;

import civ.Block.BlockJobPost;
import civ.TileEntity.TileEntityJobPost;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMonument extends BlockContainer
{

    public BlockMonument()
    {
    	//new BlockCompressed().setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setBlockName("blockIron").setBlockTextureName("iron_block"));
        super(Material.iron);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setBlockName("blockIron").setBlockTextureName("iron_block");
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return null;//new TileEntityCultureGen();
	}
	
    @Override
    public void breakBlock(World w, int x, int y, int z, Block p_149749_5_, int p_149749_6_)
    {
    	TileEntity te = w.getTileEntity(x, y, z);

    	super.breakBlock(w, x, y, z, p_149749_5_, p_149749_6_);
    	
    	w.removeTileEntity(x, y, z);    	
    }
}
