package no.westerdals.ta.vegeiv13.threads;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

class Clock extends JFrame {
    private static final long serialVersionUID = 1L;

    public Clock() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        JTextField seconds = new JTextField(2);
        JTextField minutes = new JTextField(2);
        seconds.setEditable(false);
        seconds.setText("0");
        minutes.setText("0");
        minutes.setEditable(false);
        add(minutes);
        add(seconds);
        setSize(200, 100);
        setVisible(true);
        CommonObject common = new CommonObject();
        new Thread(new MinuteRunnable(common, minutes)).start();
        new Thread(new SecondRunnable(common, seconds)).start();
    }

    public static void main(String[] args) {
        new Clock();
    }

    private class MinuteRunnable implements Runnable {

        private final CommonObject common;
        private final JTextComponent textComponent;

        public MinuteRunnable(CommonObject common, JTextComponent textComponent) {
            this.common = common;
            this.textComponent = textComponent;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    if (common.isTickMinutes()) {
                        String text = textComponent.getText();
                        int value = Integer.parseInt(text);
                        common.setTickMinutes(false);
                        textComponent.setText(String.valueOf(value + 1));

                        Thread.sleep(1);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class SecondRunnable implements Runnable {

        private final CommonObject common;
        private final JTextComponent textComponent;

        public SecondRunnable(CommonObject common, JTextComponent textComponent) {
            this.common = common;
            this.textComponent = textComponent;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    long stamp = System.currentTimeMillis();
                    int value = Integer.parseInt(textComponent.getText());
                    value++;
                    if (value >= 60) {
                        value = 0;
                        common.setTickMinutes(true);
                    }
                    textComponent.setText(String.valueOf(value));
                    long newStamp = System.currentTimeMillis();
                    Thread.sleep(1000 - (newStamp - stamp));
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    private class CommonObject {

        private boolean tickMinutes;

        public boolean isTickMinutes() {
            return tickMinutes;
        }

        public void setTickMinutes(final boolean tickMinutes) {
            this.tickMinutes = tickMinutes;
        }

        public boolean flipTickMinutes() {
            this.tickMinutes = !tickMinutes;
            return tickMinutes;
        }

    }
}
