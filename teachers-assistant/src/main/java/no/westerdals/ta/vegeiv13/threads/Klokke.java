package loesninger;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Klokke extends JFrame {
    private static final long serialVersionUID = 1L;

    public Klokke() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        JTextField sekunder = new JTextField(2);
        JTextField minutter = new JTextField(2);
        sekunder.setEditable(false);
        minutter.setEditable(false);
        add(minutter);
        add(sekunder);
        setSize(200, 100);
        setVisible(true);
        Felles felles = new Felles();
        MinuttTraad minutt = new MinuttTraad(felles, minutter);
        SekundTraad sekund = new SekundTraad(felles, sekunder);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(minutt);
        executor.execute(sekund);
        executor.shutdown();
    }

    public static void main(String[] args) {
        new Klokke();
    }
}
