package Views;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.io.BufferedReader;

public class VentanaLog extends JFrame {

    private JPanel panel1;
    BufferedReader b;

    public VentanaLog(BufferedReader b) {

        JPanel middlePanel = new JPanel ();
        middlePanel.setBorder(new TitledBorder(new EtchedBorder(), "Logs" ));

        JTextArea display = new JTextArea(32, 75);
        display.setEditable(false);
        JScrollPane scroll = new JScrollPane(display);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        middlePanel.add(scroll);

        add(middlePanel);

        setTitle("Logs");
        setResizable(false);
        setSize(920, 600);
        this.b = b;

        try {
            String cadena = b.readLine();
            while (cadena != null) {
                display.append(cadena + "\n");
                cadena = b.readLine();
            }
            b.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
