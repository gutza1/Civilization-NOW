package civ.Item.Document;

import civ.Core.CivMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;

public class EmptyDocument extends Item
{		
    @Override
    public boolean getShareTag()
    {
        return true;
    }
    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
    	return "Document";
    }
    
	public EmptyDocument()
	{
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
}
