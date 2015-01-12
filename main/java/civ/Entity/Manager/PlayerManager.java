package civ.Entity.Manager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerManager {

	public static void AttemptSetDefault(EntityPlayer ep, boolean override)
	{
		NBTTagCompound nbt = ep.getEntityData();
		
		if (override || !nbt.hasKey("currentCulture"))
		{			
			//int[] nc = ChunkCulture.GenerateNewRandomCulture();
			//ChunkCulture.WriteToNBT(nbt, nc);
		}
		
	}
}
