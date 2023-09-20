package cc.altoya.settlements.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.ClaimUtil;

public class EventProtectBlocks implements Listener{
  @EventHandler
  public void playerInteract(BlockBreakEvent event){
    if(!ClaimUtil.isPlayerTrusted(event.getPlayer(), event.getBlock().getLocation())){
      event.setCancelled(true);
      ChatUtil.sendErrorMessage(event.getPlayer(), "You are not trusted within this claim!");
      return;
    }
  }

  @EventHandler
  public void playerInteract(BlockPlaceEvent event){
    if(!ClaimUtil.isPlayerTrusted(event.getPlayer(), event.getBlock().getLocation())){
      event.setCancelled(true);
      ChatUtil.sendErrorMessage(event.getPlayer(), "You are not trusted within this claim!");
      return;
    }
  }

}
