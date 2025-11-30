package com.dungeon.plugin.commands;

import com.dungeon.plugin.DungeonPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class DungeonCommand implements CommandExecutor {
    
    private final DungeonPlugin plugin;
    
    public DungeonCommand(DungeonPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "create":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Apenas jogadores podem usar este comando!");
                    return true;
                }
                
                Player player = (Player) sender;
                Location loc = player.getLocation();
                
                sender.sendMessage(ChatColor.GREEN + "Gerando dungeon na sua localização...");
                plugin.getDungeonGenerator().generateDungeon(loc);
                sender.sendMessage(ChatColor.GREEN + "Dungeon gerada com sucesso!");
                break;
                
            case "list":
                Set<String> locations = plugin.getDungeonGenerator().getGeneratedLocations();
                
                if (locations.isEmpty()) {
                    sender.sendMessage(ChatColor.YELLOW + "Nenhuma dungeon foi gerada ainda.");
                } else {
                    sender.sendMessage(ChatColor.GOLD + "=== Dungeons Geradas ===");
                    sender.sendMessage(ChatColor.YELLOW + "Total: " + locations.size());
                    
                    int count = 0;
                    for (String loc_str : locations) {
                        sender.sendMessage(ChatColor.GRAY + "- " + loc_str);
                        count++;
                        if (count >= 10) {
                            sender.sendMessage(ChatColor.GRAY + "... e " + (locations.size() - 10) + " mais");
                            break;
                        }
                    }
                }
                break;
                
            case "reload":
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Configuração recarregada!");
                break;
                
            case "info":
                sender.sendMessage(ChatColor.GOLD + "===== DungeonPlugin Info =====");
                sender.sendMessage(ChatColor.YELLOW + "Versão: " + ChatColor.WHITE + "1.0.0");
                sender.sendMessage(ChatColor.YELLOW + "Dungeons Geradas: " + ChatColor.WHITE + 
                    plugin.getDungeonGenerator().getGeneratedLocations().size());
                sender.sendMessage(ChatColor.YELLOW + "Tipos: " + ChatColor.WHITE + "Medieval, Místico, Subterrâneo");
                sender.sendMessage(ChatColor.YELLOW + "Tamanhos: " + ChatColor.WHITE + "Médio, Grande");
                sender.sendMessage(ChatColor.YELLOW + "Frequência: " + ChatColor.WHITE + "Rara (1/700 chunks)");
                break;
                
            default:
                sendHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "===== Comandos DungeonPlugin =====");
        sender.sendMessage(ChatColor.YELLOW + "/dungeon create " + ChatColor.WHITE + "- Gera uma dungeon na sua localização");
        sender.sendMessage(ChatColor.YELLOW + "/dungeon list " + ChatColor.WHITE + "- Lista dungeons geradas");
        sender.sendMessage(ChatColor.YELLOW + "/dungeon info " + ChatColor.WHITE + "- Informações do plugin");
        sender.sendMessage(ChatColor.YELLOW + "/dungeon reload " + ChatColor.WHITE + "- Recarrega a configuração");
    }
}
