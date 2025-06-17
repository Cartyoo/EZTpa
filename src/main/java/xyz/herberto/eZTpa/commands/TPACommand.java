package xyz.herberto.eZTpa.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.herberto.eZTpa.EZTpa;
import xyz.herberto.eZTpa.utils.CC;

import java.util.HashMap;
import java.util.UUID;

public class TPACommand extends BaseCommand {
    private final HashMap<UUID, UUID> requests = new HashMap<>();

    @CommandAlias("tpa")
    @Description("Teleport to another player")
    public void tpa(Player player, OnlinePlayer target) {

        if(requests.containsValue(target.getPlayer().getUniqueId())) {
            player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpa.pending-request").replaceAll("<target>", target.getPlayer().getName())));
            return;
        }

        requests.put(player.getUniqueId(), target.getPlayer().getUniqueId());
        player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpa.request-sent").replaceAll("<target>", target.getPlayer().getName())));
        target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpa.requested").replaceAll("<player>", player.getName())));

    }

    @CommandAlias("tpaccept|tpaaccept")
    @Description("Accept a teleport request")
    public void accept(Player player, OnlinePlayer target) {
        if(requests.containsValue(player.getUniqueId())) {
            Location location = target.getPlayer().getLocation();
            target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaaccept.teleporting").replaceAll("<target>", target.getPlayer().getName())));

            new BukkitRunnable() {
                int waited = 0;

                @Override
                public void run() {
                    if(target.getPlayer().getLocation().distance(location) > 0.7) {
                        target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaaccept.cancelled-movement")));
                        requests.remove(target.getPlayer().getUniqueId());
                        cancel();
                        return;
                    }
                    waited += 20;

                    if (waited % 20 == 0 && waited <= 80) {
                        int secondsLeft = 5 - (waited / 20);
                        target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.timer").replaceAll("<time-remaining>", String.valueOf(secondsLeft))));
                    }


                    if(waited >= 100) {
                        target.getPlayer().teleport(player.getLocation());
                        target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.teleported").replaceAll("<player>", player.getName())));
                        player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.teleported-player").replaceAll("<player>", target.getPlayer().getName())));
                        requests.remove(target.getPlayer().getUniqueId());
                        cancel();
                    }
                }
            }.runTaskTimer(EZTpa.getInstance(), 0, 20);

        } else {
            player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.no-requests")));
        }
    }

}
