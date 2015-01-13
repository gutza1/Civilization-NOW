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

public abstract class TileEntityPost extends TileEntity implements IInventory
{
	//Fields
    private int numPlayersUsing = 0;
    protected ItemStack[] chestContents = new ItemStack[0];
    protected String myOwner = "", customName;
	
    //Setters
	public void setOwner(String newOwner)
	{
		this.myOwner = newOwner;
	}
	public boolean HasOwner()
	{
		return this.myOwner != null && this.myOwner != "";
	}
	public String getOwner()
	{
		return this.myOwner;
	}
	public int NumEmptyInventorySlots()
	{
		int r = 0;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 6; j++)
				if (this.chestContents[i + j * 12] == null)
					r++;
		
		return r;
	}
	public IInventory GetNeighborChest()
	{
		for (int x = -1; x < 2; x++)
			for (int z = -1; z < 2; z++)
			{
				if (x == 0 && z == 0)
					continue;
				
				TileEntity te = this.worldObj.getTileEntity(this.xCoord + x, this.yCoord, this.zCoord + z);
				
				if (te == null)
					continue;
				
				if (IInventory.class.isInstance(te))
				{
					return (IInventory)te;
				}
			}
		
		return null;
	}
	
	public TileEntityPost()
	{
		super();
		
		this.blockType = CivMod.ShopPost;			
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {        
        //Merchant NBT
        nbt.setString("owner", this.myOwner);      
        
        //Storage NBT
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.chestContents.length; ++i)
        {
            if (this.chestContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName())
        	nbt.setString("CustomName", this.customName);

        super.writeToNBT(nbt);
    }
	@Override
  	public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        
        //Merchant NBT
        this.myOwner = nbt.getString("owner");
        
        //Storage NBT
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        this.chestContents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.chestContents.length)
            {
                this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        if (nbt.hasKey("CustomName", 8))
            this.customName = nbt.getString("CustomName");
    }

  	//Update
  	@Override
  	public void updateEntity()
  	{
  	}
  	
    //Storage Functions   
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }
    public boolean hasCustomInventoryName()
    {
        return this.customName != null && this.customName.length() > 0;
    }
    public int getSizeInventory()
    {
        return 0;
    }    
    public int getInventoryStackLimit()
    {
       return 64;
    }
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.customName : "Merchant Table";
    }
    public ItemStack getStackInSlot(int par1)
    {
    	if (par1 >= this.chestContents.length)
    	{
    		return null;
    	}
    	
        return this.chestContents[par1];
    }   
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack = this.chestContents[par1];
            this.chestContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack; 

            if (this.chestContents[par1].stackSize <= par2)
            {
                itemstack = this.chestContents[par1];
                this.chestContents[par1] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.chestContents[par1].splitStack(par2); 

                if (this.chestContents[par1].stackSize == 0)
                {
                    this.chestContents[par1] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.chestContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
 
        this.markDirty();
              
    }
    public void openInventory()
    {
        if (this.numPlayersUsing < 0)
        {
            this.numPlayersUsing = 0;
        }

        ++this.numPlayersUsing;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
    }
    public void closeInventory()
    {
        //if (this.getBlockType() instanceof ShopPost)
        {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());

            if (this.worldObj.isRemote)
            {
            	//NBTTagCompound nbtTag = new NBTTagCompound();
            	//this.writeToNBT(nbtTag);
            	//CivMod.network.sendToServer(new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag));  
            }
            
        }
    }

}
