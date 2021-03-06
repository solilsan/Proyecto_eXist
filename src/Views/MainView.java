package Views;

import javax.swing.*;
import Controller.GenerarXML;

import Controller.InitLog;
import org.xmldb.api.base.Collection;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainView extends JFrame {
    private JPanel panel1;
    private JButton generarXMLsButton;
    private JButton entrarButton;
    private JLabel listaRecursos;

    Logger logger = Logger.getLogger("MyLog");

    public MainView() {

        add(panel1);

        setTitle("Generar XML");
        setResizable(false);
        setSize(520, 300);

        InitLog.iniciarLog(logger);

        recargarCol();

        generarXMLsButton.addActionListener(e -> {

            String msgInputDialog = "�Seguro que deseas generar los XML?, se sobreescribir� los datos.";
            int input = JOptionPane.showConfirmDialog(null, msgInputDialog, "Generar", JOptionPane.YES_NO_OPTION);

            if (input == 0) {

                String generar = GenerarXML.generarCargarCol();

                if (generar.equals("exito")) {
                    JOptionPane.showMessageDialog(null, "XMLs generados con �xito y almacenados en collecion 'proyectoExist'.", "Informaci�n", JOptionPane.INFORMATION_MESSAGE);
                    recargarCol();

                    logger.log(Level.INFO, "[MainView] Xmls generados");
                } else {
                    JOptionPane.showMessageDialog(null, generar, "Informaci�n", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        entrarButton.addActionListener(e -> {
            this.setVisible(false);
            VentanaPrincipal vp = new VentanaPrincipal(logger);
            vp.setLocationRelativeTo(null);
            vp.setVisible(true);
        });

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

    }

    private void recargarCol() {

        Collection collection = GenerarXML.listaRecursos();

        if (collection != null) {

            try {

                StringBuilder labelRecursos = new StringBuilder();

                for (String colRe : collection.listResources()) {
                    labelRecursos.append(colRe).append(",  ");
                }

                labelRecursos.deleteCharAt(labelRecursos.length() - 3);
                listaRecursos.setText(labelRecursos.toString());

                if (!listaRecursos.getText().equals("") && !listaRecursos.getText().equals("Vac�a")) {
                    entrarButton.setEnabled(GenerarXML.comprobarXML());
                }

            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            listaRecursos.setText("Vac�a");
            JOptionPane.showMessageDialog(null, "Falta collecion 'proyectoExist' en eXist", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

}
