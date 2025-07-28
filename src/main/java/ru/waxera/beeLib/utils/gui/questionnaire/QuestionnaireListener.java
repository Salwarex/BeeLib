package ru.waxera.beeLib.utils.gui.questionnaire;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class QuestionnaireListener implements Listener {
    @EventHandler
    public void onChat(PlayerChatEvent e){
        Player player = e.getPlayer();
        if(QuestionnairePool.getInstance().contains(player)){
            Questionnaire questionnaire = QuestionnairePool.getInstance().get(player);
            if(questionnaire.isOver()) {questionnaire.endQuestionnaire(false); return;}
            e.setCancelled(true);
            String answer = e.getMessage();
            questionnaire.setAnswer(answer);
        }
    }
}
