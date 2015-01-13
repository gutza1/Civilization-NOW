package civ.Item.Document;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import civ.Core.CivMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class MultiLocationDocument extends LocationDocument
{		
	private static List<Point3i> GetPoints(EntityPlayer ep, ItemStack is)
	{
		NBTTagCompound nbt = is.getTagCompound();
		List<Point3i> r = new ArrayList<Point3i>();
		
		if (nbt == null)
			nbt = MultiLocationDocument.SetDefault(ep, is);
		
		if (nbt.hasKey("locs"))
		{
			NBTTagList nbtl = nbt.getTagList("locs", 10);
			
			for (int i = 0; i < nbtl.tagCount(); i++)
			{
				NBTTagCompound nbti = nbtl.getCompoundTagAt(i);
				r.add(new Point3i(nbti.getInteger("x"),nbti.getInteger("y"),nbti.getInteger("z")));
			}
		}
		
		return r;
	}
	public static NBTTagCompound SetDefault(EntityPlayer ep, ItemStack nis)
	{				
		NBTTagCompound locDocNBT = new NBTTagCompound();
		
		locDocNBT.setString("author", ep.getCommandSenderName());
		locDocNBT.setTag("locs", new NBTTagList());
		//locDocNBT.setInteger("x", x);
		//locDocNBT.setInteger("y", y);
		//locDocNBT.setInteger("z", z);
		
		nis.setTagCompound(locDocNBT);
		
		return locDocNBT;
	}
	
    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
    	String r = "MultiLocation Document";
    	
    	NBTTagCompound nbt = par1ItemStack.getTagCompound();
    	/*if (nbt != null)
    	{
			int x = nbt.getInteger("x");
			int y = nbt.getInteger("y");
			int z = nbt.getInteger("z");
			
			r = "Location Document (" + x + ", " + y + ", " + z + ")"; 		
    	}*/
    	
    	return r;
    }
    
	public MultiLocationDocument()
	{
		super();
	}
	public static ItemStack CreateMultiLocationDocument(String author)
	{				
		ItemStack nis = new ItemStack(CivMod.MultiLocationDocument);
		
		NBTTagCompound locDocNBT = new NBTTagCompound();
		
		locDocNBT.setString("author", author);
		locDocNBT.setTag("locs", new NBTTagList());
		//locDocNBT.setInteger("x", x);
		//locDocNBT.setInteger("y", y);
		//locDocNBT.setInteger("z", z);
		
		nis.setTagCompound(locDocNBT);
		
		return nis;
	}
	
	public static void Interact(PlayerInteractEvent e)
	{
		if (e.action == Action.LEFT_CLICK_BLOCK)
		{
			e.entityPlayer.inventory.setInventorySlotContents(e.entityPlayer.inventory.currentItem, new ItemStack(new MultiLocationDocument(), 1));
			return;
		}
			
		if (e.action == Action.RIGHT_CLICK_BLOCK)
		{
			
			
		}
			
		List<Point3i> cps = MultiLocationDocument.GetPoints(e.entityPlayer, e.entityPlayer.getHeldItem());
				
		
		
		return;
	}
}
