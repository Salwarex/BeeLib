package ru.waxera.beeLib.utils.gui.questionnaire;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.gui.Action;
import ru.waxera.beeLib.utils.message.Message;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementation of a user interface (UI) for interaction
 * between server players and a BeeLib-dependent plugin using chat-system.
 *
 * @version 1
 * @since v1.2
 * @author Salwarex
 */

public class Questionnaire {
    private Player player;
    private HashMap<String, Question> questions = new HashMap<>();
    private Action action;
    private final Plugin plugin;
    private int actualQuestion = 0;
    private Sound questionSound = null;
    private String stopWord;

    public Questionnaire(Plugin plugin,
                         Player player,
                         Action action,
                         Sound questionSound,
                         String stopWord,
                         Question ... questions){
        this.plugin = plugin;
        this.player = player;
        this.action = action;
        this.questionSound = questionSound;
        this.stopWord = stopWord;
        for(Question question : questions){
            this.questions.put(question.getVariable(), question);
        }
        sendQuestion();
    }

    public boolean isOver(){
        return actualQuestion >= questions.keySet().size();
    }

    private Question getActualQuestion(){
        if(isOver()) { Message.error(BeeLib.getInstance(),
                "Questionnaire error: The questions are over!"); return null; }
        ArrayList<String> keys = new ArrayList<>(questions.keySet());
        String now_key = keys.get(actualQuestion);
        return this.questions.get(now_key);
    }

    private void sendQuestion(){
        if(!QuestionnairePool.getInstance().contains(player)){
            Message.send(this.plugin, player, "@qsnr-announce@");
            QuestionnairePool.getInstance().add(player, this);
        }
        if(isOver()){
            endQuestionnaire(false);
            return;
        }
        if(questionSound != null) player.playSound(player.getLocation(), questionSound, 1, 1);
        Question question = getActualQuestion();
        Message.send(BeeLib.getInstance(), player, question.getQuestion());
    }

    public void endQuestionnaire(boolean force){
        if(!force) action.run(player, null);
        QuestionnairePool.getInstance().remove(player);
    }

    public void setAnswer(String answer){
        if(stopWord != null){
            if(answer.equalsIgnoreCase(stopWord)) {
                Message.send(this.plugin, player, "@qsnr-stop-word@");
                endQuestionnaire(true); return;
            }
        }
        Message.send(this.plugin, player, "@qsnr-your-answer@: " + answer);
        Question question = getActualQuestion();
        if(question == null){
            endQuestionnaire(false);
            return;
        }
        question.setAnswer(answer);
        actualQuestion += 1;
        sendQuestion();
    }

    public String getAnswer(String variable){
        return questions.get(variable).getAnswer();
    }
}
