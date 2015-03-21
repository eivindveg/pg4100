package no.westerdals.student.vegeiv13.pg4100.assignment2.quiz;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz;
import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.annotations.QuizField;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static org.reflections.ReflectionUtils.withAnnotation;

public class QuizGenerator {

    private final List<Class> classes;
    private Random random;

    public QuizGenerator(ClassScanner classScanner) {
        classes = classScanner.getClasses();
        random = new Random();
    }

    public Quiz fromObject(Object object) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class clazz = object.getClass();
        System.out.println(object);
        if(!classes.contains(clazz)) {
            throw new ClassNotFoundException("Target object's class is not validated as Quizzable.");
        }
        System.out.println(clazz);
        System.out.println(Arrays.toString(clazz.getDeclaredFields()));
        Field field = getRandomFieldFromClass(clazz);
        QuizField annotation = field.getAnnotation(QuizField.class);
        String question = annotation.value();
        String[] identifiers = annotation.identifiers();
        for (final String identifier : identifiers) {
            Field targetField = clazz.getDeclaredField(identifier);
            if(!targetField.isAccessible()) {
                targetField.setAccessible(true);
            }
            String value = String.valueOf(targetField.get(object));
            question = question.replaceFirst("%i", value);
        }

        if(!field.isAccessible()) {
            field.setAccessible(true);
        }
        String answer = String.valueOf(field.get(object));

        return new Quiz(question, answer);

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
