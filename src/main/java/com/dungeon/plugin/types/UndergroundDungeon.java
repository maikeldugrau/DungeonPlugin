package com.dungeon.plugin.types;

import com.dungeon.plugin.DungeonPlugin;
import com.dungeon.plugin.content.BossManager;
import com.dungeon.plugin.content.LootManager;
import com.dungeon.plugin.content.PuzzleManager;
import com.dungeon.plugin.content.TrapManager;
import com.dungeon.plugin.generator.RoomGenerator;
import com.dungeon.plugin.generator.StructureBuilder;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Random;

public class UndergroundDungeon {
    
    private final DungeonPlugin plugin;
    private final Random random;
    private final RoomGenerator roomGen;
    private final StructureBuilder structureBuilder;
    private final LootManager lootManager;
    private final TrapManager trapManager;
    private final PuzzleManager puzzleManager;
    private final BossManager bossManager;
    
    public UndergroundDungeon(DungeonPlugin plugin) {
        this.plugin = plugin;
        this.random = new Random();
        this.roomGen = new RoomGenerator();
        this.structureBuilder = new StructureBuilder();
        this.lootManager = new LootManager();
        this.trapManager = new TrapManager();
        this.puzzleManager = new PuzzleManager();
        this.bossManager = new BossManager();
    }
    
    public void generate(Location start, boolean isLarge) {
        int roomCount = isLarge ? 9 : 5;
        int roomSize = isLarge ? 18 : 14;
        
        Material wallMat = Material.DEEPSLATE;
        Material floorMat = Material.COBBLED_DEEPSLATE;
        Material accentMat = Material.DEEPSLATE_BRICKS;
        
        // Sala de entrada - caverna natural
        Location entranceRoom = start.clone();
        generateCaveRoom(entranceRoom, roomSize, wallMat, floorMat);
        addUndergroundDecorations(entranceRoom, roomSize);
        
        // Sistema de túneis e salas
        Location currentLoc = entranceRoom.clone();
        
        for (int i = 0; i < roomCount - 1; i++) {
            // Alternar direção
            if (i % 2 == 0) {
                currentLoc.add(roomSize + 4, random.nextInt(3) - 1, 0);
            } else {
                currentLoc.add(0, random.nextInt(3) - 1, roomSize + 4);
            }
            
            generateCaveRoom(currentLoc, roomSize - 2, wallMat, floorMat);
            
            // Adicionar conteúdo de mina
            if (i % 3 == 0) {
                lootManager.placeChest(currentLoc.clone().add(roomSize/2, 0, roomSize/2), "mining");
                addOreDeposits(currentLoc, roomSize - 2);
            } else if (i % 3 == 1) {
                lootManager.placeSpawner(currentLoc.clone().add(roomSize/2, 0, roomSize/2), "zombie");
                trapManager.placeTrap(currentLoc.clone().add(3, 0, 3), "lava");
            } else {
                puzzleManager.createPuzzle(currentLoc.clone().add(roomSize/2, 0, 2), "pressure_plate");
                addMinecartTracks(currentLoc, roomSize - 2);
            }
            
            addUndergroundDecorations(currentLoc, roomSize - 2);
        }
        
        // Sala do Boss - grande caverna profunda
        Location bossRoom = currentLoc.clone().add(roomSize, -5, roomSize);
        int bossRoomSize = isLarge ? 30 : 22;
        generateCaveRoom(bossRoom, bossRoomSize, Material.DEEPSLATE, Material.DEEPSLATE_TILES);
        
        // Lago de lava no centro
        createLavaPool(bossRoom.clone().add(bossRoomSize/2 - 3, -1, bossRoomSize/2 - 3), 6);
        
        // Plataformas ao redor da lava
        createPlatform(bossRoom.clone().add(bossRoomSize/2 - 8, 0, bossRoomSize/2), 4, Material.DEEPSLATE_BRICKS);
        createPlatform(bossRoom.clone().add(bossRoomSize/2 + 4, 0, bossRoomSize/2), 4, Material.DEEPSLATE_BRICKS);
        createPlatform(bossRoom.clone().add(bossRoomSize/2, 0, bossRoomSize/2 - 8), 4, Material.DEEPSLATE_BRICKS);
        createPlatform(bossRoom.clone().add(bossRoomSize/2, 0, bossRoomSize/2 + 4), 4, Material.DEEPSLATE_BRICKS);
        
        // Spawnar boss da caverna
        bossManager.spawnBoss(bossRoom.clone().add(bossRoomSize/2, 1, bossRoomSize/2), "cave_golem");
        
        // Baús de recompensa nas plataformas
        lootManager.placeChest(bossRoom.clone().add(bossRoomSize/2 - 8, 1, bossRoomSize/2), "underground_boss");
        lootManager.placeChest(bossRoom.clone().add(bossRoomSize/2 + 4, 1, bossRoomSize/2), "underground_boss");
        
        // Pilares de suporte
        structureBuilder.buildPillar(bossRoom.clone().add(5, 0, 5), 10, accentMat);
        structureBuilder.buildPillar(bossRoom.clone().add(bossRoomSize-6, 0, 5), 10, accentMat);
        structureBuilder.buildPillar(bossRoom.clone().add(5, 0, bossRoomSize-6), 10, accentMat);
        structureBuilder.buildPillar(bossRoom.clone().add(bossRoomSize-6, 0, bossRoomSize-6), 10, accentMat);
        
        // Cristais raros
        addRareCrystals(bossRoom, bossRoomSize);
    }
    
    private void generateCaveRoom(Location center, int size, Material wallMat, Material floorMat) {
        // Criar forma irregular de caverna
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < 6; y++) {
                for (int z = 0; z < size; z++) {
                    double distance = Math.sqrt(
                        Math.pow(x - size/2.0, 2) + 
                        Math.pow(z - size/2.0, 2)
                    );
                    
                    Location blockLoc = center.clone().add(x, y, z);
                    
                    if (distance < size/2.0 + random.nextInt(3)) {
                        if (y == 0) {
                            blockLoc.getBlock().setType(floorMat);
                        } else if (y == 5) {
                            blockLoc.getBlock().setType(wallMat);
                        } else {
                            blockLoc.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
    
    private void addUndergroundDecorations(Location room, int roomSize) {
        // Estalactites e estalagmites
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(roomSize);
            int z = random.nextInt(roomSize);
            
            Location stalactiteLoc = room.clone().add(x, 5, z);
            stalactiteLoc.getBlock().setType(Material.POINTED_DRIPSTONE);
            
            Location stalagmiteLoc = room.clone().add(x, 0, z);
            stalagmiteLoc.getBlock().setType(Material.DRIPSTONE_BLOCK);
        }
        
        // Teias de aranha
        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(roomSize);
            int y = 1 + random.nextInt(3);
            int z = random.nextInt(roomSize);
            
            Location webLoc = room.clone().add(x, y, z);
            if (webLoc.getBlock().getType() == Material.AIR) {
                webLoc.getBlock().setType(Material.COBWEB);
            }
        }
        
        // Tochas de redstone
        for (int i = 2; i < roomSize; i += 4) {
            Location torchLoc = room.clone().add(i, 2, 1);
            torchLoc.getBlock().setType(Material.REDSTONE_TORCH);
        }
    }
    
    private void addOreDeposits(Location room, int roomSize) {
        Material[] ores = {Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, 
                          Material.DIAMOND_ORE, Material.EMERALD_ORE};
        
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(roomSize);
            int z = random.nextInt(roomSize);
            Material ore = ores[random.nextInt(ores.length)];
            
            Location oreLoc = room.clone().add(x, 0, z);
            if (oreLoc.getBlock().getType().isSolid()) {
                oreLoc.getBlock().setType(ore);
            }
        }
    }
    
    private void addMinecartTracks(Location room, int roomSize) {
        for (int i = 0; i < roomSize - 1; i++) {
            Location railLoc = room.clone().add(i, 0, roomSize/2);
            if (railLoc.getBlock().getType() == Material.AIR || !railLoc.getBlock().getType().isSolid()) {
                railLoc.clone().add(0, -1, 0).getBlock().setType(Material.COBBLED_DEEPSLATE);
                railLoc.getBlock().setType(Material.RAIL);
            }
        }
    }
    
    private void createLavaPool(Location center, int radius) {
        for (int x = 0; x < radius; x++) {
            for (int z = 0; z < radius; z++) {
                Location lavaLoc = center.clone().add(x, 0, z);
                lavaLoc.getBlock().setType(Material.LAVA);
            }
        }
    }
    
    private void createPlatform(Location center, int size, Material material) {
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                Location platLoc = center.clone().add(x, 0, z);
                platLoc.getBlock().setType(material);
            }
        }
    }
    
    private void addRareCrystals(Location room, int roomSize) {
        for (int i = 0; i < 3; i++) {
            int x = 5 + random.nextInt(roomSize - 10);
            int z = 5 + random.nextInt(roomSize - 10);
            
            Location crystalLoc = room.clone().add(x, 1, z);
            structureBuilder.buildPillar(crystalLoc, 3, Material.AMETHYST_BLOCK);
            crystalLoc.clone().add(0, 3, 0).getBlock().setType(Material.AMETHYST_CLUSTER);
        }
    }
}