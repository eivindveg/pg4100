package loesninger;

import javax.swing.*;

public class MinuttTraad implements Runnable {

	private int minutter;
	private Felles felles;
	private JTextField tekstfelt;

	public MinuttTraad(Felles f, JTextField t) {
		felles = f;
		tekstfelt = t;
	}

	public void run() {
		while (true) {
			felles.vent();
			if (minutter == 59)
				minutter = 0;
			else
				minutter++;
			String tekst = "";
			if (minutter < 10)
				tekst += "0";
			tekst += minutter;
			tekstfelt.setText(tekst);
		}
	}
}
