package civ.Item.Document;

import civ.Core.CivMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;

public class IdentityDocument extends EmptyDocument
{		
	public static Long GetId(ItemStack par1ItemStack)
	{
    	String r = "Identity Document";
    	
    	NBTTagCompound nbt = par1ItemStack.getTagCompound();
    	if (nbt != null)
    	{			
			return  nbt.getLong("id"); 		
    	} 
    	
    	return null;
	}
	public static Long GetId(EntityVillager ev)
	{
		return ev.getEntityData().getLong("civid");
	}
	
    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
    	String r = "Identity Document";
    	
    	NBTTagCompound nbt = par1ItemStack.getTagCompound();
    	if (nbt != null)
    	{			
			r = "Identity Document (" + nbt.getString("name: ") + nbt.getLong("id") + ")"; 		
    	}
    	
    	return r;
    }
    
	public IdentityDocument()
	{
		super();
	}
	
	public static ItemStack CreateIdentityDocument(String author, String name, long id)
	{				
		ItemStack nis = new ItemStack(CivMod.IdentityDocument);
		
		NBTTagCompound locDocNBT = new NBTTagCompound();
		
		locDocNBT.setString("author", author);
		locDocNBT.setString("name", name);
		locDocNBT.setLong("id", id);
		
		nis.setTagCompound(locDocNBT);
		
		return nis;
	}
}
