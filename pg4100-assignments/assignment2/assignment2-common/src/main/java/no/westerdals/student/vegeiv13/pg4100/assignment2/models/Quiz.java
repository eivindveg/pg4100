package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Quiz implements Serializable, Cloneable {

    private static final long serialVersionUID = 2L;
    private final String question;
    private String answer;

    public Quiz(final String question, final String answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("question", question)
                .append("answer", answer)
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Quiz quiz = (Quiz) o;

        if (answer != null ? !answer.equals(quiz.answer) : quiz.answer != null) return false;
        if (question != null ? !question.equals(quiz.question) : quiz.question != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = question != null ? question.hashCode() : 0;
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        return result;
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

}
