package Views;

import Class.Asignatura;
import Class.ExistConnection;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XPathQueryService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class VentanaGestionAsignaturas extends JFrame {

    private JPanel panel1;
    Logger logger = Logger.getLogger("MyLog");

    public VentanaGestionAsignaturas() {

        add(panel1);
        setResizable(false);
        setTitle("Gestión de Asignaturas");
        setBounds(100, 100, 600, 400);
        iniciar_log();

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTabbedPane panelDePestanas = new JTabbedPane(JTabbedPane.TOP);
        panelDePestanas.setBounds(10, 11, 564, 340);
        contentPane.add(panelDePestanas);

        JPanel panel1 = new JPanel();
        panelDePestanas.addTab("Gestión de Asignaturas", null, panel1, null);
        panel1.setLayout(null);

        JLabel jlTitulo = new JLabel("ALTAS, BAJAS Y MODIFICACIONES");
        jlTitulo.setBounds(40, 20, 348, 20);
        panel1.add(jlTitulo);

        // Codigo Asignatura
        JLabel jlCodProv = new JLabel("Código:");
        jlCodProv.setBounds(90, 60, 200, 20);
        panel1.add(jlCodProv);
        JTextField jtCodProv = new JTextField();
        jtCodProv.setBounds(180, 60, 100, 20);
        panel1.add(jtCodProv);

        // Nombre Asignatura
        JLabel jlNombre = new JLabel("Nombre:");
        jlNombre.setBounds(90, 100, 200, 20);
        panel1.add(jlNombre);
        JTextField jtNombre = new JTextField();
        jtNombre.setBounds(180, 100, 200, 20);
        panel1.add(jtNombre);

        // Profesor Asignatura
        JLabel jlProfesor = new JLabel("Profesor:");
        jlProfesor.setBounds(90, 140, 200, 20);
        panel1.add(jlProfesor);
        JTextField jtProfesor = new JTextField();
        jtProfesor.setBounds(180, 140, 200, 20);
        panel1.add(jtProfesor);

        // Nª horas Asignatura
        JLabel jlhoras = new JLabel("NªHoras:");
        jlhoras.setBounds(90, 180, 200, 20);
        panel1.add(jlhoras);
        JTextField jthoras = new JTextField();
        jthoras.setBounds(180, 180, 50, 20);
        panel1.add(jthoras);

        // Boton limpiar
        JButton jbLimpiar = new JButton("Limpiar");
        jbLimpiar.setBounds(70, 240, 100, 40);
        panel1.add(jbLimpiar);

        // Boton insertar
        JButton jbInsert = new JButton("Insertar");
        jbInsert.setBounds(180, 240, 100, 40);
        panel1.add(jbInsert);

        // Boton modificar
        JButton jbModify = new JButton("Modificar");
        jbModify.setBounds(290, 240, 100, 40);
        jbModify.setEnabled(false);
        panel1.add(jbModify);

        // Boton eliminar
        JButton jbDelete = new JButton("Eliminar");
        jbDelete.setBounds(400, 240, 100, 40);
        jbDelete.setEnabled(false);
        panel1.add(jbDelete);

        // Ejecución del boton limpiar
        jbLimpiar.addActionListener(e -> {
            jtCodProv.setText("");
            jtNombre.setText("");
            jtProfesor.setText("");
            jthoras.setText("");

            jbModify.setEnabled(false);
            jbDelete.setEnabled(false);
        });

        // consulta por el codigo del proveedor
        jtCodProv.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { }
            public void removeUpdate(DocumentEvent e) {
                jbModify.setEnabled(false);
                jbDelete.setEnabled(false);
            }
            public void insertUpdate(DocumentEvent e) {

                try {

                    ExistConnection conn = new ExistConnection();

                    Collection col = conn.Col();

                    XPathQueryService busqueda = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    ResourceSet codigo = busqueda.query("/Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]");

                    ResourceIterator i = codigo.getIterator();
                    if (i.hasMoreResources()) {
                        ResourceIterator nombreIter = busqueda.query("/Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]/nombre/text()").getIterator();
                        String nombre = (String) nombreIter.nextResource().getContent();
                        jtNombre.setText(nombre);

                        ResourceIterator profesorIter = busqueda.query("/Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]/profesor/text()").getIterator();
                        String profesor = (String) profesorIter.nextResource().getContent();
                        jtProfesor.setText(profesor);

                        ResourceIterator horasIter = busqueda.query("/Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]/horas/text()").getIterator();
                        String horas = (String) horasIter.nextResource().getContent();
                        jthoras.setText(horas);

                        jbModify.setEnabled(true);
                        jbDelete.setEnabled(true);

                    } else {
                        jtNombre.setText("");
                        jtProfesor.setText("");
                        jthoras.setText("");

                        jbModify.setEnabled(false);
                        jbDelete.setEnabled(false);
                    }

                    col.close();

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

            }
        });

        // Ejecución del boton insertar
        jbInsert.addActionListener(e -> {

            try {

                if (!jtCodProv.getText().isEmpty() && !jtNombre.getText().isEmpty() && !jtProfesor.getText().isEmpty() && !jthoras.getText().isEmpty()) {

                    int horas = Integer.parseInt(jthoras.getText());

                    Asignatura asig = new Asignatura(jtCodProv.getText(), jtNombre.getText(), jtProfesor.getText(), horas);
                    ExistConnection conn = new ExistConnection();

                    Collection col = conn.Col();

                    XPathQueryService busqueda = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    ResourceSet result = busqueda.query("/Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]");

                    logger.log(Level.INFO, "[Gestion Asignaturas] Consulta realizada: " + "/Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]");

                    ResourceIterator i = result.getIterator();
                    if (!i.hasMoreResources()) {
                        XPathQueryService insert = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                        insert.query("update insert " + asig.toString() + " into /Asignaturas");

                        jbModify.setEnabled(true);
                        jbDelete.setEnabled(true);

                        logger.log(Level.INFO, "[Gestion Asignaturas] Insert realizado: " + "update insert " + asig.toString() + " into /Asignaturas");
                    } else {
                        JOptionPane.showMessageDialog(null, "Ya existe un asignatura con ese código", "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                    col.close();

                } else {
                    JOptionPane.showMessageDialog(null, "Faltan datos por rellenar", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Introduce un número, en NªHoras por favor", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        });

        jbModify.addActionListener(e -> {

            try {

                if (!jtCodProv.getText().isEmpty() && !jtNombre.getText().isEmpty() && !jtProfesor.getText().isEmpty() && !jthoras.getText().isEmpty()) {

                    int horas = Integer.parseInt(jthoras.getText());

                    ExistConnection conn = new ExistConnection();

                    Collection col = conn.Col();

                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

                    servicio.query("update value /Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]/nombre with data('" + jtNombre.getText() + "') ");
                    servicio.query("update value /Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]/profesor with data('" + jtProfesor.getText() + "') ");
                    servicio.query("update value /Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]/horas with data('" + horas + "') ");

                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]/nombre with data('" + jtNombre.getText() + "') ");
                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]/profesor with data('" + jtProfesor.getText() + "') ");
                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]/horas with data('" + horas + "') ");

                    col.close();

                } else {
                    JOptionPane.showMessageDialog(null, "Faltan datos por rellenar", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Introduce un número, en NªHoras por favor", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        });

        jbDelete.addActionListener(e -> {

            try {

                if (!jtCodProv.getText().isEmpty()) {

                    String msgInputDialog = "¿Seguro que deseas borrar esta asignatura?";
                    int input = JOptionPane.showConfirmDialog(null, msgInputDialog, "Borrar", JOptionPane.YES_NO_OPTION);

                    if (input == 0) {

                        ExistConnection conn = new ExistConnection();

                        Collection col = conn.Col();

                        XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                        servicio.query("update delete /Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]");

                        logger.log(Level.INFO, "[Gestion Asignaturas] Delete realizado: " + "update delete /Asignaturas/Asignatura[@ID=" + jtCodProv.getText() + "]");

                        col.close();

                        jtCodProv.setText("");
                        jtNombre.setText("");
                        jtProfesor.setText("");
                        jthoras.setText("");

                        jbModify.setEnabled(false);
                        jbDelete.setEnabled(false);

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Introduce un código por favor.", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        });

    }

    private void iniciar_log(){
        FileHandler fh;
        try {

            fh = new FileHandler("ficheros/log_actividad.txt", true);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);

            SimpleFormatter formatter = new SimpleFormatter();

            fh.setFormatter(formatter);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

}
