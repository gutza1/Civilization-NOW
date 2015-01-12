package civ.Technology;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;

public class TechAchievement extends Achievement {

	public String Description;
	
	public TechAchievement(String p_i45300_1_, String p_i45300_2_,
			int p_i45300_3_, int p_i45300_4_, Item p_i45300_5_,
			Achievement p_i45300_6_) {
		super(p_i45300_1_, p_i45300_2_, p_i45300_3_, p_i45300_4_, p_i45300_5_,
				p_i45300_6_);
		
	}


}
