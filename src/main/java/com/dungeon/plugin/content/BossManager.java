package com.dungeon.plugin.content;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class BossManager {
    
    private final Random random;
    
    public BossManager() {
        this.random = new Random();
    }
    
    public void spawnBoss(Location loc, String bossType) {
        switch (bossType) {
            case "medieval_knight":
                spawnMedievalKnight(loc);
                break;
            case "mystic_sorcerer":
                spawnMysticSorcerer(loc);
                break;
            case "cave_golem":
                spawnCaveGolem(loc);
                break;
            default:
                spawnGenericBoss(loc);
        }
    }
    
    private void spawnMedievalKnight(Location loc) {
        Zombie knight = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        
        // Configurar atributos
        knight.setCustomName("§c§lCavaleiro das Trevas");
        knight.setCustomNameVisible(true);
        knight.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200.0);
        knight.setHealth(200.0);
        knight.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10.0);
        knight.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        
        // Equipamento
        EntityEquipment equipment = knight.getEquipment();
        if (equipment != null) {
            ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
            sword.addEnchantment(Enchantment.SHARPNESS, 5);
            equipment.setItemInMainHand(sword);
            
            ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
            helmet.addEnchantment(Enchantment.PROTECTION, 4);
            equipment.setHelmet(helmet);
            
            ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
            chestplate.addEnchantment(Enchantment.PROTECTION, 4);
            equipment.setChestplate(chestplate);
            
            ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
            leggings.addEnchantment(Enchantment.PROTECTION, 4);
            equipment.setLeggings(leggings);
            
            ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
            boots.addEnchantment(Enchantment.PROTECTION, 4);
            equipment.setBoots(boots);
            
            equipment.setHelmetDropChance(0.1f);
            equipment.setChestplateDropChance(0.1f);
            equipment.setLeggingsDropChance(0.1f);
            equipment.setBootsDropChance(0.1f);
            equipment.setItemInMainHandDropChance(0.2f);
        }
        
        // Efeitos
        knight.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
        knight.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        
        // Guardiões
        for (int i = 0; i < 2; i++) {
            Location guardLoc = loc.clone().add(random.nextInt(5) - 2, 0, random.nextInt(5) - 2);
            Skeleton guard = (Skeleton) loc.getWorld().spawnEntity(guardLoc, EntityType.SKELETON);
            guard.setCustomName("§7Guardião");
            guard.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
            guard.setHealth(40.0);
        }
    }
    
    private void spawnMysticSorcerer(Location loc) {
        Witch sorcerer = (Witch) loc.getWorld().spawnEntity(loc, EntityType.WITCH);
        
        // Configurar atributos
        sorcerer.setCustomName("§5§lFeiticeiro Ancestre");
        sorcerer.setCustomNameVisible(true);
        sorcerer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(180.0);
        sorcerer.setHealth(180.0);
        sorcerer.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35);
        
        // Efeitos mágicos
        sorcerer.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
        sorcerer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        sorcerer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        
        // Invocar endermans como servos
        for (int i = 0; i < 3; i++) {
            Location servantLoc = loc.clone().add(random.nextInt(6) - 3, 0, random.nextInt(6) - 3);
            Enderman servant = (Enderman) loc.getWorld().spawnEntity(servantLoc, EntityType.ENDERMAN);
            servant.setCustomName("§5Servo Místico");
            servant.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50.0);
            servant.setHealth(50.0);
        }
        
        // Vexes voadores
        for (int i = 0; i < 4; i++) {
            Location vexLoc = loc.clone().add(random.nextInt(4) - 2, 2, random.nextInt(4) - 2);
            Vex vex = (Vex) loc.getWorld().spawnEntity(vexLoc, EntityType.VEX);
            vex.setCustomName("§dEspírito");
        }
    }
    
    private void spawnCaveGolem(Location loc) {
        IronGolem golem = (IronGolem) loc.getWorld().spawnEntity(loc, EntityType.IRON_GOLEM);
        
        // Configurar atributos
        golem.setCustomName("§8§lGolem das Cavernas");
        golem.setCustomNameVisible(true);
        golem.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(250.0);
        golem.setHealth(250.0);
        golem.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15.0);
        golem.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        
        // Efeitos
        golem.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
        golem.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        golem.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0));
        
        // Spawn de mobs de suporte
        for (int i = 0; i < 4; i++) {
            Location spiderLoc = loc.clone().add(random.nextInt(6) - 3, 0, random.nextInt(6) - 3);
            Spider spider = (Spider) loc.getWorld().spawnEntity(spiderLoc, EntityType.SPIDER);
            spider.setCustomName("§8Aranha da Caverna");
            spider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30.0);
            spider.setHealth(30.0);
            
            // Cave Spider montado
            if (i % 2 == 0) {
                Skeleton rider = (Skeleton) loc.getWorld().spawnEntity(spiderLoc, EntityType.SKELETON);
                spider.addPassenger(rider);
            }
        }
        
        // Zombies de mina
        for (int i = 0; i < 3; i++) {
            Location zombieLoc = loc.clone().add(random.nextInt(8) - 4, 0, random.nextInt(8) - 4);
            Zombie zombie = (Zombie) loc.getWorld().spawnEntity(zombieLoc, EntityType.ZOMBIE);
            zombie.setCustomName("§7Mineiro Zumbi");
            zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(35.0);
            zombie.setHealth(35.0);
            
            EntityEquipment eq = zombie.getEquipment();
            if (eq != null) {
                eq.setItemInMainHand(new ItemStack(Material.IRON_PICKAXE));
            }
        }
    }
    
    private void spawnGenericBoss(Location loc) {
        Zombie boss = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        boss.setCustomName("§c§lChefe da Dungeon");
        boss.setCustomNameVisible(true);
        boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(150.0);
        boss.setHealth(150.0);
        boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(8.0);
        
        EntityEquipment equipment = boss.getEquipment();
        if (equipment != null) {
            equipment.setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
            equipment.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            equipment.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        }
    }
}
