package civ.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.*;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent.Finish;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent.Start;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Post;
import net.minecraftforge.event.world.ChunkDataEvent.*;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

import civ.Block.Structure.BlockGranary;
import civ.Block.Structure.MonumentHelper;
import civ.Chunk.ChunkManager;
import civ.Entity.*;
import civ.Entity.AI.AIBase;
import civ.Entity.AI.AIHelper;
import civ.Entity.AI.AIHelper.AIState;
import civ.Entity.Manager.AnimalManager;
import civ.Entity.Manager.PlayerManager;
import civ.Item.ItemManager;
import civ.Item.Key;
import civ.Item.Document.IdentityDocument;
import civ.Item.Document.LocationDocument;
import civ.Item.Document.MultiLocationDocument;

public class EventManager {

	public static boolean EnableCivAI = true;
	public static boolean EnableCivVillager = false;
	public static boolean EnableChestedHorse = true;
	public static boolean EnableSoilBlocks = false;
	public static boolean EnableOverworldSkeleton = false;
	public static boolean EnableOverworldCreeper = false;
	public static boolean EnableIntenseFire = true;
	public static boolean EnableChickenEggSpawn = false;
	
	public static List<Block>[] DropBlockConvert = null;
	public static void InitDropBlockConvert()
	{
		//List<Class> b1 = new ArrayList() { c1 };
		//List<Block> b2 = new ArrayList<Class>() { CivMod.MonumentBlock.getClass() };
		List<Block>[] r = new ArrayList[] { 
			new ArrayList<Block>(), 
			new ArrayList<Block>()};
		
		r[0].add(CivMod.MonumentBlock);
		r[1].add(Blocks.cobblestone);
		
		r[0].add(CivMod.GranaryBlock);
		r[1].add(Blocks.cobblestone);
		
		EventManager.DropBlockConvert = r;
	}
	
	public static Class EntityTypeToErase = null;//EntityChicken.class;
	
	public static boolean EnableCivID = true;
	private static long _randIDLength = Long.MAX_VALUE;
	
	public static int OverworldSkelCreepHeightThreshold = 55;
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent e)
	{				
		boolean remote = e.entity.worldObj.isRemote;
				Block b = CivMod.MonumentBlock;
		if (remote)
			return;
		
		if (EventManager.EntityTypeToErase != null && EventManager.EntityTypeToErase.isInstance(e.entity))
		{
			e.entity.setDead();
			return;
		}
		
		if (EntityPlayer.class.isInstance(e.entity))
		{
			PlayerManager.AttemptSetDefault((EntityPlayer)e.entity, false);
			return;
		}		
		
		//Do new entity generatoin stuff
		Entity newEntity = null;
    	
		//Standard Econ Agent Adder, otherwise create new econ agent from vanilla villager
		//if (e.entity.getClass().toString().contains(EntityCivVillager.class.toString()))
		//Class[] ints = e.entity.getClass().getInterfaces();
		//for (int i = 0; i < ints.length; i++)
		//	if (ints[i].getSimpleName().equalsIgnoreCase(IAgentEntity.class.getSimpleName()))

		boolean isVillager = EntityVillager.class.isInstance(e.entity);
		boolean isCivHorse = EntityCivHorse.class.isInstance(e.entity);
		boolean isHorse = EntityHorse.class.isInstance(e.entity);		
		
		//Check for entity ID
		NBTTagCompound nbt = e.entity.getEntityData();
		if (isVillager && !nbt.hasKey("civid"))
		{
			((EntityVillager)e.entity).setProfession(0);
			nbt.setLong("civid", (long)(CivMod.RandomObj.nextDouble() * EventManager._randIDLength));
		}
		
		//Handle Instant removals
		if (!EnableOverworldCreeper && EntityCreeper.class.isInstance(e.entity) && e.entity.posY > OverworldSkelCreepHeightThreshold)
			e.setCanceled(true);	
		
		if (!EnableOverworldSkeleton && EntitySkeleton.class.isInstance(e.entity) && e.entity.posY > OverworldSkelCreepHeightThreshold)
			e.setCanceled(true);
		
		if (e.isCanceled())
			return;

		//Handle Civ Horse
		if (EnableChestedHorse && isHorse && !isCivHorse)
			newEntity = new EntityCivHorse(e.world);	
		
		if (!EnableChestedHorse && isCivHorse)
			newEntity = new EntityHorse(e.world);	
		
		//If New entity exists to be added, then do so
		if (newEntity != null)
		{
			e.setResult(Result.DENY);
			e.setCanceled(true);			

			e.entity.writeToNBT(new NBTTagCompound());
			if (e.entity.getEntityData().hasNoTags())
				newEntity.copyDataFrom(e.entity, true);
			newEntity.copyLocationAndAnglesFrom(e.entity);
			
			newEntity.forceSpawn = true;
			e.world.spawnEntityInWorld(newEntity);
			e.entity.setDead();	
			
			return;
		}
		
		//Handle Villager AI
		if (EnableCivAI && isVillager)
			AIHelper.SetAIState((EntityVillager)e.entity, AIState.Idle);
	}
	@SubscribeEvent
	public void onLivingDropsEvent(LivingDropsEvent e)
	{
		if (EntityChicken.class.isInstance(e.entity))
			e.drops.add(new EntityItem(e.entity.worldObj, e.entity.posX, e.entity.posY, e.entity.posZ, 
					new ItemStack(Items.bone, CivMod.RandomObj.nextInt(2))));
		
		if (EntitySheep.class.isInstance(e.entity))
			e.drops.add(new EntityItem(e.entity.worldObj, e.entity.posX, e.entity.posY, e.entity.posZ, 
					new ItemStack(Items.bone, CivMod.RandomObj.nextInt(3))));
		
		if (EntityPig.class.isInstance(e.entity))
			e.drops.add(new EntityItem(e.entity.worldObj, e.entity.posX, e.entity.posY, e.entity.posZ, 
					new ItemStack(Items.bone, CivMod.RandomObj.nextInt(4))));
		
		if (EntityCow.class.isInstance(e.entity))
			e.drops.add(new EntityItem(e.entity.worldObj, e.entity.posX, e.entity.posY, e.entity.posZ, 
					new ItemStack(Items.bone, CivMod.RandomObj.nextInt(4) + 1)));
		
		if (EntityWolf.class.isInstance(e.entity))
			e.drops.add(new EntityItem(e.entity.worldObj, e.entity.posX, e.entity.posY, e.entity.posZ, 
					new ItemStack(Items.bone, CivMod.RandomObj.nextInt(3))));
		
		if (EntityOcelot.class.isInstance(e.entity))
			e.drops.add(new EntityItem(e.entity.worldObj, e.entity.posX, e.entity.posY, e.entity.posZ, 
					new ItemStack(Items.bone, CivMod.RandomObj.nextInt(2))));
		
		if (EntityVillager.class.isInstance(e.entity))
			e.drops.add(new EntityItem(e.entity.worldObj, e.entity.posX, e.entity.posY, e.entity.posZ, 
					new ItemStack(Items.bone, CivMod.RandomObj.nextInt(3))));
		
		if (EntityZombie.class.isInstance(e.entity))
			e.drops.add(new EntityItem(e.entity.worldObj, e.entity.posX, e.entity.posY, e.entity.posZ, 
					new ItemStack(Items.bone, CivMod.RandomObj.nextInt(3))));
		
		if (EntityWitch.class.isInstance(e.entity))
			e.drops.add(new EntityItem(e.entity.worldObj, e.entity.posX, e.entity.posY, e.entity.posZ, 
					new ItemStack(Items.bone, CivMod.RandomObj.nextInt(3))));
		
		if (EntityCreeper.class.isInstance(e.entity))
			e.drops.add(new EntityItem(e.entity.worldObj, e.entity.posX, e.entity.posY, e.entity.posZ, 
					new ItemStack(Items.gunpowder, CivMod.RandomObj.nextInt(7) + 1)));
		
		/*if (IAgentEntity.class.isInstance(e.entity))
		{
			BaseAgent ba = BaseAgent.getAgent(((IAgentEntity)e.entity));
			List<ItemStack> emptiedInventory = ba.EmptyInventory();
			for (int i = 0; i < emptiedInventory.size(); i++)
				e.drops.add(new EntityItem(e.entity.worldObj, 
						e.entity.posX, e.entity.posY, e.entity.posZ, 
						emptiedInventory.get(i)));
		}*/
	}
	
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save save)
	{		
		if (save != null)
		{
			boolean remote = save.world.isRemote;		
			if (remote)
				return;
		}
	
		//LifeAgent.SaveAllAgents();
	}		
	@SubscribeEvent
	public void onChunkDataSave(ChunkDataEvent.Save e)
	{		
		boolean remote = e.world.isRemote;
		
		if (remote)
			return;
		
		if (CivMod.RandomObj.nextDouble() < .05 && CivMod.RandomObj.nextDouble() / e.world.playerEntities.size() > .95)
			this.onWorldSave((WorldEvent.Save)null);
		
		Chunk ch = e.getChunk();
		ChunkManager.UnLoadChunkOffMap(ch);
	}		
	@SubscribeEvent
	public void onChunkDataLoad(ChunkDataEvent.Load e)
	{		
		boolean remote = e.world.isRemote;
		
		if (remote)
			return;
		
		NBTTagCompound nbt = e.getData();
		ChunkManager.LoadChunkIntoMap(e.getChunk(), e.getData());
	}
	
	//Terrain Manipulator
	@SubscribeEvent
	public void onLoad(ChunkEvent.Load e)
	{
		if (!EnableSoilBlocks)
			return;
		
		World w = e.world;
		//Chunk c = e.world.getChunkFromChunkCoords(e.chunkX, e.chunkZ);
		Chunk c = e.getChunk();
		
		boolean remote = e.world.isRemote;
		
		if (remote)
			return;		

		int xs = c.xPosition * 16; int zs = c.zPosition * 16;
		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{				
				int h = c.getHeightValue(x, z);				
						
				//for (int y = 0; y <= h; y++)		
				//{		
					int y = h;
					Block b = c.getBlock(x, y, z);
					//if (Block.isEqualTo(b, Blocks.sand))
						//c.func_150807_a(x, y, z, CivMod.Sand, w.getBlockMetadata(x, y, z));
				//}
			}
		}
		
		c.isModified = true;
	}
		
	@SubscribeEvent
	public void onItemExpireEvent(ItemExpireEvent e)
	{		
		ItemStack curItem = e.entityItem.getEntityItem();
				
		if (EnableChickenEggSpawn && curItem.isItemEqual(new ItemStack(Items.egg)))
		{
			for (int i = 0; i < curItem.stackSize; i++)
			{
				EntityChicken ne = new EntityChicken(e.entity.worldObj);
				ne.setGrowingAge(-24000); //Vanilla 
				ne.setLocationAndAngles(e.entityItem.posX, e.entityItem.posY, e.entityItem.posZ, 0, 0);
				
				if (curItem.hasTagCompound() &&  curItem.getTagCompound().hasKey("genes"))
					ne.writeToNBT(curItem.getTagCompound().getCompoundTag("genes"));
				
				e.entity.worldObj.spawnEntityInWorld(ne);
			}
		}
	}
	
	@SubscribeEvent
	public void onBreakEvent(BreakEvent e)
	{
		Block b = e.block;
		String s = b.getItemIconName();
		String s2 = b.getLocalizedName();
		String s3 = b.getUnlocalizedName();
		String s4 = "" + b.getRenderType();
		String s5 = b.getBlockTextureFromSide(0).getIconName();
		b.blockRegistry.getNameForObject(b);
		//String s6 = b.
		//is.
		
		System.out.println(s + ", " + s2 + ", " + s3 + ", " + s4 + ", " + s5);
		
		if (Blocks.dirt.getClass().isInstance(e.block) && e.blockMetadata == 2)
		{
			e.setResult(Result.DENY);
			e.setCanceled(true);
			//e.world.spawnEntityInWorld(new ItemStack(Items.emerald));
			
		}
		
	}
	
	@SubscribeEvent
	public void onEntityItemPickupEvent(EntityItemPickupEvent e)
	{
		
		EntityItem i = e.item;
		ItemStack is = i.getEntityItem();
		String dn = is.getDisplayName();
		String un = is.getItem().getUnlocalizedName();
		String cn = is.getItem().getClass().toString();
		//is.
		
		System.out.println(dn + ", " + un + ", " + cn);
		
	}
	
	@SubscribeEvent
	public void onHarvestDropsEvent(HarvestDropsEvent dropEvent)
	{			
		boolean remote = dropEvent.world.isRemote;
		if (remote)
			return;
		
		if (!dropEvent.isSilkTouching)
		{
			//Handle Additional Block Breaks from Tech
			ItemManager.AdditionalBlockDrops(dropEvent.block, dropEvent.drops);
		}
		
		if (Blocks.dirt.getClass().isInstance(dropEvent.block) && dropEvent.blockMetadata == 2)
		{
			Block b = Blocks.dirt;
			//b.set
		}
		
		for (int i = 0; i < EventManager.DropBlockConvert[0].size(); i++)
			for (int j = 0; j < dropEvent.drops.size(); j++)
				if (Block.isEqualTo(Block.getBlockFromItem(dropEvent.drops.get(j).getItem()), EventManager.DropBlockConvert[0].get(i)))
				{
					dropEvent.drops.add(new ItemStack(EventManager.DropBlockConvert[1].get(i), dropEvent.drops.get(j).stackSize));
					dropEvent.drops.remove(j);
				}
	}
	
	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event)
	{
		if (event.entity.worldObj.isRemote)
			return;
				
		if (event.entity instanceof EntityVillager)
			AnimalManager.HandleAnimalUpdate((EntityVillager)event.entity);
	}

	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent e)
	{
		if (e.entityPlayer.worldObj.isRemote)
			return;
		
		Block b = e.entityPlayer.worldObj.getBlock(e.x, e.y, e.z);
		TileEntity te = e.entityPlayer.worldObj.getTileEntity(e.x, e.y, e.z);
		
		if (e.action == Action.RIGHT_CLICK_BLOCK &&
			e.entityPlayer.getHeldItem() != null && 
			(e.entityPlayer.getHeldItem().isItemEqual(new ItemStack(CivMod.UnwrittenDocument)) || e.entityPlayer.getHeldItem().isItemEqual(new ItemStack(CivMod.LocationDocument))) &&
			LocationDocument.IsValidTileEntity(te))
		{
			e.entityPlayer.inventory.setInventorySlotContents(e.entityPlayer.inventory.currentItem, 
				LocationDocument.CreateLocationDocument(e.entityPlayer.getCommandSenderName(), e.x, e.y, e.z));
			e.setCanceled(true);
			return;
		}
		
		if (e.action == Action.RIGHT_CLICK_BLOCK && te == null)
		{
			//Demo Monument
			if ((b == Blocks.iron_block) || (b == Blocks.gold_block) || (b == Blocks.diamond_block))
			{
				int[] h = MonumentHelper.isStructure(e);
				boolean isMon = MonumentHelper.isMonument(h[0]);
				
				if (h[0] > 0)
					MonumentHelper.CreateStructureTileEntity(h, e);
			}
			
			//Handle Granary Structure
			if (b == Blocks.cobblestone && e.entityPlayer.getHeldItem() != null && ItemHoe.class.isInstance(e.entityPlayer.getHeldItem().getItem()))
				CivMod.GranaryBlock.HandleStructure(e);
			
		}
		
		if (e.action == Action.RIGHT_CLICK_BLOCK && te == null)
		{
			if ((b == Blocks.iron_block) || (b == Blocks.gold_block) || (b == Blocks.diamond_block))
			{
				int[] h = MonumentHelper.isStructure(e);
				boolean isMon = MonumentHelper.isMonument(h[0]);
				
				if (h[0] > 0)
					MonumentHelper.CreateStructureTileEntity(h, e);
			}
			
			if (b == Blocks.cobblestone && e.entityPlayer.getHeldItem() != null && ItemHoe.class.isInstance(e.entityPlayer.getHeldItem().getItem()))
				CivMod.GranaryBlock.HandleStructure(e);
		}
		
		if (e.entityPlayer.getHeldItem() != null && e.entityPlayer.getHeldItem().isItemEqual(new ItemStack(CivMod.MultiLocationDocument)))
			MultiLocationDocument.Interact(e);
	}
	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteractEvent event)
	{		
		if (event.entity.worldObj.isRemote)
			return;
		
		//Horse
		if (event.target instanceof EntityCivHorse)
		{
			EntityHorse h = (EntityHorse)event.target;
			ItemStack heldItem = event.entityPlayer.getHeldItem();
			
			if (!h.isChested() && heldItem != null && heldItem.isItemEqual(new ItemStack(Blocks.chest)))
			{
				h.setChested(true);
				
				InventoryPlayer ip = event.entityPlayer.inventory;
				ItemStack is = ip.getStackInSlot(ip.currentItem);
				is.stackSize--;
				
				if (is.stackSize > 0)
					ip.setInventorySlotContents(ip.currentItem, is);
				else
					ip.setInventorySlotContents(ip.currentItem, null);
				
				event.setCanceled(true);
			}
		}
		
		//Document
		if (event.entityPlayer.getHeldItem() != null && 
				(event.entityPlayer.getHeldItem().isItemEqual(new ItemStack(CivMod.UnwrittenDocument)) || event.entityPlayer.getHeldItem().isItemEqual(new ItemStack(CivMod.IdentityDocument))) && 
				event.target instanceof EntityVillager)
		{
			String name = "Villager";
			InventoryPlayer ip = event.entityPlayer.inventory;
			boolean longSet = false;
			long id = 0;
			if (event.target.getEntityData().hasKey("civid"))
			{
				id = event.target.getEntityData().getLong("civid");
				longSet = true;
				
			}
			
			if (!longSet)
				return;
			
			if (event.target instanceof EntityPlayer)
				name = ((EntityPlayer)event.target).getDisplayName();
			
			ip.setInventorySlotContents(ip.currentItem, 
					IdentityDocument.CreateIdentityDocument(event.entityPlayer.getDisplayName(), name, id));
			
			event.setCanceled(true);
		}
		
		//Villager
		if (EntityVillager.class.isInstance(event.target) && event.entityPlayer.getHeldItem() != null)
		{
			if (ItemFood.class.isInstance(event.entityPlayer.getHeldItem().getItem()))
			{
				InventoryPlayer inv = event.entityPlayer.inventory;
				float hungerAdd = ((ItemFood)event.entityPlayer.getHeldItem().getItem()).func_150905_g(null);
				boolean addSuccess = AnimalManager.AddHunger((EntityVillager)event.target, hungerAdd);
				
				if (addSuccess)
				{
					if (inv.getStackInSlot(inv.currentItem).stackSize == 1)
						inv.setInventorySlotContents(inv.currentItem, null);
					else
						inv.getStackInSlot(inv.currentItem).stackSize--;
					
					event.setCanceled(true);
				}
			}
		}
		
		return;
	}
	
	
}
