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

public class MedievalDungeon {
    
    private final DungeonPlugin plugin;
    private final Random random;
    private final RoomGenerator roomGen;
    private final StructureBuilder structureBuilder;
    private final LootManager lootManager;
    private final TrapManager trapManager;
    private final PuzzleManager puzzleManager;
    private final BossManager bossManager;
    
    public MedievalDungeon(DungeonPlugin plugin) {
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
        int roomCount = isLarge ? 7 : 4;
        int roomSize = isLarge ? 20 : 15;
        
        Material wallMat = Material.STONE_BRICKS;
        Material floorMat = Material.SMOOTH_STONE;
        Material accentMat = Material.MOSSY_STONE_BRICKS;
        
        // Sala de entrada
        Location entranceRoom = start.clone();
        roomGen.generateRoom(entranceRoom, roomSize, 6, roomSize, wallMat, floorMat);
        addMedievalDecorations(entranceRoom, roomSize);
        
        // Salas conectadas
        Location currentLoc = entranceRoom.clone().add(roomSize + 5, 0, 0);
        
        for (int i = 0; i < roomCount - 2; i++) {
            roomGen.generateRoom(currentLoc, roomSize - 3, 5, roomSize - 3, wallMat, floorMat);
            
            // Adicionar conteúdo
            if (i % 2 == 0) {
                lootManager.placeChest(currentLoc.clone().add(roomSize/2, 0, roomSize/2), "medieval");
                trapManager.placeTrap(currentLoc.clone().add(3, 0, 3), "arrow");
            } else {
                trapManager.placeTrap(currentLoc.clone().add(roomSize/2, 0, roomSize/2), "tnt");
                lootManager.placeSpawner(currentLoc.clone().add(2, 0, 2), "skeleton");
            }
            
            addMedievalDecorations(currentLoc, roomSize - 3);
            
            // Corredor para próxima sala
            Location corridorStart = currentLoc.clone().add(roomSize - 3, 0, roomSize/2);
            Location corridorEnd = corridorStart.clone().add(5, 0, 0);
            roomGen.createCorridor(corridorStart, corridorEnd, 3, wallMat, floorMat);
            
            currentLoc.add(roomSize + 2, 0, 0);
        }
        
        // Sala do Boss (final)
        Location bossRoom = currentLoc.clone();
        int bossRoomSize = isLarge ? 25 : 18;
        roomGen.generateRoom(bossRoom, bossRoomSize, 8, bossRoomSize, wallMat, floorMat);
        
        // Adicionar pilares decorativos
        structureBuilder.buildPillar(bossRoom.clone().add(3, 0, 3), 7, accentMat);
        structureBuilder.buildPillar(bossRoom.clone().add(bossRoomSize-4, 0, 3), 7, accentMat);
        structureBuilder.buildPillar(bossRoom.clone().add(3, 0, bossRoomSize-4), 7, accentMat);
        structureBuilder.buildPillar(bossRoom.clone().add(bossRoomSize-4, 0, bossRoomSize-4), 7, accentMat);
        
        // Spawnar boss
        bossManager.spawnBoss(bossRoom.clone().add(bossRoomSize/2, 0, bossRoomSize/2), "medieval_knight");
        
        // Baús de recompensa
        lootManager.placeChest(bossRoom.clone().add(bossRoomSize/2 + 3, 0, bossRoomSize/2), "boss_reward");
        lootManager.placeChest(bossRoom.clone().add(bossRoomSize/2 - 3, 0, bossRoomSize/2), "boss_reward");
        
        // Puzzle opcional
        if (isLarge) {
            puzzleManager.createPuzzle(bossRoom.clone().add(bossRoomSize/2, 0, 3), "lever");
        }
    }
    
    private void addMedievalDecorations(Location room, int roomSize) {
        // Tochas nas paredes
        for (int i = 2; i < roomSize; i += 3) {
            if (random.nextBoolean()) {
                Location torchLoc = room.clone().add(1, 2, i);
                torchLoc.getBlock().setType(Material.WALL_TORCH);
            }
        }
        
        // Alguns blocos musgosos aleatórios
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(roomSize);
            int z = random.nextInt(roomSize);
            Location mossLoc = room.clone().add(x, 0, z);
            if (mossLoc.getBlock().getType() == Material.STONE_BRICKS) {
                mossLoc.getBlock().setType(Material.MOSSY_STONE_BRICKS);
            }
        }
        
        // Pilares decorativos ocasionais
        if (random.nextInt(3) == 0) {
            structureBuilder.buildPillar(room.clone().add(roomSize/4, 0, roomSize/4), 5, Material.CHISELED_STONE_BRICKS);
        }
    }
}
