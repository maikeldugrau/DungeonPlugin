package com.dungeon.plugin.content;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Random;

public class PuzzleManager {
    
    private final Random random;
    
    public PuzzleManager() {
        this.random = new Random();
    }
    
    public void createPuzzle(Location loc, String puzzleType) {
        switch (puzzleType) {
            case "lever":
                createLeverPuzzle(loc);
                break;
            case "button":
                createButtonPuzzle(loc);
                break;
            case "pressure_plate":
                createPressurePlatePuzzle(loc);
                break;
            default:
                createRedstonePuzzle(loc);
        }
    }
    
    private void createLeverPuzzle(Location loc) {
        // Puzzle de alavancas - 4 alavancas que precisam estar na posição correta
        for (int i = 0; i < 4; i++) {
            Location leverLoc = loc.clone().add(i * 2, 0, 0);
            Block wall = leverLoc.getBlock();
            wall.setType(Material.STONE_BRICKS);
            
            Location leverPos = leverLoc.clone().add(0, 1, 0);
            leverPos.getBlock().setType(Material.LEVER);
        }
        
        // Porta de ferro que abre
        Location doorLoc = loc.clone().add(4, 0, 3);
        doorLoc.getBlock().setType(Material.IRON_DOOR);
        doorLoc.clone().add(0, 1, 0).getBlock().setType(Material.IRON_DOOR);
        
        // Redstone conectando (simplificado)
        Location redstoneLoc = loc.clone().add(2, 0, 1);
        redstoneLoc.getBlock().setType(Material.REDSTONE_WIRE);
    }
    
    private void createButtonPuzzle(Location loc) {
        // Puzzle de botões em sequência
        for (int i = 0; i < 3; i++) {
            Location buttonLoc = loc.clone().add(0, 1, i * 2);
            Block wall = buttonLoc.clone().add(-1, 0, 0).getBlock();
            wall.setType(Material.STONE);
            
            buttonLoc.getBlock().setType(Material.STONE_BUTTON);
        }
        
        // Recompensa - baú
        Location chestLoc = loc.clone().add(3, 0, 2);
        chestLoc.getBlock().setType(Material.CHEST);
        
        // Redstone
        for (int i = 0; i < 3; i++) {
            Location redstoneLoc = loc.clone().add(1, 0, i);
            redstoneLoc.getBlock().setType(Material.REDSTONE_WIRE);
        }
    }
    
    private void createPressurePlatePuzzle(Location loc) {
        // Puzzle de placas de pressão com peso
        for (int i = 0; i < 4; i++) {
            Location plateLoc = loc.clone().add(i, 0, 0);
            plateLoc.getBlock().setType(Material.STONE_PRESSURE_PLATE);
        }
        
        // Porta que abre quando todas estão pressionadas
        Location doorLoc = loc.clone().add(2, 0, 3);
        doorLoc.getBlock().setType(Material.IRON_DOOR);
        doorLoc.clone().add(0, 1, 0).getBlock().setType(Material.IRON_DOOR);
        
        // Sistema redstone
        for (int i = 0; i < 4; i++) {
            Location redstoneLoc = loc.clone().add(i, 0, 1);
            redstoneLoc.getBlock().setType(Material.REDSTONE_WIRE);
        }
    }
    
    private void createRedstonePuzzle(Location loc) {
        // Puzzle complexo de redstone
        // Criar grid de redstone
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                Location gridLoc = loc.clone().add(x, 0, z);
                if ((x + z) % 2 == 0) {
                    gridLoc.getBlock().setType(Material.REDSTONE_WIRE);
                } else {
                    gridLoc.getBlock().setType(Material.REDSTONE_TORCH);
                }
            }
        }
        
        // Alavanca de ativação
        Location leverLoc = loc.clone().add(2, 1, -1);
        leverLoc.getBlock().setType(Material.LEVER);
        
        // Recompensa
        Location rewardLoc = loc.clone().add(2, 0, 6);
        rewardLoc.getBlock().setType(Material.CHEST);
    }
    
    public void createMazePuzzle(Location start, int size) {
        // Criar um pequeno labirinto
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                Location wallLoc = start.clone().add(x, 0, z);
                
                // Paredes externas
                if (x == 0 || x == size - 1 || z == 0 || z == size - 1) {
                    wallLoc.getBlock().setType(Material.STONE_BRICKS);
                    wallLoc.clone().add(0, 1, 0).getBlock().setType(Material.STONE_BRICKS);
                    wallLoc.clone().add(0, 2, 0).getBlock().setType(Material.STONE_BRICKS);
                }
                // Paredes internas aleatórias
                else if (random.nextInt(3) == 0) {
                    wallLoc.getBlock().setType(Material.STONE_BRICKS);
                    wallLoc.clone().add(0, 1, 0).getBlock().setType(Material.STONE_BRICKS);
                }
            }
        }
        
        // Entrada
        Location entrance = start.clone().add(0, 0, size / 2);
        entrance.getBlock().setType(Material.AIR);
        entrance.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
        
        // Saída
        Location exit = start.clone().add(size - 1, 0, size / 2);
        exit.getBlock().setType(Material.AIR);
        exit.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
        
        // Recompensa no centro
        Location center = start.clone().add(size / 2, 0, size / 2);
        center.getBlock().setType(Material.CHEST);
    }
}