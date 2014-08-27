package cz.majncraft.protection;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.meta.BookMeta;

import cz.majncraft.MajnCorePlugin;

public class BookTrade implements Listener {
	
	private static List<String> names = MajnCorePlugin.instance.getCfg().getStringList("protection.bookTrade");
	
	@EventHandler
    public void StopPlaceOfCustomBook(InventoryClickEvent e) {
        if (!(e.getInventory() instanceof MerchantInventory)) {
            return;
        }
        
        ItemStack i = e.getCurrentItem();
        if(i == null || i.getType() != Material.WRITTEN_BOOK)
        	return;
        
        BookMeta book = (BookMeta)i.getItemMeta();
        boolean restrictedAuthor = false;
        
        for (String name: names){
        	if(name.equalsIgnoreCase(book.getAuthor())){
        		restrictedAuthor = true;
        		break;
        	}
        }
        if(!restrictedAuthor)
        	return;
        
        Player sender = (Player)e.getWhoClicked();
        //sender.sendMessage("You can't click this book while you're in Merchant's inventory!");
        sender.sendMessage(ChatColor.RED + "Tuto knizku nelze presouvat, kdyz jsi v nabidce obchodnika!");
        e.setCancelled(true);
        
	}

}
