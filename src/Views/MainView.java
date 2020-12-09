package Views;

import javax.swing.*;
import Class.GenerarXML;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.xmldb.api.base.Collection;

public class MainView extends JFrame {
    private JPanel panel1;
    private JButton generarXMLsButton;
    private JButton entrarButton;
    private JLabel listaRecursos;

    public MainView() {

        add(panel1);

        setTitle("Generar XML");
        setResizable(false);
        setSize(520, 300);

        recargarCol();

        generarXMLsButton.addActionListener(e -> {

            String msgInputDialog = "¿Seguro que deseas generar los XML?, se sobreescribirá los datos.";
            int input = JOptionPane.showConfirmDialog(null, msgInputDialog, "Generar", JOptionPane.YES_NO_OPTION);

            if (input == 0) {

                String generar = GenerarXML.generarCargarCol();

                if (generar.equals("exito")) {
                    JOptionPane.showMessageDialog(null, "XMLs generados con éxito y almacenados en collecion 'proyectoExist'.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    recargarCol();
                } else {
                    JOptionPane.showMessageDialog(null, generar, "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
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

                if (!listaRecursos.getText().equals("") && !listaRecursos.getText().equals("Vacía")) {
                    entrarButton.setEnabled(GenerarXML.comprobarXML());
                }

            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            listaRecursos.setText("Vacía");
            JOptionPane.showMessageDialog(null, "Falta collecion 'proyectoExist' en eXist", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

}
