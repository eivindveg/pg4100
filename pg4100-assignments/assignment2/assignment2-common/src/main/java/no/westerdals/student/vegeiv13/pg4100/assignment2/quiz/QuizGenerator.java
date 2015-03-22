package no.westerdals.student.vegeiv13.pg4100.assignment2.quiz;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz;
import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.annotations.QuizField;
import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.annotations.Quizzable;
import org.reflections.ReflectionUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;

public class QuizGenerator {

    private Random random;

    public QuizGenerator() {
        random = new Random();
    }

    public Quiz fromObject(@NotNull Object object) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class clazz = object.getClass();
        if(!clazz.isAnnotationPresent(Quizzable.class)) {
            throw new UnsupportedOperationException("Class is not annotated as @Quizzable");
        }

        Field field = getRandomFieldFromClass(clazz);
        QuizField annotation = field.getAnnotation(QuizField.class);
        String question = buildQuestion(object, clazz, annotation);

        String answer = readString(object, field);

        return new Quiz(question, answer);
    }

    private String readString(final Object object, final Field field) throws IllegalAccessException {
        if(!field.isAccessible()) {
            field.setAccessible(true);
        }
        return String.valueOf(field.get(object));
    }

    private String buildQuestion(final Object object, final Class clazz, final QuizField annotation) throws NoSuchFieldException, IllegalAccessException {
        String question = annotation.value();
        String[] identifiers = annotation.identifiers();
        for (final String identifier : identifiers) {
            Field targetField = clazz.getDeclaredField(identifier);
            String value = readString(object, targetField);
            question = question.replaceFirst("%i", value);
        }
        return question;
    }

    @SuppressWarnings("unchecked")
    private Field getRandomFieldFromClass(final Class clazz) {
        List<Field> annotatedFields = new ArrayList<>();
        Set<Field> allFields = ReflectionUtils.getAllFields(clazz, withAnnotation(QuizField.class));
        annotatedFields.addAll(allFields);
        int fieldNumber = random.nextInt(annotatedFields.size());
        return annotatedFields.get(fieldNumber);
    }
}
