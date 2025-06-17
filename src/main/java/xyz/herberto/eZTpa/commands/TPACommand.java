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
import xyz.herberto.eZTpa.utils.TPAData;
import xyz.herberto.eZTpa.utils.TPAUtils;


public class TPACommand extends BaseCommand {

    @CommandAlias("tpa")
    @Description("Send a teleport request to another player")
    public void tpa(Player player, OnlinePlayer target) {

        if(TPAUtils.getRequests().values().stream().anyMatch(req -> req.getTarget().equals(target.getPlayer().getUniqueId()))) {
            player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpa.pending-request").replaceAll("<target>", target.getPlayer().getName())));
            return;
        }

        TPAData data = new TPAData(target.getPlayer().getUniqueId());
        TPAUtils.getRequests().put(player.getUniqueId(), data);
        player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpa.request-sent").replaceAll("<target>", target.getPlayer().getName())));
        target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpa.requested").replaceAll("<player>", player.getName()).replaceAll("\\n", "\n")));

    }

    @CommandAlias("tpaccept|tpaaccept")
    @Description("Accept a teleport request")
    public void accept(Player player, OnlinePlayer target) {
        if(TPAUtils.getRequests().values().stream().anyMatch(req -> req.getTarget().equals(player.getUniqueId()))) {
            Location location = target.getPlayer().getLocation();
            target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.teleporting").replaceAll("<target>", player.getName())));

            player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.accepted").replaceAll("<player>", target.getPlayer().getName() + (target.getPlayer().getName().endsWith("s") ? "'" : "'s"))));

            new BukkitRunnable() {
                int waited = 0;

                @Override
                public void run() {
                    if(target.getPlayer().getLocation().distance(location) > 0.7) {
                        target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.cancelled.movement")));
                        player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.cancelled.cancelled-player").replaceAll("<player>", target.getPlayer().getName())));
                        TPAUtils.getRequests().remove(target.getPlayer().getUniqueId());
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
                        target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.teleported").replaceAll("<target>", player.getName())));
                        player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.teleported-player").replaceAll("<player>", target.getPlayer().getName())));
                        TPAUtils.getRequests().remove(target.getPlayer().getUniqueId());
                        cancel();
                    }
                }
            }.runTaskTimer(EZTpa.getInstance(), 0, 20);

        } else {
            player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.no-requests").replaceAll("<target>", target.getPlayer().getName())));
        }
    }

    @Subcommand("tpdeny|tpadeny")
    @Description("Deny a teleport request")
    public void deny(Player player, OnlinePlayer target) {
        if(TPAUtils.getRequests().values().stream().anyMatch(req -> req.getTarget().equals(player.getUniqueId()))) {
            TPAUtils.getRequests().remove(target.getPlayer().getUniqueId());

            player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpdeny.denied-player").replaceAll("<player>", target.getPlayer().getName())));
            target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpdeny.denied").replaceAll("<target>", player.getName())));
        } else {
            player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.tpaccept.no-requests").replaceAll("<target>", target.getPlayer().getName())));
        }
    }

    @Subcommand("tpcancel|tpacancel")
    @Description("Cancel a teleport request")
    public void cancel(Player player, OnlinePlayer target) {

        if (TPAUtils.getRequests().values().stream().anyMatch(req -> req.getTarget().equals(player.getUniqueId()))) {
            TPAUtils.getRequests().remove(target.getPlayer().getUniqueId());

            player.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.cancelled.cancelled").replaceAll("<target>", target.getPlayer().getName())));
            target.getPlayer().sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.cancelled.cancelled-player").replaceAll("<player>", target.getPlayer().getName())));

        }
    }


}
