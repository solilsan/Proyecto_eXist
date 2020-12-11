package Views;

import Class.Asignatura;
import Class.Alumno;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentanaGestionAsignaturas extends JFrame {

    private JPanel panel1;
    private List<Asignatura> listaAsignaturas = new ArrayList<>();
    private int regActual;
    Logger logger;

    public VentanaGestionAsignaturas(Logger logger) {

        add(panel1);
        setResizable(false);
        setTitle("Gestión de Asignaturas");
        setBounds(100, 100, 600, 400);

        this.logger = logger;

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
        JLabel jlCod = new JLabel("Código:");
        jlCod.setBounds(90, 60, 200, 20);
        panel1.add(jlCod);
        JTextField jtCod = new JTextField();
        jtCod.setBounds(180, 60, 100, 20);
        panel1.add(jtCod);

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
        JLabel jlhoras = new JLabel("Nº Horas:");
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
            jtCod.setText("");
            jtNombre.setText("");
            jtProfesor.setText("");
            jthoras.setText("");

            jbModify.setEnabled(false);
            jbDelete.setEnabled(false);
        });

        // consulta por el codigo del proveedor
        jtCod.getDocument().addDocumentListener(new DocumentListener() {
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
                    ResourceSet codigo = busqueda.query("/Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]");

                    ResourceIterator i = codigo.getIterator();
                    if (i.hasMoreResources()) {
                        ResourceIterator nombreIter = busqueda.query("/Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]/nombre/text()").getIterator();
                        String nombre = (String) nombreIter.nextResource().getContent();
                        jtNombre.setText(nombre);

                        ResourceIterator profesorIter = busqueda.query("/Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]/profesor/text()").getIterator();
                        String profesor = (String) profesorIter.nextResource().getContent();
                        jtProfesor.setText(profesor);

                        ResourceIterator horasIter = busqueda.query("/Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]/horas/text()").getIterator();
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

                if (!jtCod.getText().isEmpty() && !jtNombre.getText().isEmpty() && !jtProfesor.getText().isEmpty() && !jthoras.getText().isEmpty()) {

                    int horas = Integer.parseInt(jthoras.getText());

                    Asignatura asig = new Asignatura(jtCod.getText(), jtNombre.getText(), jtProfesor.getText(), horas);
                    ExistConnection conn = new ExistConnection();

                    Collection col = conn.Col();

                    XPathQueryService busqueda = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    ResourceSet result = busqueda.query("/Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]");

                    logger.log(Level.INFO, "[Gestion Asignaturas] Consulta realizada: " + "/Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]");

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

                if (!jtCod.getText().isEmpty() && !jtNombre.getText().isEmpty() && !jtProfesor.getText().isEmpty() && !jthoras.getText().isEmpty()) {

                    int horas = Integer.parseInt(jthoras.getText());

                    ExistConnection conn = new ExistConnection();

                    Collection col = conn.Col();

                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

                    servicio.query("update value /Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]/nombre with data('" + jtNombre.getText() + "') ");
                    servicio.query("update value /Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]/profesor with data('" + jtProfesor.getText() + "') ");
                    servicio.query("update value /Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]/horas with data('" + horas + "') ");

                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]/nombre with data('" + jtNombre.getText() + "') ");
                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]/profesor with data('" + jtProfesor.getText() + "') ");
                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]/horas with data('" + horas + "') ");

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

                if (!jtCod.getText().isEmpty()) {

                    String msgInputDialog = "¿Seguro que deseas borrar esta asignatura?";
                    int input = JOptionPane.showConfirmDialog(null, msgInputDialog, "Borrar", JOptionPane.YES_NO_OPTION);

                    if (input == 0) {

                        ExistConnection conn = new ExistConnection();

                        Collection col = conn.Col();

                        XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                        servicio.query("update delete /Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]");

                        logger.log(Level.INFO, "[Gestion Asignaturas] Delete realizado: " + "update delete /Asignaturas/Asignatura[@ID=" + jtCod.getText() + "]");

                        col.close();

                        jtCod.setText("");
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

        // otro panel de igual forma
        JPanel panel2 = new JPanel();
        panelDePestanas.addTab("Consulta de Asignaturas", null, panel2, null);
        panel2.setLayout(null);

        // otra etiqueta ésta vez en el segundo panel
        JLabel lbl2 = new JLabel("BUSQUEDA POR CUALQUIER PARÁMETRO");
        lbl2.setBounds(20, 20, 480, 14);
        panel2.add(lbl2);

        // lista alumnos en la asignatura seleccionada
        JButton jbListaAlumnos = new JButton("Lista Alumnos");
        jbListaAlumnos.setBounds(400, 20, 150, 40);
        panel2.add(jbListaAlumnos);

        // Codigo Asignatura
        JLabel jlCodVer = new JLabel("Código:");
        jlCodVer.setBounds(90, 60, 200, 20);
        panel2.add(jlCodVer);
        JTextField jtCodVer = new JTextField();
        jtCodVer.setBounds(180, 60, 100, 20);
        panel2.add(jtCodVer);

        // Nombre Asignatura
        JLabel jlNombreVer = new JLabel("Nombre:");
        jlNombreVer.setBounds(90, 90, 200, 20);
        panel2.add(jlNombreVer);
        JTextField jtNombreVer = new JTextField();
        jtNombreVer.setBounds(180, 90, 200, 20);
        panel2.add(jtNombreVer);

        // Profesor Asignatura
        JLabel jlProfesorVer = new JLabel("Profesor:");
        jlProfesorVer.setBounds(90, 120, 200, 20);
        panel2.add(jlProfesorVer);
        JTextField jtProfesorVer = new JTextField();
        jtProfesorVer.setBounds(180, 120, 200, 20);
        panel2.add(jtProfesorVer);

        // NºHoras Asignatura
        JLabel jlHorasVer = new JLabel("Nº Horas:");
        jlHorasVer.setBounds(90, 150, 200, 20);
        panel2.add(jlHorasVer);
        JTextField jtHorasVer = new JTextField();
        jtHorasVer.setBounds(180, 150, 50, 20);
        panel2.add(jtHorasVer);

        // Resgistro
        JLabel jlReg = new JLabel("REG:");
        jlReg.setBounds(80, 200, 40, 20);
        panel2.add(jlReg);

        // Resgistro actual
        JTextField jtpag1 = new JTextField();
        jtpag1.setBounds(120, 200, 40, 20);
        jtpag1.setText("0");
        jtpag1.setHorizontalAlignment(JTextField.CENTER);
        jtpag1.setEditable(false);
        panel2.add(jtpag1);

        // Barra entre el numero de registros
        JLabel jlBarra = new JLabel("/");
        jlBarra.setBounds(170, 200, 10, 20);
        panel2.add(jlBarra);

        // Resgistros totales
        JTextField jtpag2 = new JTextField();
        jtpag2.setBounds(180, 200, 40, 20);
        jtpag2.setText("0");
        jtpag2.setHorizontalAlignment(JTextField.CENTER);
        jtpag2.setEditable(false);
        panel2.add(jtpag2);

        // Boton primer registro
        JButton jbFistReg = new JButton("|<<");
        jbFistReg.setBounds(240, 195, 55, 30);
        jbFistReg.setEnabled(false);
        panel2.add(jbFistReg);

        // Boton registro anterior
        JButton jbMesReg = new JButton("<<");
        jbMesReg.setBounds(300, 195, 55, 30);
        jbMesReg.setEnabled(false);
        panel2.add(jbMesReg);

        // Boton registro siguiente
        JButton jbSigReg = new JButton(">>");
        jbSigReg.setBounds(360, 195, 55, 30);
        jbSigReg.setEnabled(false);
        panel2.add(jbSigReg);

        // Boton ultimo registro
        JButton jbLastReg = new JButton(">>|");
        jbLastReg.setBounds(420, 195, 55, 30);
        jbLastReg.setEnabled(false);
        panel2.add(jbLastReg);

        // Boton ejecutar consulta
        JButton jbEjecuteCon = new JButton("Ejecutar Consulta");
        jbEjecuteCon.setBounds(200, 250, 280, 40);
        panel2.add(jbEjecuteCon);

        // Boton limpiar
        JButton jbLimpiarVer = new JButton("Limpiar");
        jbLimpiarVer.setBounds(80, 250, 100, 40);
        panel2.add(jbLimpiarVer);

        // Boton lista alumno
        jbListaAlumnos.addActionListener(e -> {

            try {

                if (!jtCodVer.getText().isEmpty() && !jtNombreVer.getText().isEmpty()) {

                    ExistConnection conn = new ExistConnection();

                    Collection col = conn.Col();

                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

                    String query = "for $asig in /Asignaturas/Asignatura, $matri in /Matriculas/Matricula";
                    query += " where data($asig/[@ID]) = data($matri/idAsignatura) and data($asig/[@ID='" + jtCodVer.getText() +"'])";
                    query += " return <res>concat(|{data($matri/dniAlumno)}|)</res>";

                    logger.log(Level.INFO, "[Gestion Asignaturas] Consulta realizada: " + query);

                    ResourceSet result = servicio.query(query);
                    ResourceIterator i = result.getIterator();

                    List<String> listaDniAlumnos = new ArrayList<>();
                    List<Alumno> listaAlumnos = new ArrayList<>();

                    while (i.hasMoreResources()) {
                        Resource r = i.nextResource();
                        String dato = (String) r.getContent();
                        String[] parts = dato.split("\\|");
                        listaDniAlumnos.add(parts[1]);
                    }

                    if (listaDniAlumnos.size() > 0) {
                        for (int x = 0; x < listaDniAlumnos.size(); x++) {
                            query = "for $alumn in /Alumnos/Alumno";
                            query += " where data($alumn[@DNI='" + listaDniAlumnos.get(x) + "'])";
                            query += " return <res>concat(|{data($alumn/[@DNI])}|{data($alumn/nombre)}|{data($alumn/apellidos)}|{data($alumn/direccion)}|{data($alumn/email)}|{data($alumn/telefono)}|)</res>";

                            logger.log(Level.INFO, "[Gestion Asignaturas] Consulta realizada: " + query);

                            result = servicio.query(query);
                            i = result.getIterator();
                            if (i.hasMoreResources()) {
                                Resource r = i.nextResource();
                                String dato = (String) r.getContent();
                                String[] parts = dato.split("\\|");
                                listaAlumnos.add(new Alumno(parts[1], parts[2], parts[3], parts[4], parts[5], Integer.parseInt(parts[6])));
                            }
                        }
                    }

                    col.close();

                    if (listaAlumnos.size() == 0) {
                        JOptionPane.showMessageDialog(null, "Esta asignatura no tiene alumnos.", "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        VentanaListaAlumnosAsignatura vlaa = new VentanaListaAlumnosAsignatura(listaAlumnos, jtNombreVer.getText());
                        vlaa.setLocationRelativeTo(null);
                        vlaa.setVisible(true);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Ejecuta la búsqueda primero por favor.", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        });

        // Ejecución del boton consulta
        jbEjecuteCon.addActionListener(e -> {

            try {

                listaAsignaturas = new ArrayList<>();
                regActual = 0;

                ExistConnection conn = new ExistConnection();

                Collection col = conn.Col();

                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

                String query = "for $asig in /Asignaturas/Asignatura";

                if (!jtCodVer.getText().isEmpty()) {
                    query += " where data($asig[@ID='" + jtCodVer.getText() + "'])";

                } else {

                    if (!jtNombreVer.getText().isEmpty()) {
                        query += " where data($asig/nombre) = data('" + jtNombreVer.getText() + "')";
                    }

                    if (!jtProfesorVer.getText().isEmpty() && jtNombreVer.getText().isEmpty()) {
                        query += " where data($asig/profesor) = data('" + jtProfesorVer.getText() + "')";

                    } else if (!jtProfesorVer.getText().isEmpty() && !jtNombreVer.getText().isEmpty()) {
                        query += " and data($asig/profesor) = data('" + jtProfesorVer.getText() + "')";
                    }

                    if (!jtHorasVer.getText().isEmpty() && jtProfesorVer.getText().isEmpty()) {
                        query += " where data($asig/horas) = data('" + jtHorasVer.getText() + "')";

                    } else if (!jtHorasVer.getText().isEmpty() && !jtProfesorVer.getText().isEmpty()) {
                        query += " and data($asig/horas) = data('" + jtHorasVer.getText() + "')";
                    }

                }

                query += " return <res>concat(|{data($asig/[@ID])}|{data($asig/nombre)}|{data($asig/profesor)}|{data($asig/horas)}|)</res>";

                logger.log(Level.INFO, "[Gestion Asignaturas] Consulta realizada: " + query);

                ResourceSet result = servicio.query(query);

                // recorrer los datos del recurso.
                ResourceIterator i = result.getIterator();
                if (!i.hasMoreResources())
                    JOptionPane.showMessageDialog(null, "No existe un asignatura con esos parámetros.", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                while (i.hasMoreResources()) {
                    Resource r = i.nextResource();
                    String dato = (String) r.getContent();
                    String[] parts = dato.split("\\|");
                    listaAsignaturas.add(new Asignatura(parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
                }

                col.close();

                if (listaAsignaturas.size() > 0) {

                    jtCodVer.setText(listaAsignaturas.get(regActual).getId());
                    jtNombreVer.setText(listaAsignaturas.get(regActual).getNombre());
                    jtProfesorVer.setText(listaAsignaturas.get(regActual).getProfesor());
                    jtHorasVer.setText(listaAsignaturas.get(regActual).getHoras().toString());

                    jtpag1.setText(String.valueOf(regActual + 1));
                    jtpag2.setText(String.valueOf(listaAsignaturas.size()));

                    if (listaAsignaturas.size() > 1) {
                        jbSigReg.setEnabled(true);
                        jbLastReg.setEnabled(true);

                    } else if (listaAsignaturas.size() == 1) {
                        jbSigReg.setEnabled(false);
                        jbLastReg.setEnabled(false);
                        jbFistReg.setEnabled(false);
                        jbMesReg.setEnabled(false);
                    }

                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        });

        // Ejecución del boton siguiente registro
        jbSigReg.addActionListener(e -> {

            try {

                if (listaAsignaturas.size() > 1 && listaAsignaturas.size() < listaAsignaturas.size() + 1) {

                    regActual++;

                    jtCodVer.setText(listaAsignaturas.get(regActual).getId());
                    jtNombreVer.setText(listaAsignaturas.get(regActual).getNombre());
                    jtProfesorVer.setText(listaAsignaturas.get(regActual).getProfesor());
                    jtHorasVer.setText(listaAsignaturas.get(regActual).getHoras().toString());

                    jtpag1.setText(String.valueOf(regActual + 1));
                    jbFistReg.setEnabled(true);
                    jbMesReg.setEnabled(true);

                    if ((regActual + 1) == listaAsignaturas.size()) {
                        jbSigReg.setEnabled(false);
                        jbLastReg.setEnabled(false);
                    }

                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        // Ejecución del boton ultimo registro
        jbLastReg.addActionListener(e -> {

            try {

                jbSigReg.setEnabled(false);
                jbLastReg.setEnabled(false);

                jbFistReg.setEnabled(true);
                jbMesReg.setEnabled(true);

                if (listaAsignaturas.size() > 1 && listaAsignaturas.size() < listaAsignaturas.size() + 1) {

                    regActual = listaAsignaturas.size() - 1;

                    jtCodVer.setText(listaAsignaturas.get(listaAsignaturas.size() - 1).getId());
                    jtNombreVer.setText(listaAsignaturas.get(listaAsignaturas.size() - 1).getNombre());
                    jtProfesorVer.setText(listaAsignaturas.get(listaAsignaturas.size() - 1).getProfesor());
                    jtHorasVer.setText(listaAsignaturas.get(listaAsignaturas.size() - 1).getHoras().toString());

                    jtpag1.setText(String.valueOf(listaAsignaturas.size()));

                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        // Ejecución del boton registro anterior
        jbMesReg.addActionListener(e -> {

            try {

                if (regActual > 0) {

                    regActual--;

                    jtCodVer.setText(listaAsignaturas.get(regActual).getId());
                    jtNombreVer.setText(listaAsignaturas.get(regActual).getNombre());
                    jtProfesorVer.setText(listaAsignaturas.get(regActual).getProfesor());
                    jtHorasVer.setText(listaAsignaturas.get(regActual).getHoras().toString());

                    jtpag1.setText(String.valueOf(regActual + 1));

                    jbSigReg.setEnabled(true);
                    jbLastReg.setEnabled(true);

                    if (regActual == 0) {
                        jbMesReg.setEnabled(false);
                        jbFistReg.setEnabled(false);
                    }

                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        // Ejecución del boton primer registro
        jbFistReg.addActionListener(e -> {

            try {

                if (regActual >= 0) {

                    regActual = 0;

                    jtCodVer.setText(listaAsignaturas.get(regActual).getId());
                    jtNombreVer.setText(listaAsignaturas.get(regActual).getNombre());
                    jtProfesorVer.setText(listaAsignaturas.get(regActual).getProfesor());
                    jtHorasVer.setText(listaAsignaturas.get(regActual).getHoras().toString());

                    jtpag1.setText(String.valueOf(regActual + 1));

                    jbSigReg.setEnabled(true);
                    jbLastReg.setEnabled(true);

                    if (regActual == 0) {
                        jbMesReg.setEnabled(false);
                        jbFistReg.setEnabled(false);
                    }

                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        // Ejecución del boton limpiar
        jbLimpiarVer.addActionListener(e -> {
            jtCodVer.setText("");
            jtNombreVer.setText("");
            jtProfesorVer.setText("");
            jtHorasVer.setText("");

            jtpag1.setText("0");
            jtpag2.setText("0");

            jbSigReg.setEnabled(false);
            jbLastReg.setEnabled(false);
            jbFistReg.setEnabled(false);
            jbMesReg.setEnabled(false);

            listaAsignaturas = new ArrayList<>();
        });

    }

}
