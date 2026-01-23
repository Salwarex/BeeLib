package ru.waxera.beeLib.utils.command;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.message.Message;
import ru.waxera.beeLib.utils.player.PlayerData;
import ru.waxera.beeLib.utils.player.PlayerPool;

import java.util.List;
import java.util.UUID;

public final class BeeLibCommand extends AbstractCommand{
    public BeeLibCommand() {super(BeeLib.getInstance(), "beelib", "bl");}

    @Override
    public void execute(CommandSender sender, String s, String[] args){
        if(!sender.hasPermission("beelib.command")){
            Message.send(null, sender, "&cYou can't do it!");
            return;
        }

        if(args.length == 0){
            Message.send(null, sender, "&cPlease, write the arguments!");
            return;
        }

        if(args[0].equalsIgnoreCase("perms")){
            if(args.length < 4) { Message.send(null, sender, "&cThis commands requires 4 arguments!"); return; }
            String action = args[1];
            String playerName = args[2];
            OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
            String permission = args[3];

            if(player == null) { Message.send(null, sender, "&cPlayer doesn't cached!"); return; }
            UUID uuid = player.getUniqueId();
            PlayerData playerData = PlayerPool.getInstance().get(uuid);
            if(playerData == null) { Message.send(null, sender, "&cPlayer data doesn't cached!"); return; }
            switch (action){
                case "add" -> { playerData.addPermission(permission); Message.send(null, sender,
                        "&aPermission added to cached player data!"); }
                case "remove" -> { playerData.removePermission(permission); Message.send(null, sender,
                        "&aPermission removed from cached player data!"); }
                default -> Message.send(null, sender, "&cUnavailable action!");
            }
            return;
        }

        Message.send(null, sender, "&cUnknown command!");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args){
        if(args.length == 1 && sender.hasPermission("beelib.command")) return Lists.newArrayList("perms");
        if(args.length == 2 && args[0].equalsIgnoreCase("perms")
                && sender.hasPermission("beelib.command")) return Lists.newArrayList("add", "remove");
        if(args.length == 3 && args[0].equalsIgnoreCase("perms")
                && sender.hasPermission("beelib.command")) return PlayerPool.getInstance().playerNames();
        if(args.length == 4 && args[0].equalsIgnoreCase("perms")
                && sender.hasPermission("beelib.command")) return Lists.newArrayList("%permission%");
        return Lists.newArrayList();
    }
}
