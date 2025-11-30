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
import org.bukkit.Particle;

import java.util.Random;

public class MysticDungeon {
    
    private final DungeonPlugin plugin;
    private final Random random;
    private final RoomGenerator roomGen;
    private final StructureBuilder structureBuilder;
    private final LootManager lootManager;
    private final TrapManager trapManager;
    private final PuzzleManager puzzleManager;
    private final BossManager bossManager;
    
    public MysticDungeon(DungeonPlugin plugin) {
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
        int roomCount = isLarge ? 8 : 5;
        int roomSize = isLarge ? 22 : 16;
        
        Material wallMat = Material.DARK_PRISMARINE;
        Material floorMat = Material.PRISMARINE;
        Material accentMat = Material.PRISMARINE_BRICKS;
        
        // Sala de entrada mística
        Location entranceRoom = start.clone();
        roomGen.generateRoom(entranceRoom, roomSize, 7, roomSize, wallMat, floorMat);
        addMysticDecorations(entranceRoom, roomSize);
        
        // Torre central (se grande)
        if (isLarge) {
            Location towerLoc = entranceRoom.clone().add(roomSize/2, 0, roomSize/2);
            structureBuilder.buildTower(towerLoc, 12, 3, Material.OBSIDIAN, Material.CRYING_OBSIDIAN);
        }
        
        // Salas em círculo ao redor da central
        double angleStep = (2 * Math.PI) / (roomCount - 1);
        int radius = roomSize + 8;
        
        for (int i = 0; i < roomCount - 1; i++) {
            double angle = i * angleStep;
            int offsetX = (int) (Math.cos(angle) * radius);
            int offsetZ = (int) (Math.sin(angle) * radius);
            
            Location roomLoc = entranceRoom.clone().add(offsetX, 0, offsetZ);
            roomGen.generateRoom(roomLoc, roomSize - 4, 6, roomSize - 4, wallMat, floorMat);
            
            // Adicionar conteúdo místico
            if (i % 3 == 0) {
                lootManager.placeChest(roomLoc.clone().add(roomSize/2, 0, roomSize/2), "mystic");
                puzzleManager.createPuzzle(roomLoc.clone().add(3, 0, 3), "button");
            } else if (i % 3 == 1) {
                lootManager.placeSpawner(roomLoc.clone().add(roomSize/2, 0, roomSize/2), "enderman");
                trapManager.placeTrap(roomLoc.clone().add(2, 0, 2), "magic");
            } else {
                structureBuilder.buildAltar(roomLoc.clone().add(roomSize/2, 0, roomSize/2), 
                    Material.OBSIDIAN, Material.ENCHANTING_TABLE);
            }
            
            addMysticDecorations(roomLoc, roomSize - 4);
            
            // Corredor até o centro
            Location corridorStart = entranceRoom.clone().add(roomSize/2, 0, roomSize/2);
            Location corridorEnd = roomLoc.clone().add((roomSize-4)/2, 0, (roomSize-4)/2);
            roomGen.createCorridor(corridorStart, corridorEnd, 3, wallMat, floorMat);
        }
        
        // Sala do Boss (acima)
        Location bossRoom = entranceRoom.clone().add(0, 15, 0);
        int bossRoomSize = isLarge ? 28 : 20;
        roomGen.generateRoom(bossRoom, bossRoomSize, 10, bossRoomSize, Material.OBSIDIAN, Material.CRYING_OBSIDIAN);
        
        // Escada para sala do boss
        Location stairStart = entranceRoom.clone().add(roomSize/2, 1, 1);
        structureBuilder.buildStairs(stairStart, 15, Material.PRISMARINE_STAIRS, true);
        
        // Altar central na sala do boss
        structureBuilder.buildAltar(bossRoom.clone().add(bossRoomSize/2, 0, bossRoomSize/2), 
            Material.OBSIDIAN, Material.BEACON);
        
        // Spawnar boss místico
        bossManager.spawnBoss(bossRoom.clone().add(bossRoomSize/2, 2, bossRoomSize/2), "mystic_sorcerer");
        
        // Baús de recompensa mística
        lootManager.placeChest(bossRoom.clone().add(bossRoomSize/2 + 4, 0, bossRoomSize/2), "mystic_boss");
        lootManager.placeChest(bossRoom.clone().add(bossRoomSize/2 - 4, 0, bossRoomSize/2), "mystic_boss");
        lootManager.placeChest(bossRoom.clone().add(bossRoomSize/2, 0, bossRoomSize/2 + 4), "mystic_boss");
    }
    
    private void addMysticDecorations(Location room, int roomSize) {
        // Lanternas do mar para iluminação mística
        for (int i = 3; i < roomSize; i += 4) {
            Location lanternLoc = room.clone().add(i, 3, 2);
            lanternLoc.getBlock().setType(Material.SEA_LANTERN);
            
            lanternLoc = room.clone().add(2, 3, i);
            lanternLoc.getBlock().setType(Material.SEA_LANTERN);
        }
        
        // Cristais de ametista aleatórios
        for (int i = 0; i < 3; i++) {
            int x = random.nextInt(roomSize);
            int z = random.nextInt(roomSize);
            Location crystalLoc = room.clone().add(x, 1, z);
            crystalLoc.getBlock().setType(Material.AMETHYST_CLUSTER);
        }
        
        // Pilares místicos
        if (random.nextBoolean()) {
            structureBuilder.buildPillar(room.clone().add(roomSize/3, 0, roomSize/3), 6, Material.PURPUR_PILLAR);
            structureBuilder.buildPillar(room.clone().add(2*roomSize/3, 0, 2*roomSize/3), 6, Material.PURPUR_PILLAR);
        }
        
        // Adicionar alguns blocos de End Stone para efeito
        for (int i = 0; i < 4; i++) {
            int x = random.nextInt(roomSize);
            int z = random.nextInt(roomSize);
            Location endLoc = room.clone().add(x, 0, z);
            if (endLoc.getBlock().getType() == Material.PRISMARINE) {
                endLoc.getBlock().setType(Material.END_STONE);
            }
        }
    }
}