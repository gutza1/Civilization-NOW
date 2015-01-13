package civ.Chunk;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;

public class ChunkManager {

	private static HashMap<Integer, HashMap<Integer, NBTTagCompound>> _chunkHashMap = new HashMap<Integer, HashMap<Integer, NBTTagCompound>>();

	public static void LoadChunkIntoMap(Chunk c, NBTTagCompound chunkNBT)
	{
		HashMap<Integer, NBTTagCompound> xMap = ChunkManager._chunkHashMap.get(c.xPosition);
		
		if (xMap == null)
		{
			xMap = new HashMap<Integer, NBTTagCompound>();
			ChunkManager._chunkHashMap.put(c.xPosition, xMap);
		}
		
		NBTTagCompound znbt = xMap.get(c.zPosition);
		
		if (znbt != null)
			return;
		
		xMap.put(c.zPosition, chunkNBT);
	}
	public static void UnLoadChunkOffMap(Chunk c)
	{
		HashMap<Integer, NBTTagCompound> xMap = ChunkManager._chunkHashMap.get(c.xPosition);
		
		if (xMap == null)
		{
			xMap = new HashMap<Integer, NBTTagCompound>();
			ChunkManager._chunkHashMap.put(c.xPosition, xMap);
		}
		
		NBTTagCompound znbt = xMap.get(c.zPosition);
		
		if (znbt == null)
			return;
		
		xMap.remove(c.zPosition);
		
		if (xMap.size() == 0)
			ChunkManager._chunkHashMap.remove(c.xPosition);
			
	}

	

}
