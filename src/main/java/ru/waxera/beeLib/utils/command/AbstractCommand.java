package ru.waxera.beeLib.utils.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import ru.waxera.beeLib.BeeLib;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
    public AbstractCommand(JavaPlugin plugin, String command, String ... aliases) {
        plugin = plugin == null ? BeeLib.getInstance() : plugin;
        PluginCommand pluginCommand = plugin.getCommand(command);
        if(pluginCommand != null){
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);

            if(aliases.length != 0){
                registerAliases(pluginCommand, aliases);
            }
        }
    }

    public abstract void execute(CommandSender sender, String label, String[] args);

    public List<String> complete(CommandSender sender, String[] args){
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        execute(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return filter(complete(sender,args), args);
    }

    private List<String> filter(List<String> list, String[] args){
        if(list == null) return null;
        String last = args[args.length - 1];
        List<String> result = new ArrayList<>();
        for(String arg : list){
            if(arg.toLowerCase().startsWith(last.toLowerCase())) result.add(arg);
        }
        return  result;
    }

    private void registerAliases(PluginCommand mainCommand, String[] aliases) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());

            for (String alias : aliases) {
                Command aliasCommand = new Command(alias, mainCommand.getDescription(), mainCommand.getUsage(), mainCommand.getAliases()) {
                    @Override
                    public boolean execute(CommandSender sender, String label, String[] args) {
                        return mainCommand.execute(sender, label, args);
                    }

                    @Override
                    public List<String> tabComplete(CommandSender sender, String label, String[] args) throws IllegalArgumentException {
                        return mainCommand.tabComplete(sender, label, args);
                    }
                };
                commandMap.register(alias, aliasCommand);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
