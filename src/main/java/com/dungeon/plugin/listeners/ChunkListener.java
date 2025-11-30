package com.dungeon.plugin.listeners;

import com.dungeon.plugin.DungeonPlugin;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkListener implements Listener {
    
    private final DungeonPlugin plugin;
    
    public ChunkListener(DungeonPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        World world = event.getWorld();
        
        // Apenas gerar no Overworld
        if (world.getEnvironment() != World.Environment.NORMAL) {
            return;
        }
        
        // Verificar se deve gerar dungeon
        if (plugin.getDungeonGenerator().shouldGenerateDungeon()) {
            // Executar geração assíncrona para não travar o servidor
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.getDungeonGenerator().generateDungeon(event.getChunk().getBlock(0, 64, 0).getLocation());
            }, 20L); // Delay de 1 segundo
        }
    }
}
