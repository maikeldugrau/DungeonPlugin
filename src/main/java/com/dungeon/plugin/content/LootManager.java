package com.dungeon.plugin.content;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

import java.util.Random;

public class LootManager {
    
    private final Random random;
    
    public LootManager() {
        this.random = new Random();
    }
    
    public void placeChest(Location loc, String lootType) {
        Block block = loc.getBlock();
        block.setType(Material.CHEST);
        
        if (block.getState() instanceof Chest chest) {
            Inventory inv = chest.getInventory();
            
            switch (lootType) {
                case "medieval":
                    addMedievalLoot(inv);
                    break;
                case "mystic":
                    addMysticLoot(inv);
                    break;
                case "mining":
                case "underground":
                    addMiningLoot(inv);
                    break;
                case "boss_reward":
                case "mystic_boss":
                case "underground_boss":
                    addBossLoot(inv);
                    break;
                default:
                    addBasicLoot(inv);
            }
            
            chest.update();
        }
    }
    
    private void addBasicLoot(Inventory inv) {
        inv.addItem(new ItemStack(Material.IRON_INGOT, 3 + random.nextInt(5)));
        inv.addItem(new ItemStack(Material.GOLD_INGOT, 1 + random.nextInt(3)));
        inv.addItem(new ItemStack(Material.BREAD, 5 + random.nextInt(10)));
        inv.addItem(new ItemStack(Material.ARROW, 16 + random.nextInt(32)));
        
        if (random.nextInt(3) == 0) {
            inv.addItem(new ItemStack(Material.DIAMOND, 1 + random.nextInt(3)));
        }
    }
    
    private void addMedievalLoot(Inventory inv) {
        // Armaduras e armas medievais
        inv.addItem(new ItemStack(Material.IRON_SWORD));
        inv.addItem(new ItemStack(Material.IRON_CHESTPLATE));
        inv.addItem(new ItemStack(Material.SHIELD));
        inv.addItem(new ItemStack(Material.CROSSBOW));
        inv.addItem(new ItemStack(Material.ARROW, 32));
        inv.addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
        inv.addItem(new ItemStack(Material.EMERALD, 5 + random.nextInt(10)));
        
        if (random.nextInt(2) == 0) {
            ItemStack enchantedSword = new ItemStack(Material.IRON_SWORD);
            enchantedSword.addEnchantment(Enchantment.SHARPNESS, 2);
            inv.addItem(enchantedSword);
        }
    }
    
    private void addMysticLoot(Inventory inv) {
        // Itens mágicos e raros
        inv.addItem(new ItemStack(Material.ENCHANTING_TABLE));
        inv.addItem(new ItemStack(Material.ENDER_PEARL, 3 + random.nextInt(5)));
        inv.addItem(new ItemStack(Material.LAPIS_LAZULI, 16 + random.nextInt(32)));
        inv.addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 10 + random.nextInt(20)));
        inv.addItem(new ItemStack(Material.ENCHANTED_BOOK));
        inv.addItem(new ItemStack(Material.AMETHYST_SHARD, 8 + random.nextInt(16)));
        inv.addItem(new ItemStack(Material.PRISMARINE_CRYSTALS, 5 + random.nextInt(10)));
        
        if (random.nextInt(3) == 0) {
            inv.addItem(new ItemStack(Material.DIAMOND, 3 + random.nextInt(5)));
        }
    }
    
    private void addMiningLoot(Inventory inv) {
        // Recursos de mineração
        inv.addItem(new ItemStack(Material.IRON_PICKAXE));
        inv.addItem(new ItemStack(Material.COAL, 16 + random.nextInt(32)));
        inv.addItem(new ItemStack(Material.IRON_INGOT, 8 + random.nextInt(16)));
        inv.addItem(new ItemStack(Material.GOLD_INGOT, 4 + random.nextInt(8)));
        inv.addItem(new ItemStack(Material.REDSTONE, 16 + random.nextInt(32)));
        inv.addItem(new ItemStack(Material.TORCH, 32));
        
        if (random.nextInt(4) == 0) {
            inv.addItem(new ItemStack(Material.DIAMOND, 2 + random.nextInt(4)));
        }
        
        if (random.nextInt(5) == 0) {
            inv.addItem(new ItemStack(Material.EMERALD, 1 + random.nextInt(3)));
        }
    }
    
    private void addBossLoot(Inventory inv) {
        // Recompensas épicas do boss
        ItemStack enchantedSword = new ItemStack(Material.DIAMOND_SWORD);
        enchantedSword.addEnchantment(Enchantment.SHARPNESS, 4);
        enchantedSword.addEnchantment(Enchantment.UNBREAKING, 3);
        inv.addItem(enchantedSword);
        
        ItemStack enchantedArmor = new ItemStack(Material.DIAMOND_CHESTPLATE);
        enchantedArmor.addEnchantment(Enchantment.PROTECTION, 3);
        enchantedArmor.addEnchantment(Enchantment.UNBREAKING, 3);
        inv.addItem(enchantedArmor);
        
        inv.addItem(new ItemStack(Material.DIAMOND, 10 + random.nextInt(15)));
        inv.addItem(new ItemStack(Material.EMERALD, 15 + random.nextInt(20)));
        inv.addItem(new ItemStack(Material.GOLDEN_APPLE, 5));
        inv.addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
        inv.addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 32));
        inv.addItem(new ItemStack(Material.TOTEM_OF_UNDYING));
        inv.addItem(new ItemStack(Material.NETHER_STAR));
        
        if (random.nextInt(2) == 0) {
            inv.addItem(new ItemStack(Material.NETHERITE_INGOT, 1 + random.nextInt(3)));
        }
    }
    
    public void placeSpawner(Location loc, String mobType) {
        Block block = loc.getBlock();
        block.setType(Material.SPAWNER);
        
        if (block.getState() instanceof CreatureSpawner spawner) {
            EntityType entityType = switch (mobType) {
                case "skeleton" -> EntityType.SKELETON;
                case "zombie" -> EntityType.ZOMBIE;
                case "spider" -> EntityType.SPIDER;
                case "enderman" -> EntityType.ENDERMAN;
                case "blaze" -> EntityType.BLAZE;
                default -> EntityType.ZOMBIE;
            };
            
            spawner.setSpawnedType(entityType);
            spawner.setDelay(100);
            spawner.setMinSpawnDelay(200);
            spawner.setMaxSpawnDelay(800);
            spawner.setSpawnCount(4);
            spawner.setMaxNearbyEntities(6);
            spawner.setRequiredPlayerRange(16);
            spawner.update();
        }
    }
}