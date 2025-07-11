package ru.waxera.beeLib.utils.interfaces.questionnaire;

import org.bukkit.entity.Player;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.interfaces.Action;
import ru.waxera.beeLib.utils.message.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class Questionnaire {
    private Player player;
    private HashMap<String, Question> questions = new HashMap<>();
    private Action action;
    private int actual_question = 0;

    public Questionnaire(Player player, Action action, Question ... questions){
        this.player = player;
        this.action = action;
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
        if(!QuestionnaireHandler.contains(player)){QuestionnaireHandler.add(player, this);}
        if(isOver()){
            endQuestionnaire();
            return;
        }
        Question question = getActualQuestion();
        Message.send(BeeLib.getInstance(), player, question.getQuestion());
    }

    public void endQuestionnaire(){
        action.run(player, null);
        QuestionnaireHandler.remove(player);
    }

    public void setAnswer(String answer){
        Question question = getActualQuestion();
        if(question == null){
            endQuestionnaire();
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
