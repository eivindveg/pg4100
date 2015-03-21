package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

import java.io.Serializable;

public class Quiz implements Serializable, Cloneable {

    private static final long serialVersionUID = 2L;
    private String question;
    private String answer;

    public Quiz(final String question, final String answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    public Quiz cloneNoAnswer() {
        try {
            Quiz clone = (Quiz) clone();
            clone.setAnswer(null);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Could not clone Quiz due to " + e.getClass());
        }
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }
}
