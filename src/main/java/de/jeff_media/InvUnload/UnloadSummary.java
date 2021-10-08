package de.jeff_media.InvUnload;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UnloadSummary {
	
	final HashMap<Location,HashMap<String,Integer>> unloads;
	
	UnloadSummary() {

		//System.out.println("Creating summary");
		unloads = new HashMap<>();
	}
	
	void protocolUnload(Location loc, ItemStack item) {
		String name = item.getI18NDisplayName();
		if (item.getItemMeta().hasDisplayName()) {
			name = item.getItemMeta().getDisplayName();
		}
		if(item.getAmount()==0) return;
		if(!unloads.containsKey(loc)) {
			unloads.put(loc, new HashMap<>());
			unloads.get(loc).put(name, item.getAmount());
		} else {
			if(unloads.get(loc).containsKey(name)) {
				unloads.get(loc).put(name, unloads.get(loc).get(name)+item.getAmount());
			} else {
				unloads.get(loc).put(name, item.getAmount());
			}
		}
	}
	
	String loc2str(Location loc) {
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		String name = loc.getBlock().getType().name();
		BlockState state = loc.getWorld().getBlockAt(x,y,z).getState();
		if(state instanceof Container) {
			Container container = (Container) state;
			if(container.getCustomName() != null) {
				name = container.getCustomName();
			}
		}
		return String.format(ChatColor.LIGHT_PURPLE + "§l%s   §r§a§lX: §f%d §a§lY: §f%d §a§lZ: §f%d", name,x,y,z);
	}
	
	String amount2str(int amount) {
		return String.format(ChatColor.DARK_PURPLE+"|§7%5dx  ", amount);
	}
	
	void print(PrintRecipient recipient, Player p) {
		if(unloads.size()>0) printTo(recipient,p," ");
		for(Entry<Location,HashMap<String,Integer>> entry : unloads.entrySet()) {
			printTo(recipient,p," ");
			printTo(recipient,p,loc2str(entry.getKey()));
			HashMap<String,Integer> map = entry.getValue();
			for(Entry<String,Integer> entry2 : map.entrySet()) {
				printTo(recipient,p,
						amount2str(entry2.getValue()) + ChatColor.GOLD + entry2.getKey());
			}
			//printTo(recipient,p," ");
		}
	}
	
	enum PrintRecipient {
		PLAYER, CONSOLE
	}
	
	void printTo(PrintRecipient recipient, Player p, String text) {
		//System.out.println("Printing");
		if(recipient == PrintRecipient.CONSOLE) {
			System.out.println(text);
		} else {
			p.sendMessage(text);
		}
	}
	
	

}
