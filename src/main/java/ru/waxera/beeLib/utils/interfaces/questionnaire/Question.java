package ru.waxera.beeLib.utils.interfaces.questionnaire;

import org.bukkit.plugin.Plugin;
import ru.waxera.beeLib.utils.StringUtils;

public class Question {
    private final String variable;
    private final String question;
    private String answer;

    public Question(Plugin plugin, String variable, String question){
        this.variable = variable;
        this.question = StringUtils.format(question, plugin);
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public String getVariable() {
        return variable;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}
