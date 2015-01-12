package civ.TileEntity;

import java.util.ArrayList;
import java.util.List;

import civ.Block.BlockAutoShopTable;
import civ.Block.BlockShopPost;
import civ.Block.BlockShopTable;
import civ.Core.CivMod;
import civ.Merchant.MerchantTradeRecipe;
import civ.Merchant.TradeManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class TileEntityShopPost extends TileEntityPost
{
	//Fields
    protected int tableX, tableY, tableZ;
    protected boolean hasTable;
	
	protected void setShopTableOwner(int x, int y, int z)
	{
		this.tableX = x;
		this.tableY = y;
		this.tableZ = z;
	}
	public boolean setShopTableOwner(World w, int x, int y, int z)
	{
		this.hasTable = false;
		
		if (w != null)
		{
			TileEntity te = w.getTileEntity(x, y, z);
			if (te != null && te instanceof TileEntityShopTable)
			{
				this.hasTable = true;
				this.setShopTableOwner(x, y, z);
				return true;
			}					
		}
		
		System.out.println("MAJOR POST LINKING ERROR");
		
		return false;
	}

	//Getters
	public boolean HasTable()
	{
		return this.hasTable;
	}
	public TileEntityShopTable GetTileEntityOwner(World w)
	{
		if (w != null)
		{
			TileEntity te = w.getTileEntity(this.tableX, this.tableY, this.tableZ);
			
			if (te instanceof TileEntityShopTable)
			{
				return (TileEntityShopTable)te;
			}					
		}
		
		return null;
	}	
	public BlockShopTable GetBlock(World w)
	{
		if (w != null)
		{
			Block te = w.getBlock(this.tableX, this.tableY, this.tableZ);
			
			if (te instanceof BlockShopTable)
			{
				return (BlockShopTable)te;
			}					
		}
		
		return null;
	}	
	public Block getBlockType()
	{
		return CivMod.ShopTable;
	}
	
	public TileEntityShopPost()
	{
		super();
		
		this.blockType = CivMod.ShopPost;			
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {        
        //Merchant NBT
        nbt.setBoolean("hasTable", this.hasTable);
        nbt.setInteger("tableX", this.tableX);
        nbt.setInteger("tableY", this.tableY);
        nbt.setInteger("tableZ", this.tableZ);        
        
        super.writeToNBT(nbt);
    }
	@Override
  	public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        
        //Merchant NBT
        this.hasTable = nbt.getBoolean("hasTable");
        this.tableX = nbt.getInteger("tableX");
        this.tableY = nbt.getInteger("tableY");
        this.tableZ = nbt.getInteger("tableZ");
        
    }
  	
  	//ModFunctions
  	public void ActivateMerchantOwner(World w, EntityPlayer ep, boolean tradeOnly)
  	{
  		if (this.hasTable)
  		{
  			BlockShopTable b = this.GetBlock(w);
  			
  			if (b != null)
  				b.ActivateBlock(w, this.tableX, this.tableY, this.tableZ, ep, tradeOnly);
  		}
  	}
  	public void ActivateMerchantOwner(World w, EntityPlayer ep)
  	{
  		this.ActivateMerchantOwner(w, ep, false);
  	}
  	
  	//Yodate
  	@Override
  	public void updateEntity()
  	{
  		if (this.hasTable)
  		{
  			TileEntityShopTable b = this.GetTileEntityOwner(this.worldObj);
  			
  			if (b != null && TileEntityAutoShopTable.class.isInstance(b))
  				((TileEntityAutoShopTable)b).updateEntity(TradeManager.GetNearbyMerchants(null, this.worldObj, true, 
  					this.xCoord, this.yCoord, this.zCoord));
  		}
  	}  	
}
