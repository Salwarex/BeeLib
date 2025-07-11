package ru.waxera.beeLib.utils.interfaces.questionnaire;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.interfaces.Action;
import ru.waxera.beeLib.utils.message.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class Questionnaire {
    private Player player;
    private HashMap<String, Question> questions = new HashMap<>();
    private Action action;
    private final Plugin plugin;
    private int actual_question = 0;
    private Sound question_sound = null;
    private String stop_word;

    public Questionnaire(Plugin plugin,
                         Player player,
                         Action action,
                         Sound question_sound,
                         String stop_word,
                         Question ... questions){
        this.plugin = plugin;
        this.player = player;
        this.action = action;
        this.question_sound = question_sound;
        this.stop_word = stop_word;
        for(Question question : questions){
            this.questions.put(question.getVariable(), question);
        }
        sendQuestion();
    }

    public boolean isOver(){
        return actual_question >= questions.keySet().size();
    }

    private Question getActualQuestion(){
        if(isOver()) { Message.error(BeeLib.getInstance(),
                "Questionnaire error: The questions are over!"); return null; }
        ArrayList<String> keys = new ArrayList<>(questions.keySet());
        String now_key = keys.get(actual_question);
        return this.questions.get(now_key);
    }

    private void sendQuestion(){
        if(!QuestionnaireHandler.contains(player)){
            Message.send(this.plugin, player, "@qsnr-announce@");
            QuestionnaireHandler.add(player, this);
        }
        if(isOver()){
            endQuestionnaire(false);
            return;
        }
        if(question_sound != null) player.playSound(player.getLocation(), question_sound, 1, 1);
        Question question = getActualQuestion();
        Message.send(BeeLib.getInstance(), player, question.getQuestion());
    }

    public void endQuestionnaire(boolean force){
        if(!force) action.run(player, null);
        QuestionnaireHandler.remove(player);
    }

    public void setAnswer(String answer){
        if(stop_word != null){
            if(answer.equalsIgnoreCase(stop_word)) {
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
        actual_question += 1;
        sendQuestion();
    }

    public String getAnswer(String variable){
        return questions.get(variable).getAnswer();
    }
}
