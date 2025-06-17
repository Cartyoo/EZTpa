package xyz.herberto.eZTpa.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import xyz.herberto.eZTpa.EZTpa;
import xyz.herberto.eZTpa.utils.CC;

@CommandAlias("eztpa")
public class EZTpaCommand extends BaseCommand {

    @HelpCommand
    public void help(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("reload")
    @Description("Reload the configuration file")
    @CommandPermission("eztpa.command.reload")
    public void reload(CommandSender sender) {
        EZTpa.getInstance().reloadConfig();
        sender.sendMessage(CC.translate(EZTpa.getInstance().getConfig().getString("messages.eztpa.reloaded")));
    }

}
