package ru.waxera.beeLib.utils.gui.questionnaire;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.HashMap;

public class QuestionnaireHandler implements Listener {
    private static HashMap<Player, Questionnaire> list = new HashMap<>();

    @EventHandler
    public void onChat(PlayerChatEvent e){
        Player player = e.getPlayer();
        if(contains(player)){
            Questionnaire questionnaire = list.get(player);
            if(questionnaire.isOver()) {questionnaire.endQuestionnaire(false); return;}
            e.setCancelled(true);
            String answer = e.getMessage();
            questionnaire.setAnswer(answer);
        }
    }

    public static void add(Player player, Questionnaire questionnaire){
        list.put(player, questionnaire);
    }
    public static void remove(Player player){
        list.remove(player);
    }
    public static boolean contains(Player player){
        return list.containsKey(player);
    }
}
