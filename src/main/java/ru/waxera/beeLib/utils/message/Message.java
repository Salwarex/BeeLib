package ru.waxera.beeLib.utils.message;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.waxera.beeLib.utils.StringUtils;

import java.util.ArrayList;

public class Message {
    public static void send(MessageType type, Player player, String... message) {
        switch (type) {
            case DEFAULT -> {
                for (String mes : message) {
                    send(player, mes);
                }
            }
            case BROADCAST -> {
                ArrayList<Player> playersArray = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (String mes : message) {
                    send(playersArray, mes);
                }
            }
            case TITLE -> sendTitle(player, message[0], message[1]);
            case TITLE_BROADCAST -> {
                ArrayList<Player> playersArray = new ArrayList<>(Bukkit.getOnlinePlayers());
                sendTitle(playersArray, message[0], message[1]);
            }
            case ACTION_BAR -> {
                for (String mes : message) {
                    sendActionBar(player, mes);
                }
            }
            case ACTION_BAR_BROADCAST -> {
                ArrayList<Player> playersArray = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (String mes : message) {
                    sendActionBar(playersArray, mes);
                }
            }
            case LOGGING -> {
                for (String mes : message) {
                    send(mes);
                }
            }
        }
    }

    public static void send(MessageType type, CommandSender sender, String... message) {
        switch (type) {
            case DEFAULT -> {
                for (String mes : message) {
                    send(sender, mes);
                }
            }
            case BROADCAST -> {
                ArrayList<Player> playersArray = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (String mes : message) {
                    send(playersArray, mes);
                }
            }
            case TITLE -> sendTitle(sender, message[0], message[1]);
            case TITLE_BROADCAST -> {
                ArrayList<Player> playersArray = new ArrayList<>(Bukkit.getOnlinePlayers());
                sendTitle(playersArray, message[0], message[1]);
            }
            case ACTION_BAR -> {
                for (String mes : message) {
                    sendActionBar(sender, mes);
                }
            }
            case ACTION_BAR_BROADCAST -> {
                ArrayList<Player> playersArray = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (String mes : message) {
                    sendActionBar(playersArray, mes);
                }
            }
            case LOGGING -> {
                for (String mes : message) {
                    send(mes);
                }
            }
        }
    }

    //default messages
    public static void send(CommandSender sender, String message) {
        sender.sendMessage(StringUtils.format(message));
    }

    public static void send(Player player, String message) {
        player.sendMessage(StringUtils.format(message));
    }

    public static void send(ArrayList<Player> receivers, String message) {
        for (Player player : receivers) {
            player.sendMessage(StringUtils.format(message));
        }
    }

    //title messages
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (title == null) {
            title = "";
        }
        if (subtitle == null) {
            subtitle = "";
        }

        player.sendTitle(StringUtils.format(title), StringUtils.format(subtitle), fadeIn, stay, fadeOut);
    }

    private static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 10, 100, 10);
    }

    public static void sendTitle(CommandSender sender, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (player != null) {
            sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    private static void sendTitle(CommandSender sender, String title, String subtitle) {
        sendTitle(sender, title, subtitle, 10, 100, 10);
    }

    public static void sendTitle(ArrayList<Player> receivers, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : receivers) {
            sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    private static void sendTitle(ArrayList<Player> receivers, String title, String subtitle) {
        sendTitle(receivers, title, subtitle, 10, 100, 10);
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(StringUtils.format(message)));
    }

    public static void sendActionBar(CommandSender sender, String message) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (player != null) {
            sendActionBar(player, message);
        }
    }

    public static void sendActionBar(ArrayList<Player> receivers, String message) {
        for (Player player : receivers) {
            sendActionBar(player, message);
        }
    }

    //console logging
    public static void send(String message) {
        System.out.println(StringUtils.format(message));
    }

    public static void error(String message) {
        System.err.println(StringUtils.format(message));
    }
}
