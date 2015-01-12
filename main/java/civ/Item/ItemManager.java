package civ.Item;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import civ.Core.CivMod;
import civ.Item.Document.BakerJobDocument;
import civ.Item.Document.CaravanJobDocument;
import civ.Item.Document.EmptyDocument;
import civ.Item.Document.FarmJobDocument;
import civ.Item.Document.HerderJobDocument;
import civ.Item.Document.IdentityDocument;
import civ.Item.Document.JobDocument;
import civ.Item.Document.LocationDocument;
import civ.Item.Document.LumberJobDocument;
import civ.Item.Document.MultiLocationDocument;
import civ.Item.Document.QuarryJobDocument;
import civ.Item.Tool.FlintAxe;
import civ.Item.Tool.FlintHoe;
import civ.Item.Tool.FlintPickaxe;
import civ.Item.Tool.FlintShovel;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemManager {
	
	//Static Fields
	public static ItemManager instance = new ItemManager();
	
	//Static Inventory Functions
	
	
	//Static Functions
	public static void RemoveVanillaItems()
	{ 
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		
        Iterator<IRecipe> Leash = recipes.iterator();
        		          
        	while (Leash.hasNext()) {
        		ItemStack is = Leash.next().getRecipeOutput();
        		if (is != null)
        		{
        			boolean rem = false;
        			Item item = is.getItem();
        			Block block = null;
        			if (ItemBlock.class.isInstance(item))
        				block = ((ItemBlock)item).field_150939_a;
        			
        			if (item == Items.wooden_axe)
        				rem = true;
        			if (item == Items.wooden_pickaxe)
        				rem = true;
        			if (item == Items.wooden_sword)
        				rem = true;     
        			if (item == Items.stone_axe)
        				rem = true;
        			if (item == Items.stone_pickaxe)
        				rem = true;
        			if (item == Items.stone_sword)
        				rem = true;
        			
        			if (item == Items.dye && item.getMetadata(15) == 15)
        			{
        				continue;
        			}
        			
        			if (rem)
        			{
        				Leash.remove();
        				
        				if (item != null)
        					item.setCreativeTab(null);
        				if (block != null)
        					block.setCreativeTab(null);
        			}
        		}
        	};
	}
	public static void AddCivSurvivalItems()
	{        
		CraftingManager inst = CraftingManager.getInstance();
		
        //Add Flint pickaxe       
        CivMod.FlintPickaxe = new FlintPickaxe().setUnlocalizedName("itemPickaxeFlint").setTextureName("civmod:flint_pickaxe");
        GameRegistry.registerItem(CivMod.FlintPickaxe, "Flint Pickaxe", "Civilization Mod");
        LanguageRegistry.addName(CivMod.FlintPickaxe, "Flint Pickaxe");
        inst.addRecipe(new ItemStack(CivMod.FlintPickaxe), new Object[] {new String[] {"XXX", " # ", " # "}, '#', Items.stick, 'X', Items.flint});      
        
        //Add Flint Axe
        CivMod.FlintAxe = new FlintAxe().setUnlocalizedName("itemHatchetFlint").setTextureName("civmod:flint_axe");
        GameRegistry.registerItem(CivMod.FlintAxe, "Flint Axe", "Civilization Mod");
        LanguageRegistry.addName(CivMod.FlintAxe, "Flint Axe");
        inst.addRecipe(new ItemStack(CivMod.FlintAxe), new Object[] {new String[] {"XX", "X#", " #"}, '#', Items.stick, 'X', Items.flint});
        
        //Add Flint Hoe
        CivMod.FlintHoe = new FlintHoe().setUnlocalizedName("itemHoeFlint").setTextureName("civmod:flint_hoe");
        GameRegistry.registerItem(CivMod.FlintHoe, "Flint Hoe", "Civilization Mod");
        LanguageRegistry.addName(CivMod.FlintHoe, "Flint Hoe");
        inst.addRecipe(new ItemStack(CivMod.FlintHoe), new Object[] {new String[] {"XX", "X#", " #"}, '#', Items.stick, 'X', Items.flint});
      
        //Add Flint Shovel
        CivMod.FlintShovel = new FlintShovel().setUnlocalizedName("itemShovelFlint").setTextureName("civmod:flint_shovel");
        GameRegistry.registerItem(CivMod.FlintShovel, "Flint Shovel", "Civilization Mod");
        LanguageRegistry.addName(CivMod.FlintShovel, "Flint Shovel");
        inst.addRecipe(new ItemStack(CivMod.FlintShovel), new Object[] {new String[] {"X", "#", "#"}, '#', Items.stick, 'X', Items.flint});
        
        //Add Spears
        /*
        CivMod.WoodenSpear = new WoodenSpear().setUnlocalizedName("itemWoodenSpear").setTextureName("civmod:wooden_spear");
        GameRegistry.registerItem(CivMod.WoodenSpear, "Wooden Spear", "Civilization Mod");
        LanguageRegistry.addName(CivMod.WoodenSpear, "Wooden Spear");
        inst.addRecipe(new ItemStack(CivMod.WoodenSpear), new Object[] {new String[] {"X", "X", "X"}, 'X', Items.stick});

        CivMod.StoneSpear = new StoneSpear().setUnlocalizedName("itemStoneSpear").setTextureName("civmod:stone_spear");
        GameRegistry.registerItem(CivMod.StoneSpear, "Stone Spear", "Civilization Mod");
        LanguageRegistry.addName(CivMod.StoneSpear, "Stone Spear");
        inst.addRecipe(new ItemStack(CivMod.StoneSpear), new Object[] {new String[] {"X", "X", "#"}, 'X', Items.stick, '#', Blocks.cobblestone});

        CivMod.FlintSpear = new FlintSpear().setUnlocalizedName("itemFlintSpear").setTextureName("civmod:flint_spear");
        GameRegistry.registerItem(CivMod.FlintSpear, "Flint Spear", "Civilization Mod");
        LanguageRegistry.addName(CivMod.FlintSpear, "Flint Spear");
        inst.addRecipe(new ItemStack(CivMod.FlintSpear), new Object[] {new String[] {"X", "X", "#"}, 'X', Items.stick, '#', Items.flint});

        CivMod.IronSpear = new WoodenSpear().setUnlocalizedName("itemIronSpear").setTextureName("civmod:iron_spear");
        GameRegistry.registerItem(CivMod.IronSpear, "Iron Spear", "Civilization Mod");
        LanguageRegistry.addName(CivMod.IronSpear, "Iron Spear");
        inst.addRecipe(new ItemStack(CivMod.IronSpear), new Object[] {new String[] {"X", "X", "#"}, 'X', Items.stick, '#', Items.iron_ingot});
      	*/
      
        //Add Saddle
        inst.addRecipe(new ItemStack(Items.saddle), new Object[] {new String[] {"XXX", "X#X", "# #"}, 'X', Items.leather, '#', Items.iron_ingot});
        
        //Add Wool/String recipes
	    inst.addRecipe(new ItemStack(Blocks.wool), new Object[] {"##", "##", '#', Items.string});
	    inst.addRecipe(new ItemStack(Items.string, 4), new Object[] {"#", '#', Blocks.wool});	
	    
	    //Add Gravel to Cobblestone
	    inst.addRecipe(new ItemStack(Blocks.cobblestone), new Object[] {"##", "##", '#', Blocks.gravel});
	    
	    //Add Gravel/Sand/Dirt interchangers
	    //inst.addRecipe(new ItemStack(Blocks), new Object[] {"##", "##", '#', Items.string});
	    
	    //BucketMod
	    //GameRegistry.registerItem(new ItemBucket(Blocks.flowing_water), name)
	    //itemRegistry.addObject(326, "water_bucket", (new ItemBucket(Blocks.flowing_water)).setUnlocalizedName("bucketWater").setContainerItem(item).setTextureName("bucket_water"));
	}
	public static void AddCivEconItems()
	{		
		CraftingManager inst = CraftingManager.getInstance();
		
		//Add Gold nugget to Ingot Recipes
	    inst.addRecipe(new ItemStack(Items.gold_nugget, 9), new Object[] {"#", '#', Items.gold_ingot});	
	
	    //Add Emerald Shard      
	    CivMod.EmeraldShard = new EmeraldShard().setUnlocalizedName("itemEmeraldShard").setTextureName("civmod:emerald_shard");
	    GameRegistry.registerItem(CivMod.EmeraldShard, "Emerald Shard", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.EmeraldShard, "Emerald Shard");          
	    inst.addRecipe(new ItemStack(Items.emerald), new Object[] {"###", "###", "###", '#', CivMod.EmeraldShard});
	    inst.addRecipe(new ItemStack(CivMod.EmeraldShard, 9), new Object[] {"#", '#', Items.emerald});	

	    //Add Iron Nugget  
	    CivMod.IronNugget = new IronNugget().setUnlocalizedName("itemIronNugget").setTextureName("civmod:iron_nugget");
	    GameRegistry.registerItem(CivMod.IronNugget, "Iron Nugget", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.IronNugget, "Iron Nugget");          
	    inst.addRecipe(new ItemStack(Items.iron_ingot), new Object[] {"###", "###", "###", '#', CivMod.IronNugget});
	    inst.addRecipe(new ItemStack(CivMod.IronNugget, 9), new Object[] {"#", '#', Items.iron_ingot});	
	    
	    //Add Key    
	    CivMod.Key = new Key().setUnlocalizedName("key").setTextureName("civmod:key").setCreativeTab(CreativeTabs.tabMisc);
	    GameRegistry.registerItem(CivMod.Key, "Key", "Civilization Mod");
	    //LanguageRegistry.addName(CivMod.Key, "Key");   
	    inst.addRecipe(new ItemStack(CivMod.Key), new Object[] {"#X", "#X", "#", 'X', Items.gold_nugget, '#', Items.gold_ingot});
	    
	    //Add Document
	    CivMod.UnwrittenDocument = new EmptyDocument().setUnlocalizedName("itemUnwrittenDocument").setTextureName("civmod:document");
	    GameRegistry.registerItem(CivMod.UnwrittenDocument, "Document", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.UnwrittenDocument, "Document");  
	    inst.addRecipe(new ItemStack(CivMod.UnwrittenDocument, 4), new Object[] {"##", "#X",
	    	'#', Items.paper, 'X', Items.dye});
	    
	    //Add Location Document
	    CivMod.LocationDocument = new LocationDocument().setUnlocalizedName("itemLocationDocument").setTextureName("civmod:document_written");
	    GameRegistry.registerItem(CivMod.LocationDocument, "Location Document", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.LocationDocument, "Location Document"); 
	    
	    //Add MultiLocation Document
	    CivMod.MultiLocationDocument = new MultiLocationDocument().setUnlocalizedName("itemMultiLocationDocument").setTextureName("civmod:document_written");
	    GameRegistry.registerItem(CivMod.MultiLocationDocument, "MultiLocation Document", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.MultiLocationDocument, "MultiLocation Document"); 
	    
	    //Add Identity Document
	    CivMod.IdentityDocument = new IdentityDocument().setUnlocalizedName("itemIdentityDocument").setTextureName("civmod:document_written");
	    GameRegistry.registerItem(CivMod.IdentityDocument, "Identity Document", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.IdentityDocument, "Identity Document"); 
	    
	    //Add Farm Contract       
	    CivMod.FarmContract = (JobDocument) new FarmJobDocument().setUnlocalizedName("itemFarmContract").setTextureName("civmod:farm_contract");
	    GameRegistry.registerItem(CivMod.FarmContract, "Farm Job Document", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.FarmContract, "Farm Job Document");    
	    
	    
	    //Add Lumber Contract       
	    CivMod.LumberContract = (JobDocument) new LumberJobDocument().setUnlocalizedName("itemLumberContract").setTextureName("civmod:lumber_contract");
	    GameRegistry.registerItem(CivMod.LumberContract, "Lumber Job Document", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.LumberContract, "Lumber Job Document"); 
	    
	    //Add Quarry Contract       
	    CivMod.QuarryContract = (JobDocument) new QuarryJobDocument().setUnlocalizedName("itemQuarryContract").setTextureName("civmod:quarry_contract");
	    GameRegistry.registerItem(CivMod.QuarryContract, "Quarry Job Document", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.QuarryContract, "Quarry Job Document"); 
	    
	    //Add Caravan Contract       
	    CivMod.CaravanContract = (JobDocument) new CaravanJobDocument().setUnlocalizedName("itemCaravanContract").setTextureName("civmod:caravan_contract");
	    GameRegistry.registerItem(CivMod.CaravanContract, "Caravan Job Document", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.CaravanContract, "Caravan Job Document"); 
	    inst.addShapelessRecipe(new ItemStack(CivMod.CaravanContract, 1), new ItemStack(CivMod.EmeraldShard), new ItemStack(CivMod.UnwrittenDocument, 1));
	        
	    //Add Baker Contract       
	    CivMod.BakerContract = (JobDocument) new BakerJobDocument().setUnlocalizedName("itemBakerContract").setTextureName("civmod:baker_contract");
	    GameRegistry.registerItem(CivMod.BakerContract, "Baker Job Document", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.BakerContract, "Baker Job Document"); 
	    inst.addShapelessRecipe(new ItemStack(CivMod.BakerContract, 1), new ItemStack(CivMod.UnwrittenDocument, 1), new ItemStack(Items.bread, 1));
	    
	    //Add Animal Herder Contract       
	    CivMod.HerderContract = (JobDocument) new HerderJobDocument().setUnlocalizedName("itemHerderContract").setTextureName("civmod:herder_contract");
	    GameRegistry.registerItem(CivMod.HerderContract, "Animal Herder Job Document", "Civilization Mod");
	    LanguageRegistry.addName(CivMod.HerderContract, "Animal Herder Job Document"); 
	    inst.addShapelessRecipe(new ItemStack(CivMod.HerderContract, 1), new ItemStack(CivMod.UnwrittenDocument, 1), new ItemStack(Items.egg, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.HerderContract, 1), new ItemStack(CivMod.UnwrittenDocument, 1), new ItemStack(Items.feather, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.HerderContract, 1), new ItemStack(CivMod.UnwrittenDocument, 1), new ItemStack(Items.leather, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.HerderContract, 1), new ItemStack(CivMod.UnwrittenDocument, 1), new ItemStack(Item.getItemFromBlock(Blocks.wool), 1));
	    
	    //Add Hoe Document
	    inst.addShapelessRecipe(new ItemStack(CivMod.FarmContract, 1), new ItemStack(Items.wooden_hoe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.FarmContract, 1), new ItemStack(Items.stone_hoe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.FarmContract, 1), new ItemStack(CivMod.FlintHoe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.FarmContract, 1), new ItemStack(Items.iron_hoe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    
	    //Add Axe documents
	    inst.addShapelessRecipe(new ItemStack(CivMod.LumberContract, 1), new ItemStack(Items.wooden_axe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.LumberContract, 1), new ItemStack(Items.stone_axe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.LumberContract, 1), new ItemStack(CivMod.FlintAxe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.LumberContract, 1), new ItemStack(Items.iron_axe), new ItemStack(CivMod.UnwrittenDocument, 1));		

	    //Add pickaxe documents
	    inst.addShapelessRecipe(new ItemStack(CivMod.QuarryContract, 1), new ItemStack(Items.wooden_pickaxe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.QuarryContract, 1), new ItemStack(Items.stone_pickaxe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.QuarryContract, 1), new ItemStack(CivMod.FlintPickaxe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    inst.addShapelessRecipe(new ItemStack(CivMod.QuarryContract, 1), new ItemStack(Items.iron_pickaxe), new ItemStack(CivMod.UnwrittenDocument, 1));
	    
	    
	    
	}
	public static void AddCivTechItems()
	{				
		CraftingManager inst = CraftingManager.getInstance();
		
	    //Add Farm Contract       
	    CivMod.BookResearch = new BookResearch().setUnlocalizedName("bookResearch").setTextureName("book_normal");
	    GameRegistry.registerItem(CivMod.BookResearch, "Research", "civmod");
	    LanguageRegistry.addName(CivMod.BookResearch, "Research");        
		
	

	}
	
	//Event Functions
	public static void AdditionalBlockDrops(Block broken, List<ItemStack> drops)
	{		
		//Handle Gravel Drops
		/*if (instance.myRandom.nextDouble() > .9 && 
				Block.isEqualTo(broken, Blocks.gravel))
	    {
			double r2 = instance.myRandom.nextDouble();
			
			if (r2 > .5)
				drops.add(new ItemStack(Items.flint));
			else
				drops.add(new ItemStack(Blocks.cobblestone));
			return;
		}*/
		
		//Handle Sand, Dirt, Grass  Drops
		if (instance.myRandom.nextDouble() > .95 && (
				Block.isEqualTo(broken, Blocks.sand) || (Block.isEqualTo(broken, Blocks.sandstone) || 
				Block.isEqualTo(broken, Blocks.dirt) || Block.isEqualTo(broken, Blocks.grass) ||
				Block.isEqualTo(broken, Blocks.gravel) || Block.isEqualTo(broken, Blocks.stone)
					)))						
		{
			double r2 = instance.myRandom.nextDouble();
			
			if (r2 > .5)
				drops.add(new ItemStack(Items.flint));
			else
				drops.add(new ItemStack(Blocks.cobblestone));
			return;			
		}
	}
	
	//private fields
	private Random myRandom;
	
	//Constructor
	protected ItemManager()
	{
		this.myRandom = new Random(Minecraft.getSystemTime());		
	}
}
