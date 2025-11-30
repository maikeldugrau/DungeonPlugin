package com.dungeon.plugin.content;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class TrapManager {
    
    private final Random random;
    
    public TrapManager() {
        this.random = new Random();
    }
    
    public void placeTrap(Location loc, String trapType) {
        switch (trapType) {
            case "arrow":
                createArrowTrap(loc);
                break;
            case "tnt":
                createTNTTrap(loc);
                break;
            case "lava":
                createLavaTrap(loc);
                break;
            case "magic":
                createMagicTrap(loc);
                break;
            default:
                createPressurePlateTrap(loc);
        }
    }
    
    private void createArrowTrap(Location loc) {
        // Dispenser com flechas
        Block dispenser = loc.getBlock();
        dispenser.setType(Material.DISPENSER);
        
        if (dispenser.getState() instanceof Dispenser dispenserState) {
            dispenserState.getInventory().addItem(new ItemStack(Material.ARROW, 64));
            dispenserState.update();
        }
        
        // Placa de pressão para ativar
        Location plateLoc = loc.clone().add(3, 0, 0);
        plateLoc.getBlock().setType(Material.STONE_PRESSURE_PLATE);
        
        // Redstone conectando
        Location redstoneLoc = loc.clone().add(1, 0, 0);
        redstoneLoc.getBlock().setType(Material.REDSTONE_WIRE);
        redstoneLoc.clone().add(1, 0, 0).getBlock().setType(Material.REDSTONE_WIRE);
    }
    
    private void createTNTTrap(Location loc) {
        // TNT escondido
        loc.getBlock().setType(Material.TNT);
        
        // Placa de pressão acima
        Location plateLoc = loc.clone().add(0, 1, 0);
        plateLoc.getBlock().setType(Material.STONE_PRESSURE_PLATE);
        
        // Redstone torch embaixo
        Location torchLoc = loc.clone().add(0, -1, 0);
        torchLoc.getBlock().setType(Material.REDSTONE_TORCH);
    }
    
    private void createLavaTrap(Location loc) {
        // Poço de lava escondido
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                Location lavaLoc = loc.clone().add(x, -1, z);
                lavaLoc.getBlock().setType(Material.LAVA);
                
                // Carpet para esconder
                Location carpetLoc = loc.clone().add(x, 0, z);
                carpetLoc.getBlock().setType(Material.AIR);
            }
        }
        
        // String trip wire
        Location tripLoc = loc.clone().add(1, 0, -1);
        tripLoc.getBlock().setType(Material.TRIPWIRE);
    }
    
    private void createMagicTrap(Location loc) {
        // Efeito mágico com observer e redstone
        loc.getBlock().setType(Material.OBSERVER);
        
        Location redstoneLoc = loc.clone().add(1, 0, 0);
        redstoneLoc.getBlock().setType(Material.REDSTONE_WIRE);
        
        // Dispenser com poções de dano
        Location dispenserLoc = loc.clone().add(2, 0, 0);
        Block dispenser = dispenserLoc.getBlock();
        dispenser.setType(Material.DISPENSER);
        
        if (dispenser.getState() instanceof Dispenser dispenserState) {
            dispenserState.getInventory().addItem(new ItemStack(Material.SPLASH_POTION, 8));
            dispenserState.update();
        }
    }
    
    private void createPressurePlateTrap(Location loc) {
        // Armadilha simples de placa de pressão
        loc.getBlock().setType(Material.STONE_PRESSURE_PLATE);
        
        // Pistões ao redor
        Location pistonLoc1 = loc.clone().add(1, 0, 0);
        pistonLoc1.getBlock().setType(Material.STICKY_PISTON);
        
        Location pistonLoc2 = loc.clone().add(-1, 0, 0);
        pistonLoc2.getBlock().setType(Material.STICKY_PISTON);
        
        // Redstone
        Location redstoneLoc = loc.clone().add(0, -1, 0);
        redstoneLoc.getBlock().setType(Material.REDSTONE_BLOCK);
    }
}