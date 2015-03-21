package no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface used to annotate fields in a model to enable them to be used in a quiz.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QuizField {

    /**
     * The question format for this question, %i refers to the
     * annotated field's class' identifier.
     */
    String value() default "question %i?";

    String[] identifiers();
}
