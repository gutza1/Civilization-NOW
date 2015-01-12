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
    	Integer[] block1 = {parX + 1, parY, parZ};
    	Integer[] block2 = {parX - 1, parY, parZ};
    	Integer[] block3 = {parX, parY + 1, parZ};
    	Integer[] block4 = {parX, parY - 1, parZ};
    	Integer[] block5 = {parX, parY, parZ + 1};
    	Integer[] block6 = {parX, parY, parZ - 1};
    	Integer[] block7 = {parX + 1, parY + 1, parZ};
    	Integer[] block8 = {parX + 1, parY - 1, parZ};
    	Integer[] block9 = {parX + 1, parY, parZ + 1};
    	Integer[] block10 = {parX + 1, parY, parZ - 1};
    	Integer[] block11 = {parX - 1, parY + 1, parZ};
    	Integer[] block12 = {parX - 1, parY - 1, parZ};
    	Integer[] block13 = {parX - 1, parY, parZ + 1};
    	Integer[] block14 = {parX - 1, parY, parZ - 1};
    	Integer[] block15 = {parX, parY + 1, parZ + 1};
    	Integer[] block16 = {parX, parY + 1, parZ - 1};
    	Integer[] block17 = {parX, parY - 1, parZ + 1};
    	Integer[] block18 = {parX, parY - 1, parZ - 1};
    	Integer[] block19 = {parX + 1, parY + 1, parZ + 1};
    	Integer[] block20 = {parX + 1, parY + 1, parZ - 1};
    	Integer[] block21 = {parX + 1, parY - 1, parZ + 1};
    	Integer[] block22 = {parX + 1, parY - 1, parZ - 1};
    	Integer[] block23 = {parX - 1, parY + 1, parZ + 1};
    	Integer[] block24 = {parX - 1, parY + 1, parZ - 1};
    	Integer[] block25 = {parX - 1, parY - 1, parZ + 1};
    	Integer[] block26 = {parX - 1, parY - 1, parZ - 1};
    	
    	List<Integer[]> neighbors = new ArrayList<Integer[]>();
    	if (parWorld.getBlock(block1[0], block1[1], block1[2]) == Blocks.air){
    		neighbors.add(block1);
    	}
    	if (parWorld.getBlock(block2[0], block2[1], block2[2]) == Blocks.air){
    		neighbors.add(block2);
    	}
    	if (parWorld.getBlock(block3[0], block3[1], block3[2]) == Blocks.air){
    		neighbors.add(block3);
    	}
    	if (parWorld.getBlock(block4[0], block4[1], block4[2]) == Blocks.air){
    		neighbors.add(block4);
    	}
    	if (parWorld.getBlock(block5[0], block5[1], block5[2]) == Blocks.air){
    		neighbors.add(block5);
    	}
    	if (parWorld.getBlock(block6[0], block6[1], block6[2]) == Blocks.air){
    		neighbors.add(block6);
    	}
    	if (parWorld.getBlock(block7[0], block7[1], block7[2]) == Blocks.air){
    		neighbors.add(block7);
    	}
    	if (parWorld.getBlock(block8[0], block8[1], block8[2]) == Blocks.air){
    		neighbors.add(block8);
    	}
    	if (parWorld.getBlock(block9[0], block9[1], block9[2]) == Blocks.air){
    		neighbors.add(block9);
    	}
    	if (parWorld.getBlock(block10[0], block10[1], block10[2]) == Blocks.air){
    		neighbors.add(block10);
    	}
    	if (parWorld.getBlock(block11[0], block11[1], block11[2]) == Blocks.air){
    		neighbors.add(block11);
    	}
    	if (parWorld.getBlock(block12[0], block12[1], block12[2]) == Blocks.air){
    		neighbors.add(block12);
    	}
    	if (parWorld.getBlock(block13[0], block13[1], block13[2]) == Blocks.air){
    		neighbors.add(block13);
    	}
    	if (parWorld.getBlock(block14[0], block14[1], block14[2]) == Blocks.air){
    		neighbors.add(block14);
    	}
    	if (parWorld.getBlock(block15[0], block15[1], block15[2]) == Blocks.air){
    		neighbors.add(block15);
    	}
    	if (parWorld.getBlock(block16[0], block16[1], block16[2]) == Blocks.air){
    		neighbors.add(block16);
    	}
    	if (parWorld.getBlock(block17[0], block17[1], block17[2]) == Blocks.air){
    		neighbors.add(block17);
    	}
    	if (parWorld.getBlock(block18[0], block18[1], block18[2]) == Blocks.air){
    		neighbors.add(block18);
    	}
    	if (parWorld.getBlock(block19[0], block19[1], block19[2]) == Blocks.air){
    		neighbors.add(block6);
    	}
    	if (parWorld.getBlock(block20[0], block20[1], block20[2]) == Blocks.air){
    		neighbors.add(block20);
    	}
    	if (parWorld.getBlock(block21[0], block21[1], block21[2]) == Blocks.air){
    		neighbors.add(block21);
    	}
    	if (parWorld.getBlock(block22[0], block22[1], block22[2]) == Blocks.air){
    		neighbors.add(block22);
    	}
    	if (parWorld.getBlock(block23[0], block23[1], block23[2]) == Blocks.air){
    		neighbors.add(block23);
    	}
    	if (parWorld.getBlock(block24[0], block24[1], block24[2]) == Blocks.air){
    		neighbors.add(block24);
    	}
    	if (parWorld.getBlock(block25[0], block25[1], block25[2]) == Blocks.air){
    		neighbors.add(block25);
    	}
    	if (parWorld.getBlock(block26[0], block26[1], block26[2]) == Blocks.air){
    		neighbors.add(block22);
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