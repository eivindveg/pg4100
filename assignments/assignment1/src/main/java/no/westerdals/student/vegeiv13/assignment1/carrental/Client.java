package no.westerdals.student.vegeiv13.assignment1.carrental;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Client implements Runnable {

    private final String name;

    public Client(final String name) {
        this.name = name;
    }

    @Override
    public void run() {
        throw new NotImplementedException();
    }

    public String getName() {
        return name;
    }
}
