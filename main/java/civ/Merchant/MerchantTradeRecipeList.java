package civ.Merchant;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class MerchantTradeRecipeList extends MerchantRecipeList
{
    private static final String __OBFID = "CL_00000127";

    public MerchantTradeRecipeList() {}

    public MerchantTradeRecipeList(NBTTagCompound par1NBTTagCompound)
    {
        this.readRecipiesFromTags(par1NBTTagCompound);
    }
    public MerchantTradeRecipeList(int length) 
    {
		this.ensureCapacity(length);
		
		for (int i = 0; i < length ; i++)
			this.add(null);
	}
	
    @SuppressWarnings("unchecked")
	public void readRecipiesFromTags(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Recipes", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            
            if (nbttagcompound1.hasNoTags())
            	this.add(null);
            else            	           
            	this.add(new MerchantTradeRecipe(nbttagcompound1));
        }
    }

    public NBTTagCompound getRecipiesAsTags()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.size(); ++i)
        {
        	Object o = this.get(i);
        	
        	if (o == null)
        	{
        		nbttaglist.appendTag(new NBTTagCompound());
        	}        		
        	else
        	{
	            MerchantTradeRecipe merchantrecipe = (MerchantTradeRecipe)o;
	            nbttaglist.appendTag(merchantrecipe.writeToTags());
        	}
        }

        nbttagcompound.setTag("Recipes", nbttaglist);
        return nbttagcompound;
    }
}