package civ.Merchant;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;

public class MerchantTradeRecipe extends MerchantRecipe
{
	public static double priceChangePerTrade = .03, priceChangePerDeny = .05;
	
	public boolean IsDisabled = false;
	public double MarketDeltaBuy = 0, MarketDeltaSell = 0;
	protected ItemDemand _myItemDemand = null;
	private IInventory _myInventory;
	
	public void SetInventory(IInventory myInv)
	{
		this._myInventory = myInv;
	}
	public MerchantTradeRecipe setItemDemand(ItemDemand itDem)
	{
		this._myItemDemand = itDem;
		return this;
	}
	public ItemDemand getItemDemand()
	{
		return this._myItemDemand;
	}
	
    public MerchantTradeRecipe(ItemStack par1ItemStack, ItemStack par2ItemStack, ItemStack par3ItemStack)
    {
    	super(par1ItemStack, par2ItemStack, par3ItemStack);
    }    
    public MerchantTradeRecipe(ItemStack par1ItemStack, ItemStack par3ItemStack)
    {
    	super(par1ItemStack, par3ItemStack);
    }    
    public MerchantTradeRecipe(NBTTagCompound par1NBTTagCompound)
    {
    	super(par1NBTTagCompound);
    }
    
    public void SetEnabled(boolean b)
    {
    	this.IsDisabled = !b;
    }
    @Override
    public boolean isRecipeDisabled()
    {
        return this.IsDisabled;
    }
    public void HandleTradeAdjustment(boolean success)
    {
    	//NTORELADLKFJLDML:SFDJ:DKL:S
    	if (success)
    	{
    		if (this.getItemToBuy().stackSize > this.getItemToSell().stackSize)
    			this.MarketDeltaBuy *= MerchantTradeRecipe.priceChangePerTrade;
    		else
    			this.MarketDeltaSell /= MerchantTradeRecipe.priceChangePerTrade;
    	}
    	else
    	{
    		if (this.getItemToBuy().stackSize > this.getItemToSell().stackSize)
    			this.MarketDeltaBuy /= MerchantTradeRecipe.priceChangePerDeny;
    		else
    			this.MarketDeltaSell *= MerchantTradeRecipe.priceChangePerDeny;    		
    	}
    	
    }
    public MerchantRecipe AdjustedRecipe()
    {
    	//int buy = this.getItemToBuy().stackSize +
    	
    	//re//turn new MerchantRecipe()
    	
    	return null;
    }
    
    @Override
    public void readFromTags(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromTags(par1NBTTagCompound);
    	
    	this.IsDisabled = par1NBTTagCompound.getBoolean("isDisabled");
    	this.MarketDeltaBuy = par1NBTTagCompound.getDouble("MarketDeltaBuy");
    	this.MarketDeltaSell = par1NBTTagCompound.getDouble("MarketDeltaSell");
    }
    @Override
    public NBTTagCompound writeToTags()
    {
        NBTTagCompound nbttagcompound = super.writeToTags();
	    
        nbttagcompound.setBoolean("isDisabled", this.IsDisabled);
        nbttagcompound.setDouble("MarketDeltaBuy", this.MarketDeltaBuy);
        nbttagcompound.setDouble("MarketDeltaSell", this.MarketDeltaSell);
        
        return nbttagcompound;
    }
   
}
