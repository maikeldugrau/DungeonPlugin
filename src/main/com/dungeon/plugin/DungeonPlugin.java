package com.dungeon.plugin;

import com.dungeon.plugin.commands.DungeonCommand;
import com.dungeon.plugin.generator.DungeonGenerator;
import com.dungeon.plugin.listeners.ChunkListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DungeonPlugin extends JavaPlugin {
    
    private static DungeonPlugin instance;
    private DungeonGenerator dungeonGenerator;
    private Logger logger;
    
    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        
        // Salvar configuração padrão
        saveDefaultConfig();
        
        // Inicializar gerador de dungeons
        dungeonGenerator = new DungeonGenerator(this);
        
        // Registrar listeners
        getServer().getPluginManager().registerEvents(new ChunkListener(this), this);
        
        // Registrar comandos
        getCommand("dungeon").setExecutor(new DungeonCommand(this));
        
        logger.info("====================================");
        logger.info("  DungeonPlugin v1.0.0 Ativado!");
        logger.info("  Gerando dungeons no Overworld...");
        logger.info("====================================");
    }
    
    @Override
    public void onDisable() {
        logger.info("DungeonPlugin desativado!");
    }
    
    public static DungeonPlugin getInstance() {
        return instance;
    }
    
    public DungeonGenerator getDungeonGenerator() {
        return dungeonGenerator;
    }
}
