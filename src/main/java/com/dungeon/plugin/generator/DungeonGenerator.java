package com.dungeon.plugin.generator;

import com.dungeon.plugin.DungeonPlugin;
import com.dungeon.plugin.types.MedievalDungeon;
import com.dungeon.plugin.types.MysticDungeon;
import com.dungeon.plugin.types.UndergroundDungeon;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.*;

public class DungeonGenerator {
    
    private final DungeonPlugin plugin;
    private final Random random;
    private final Set<String> generatedLocations;
    private static final int GENERATION_CHANCE = 700; // 1 em 700 chunks (raro)
    
    public DungeonGenerator(DungeonPlugin plugin) {
        this.plugin = plugin;
        this.random = new Random();
        this.generatedLocations = new HashSet<>();
    }
    
    public boolean shouldGenerateDungeon() {
        return random.nextInt(GENERATION_CHANCE) == 0;
    }
    
    public void generateDungeon(Location location) {
        String locKey = getLocationKey(location);
        
        if (generatedLocations.contains(locKey)) {
            return; // Já foi gerada nesta localização
        }
        
        World world = location.getWorld();
        if (world == null || world.getEnvironment() != World.Environment.NORMAL) {
            return; // Apenas no Overworld
        }
        
        // Encontrar localização subterrânea adequada
        Location dungeonLoc = findSuitableLocation(location);
        if (dungeonLoc == null) {
            return;
        }
        
        // Escolher tipo de dungeon aleatoriamente
        int type = random.nextInt(3);
        boolean isLarge = random.nextBoolean(); // 50% chance de ser grande
        
        try {
            switch (type) {
                case 0:
                    new MedievalDungeon(plugin).generate(dungeonLoc, isLarge);
                    plugin.getLogger().info("Dungeon Medieval gerada em: " + 
                        dungeonLoc.getBlockX() + ", " + dungeonLoc.getBlockY() + ", " + dungeonLoc.getBlockZ());
                    break;
                case 1:
                    new MysticDungeon(plugin).generate(dungeonLoc, isLarge);
                    plugin.getLogger().info("Dungeon Mística gerada em: " + 
                        dungeonLoc.getBlockX() + ", " + dungeonLoc.getBlockY() + ", " + dungeonLoc.getBlockZ());
                    break;
                case 2:
                    new UndergroundDungeon(plugin).generate(dungeonLoc, isLarge);
                    plugin.getLogger().info("Dungeon Subterrânea gerada em: " + 
                        dungeonLoc.getBlockX() + ", " + dungeonLoc.getBlockY() + ", " + dungeonLoc.getBlockZ());
                    break;
            }
            
            generatedLocations.add(locKey);
        } catch (Exception e) {
            plugin.getLogger().warning("Erro ao gerar dungeon: " + e.getMessage());
        }
    }
    
    private Location findSuitableLocation(Location chunkLoc) {
        World world = chunkLoc.getWorld();
        int x = chunkLoc.getBlockX() + random.nextInt(16);
        int z = chunkLoc.getBlockZ() + random.nextInt(16);
        
        // Procurar uma boa altura subterrânea (entre Y=10 e Y=50)
        for (int y = 50; y >= 10; y--) {
            Block block = world.getBlockAt(x, y, z);
            Block above = world.getBlockAt(x, y + 1, z);
            Block below = world.getBlockAt(x, y - 1, z);
            
            // Encontrar área sólida com espaço acima
            if (below.getType().isSolid() && !block.getType().isSolid() && !above.getType().isSolid()) {
                return new Location(world, x, y, z);
            }
        }
        
        return null;
    }
    
    private String getLocationKey(Location loc) {
        return loc.getWorld().getName() + "_" + 
               (loc.getBlockX() / 16) + "_" + 
               (loc.getBlockZ() / 16);
    }
    
    public Set<String> getGeneratedLocations() {
        return new HashSet<>(generatedLocations);
    }
}
