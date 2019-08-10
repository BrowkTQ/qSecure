package me.Browk.qSecure;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

public class OtherListeners implements Listener {

    @EventHandler
    public void onPIEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(!Main.sesiune.containsKey(p)) {
            return;
        }
        if(!Main.sesiune.get(p).equalsIgnoreCase("conectat")) {
            p.sendMessage("§ePlay-Secure §8- §cTrebuie sa confirmi pe Discord pentru a finaliza conexiunea!");
            e.setCancelled(true);
        }
        return;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPMEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(!Main.sesiune.containsKey(p)) {
            return;
        }
        if(!Main.sesiune.get(p).equalsIgnoreCase("conectat")) {
            p.sendMessage("§ePlay-Secure §8- §cTrebuie sa confirmi pe Discord pentru a finaliza conexiunea!");
            p.teleport(e.getFrom());
        }
        return;
    }

    @EventHandler
    public void onPDEvent(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if(!Main.sesiune.containsKey(p)) {
            return;
        }
        if(!Main.sesiune.get(p).equalsIgnoreCase("conectat")) {
            p.sendMessage("§ePlay-Secure §8- §cTrebuie sa confirmi pe Discord pentru a finaliza conexiunea!");
            e.setCancelled(true);
        }
        return;
    }

    @EventHandler
    public void onPPEvent(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if(!Main.sesiune.containsKey(p)) {
            return;
        }
        if(!Main.sesiune.get(p).equalsIgnoreCase("conectat")) {
            p.sendMessage("§ePlay-Secure §8- §cTrebuie sa confirmi pe Discord pentru a finaliza conexiunea!");
            e.setCancelled(true);
        }
        return;
    }

    @EventHandler
    public void onPDEvent(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(Main.sesiune.containsKey(p)) {
                if(!Main.sesiune.get(p).equalsIgnoreCase("conectat")) {
                    p.sendMessage("§ePlay-Secure §8- §cTrebuie sa confirmi pe Discord pentru a finaliza conexiunea!");
                    e.setCancelled(true);
                }
            }
        }
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if(Main.sesiune.containsKey(p)) {
                if(!Main.sesiune.get(p).equalsIgnoreCase("conectat")) {
                    p.sendMessage("§ePlay-Secure §8- §cTrebuie sa confirmi pe Discord pentru a finaliza conexiunea!");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPChatEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(!Main.sesiune.containsKey(p)) {
            return;
        }
        if(!Main.sesiune.get(p).equalsIgnoreCase("conectat")) {
            p.sendMessage("§ePlay-Secure §8- §cTrebuie sa confirmi pe Discord pentru a finaliza conexiunea!");
            e.setCancelled(true);
        }
        return;
    }

}
