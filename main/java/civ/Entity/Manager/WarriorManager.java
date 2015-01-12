package civ.Entity.Manager;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;

public class WarriorManager 
{
	public static void SetWarriorArmor(EntityVillager v, ItemStack stack)
	{
		int slot = -1;
		
		if (stack.getItem().getUnlocalizedName().contains("sword") ||
			stack.getItem().getUnlocalizedName().contains("bow") )
			slot = 0;
		
		if (stack.getItem().getUnlocalizedName().contains("helmet"))
			slot = 1;
		if (stack.getItem().getUnlocalizedName().contains("chestplate"))
			slot = 2;
		if (stack.getItem().getUnlocalizedName().contains("leggings"))
			slot = 3;
		if (stack.getItem().getUnlocalizedName().contains("boots"))
			slot = 4;			
			
		if (slot != -1)
			v.setCurrentItemOrArmor(slot, stack);
	}

}
