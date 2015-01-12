package civ.GUI;

import org.lwjgl.opengl.GL11;

import civ.Container.ContainerShopTable;
import civ.Core.CivMod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiMerchantTable extends GuiContainer
{
	//Static Functions
    public static void DisplayGUIMerchTable_ForPlayer_FromServer(EntityPlayerMP player, IInventory inventory, int posX, int posY, int posZ)
    {
    	if (player.worldObj.isRemote)
    		return;
    	
        if (player.openContainer != player.inventoryContainer)
        {
        	player.closeScreen();
        }

        player.getNextWindowId();
        
        //player.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(player.currentWindowId, 0, inventory.getInventoryName(), inventory.getSizeInventory(), inventory.hasCustomInventoryName()));
        //CivMod.network.sendTo(new PacketGuiMerchantTable(0, player.worldObj.getTileEntity(posX, posY, posZ)), player);
        
        player.openContainer = new ContainerChest(player.inventory, inventory);
        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.addCraftingToCrafters(player);        

        player.openGui(CivMod.instance, 0, player.worldObj, posX, posY, posZ);
    }
    
    //Private Fields
    //private static final ResourceLocation field_147017_u = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation field_147017_u = new ResourceLocation("civmod", "/textures/gui/merchant_table_gui.png");
    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;
    /**
     * window height is calculated with these values; the more rows, the heigher
     */
    private int inventoryRows;
    private static final String __OBFID = "CL_00000749";

    public GuiMerchantTable(IInventory par1IInventory, IInventory par2IInventory)
    {
        super(new ContainerShopTable(par1IInventory, par2IInventory));
        this.upperChestInventory = par1IInventory;
        this.lowerChestInventory = par2IInventory;
        this.allowUserInput = false;
        short short1 = 222;
        int i = short1 - 108;
        this.inventoryRows = par2IInventory.getSizeInventory() / 12;//9;
        this.xSize = 252;
        this.ySize = i + this.inventoryRows * 18;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        this.fontRendererObj.drawString(this.lowerChestInventory.hasCustomInventoryName() ? this.lowerChestInventory.getInventoryName() : I18n.format(this.lowerChestInventory.getInventoryName(), new Object[0]), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.upperChestInventory.hasCustomInventoryName() ? this.upperChestInventory.getInventoryName() : I18n.format(this.upperChestInventory.getInventoryName(), new Object[0]), 8, this.ySize - 96 + 2, 4210752);
        this.fontRendererObj.drawString("Trades", 183, 6, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(field_147017_u);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
