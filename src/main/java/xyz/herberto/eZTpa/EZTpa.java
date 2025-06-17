package xyz.herberto.eZTpa;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.herberto.eZTpa.commands.TPACommand;

import java.util.Arrays;

public final class EZTpa extends JavaPlugin {
    @Getter
    private static EZTpa instance;


    @Override
    public void onEnable() {
        instance = this;

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("help");

        Arrays.asList(
                new TPACommand()
        ).forEach(manager::registerCommand);

    }

}
