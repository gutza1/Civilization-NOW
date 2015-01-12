package civ.Item.Document;

import civ.Core.CivMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;

public abstract class JobDocument extends Item
{	
	public JobDocument()
	{
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
	//public abstract Item[] WorkItems();
}
	
