package loesninger;

import javax.swing.*;

public class SekundTraad implements Runnable {
    private int sekunder;
    private Felles felles;
    private JTextField tekstfelt;

    public SekundTraad(Felles k, JTextField f) {
        felles = k;
        tekstfelt = f;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            if (sekunder == 59) {
                felles.minuttTikk();
                sekunder = 0;
            } else
                sekunder++;
            String tekst = "";
            if (sekunder < 10)
                tekst += "0";
            tekst += sekunder;
            tekstfelt.setText(tekst);
        }
    }
}
