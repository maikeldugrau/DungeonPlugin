package com.dungeon.plugin.generator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Random;

public class StructureBuilder {
    
    private final Random random;
    
    public StructureBuilder() {
        this.random = new Random();
    }
    
    public void buildPillar(Location loc, int height, Material material) {
        for (int y = 0; y < height; y++) {
            Block block = loc.getWorld().getBlockAt(
                loc.getBlockX(),
                loc.getBlockY() + y,
                loc.getBlockZ()
            );
            block.setType(material);
        }
    }
    
    public void buildAltar(Location center, Material baseMaterial, Material topMaterial) {
        // Base do altar (3x3)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = center.getWorld().getBlockAt(
                    center.getBlockX() + x,
                    center.getBlockY(),
                    center.getBlockZ() + z
                );
                block.setType(baseMaterial);
            }
        }
        
        // Topo do altar
        Block top = center.getWorld().getBlockAt(
            center.getBlockX(),
            center.getBlockY() + 1,
            center.getBlockZ()
        );
        top.setType(topMaterial);
    }
    
    public void buildStairs(Location start, int length, Material material, boolean ascending) {
        int direction = ascending ? 1 : -1;
        
        for (int i = 0; i < length; i++) {
            Block block = start.getWorld().getBlockAt(
                start.getBlockX() + i,
                start.getBlockY() + (i * direction),
                start.getBlockZ()
            );
            block.setType(material);
        }
    }
    
    public void buildBridge(Location start, Location end, int width, Material material) {
        int x1 = start.getBlockX();
        int y = start.getBlockY();
        int z1 = start.getBlockZ();
        int x2 = end.getBlockX();
        int z2 = end.getBlockZ();
        
        if (x1 == x2) {
            int minZ = Math.min(z1, z2);
            int maxZ = Math.max(z1, z2);
            for (int z = minZ; z <= maxZ; z++) {
                for (int w = -width/2; w <= width/2; w++) {
                    Block block = start.getWorld().getBlockAt(x1 + w, y, z);
                    block.setType(material);
                }
            }
        } else if (z1 == z2) {
            int minX = Math.min(x1, x2);
            int maxX = Math.max(x1, x2);
            for (int x = minX; x <= maxX; x++) {
                for (int w = -width/2; w <= width/2; w++) {
                    Block block = start.getWorld().getBlockAt(x, y, z1 + w);
                    block.setType(material);
                }
            }
        }
    }
    
    public void buildTower(Location base, int height, int radius, Material wallMaterial, Material floorMaterial) {
        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + z * z);
                    
                    Block block = base.getWorld().getBlockAt(
                        base.getBlockX() + x,
                        base.getBlockY() + y,
                        base.getBlockZ() + z
                    );
                    
                    if (distance <= radius && distance > radius - 1) {
                        block.setType(wallMaterial);
                    } else if (distance < radius - 1 && y % 5 == 0) {
                        block.setType(floorMaterial);
                    } else if (distance < radius - 1) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
    
    public void addRandomDecoration(Location loc, Material... materials) {
        if (random.nextInt(3) == 0) {
            Material chosen = materials[random.nextInt(materials.length)];
            Block block = loc.getWorld().getBlockAt(loc);
            block.setType(chosen);
        }
    }
}