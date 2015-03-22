package no.westerdals.student.vegeiv13.pg4100.assignment2;

public final class Constants {

    /**
     * Milliseconds that the client has to respond
     */
    public static final int TIME_LIMIT = 20000;

    /**
     * Port number for both the server and client
     */
    public static final int PORT = 4567;

    /**
     * Message to display in the client if we cannot connect
     */
    public static final String COULD_NOT_CONNECT = "Could not connect";

    /**
     * Message to display in the client if the username does not pass the Player#validate check
     */
    public static final String INVALID_USERNAME = "Invalid username";

    private Constants() {
    }
}
