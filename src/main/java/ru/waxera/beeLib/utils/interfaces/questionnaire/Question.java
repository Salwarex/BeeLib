package ru.waxera.beeLib.utils.interfaces.questionnaire;

public class Question {
    private final String variable;
    private final String question;
    private String answer;

    public Question(String variable, String question){
        this.variable = variable;
        this.question = question;
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
