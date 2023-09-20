package cc.altoya.settlements.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EventProtectEntities implements Listener{
  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
  }
  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent event) {
  }
}
