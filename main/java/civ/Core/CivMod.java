package civ.Core;

import java.lang.reflect.Method;
import java.util.Random;

import com.google.common.eventbus.EventBus;
import com.sun.java.util.jar.pack.*;
import java.lang.reflect.*;

import sun.reflect.Reflection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import civ.Block.*;
import civ.Block.Structure.BlockGranary;
import civ.Block.Structure.BlockMonument;
import civ.Entity.EntityCivHorse;
import civ.GUI.*;
import civ.Item.*;
import civ.Item.Document.JobDocument;
import civ.Technology.TechnologyManager;
import civ.TileEntity.*;

@Mod(modid = CivMod.MODID, name = CivMod.MODID, version = CivMod.VERSION)
public class CivMod extends DummyModContainer
{
    @Mod.Instance(value = "civmod")
    public static CivMod instance;   
    public static final String MODID = "civmod";
    public static final String VERSION = "0.0.0.1";
    
    public static boolean DEBUG = true;
    public static SimpleNetworkWrapper network;    
    @SidedProxy(clientSide="civ.Core.ClientProxy", serverSide="civ.Core.CommonProxy")
    public static CommonProxy proxy;
    public static Random RandomObj;
    
    public static CreativeTabs tabCivil = new CreativeTabs("tabCivil") {
        public Item getTabIconItem() {
                return Items.emerald;
        }
};
    
    //Mod Evo
    public static Block EvoBlockTallGrass;
    public static Block EvoBlockDoubleTallGrass;
    //public static Block EvoBlockSoil;
    
    //Mod Entity
    
    //Mod Econ-Blocks
    public static Block ShopTable;
    public static Block ShopPost;
    public static Block AutoShopTable;
    public static Block JobPost;
    public static Block Archive;
    
    //Mod Structure-Blocks
    public static Block MonumentBlock;
    public static BlockGranary GranaryBlock;
    
    //Mod Econ-Items
    public static Item EmeraldShard;
    public static Item IronNugget;
    public static Item Key; 
    
    //Mod Econ-Documents
    public static Item UnwrittenDocument;
    public static Item LocationDocument;
    public static Item MultiLocationDocument;
    public static Item IdentityDocument;
    public static JobDocument FarmContract;
    public static JobDocument LumberContract;
    public static JobDocument QuarryContract;
    public static JobDocument CaravanContract;
    public static JobDocument HerderContract;
    public static JobDocument BakerContract;
    
    //Mod Tech Items
    public static Item BookResearch;
    
    //Mod Tools
    public static ToolMaterial ToolMaterialFlint = EnumHelper.addToolMaterial("FLINT", 1, 131, 4.0F, 1, 26);
    public static Item FlintPickaxe;
    public static Item FlintAxe;
    public static Item FlintShovel;
    public static Item FlintHoe;
    
    //Mod weapons
    public static Item WoodenSpear;
    public static Item StoneSpear;
    public static Item FlintSpear;
    public static Item IronSpear;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{         		  	
    	//Register Bus Events
    	MinecraftForge.EVENT_BUS.register(new EventManager());
    	FMLCommonHandler.instance().bus().register(new EventManager());
        
		//Register Instance
		CivMod.instance = this;
		//CivMod.subInstance = new CivSubMod();
		//CivMod.subInstance.setEnabledState(true);wwww
		CivMod.RandomObj = new Random(0);
		//IntegratedServer.getServer().getServer().registerEvents(this, this);
		
        //Register Renderers
		proxy.registerRenderers();
		
		/*/Register Civ Villager
        EntityRegistry.registerGlobalEntityID(EntityBaseCivVillager.class, "EntityBaseCivVillager",	EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityBaseCivVillager.class, "EntityBaseCivVillager", 1, this, 40, 5, true); 
        
		//*Register Animals Cow
        EntityRegistry.registerGlobalEntityID(EntityEvoCow.class, "EntityEvoCow",	EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModssEntity(EntityEvoCow.class, "EntityEvoCow", -1, this, 40, 5, true);  
        
		//Register Animals Cow
        EntityRegistry.registerGlobalEntityID(EntityEvoCow.class, "EntityEvoAnimal",	EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityEvoCow.class, "EntityEvoAnimal", 2, this, 40, 5, true);  */
        
		//Register Custom Horse
		int civHorseID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityCivHorse.class, "EntityCivHorse", civHorseID);
        EntityRegistry.registerModEntity(EntityCivHorse.class, "EntityCivHorse", civHorseID, this, 64, 1, true);         
        
        //Load Blocks
        //((Block)Block.blockRegistry.getObject("tallGrass"))
        CraftingManager inst = CraftingManager.getInstance();

        CivMod.ShopTable = new BlockShopTable().setBlockName("Merchant Table").setBlockTextureName("civmod:merchant_table");
        GameRegistry.registerBlock(CivMod.ShopTable, "Merchant Table");
        LanguageRegistry.addName(CivMod.ShopTable, "Merchant Table");    
        
        
        CivMod.ShopPost = new BlockShopPost().setBlockName("Merchant Post").setBlockTextureName("civmod:merchant_post");
        GameRegistry.registerBlock(CivMod.ShopPost, "Merchant Post");
        LanguageRegistry.addName(CivMod.ShopPost, "Merchant Post");         
        inst.addRecipe(new ItemStack(CivMod.ShopPost), new Object[] {"WW", "PP",
	    	'P', Blocks.planks, 'W', Blocks.wool});
        
        CivMod.AutoShopTable = new BlockAutoShopTable().setBlockName("AutoMerchant Table").setBlockTextureName("civmod:merchant_table");
        GameRegistry.registerBlock(CivMod.AutoShopTable, "AutoMerchant Table");
        LanguageRegistry.addName(CivMod.AutoShopTable, "AutoMerchant Table");
        inst.addRecipe(new ItemStack(CivMod.AutoShopTable), new Object[] {"WW", "CC",
	    	'C', Blocks.chest, 'W', Blocks.wool});
        
        CivMod.JobPost = new BlockJobPost().setBlockName("Job Post").setBlockTextureName("civmod:job_table");
        GameRegistry.registerBlock(CivMod.JobPost, "Job Post");
        LanguageRegistry.addName(CivMod.JobPost, "Job Post");
        inst.addRecipe(new ItemStack(CivMod.JobPost), new Object[] {"WW", "II",
	    	'I', Items.iron_ingot, 'W', Blocks.wool});
        
        CivMod.Archive = new BlockArchive().setBlockName("Archive").setBlockTextureName("civmod:archive");
        GameRegistry.registerBlock(CivMod.Archive, "Archive");
        LanguageRegistry.addName(CivMod.Archive, "Archive");
        inst.addRecipe(new ItemStack(CivMod.Archive), new Object[] {"WW", "BB",
	    	'B', Blocks.bookshelf, 'W', Blocks.wool});
        
        CivMod.MonumentBlock = new BlockMonument().setBlockName("blockIron").setBlockTextureName("iron_block");
        GameRegistry.registerBlock(CivMod.MonumentBlock, "Monument");
        LanguageRegistry.addName(CivMod.MonumentBlock, "Monument");
        CivMod.GranaryBlock = new BlockGranary();
        GameRegistry.registerBlock(CivMod.GranaryBlock, "Granary");
        LanguageRegistry.addName(CivMod.GranaryBlock, "Granary");
        
        //CivMod.WorkBench = new BlockCivBench().setBlockName("CivBench").setBlockTextureName("crafting_table");
        //GameRegistry.registerBlock(CivMod.WorkBench, "Crafting Table");
        //LanguageRegistry.addName(CivMod.WorkBench, "Crafting Table");
        //inst.addRecipe(new ItemStack(CivMod.WorkBench), new Object[] {"PP", "PP", 'P', Blocks.planks });
        
        //CivMod.LiquidWater = new BlockLiq(Material.water).setBlockName("CivWater").setBlockTextureName("flowing_water");
        //GameRegistry.registerBlock(CivMod.LiquidWater, "Liquid Water");
        //LanguageRegistry.addName(CivMod.LiquidWater, "Liquid Water");
        
        /*CivMod.Sand = (BlockSandSoil)new BlockSandSoil().setBlockName("sandsoil").setBlockTextureName("sand");
        GameRegistry.registerBlock(CivMod.Sand, "Sandy Soil");
        LanguageRegistry.addName(CivMod.Sand, "Sandy Soil");
        CivMod.Dirt = (BlockDirtSoil)new BlockDirtSoil().setBlockName("dirtsoil").setBlockTextureName("dirt");
        GameRegistry.registerBlock(CivMod.Dirt, "Soil");
        LanguageRegistry.addName(CivMod.Dirt, "Soil");*/
        
        //Register generator
        //GameRegistry.registerWorldGenerator(CivMod.MyWorldGenerator, (int) 1000.0F);        
        
        //TileEntity
        GameRegistry.registerTileEntity(TileEntityJobPost.class, "TileEntityJobPost");
        GameRegistry.registerTileEntity(TileEntityShopPost.class, "TileEntityShopPost");
        GameRegistry.registerTileEntity(TileEntityShopTable.class, "TileEntityShopTable");
        GameRegistry.registerTileEntity(TileEntityAutoShopTable.class, "TileEntityAutoShopTable");
        GameRegistry.registerTileEntity(TileEntityArchive.class, "TileEntityArchive");
        GameRegistry.registerTileEntity(TileEntityGranary.class, "TileEntityGranary");
        
        //Items
        ItemManager.AddCivSurvivalItems();
        ItemManager.AddCivEconItems();
        ItemManager.AddCivTechItems();
        
		//Modify Tech Items
		ItemManager.RemoveVanillaItems();
        
        //Economy
        Currency.InitializeDefaultCurrencies();
        
        //Post modify
        //EntityWorldManager.EnableChestedHorse = true;
        
        //Nature
        //NatureManager.InterconnectNatureBlocks();
	}
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {      	
    	//Network 
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());  
        //this.network = NetworkRegistry.INSTANCE.newSimpleChannel("CIVV");          
        
        //network.registerMessage(PacketAgentSync.Handler.class, PacketAgentSync.class, 0, Side.CLIENT);
        //network.registerMessage(PacketGuiMerchantTable.Handler.class, PacketGuiMerchantTable.class, 1, Side.SERVER);
		
		//DEBUG Output
    	if (CivMod.DEBUG)
    		System.out.println("### CivMod Initialized ###");  
    	
    	//Event Manager
    	EventManager.InitDropBlockConvert();
        
        //Build Acheivements
        TechnologyManager.InitializeFarmingAchievements();
    }
    
	@EventHandler
    public void load(FMLInitializationEvent event) {

        
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{		
		//TechnologyManager.CollectCraftingRecipes();
		
		return;
	}
}
