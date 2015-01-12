package civ.Merchant;

import civ.TileEntity.TileEntityShopTable;
import net.minecraft.entity.IMerchant;
import net.minecraft.item.ItemStack;

public interface IEconMerchant extends IMerchant
{
	public boolean HasInventorySpaceFor(ItemStack stack);
	public boolean ContainsInventory(ItemStack stack, int count);
  	public boolean ContainsInventory(ItemStack stack);
  	public boolean AddToInventory(ItemStack stack);
  	
  	public void updateRecipes();
}
