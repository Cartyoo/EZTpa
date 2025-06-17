package xyz.herberto.eZTpa;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

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

        ).forEach(manager::registerCommand);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
