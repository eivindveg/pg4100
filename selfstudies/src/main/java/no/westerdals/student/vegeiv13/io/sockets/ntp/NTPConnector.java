package no.westerdals.student.vegeiv13.io.sockets.ntp;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class NTPConnector {
    public static void main(String[] args) throws IOException {
        byte[] bytes = new byte[4];
        int read;
        try(InputStream inputStream = new Socket("time.nist.gov", 37).getInputStream()) {
            read = inputStream.read(bytes);
        }
        byte[] bytesUnsigned = new byte[5];
        System.arraycopy(bytes, 0, bytesUnsigned, 1, bytes.length);
        BigInteger bigInteger = new BigInteger(bytesUnsigned);
        long aLong = Long.valueOf(bigInteger.toString());
        LocalDateTime datetime = LocalDateTime
                .ofInstant(Instant.ofEpochSecond(aLong), ZoneId.systemDefault())
                .minusYears(70); // Shift by 70 as NTP does not return UNIX time
        System.out.println(datetime);

        System.exit(read == 4 ? 0 : 1); // Exit with non-standard exit code if we didn't get 4 bytes
    }
}
