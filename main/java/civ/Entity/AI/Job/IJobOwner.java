package civ.Entity.AI.Job;

import java.util.List;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IJobOwner {

	Point3i GetLocation();
	List<Point3i> GetWorkSites(Block[] types);
	IInventory GetRepository();
	boolean IsBehaviorTime();
	void intakeWorkerProduct(List<ItemStack> drops);
}
