package civ.Item;

import java.util.List;

import javax.swing.Icon;

import civ.Core.CivMod;

import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Key extends Item
{
	public static int MaxKeyValue = 1000000;
	
	@Override
    public boolean getShareTag()
    {
        return true;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
    	this.itemIcon = iconRegister.registerIcon("civmod:" + "key");
    }
    public static boolean IsKeyMatch(ItemStack key, TileEntity locker)
    {    	
    	String keyVal = Key.GetKeyValue(key);
    	
    	if (keyVal == null)
    		return false;
    	
    	NBTTagCompound nbt = new NBTTagCompound();
    	locker.readFromNBT(nbt);
    	String s = nbt.getString("lock");
    	
    	return (s != null && s.equalsIgnoreCase(keyVal));
    } 
    public static String GetKeyValue(ItemStack s)
    {
    	if (s == null || s.stackSize != 1 || s.getItem() != CivMod.Key)
    		return null;    	

    	NBTTagCompound nbt = null;
    	
    	if (s.hasTagCompound())
    		nbt = s.getTagCompound();
    	
    	String a = null;
    	if (nbt == null || !nbt.hasKey("author"))
    		return null;
    	
    	String author = s.getTagCompound().getString("author");
    	
    	if (a != null && !a.equalsIgnoreCase(author))
    		return null;
    	
    	if (author == null || author.equalsIgnoreCase(""))
    		return null;
    	
    	return author;
    }
    public static boolean SetNewKeyValue(ItemStack s, int keyID, TileEntity lockable)
    {	    	
    	if (s == null)
    		return false;
    	
    	NBTTagCompound nbt = null;
    	
    	if (s.hasTagCompound())
    		nbt = s.getTagCompound();
    	else
    		nbt = new NBTTagCompound();
    	
    	int max = MaxKeyValue;
    	
    	if (keyID < 1 || keyID >= max)
    		keyID = CivMod.RandomObj.nextInt(max);
    	
    	String nkv = "" + keyID;
    	nbt.setString("author", nkv);
    	
    	s.setTagCompound(nbt);
    	
    	if (lockable != null)
    	{
    		NBTTagCompound nbtl = new NBTTagCompound();
    		nbtl.setString("lock", nkv);
    		lockable.writeToNBT(nbtl);
    	}
    	
    	return true;
    }
    public static String IsLocked(TileEntity lockable)
    {
    	NBTTagCompound nbt = new NBTTagCompound();
    	lockable.readFromNBT(nbt);
    	
    	if (!nbt.hasKey("lock"))
    		return null;
    	
    	return nbt.getString("lock");
    }
    
	public Key()
	{
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabMisc);
		
		//this.
	}
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (par1ItemStack.hasTagCompound())
        {
        	//while (true)
        	//{
	            NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
	            String s = nbttagcompound.getString("author");
	
	            if (!StringUtils.isNullOrEmpty(s))
	            {
	                par3List.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] {s}));
	                //break;
	            }
	            //else
	            //{	
	            //	nbttagcompound.setString("author", par2EntityPlayer.getCommandSenderName() + ":" + CivMod.RandomObj.nextInt(1000000));
	            //	par1ItemStack.writeToNBT(nbttagcompound);
	            //}
        	//}
        }
    }    
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
        if (par1ItemStack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
            String s = nbttagcompound.getString("author");

            if (!StringUtils.isNullOrEmpty(s))
            {
                return "Key (" + s + ")";
            }
        }

        return "Key";
    }
	

	

}
