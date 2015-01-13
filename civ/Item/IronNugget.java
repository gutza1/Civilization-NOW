package civ.Item;

import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IronNugget extends Item
{
	public IronNugget()
	{
		this.setMaxStackSize(64);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

}
