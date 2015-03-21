package no.westerdals.student.vegeiv13.pg4100.assignment2.quiz;

import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.annotations.Quizzable;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassScanner {

    private List<Class> classes;

    public ClassScanner(String... packagesToScan) {
        classes = new ArrayList<>();
        Reflections reflections = new Reflections(packagesToScan);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Quizzable.class);
        classes.addAll(typesAnnotatedWith);
    }

    public List<Class> getClasses() {
        return classes;
    }
}
