package com.dungeon.plugin.generator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Random;

public class RoomGenerator {
    
    private final Random random;
    
    public RoomGenerator() {
        this.random = new Random();
    }
    
    public void generateRoom(Location center, int width, int height, int depth, Material wallMaterial, Material floorMaterial) {
        // Limpar área
        clearArea(center, width, height, depth);
        
        // Criar chão
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                Block block = center.getWorld().getBlockAt(
                    center.getBlockX() + x,
                    center.getBlockY() - 1,
                    center.getBlockZ() + z
                );
                block.setType(floorMaterial);
            }
        }
        
        // Criar paredes
        for (int y = 0; y < height; y++) {
            // Parede X negativo
            for (int z = 0; z < depth; z++) {
                Block block = center.getWorld().getBlockAt(
                    center.getBlockX(),
                    center.getBlockY() + y,
                    center.getBlockZ() + z
                );
                block.setType(wallMaterial);
            }
            
            // Parede X positivo
            for (int z = 0; z < depth; z++) {
                Block block = center.getWorld().getBlockAt(
                    center.getBlockX() + width - 1,
                    center.getBlockY() + y,
                    center.getBlockZ() + z
                );
                block.setType(wallMaterial);
            }
            
            // Parede Z negativo
            for (int x = 0; x < width; x++) {
                Block block = center.getWorld().getBlockAt(
                    center.getBlockX() + x,
                    center.getBlockY() + y,
                    center.getBlockZ()
                );
                block.setType(wallMaterial);
            }
            
            // Parede Z positivo
            for (int x = 0; x < width; x++) {
                Block block = center.getWorld().getBlockAt(
                    center.getBlockX() + x,
                    center.getBlockY() + y,
                    center.getBlockZ() + depth - 1
                );
                block.setType(wallMaterial);
            }
        }
        
        // Criar teto
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                Block block = center.getWorld().getBlockAt(
                    center.getBlockX() + x,
                    center.getBlockY() + height,
                    center.getBlockZ() + z
                );
                block.setType(wallMaterial);
            }
        }
    }
    
    public void createDoorway(Location loc, int width, int height, Material material) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Block block = loc.getWorld().getBlockAt(
                    loc.getBlockX() + x,
                    loc.getBlockY() + y,
                    loc.getBlockZ()
                );
                block.setType(Material.AIR);
            }
        }
    }
    
    public void createCorridor(Location start, Location end, int width, Material wallMaterial, Material floorMaterial) {
        int x1 = start.getBlockX();
        int y1 = start.getBlockY();
        int z1 = start.getBlockZ();
        int x2 = end.getBlockX();
        int z2 = end.getBlockZ();
        
        // Corredor em linha reta
        if (x1 == x2) {
            int minZ = Math.min(z1, z2);
            int maxZ = Math.max(z1, z2);
            for (int z = minZ; z <= maxZ; z++) {
                for (int w = -width/2; w <= width/2; w++) {
                    Block floor = start.getWorld().getBlockAt(x1 + w, y1 - 1, z);
                    floor.setType(floorMaterial);
                    
                    for (int h = 0; h < 3; h++) {
                        Block air = start.getWorld().getBlockAt(x1 + w, y1 + h, z);
                        air.setType(Material.AIR);
                    }
                }
            }
        } else if (z1 == z2) {
            int minX = Math.min(x1, x2);
            int maxX = Math.max(x1, x2);
            for (int x = minX; x <= maxX; x++) {
                for (int w = -width/2; w <= width/2; w++) {
                    Block floor = start.getWorld().getBlockAt(x, y1 - 1, z1 + w);
                    floor.setType(floorMaterial);
                    
                    for (int h = 0; h < 3; h++) {
                        Block air = start.getWorld().getBlockAt(x, y1 + h, z1 + w);
                        air.setType(Material.AIR);
                    }
                }
            }
        }
    }
    
    private void clearArea(Location center, int width, int height, int depth) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    Block block = center.getWorld().getBlockAt(
                        center.getBlockX() + x,
                        center.getBlockY() + y,
                        center.getBlockZ() + z
                    );
                    block.setType(Material.AIR);
                }
            }
        }
    }
}