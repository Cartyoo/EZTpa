package xyz.herberto.eZTpa.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.herberto.eZTpa.EZTpa;
import xyz.herberto.eZTpa.utils.CC;

import java.util.HashMap;
import java.util.UUID;

public class TPACommand extends BaseCommand {
    private HashMap<UUID, UUID> requests = new HashMap<>();

    @CommandAlias("tpa")
    @Description("Teleport to another player")
    public void tpa(Player player, OnlinePlayer target) {

        if(requests.containsValue(target.getPlayer().getUniqueId())) {
            player.sendMessage(CC.translate("&f" + target.getPlayer().getName() + " &calready has a pending teleport request."));
            return;
        }

        requests.put(player.getUniqueId(), target.getPlayer().getUniqueId());
        player.sendMessage(CC.translate("&aYou have requested to teleport to &f" + target.getPlayer().getName()));
        target.getPlayer().sendMessage(CC.translate("&f" + player.getName() + " &ahas requested to teleport to you.\n&aYou can accept it with /tpaccept " + player.getName()));

    }

    @CommandAlias("tpaccept|tpaaccept")
    @Description("Accept a teleport request")
    public void accept(Player player, OnlinePlayer target) {
        if(requests.containsValue(player.getUniqueId())) {
            Location location = player.getLocation();
            player.sendMessage(CC.translate("&aTeleporting to &f" + target.getPlayer().getName() + ". &2Please do not move!"));

            new BukkitRunnable() {
                int waited = 0;

                @Override
                public void run() {
                    if(player.getLocation().distance(location) > 0.7) {
                        player.sendMessage(CC.translate("&cTeleport request cancelled due to movement."));
                        requests.remove(target.getPlayer().getUniqueId());
                        cancel();
                        return;
                    }
                    waited += 20;

                    if (waited % 20 == 0 && waited <= 80) {
                        int secondsLeft = 5 - (waited / 20);
                        player.sendMessage(CC.translate("&2" + secondsLeft + "..."));
                    }


                    if(waited >= 100) {
                        player.teleport(target.getPlayer());
                        player.sendMessage(CC.translate("&aYou have teleported to &f" + target.getPlayer().getName()));
                        requests.remove(target.getPlayer().getUniqueId());
                        cancel();
                    }
                }
            }.runTaskTimer(EZTpa.getInstance(), 0, 20);

        } else {
            player.sendMessage(CC.translate("&cYou do not have a pending teleport request for &f" + target.getPlayer().getName()));
        }
    }

}
