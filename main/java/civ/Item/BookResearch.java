package civ.Item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BookResearch extends Item
{
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack)
    {
        return (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("researchType"));
    }
    public boolean isItemTool(ItemStack par1ItemStack)
    {
        return false;
    }
    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
        if (par1ItemStack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
            String s = nbttagcompound.getString("researchType");

            if (!StringUtils.isNullOrEmpty(s))
            {
                return "Research (" + s + ")";
            }
        }

        return "Research";
    }
    
    public BookResearch()
    {
    	super();
    	this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	/*
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        NBTTagList nbttaglist = null;//this.func_92110_g(par1ItemStack);

        if (nbttaglist != null)
        {
            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
                short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");

                if (Enchantment.enchantmentsList[short1] != null)
                {
                    par3List.add(Enchantment.enchantmentsList[short1].getTranslatedName(short2));
                }
            }
        }
        */
    }
}
