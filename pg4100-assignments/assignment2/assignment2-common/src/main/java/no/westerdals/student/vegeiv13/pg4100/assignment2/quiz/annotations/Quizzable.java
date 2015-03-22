package no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the annotated class as Quizzable, meaning the QuizGenerator should be able to generate quizzes from it
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Quizzable {
}
