package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

import java.io.Serializable;

public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        final String test = "Testing this crappy shit";
        return "Quiz{" +
                "test='" + test + '\'' +
                '}';
    }

}
