package no.westerdals.student.vegeiv13.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileHandler {

    private final File file;

    public FileHandler(String fileName) throws IOException {
        this(new File(fileName));
    }

    public FileHandler(File file) throws IOException {
        this.file = file;
        if(!file.exists()) {
            if(!file.createNewFile()) {
                throw new IOException("Could not assert file");
            }
        }
    }

    public void append(Object o) throws IOException {
        FileUtils.write(file, o.toString(), true);
    }

    public String getLastLine() throws IOException {
        LineIterator lineIterator = FileUtils.lineIterator(file);
        String line = "";
        while(lineIterator.hasNext()) {
            line = lineIterator.nextLine();
        }
        return line;
    }

    public void appendLines(final List<?> lines) throws IOException {
        FileUtils.writeLines(file, lines, true);
    }

    public void appendLinesConcurrently(final List<Long> flush) {
        new Thread(() -> {
            try {
                appendLines(flush);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
