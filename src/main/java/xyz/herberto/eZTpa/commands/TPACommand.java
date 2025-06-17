package xyz.herberto.eZTpa.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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

    @Subcommand("accept")
    @Description("Accept a teleport request")
    public void accept(Player player, OnlinePlayer target) {
        if(requests.containsValue(player.getUniqueId())) {
            Location location = player.getLocation();
            player.sendMessage(CC.translate("&aTeleporting to &f" + target.getPlayer().getName() + ". &2Please do not move!"));

            EZTpa.getInstance().getServer().getScheduler().runTaskTimer(EZTpa.getInstance(), () -> {
                if(player.getLocation().distance(location) > 0.7) {
                    player.sendMessage(CC.translate("&cTeleport request cancelled due to movement."));
                    return;
                }
            }, 20L, 20L);
        }
    }

}
