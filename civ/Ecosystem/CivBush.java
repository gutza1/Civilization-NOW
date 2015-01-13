package civ.Ecosystem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CivBush extends BlockBush implements IGrowable
{
    protected int maxGrowthStage = 7;


    @SideOnly(Side.CLIENT)
    protected IIcon[] iIcon;

    public CivBush()
    {
     // Basic block setup
        setTickRandomly(true);
        float f = 0.5F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        setCreativeTab(CreativeTabs.tabDecorations);
        setHardness(0.0F);
        setStepSound(soundTypeGrass);
        disableStats();
    }
    
    public List<Integer[]> getNeighboringAirBlocks(World parWorld, int parX, int parY, int parZ){
    	Integer[][] blocks = {
	    	{parX + 1, parY, parZ},
	    	{parX - 1, parY, parZ},
	    	{parX, parY + 1, parZ},
	    	{parX, parY - 1, parZ},
	    	{parX, parY, parZ + 1},
	    	{parX, parY, parZ - 1},
	    	{parX + 1, parY + 1, parZ},
	    	{parX + 1, parY - 1, parZ},
	    	{parX + 1, parY, parZ + 1},
	    	{parX + 1, parY, parZ - 1},
	    	{parX - 1, parY + 1, parZ},
	    	{parX - 1, parY - 1, parZ},
	    	{parX - 1, parY, parZ + 1},
	    	{parX - 1, parY, parZ - 1},
	    	{parX, parY + 1, parZ + 1},
	    	{parX, parY + 1, parZ - 1},
	    	{parX, parY - 1, parZ + 1},
	    	{parX, parY - 1, parZ - 1},
	    	{parX + 1, parY + 1, parZ + 1},
	    	{parX + 1, parY + 1, parZ - 1},
	    	{parX + 1, parY - 1, parZ + 1},
	    	{parX + 1, parY - 1, parZ - 1},
	    	{parX - 1, parY + 1, parZ + 1},
	    	{parX - 1, parY + 1, parZ - 1},
	    	{parX - 1, parY - 1, parZ + 1},
	    	{parX - 1, parY - 1, parZ - 1}
    	};
    	List<Integer[]> neighbors = new ArrayList<Integer[]>();
    	for (Integer i = 0; i < blocks.length; i++){
    		if (parWorld.getBlock(blocks[i][0], blocks[i][2], blocks[i][2]) == Blocks.air){
    			neighbors.add(blocks[i]);
    		}
    	}

		return neighbors;
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World parWorld, int parX, int parY, int parZ, Random parRand)
    {
        super.updateTick(parWorld, parX, parY, parZ, parRand);
        int growStage = parWorld.getBlockMetadata(parX, parY, parZ) + 1;

        if (growStage > 7)
        {
            growStage = 7;
            List<Integer[]> neighbor_air_blocks = getNeighboringAirBlocks(parWorld, parX, parY, parZ);
	        System.out.println("Hello.");
		        if (neighbor_air_blocks.size() > 0){
			        System.out.println("Spreading Blocks");
			        Integer[] chosen_block = neighbor_air_blocks.get(parRand.nextInt(neighbor_air_blocks.size())); 
			        parWorld.setBlock(chosen_block[0], chosen_block[1], chosen_block[2], this, 0, 2);
		        }
	     }

        parWorld.setBlockMetadataWithNotify(parX, parY, parZ, growStage, 2);
    }
    
    public void incrementGrowStage(World parWorld, Random parRand, int parX, int parY, int parZ)
    {
        int growStage = parWorld.getBlockMetadata(parX, parY, parZ) + 
              MathHelper.getRandomIntegerInRange(parRand, 2, 5);
        if (growStage > maxGrowthStage)
        {
	        growStage = maxGrowthStage;
	        List<Integer[]> neighbor_air_blocks = getNeighboringAirBlocks(parWorld, parX, parY, parZ);
		        if (neighbor_air_blocks.size() > 0){
			        System.out.println("Spreading Blocks");
			        Integer[] chosen_block = neighbor_air_blocks.get(parRand.nextInt(neighbor_air_blocks.size()));
			        parWorld.setBlock(chosen_block[0], chosen_block[1], chosen_block[2], this, 0, 2);
		        }
	        }

        parWorld.setBlockMetadataWithNotify(parX, parY, parZ, growStage, 2);
    }
    
    @Override
    public Item getItemDropped(int p_149650_1_, Random parRand, int parFortune)
    {
        return Item.getItemFromBlock(this);
    }
    
    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int parSide, int parGrowthStage)
    {
     return iIcon[parGrowthStage];
    }
   
    /*
     * Need to implement the IGrowable interface methods
     */

    /*
     * (non-Javadoc)
     * @see net.minecraft.block.IGrowable#func_149851_a(net.minecraft.world.World, 
     * int, int, int, boolean)
     */
    @Override
    // checks if finished growing (a grow stage of 7 is final stage)
    public boolean func_149851_a(World parWorld, int parX, int parY, int parZ, 
          boolean p_149851_5_)
    {
        return parWorld.getBlockMetadata(parX, parY, parZ) != 7;
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.block.IGrowable#func_149852_a(net.minecraft.world.World, 
     * java.util.Random, int, int, int)
     */
    @Override
    public boolean func_149852_a(World p_149852_1_, Random parRand, int p_149852_3_, 
          int p_149852_4_, int p_149852_5_)
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.block.IGrowable#func_149853_b(net.minecraft.world.World, 
     * java.util.Random, int, int, int)
     */
    @Override
    public void func_149853_b(World parWorld, Random parRand, int parX, int parY, 
          int parZ)
    {
        incrementGrowStage(parWorld, parRand, parX, parY, parZ);
    }

}