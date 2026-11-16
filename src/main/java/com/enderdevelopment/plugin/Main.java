package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Main extends JavaPlugin implements Listener, CommandExecutor {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        if (getCommand("strength") != null) {
            getCommand("strength").setExecutor(this);
        }
        if (getCommand("retract") != null) {
            getCommand("retract").setExecutor(this);
        }
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPotionConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item.getType() != Material.POTION || !(item.getItemMeta() instanceof PotionMeta potionMeta)) {
            return;
        }

        boolean containsStrength = potionMeta.getCustomEffects().stream()
                .anyMatch(effect -> effect.getType().equals(PotionEffectType.STRENGTH));
        if (containsStrength) {
            event.getPlayer().sendMessage(Component.text("Strength potion consumed.", NamedTextColor.GREEN));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        if (command.getName().equalsIgnoreCase("strength")) {
            PotionEffect effect = player.getPotionEffect(PotionEffectType.STRENGTH);
            if (effect == null) {
                player.sendMessage(Component.text("You do not have an active Strength effect.", NamedTextColor.YELLOW));
            } else {
                int seconds = Math.max(0, effect.getDuration() / 20);
                player.sendMessage(Component.text("Strength " + (effect.getAmplifier() + 1)
                        + " is active for " + seconds + " seconds.", NamedTextColor.GREEN));
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("retract")) {
            if (player.hasPotionEffect(PotionEffectType.STRENGTH)) {
                player.removePotionEffect(PotionEffectType.STRENGTH);
                player.sendMessage(Component.text("Your Strength effect was retracted.", NamedTextColor.GREEN));
            } else {
                player.sendMessage(Component.text("You do not have an active Strength effect to retract.", NamedTextColor.YELLOW));
            }
            return true;
        }

        return false;
    }
}
