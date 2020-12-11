package Views;

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

import Class.ExistConnection;
import Class.JTextFieldConfig;
import Class.Alumno;

public class VentanaGestionAlumnos extends JFrame {

    private JPanel panel1;
    Logger logger;
    private List<Alumno> listaAlumnos = new ArrayList<>();
    private int regActual;

    public VentanaGestionAlumnos(Logger logger) {

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
        panelDePestanas.addTab("Gestión de Alumnos", null, panel1, null);
        panel1.setLayout(null);

        JLabel jlTitulo = new JLabel("ALTAS, BAJAS Y MODIFICACIONES");
        jlTitulo.setBounds(40, 20, 348, 20);
        panel1.add(jlTitulo);

        // Dni Alumno
        JLabel jlDni = new JLabel("Dni:");
        jlDni.setBounds(90, 60, 200, 20);
        panel1.add(jlDni);
        JTextField jtDni = new JTextField();
        jtDni.setBounds(180, 60, 100, 20);
        panel1.add(jtDni);
        jtDni.setDocument(new JTextFieldConfig(9, true));

        // Nombre Alumno
        JLabel jlNombre = new JLabel("Nombre:");
        jlNombre.setBounds(90, 90, 200, 20);
        panel1.add(jlNombre);
        JTextField jtNombre = new JTextField();
        jtNombre.setBounds(180, 90, 200, 20);
        panel1.add(jtNombre);

        // Apellidos Alumno
        JLabel jlApellidos = new JLabel("Apellidos:");
        jlApellidos.setBounds(90, 120, 200, 20);
        panel1.add(jlApellidos);
        JTextField jtApellidos = new JTextField();
        jtApellidos.setBounds(180, 120, 200, 20);
        panel1.add(jtApellidos);

        // Direccion Alumnos
        JLabel jlDir = new JLabel("Dirección:");
        jlDir.setBounds(90, 150, 200, 20);
        panel1.add(jlDir);
        JTextField jtDir = new JTextField();
        jtDir.setBounds(180, 150, 280, 20);
        panel1.add(jtDir);

        // Email Alumnos
        JLabel jlEmail = new JLabel("Email:");
        jlEmail.setBounds(90, 180, 200, 20);
        panel1.add(jlEmail);
        JTextField jtEmail = new JTextField();
        jtEmail.setBounds(180, 180, 250, 20);
        panel1.add(jtEmail);

        // Telefono Alumnos
        JLabel jlTef = new JLabel("Teléfono:");
        jlTef.setBounds(90, 210, 200, 20);
        panel1.add(jlTef);
        JTextField jtTef= new JTextField();
        jtTef.setBounds(180, 210, 100, 20);
        panel1.add(jtTef);

        // Boton limpiar
        JButton jbLimpiar = new JButton("Limpiar");
        jbLimpiar.setBounds(70, 250, 100, 40);
        panel1.add(jbLimpiar);

        // Boton insertar
        JButton jbInsert = new JButton("Insertar");
        jbInsert.setBounds(180, 250, 100, 40);
        panel1.add(jbInsert);

        // Boton modificar
        JButton jbModify = new JButton("Modificar");
        jbModify.setBounds(290, 250, 100, 40);
        jbModify.setEnabled(false);
        panel1.add(jbModify);

        // Boton eliminar
        JButton jbDelete = new JButton("Eliminar");
        jbDelete.setBounds(400, 250, 100, 40);
        jbDelete.setEnabled(false);
        panel1.add(jbDelete);

        // Ejecución del boton limpiar
        jbLimpiar.addActionListener(e -> {
            jtDni.setText("");
            jtNombre.setText("");
            jtApellidos.setText("");
            jtDir.setText("");
            jtEmail.setText("");
            jtTef.setText("");

            jbModify.setEnabled(false);
            jbDelete.setEnabled(false);
        });

        // consulta por el codigo del proveedor
        jtDni.getDocument().addDocumentListener(new DocumentListener() {
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
                    ResourceSet codigo = busqueda.query("/Alumnos/Alumno[@DNI='" + jtDni.getText() + "']");

                    ResourceIterator i = codigo.getIterator();
                    if (i.hasMoreResources()) {
                        ResourceIterator nombreIter = busqueda.query("/Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/nombre/text()").getIterator();
                        String nombre = (String) nombreIter.nextResource().getContent();
                        jtNombre.setText(nombre);

                        ResourceIterator apellidosIter = busqueda.query("/Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/apellidos/text()").getIterator();
                        String apellidos = (String) apellidosIter.nextResource().getContent();
                        jtApellidos.setText(apellidos);

                        ResourceIterator dirIter = busqueda.query("/Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/direccion/text()").getIterator();
                        String dir = (String) dirIter.nextResource().getContent();
                        jtDir.setText(dir);

                        ResourceIterator emailIter = busqueda.query("/Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/email/text()").getIterator();
                        String email = (String) emailIter.nextResource().getContent();
                        jtEmail.setText(email);

                        ResourceIterator tefIter = busqueda.query("/Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/telefono/text()").getIterator();
                        String tef = (String) tefIter.nextResource().getContent();
                        jtTef.setText(tef);

                        jbModify.setEnabled(true);
                        jbDelete.setEnabled(true);

                    } else {
                        jtNombre.setText("");
                        jtApellidos.setText("");
                        jtDir.setText("");
                        jtEmail.setText("");
                        jtTef.setText("");

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

                if (!jtDni.getText().isEmpty() && !jtNombre.getText().isEmpty() && !jtApellidos.getText().isEmpty() && !jtEmail.getText().isEmpty() && !jtTef.getText().isEmpty()) {

                    int tef = Integer.parseInt(jtTef.getText());

                    Alumno alumn = new Alumno(jtDni.getText(), jtNombre.getText(), jtApellidos.getText(), jtDir.getText(), jtEmail.getText(), tef);
                    ExistConnection conn = new ExistConnection();

                    Collection col = conn.Col();

                    XPathQueryService busqueda = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    ResourceSet result = busqueda.query("/Alumnos/Alumno[@DNI='" + jtDni.getText() + "']");

                    logger.log(Level.INFO, "[Gestion Alumnos] Consulta realizada: " + "/Alumnos/Alumno[@DNI='" + jtDni.getText() + "']");

                    ResourceIterator i = result.getIterator();
                    if (!i.hasMoreResources()) {
                        XPathQueryService insert = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                        insert.query("update insert " + alumn.toString() + " into /Alumnos");

                        jbModify.setEnabled(true);
                        jbDelete.setEnabled(true);

                        logger.log(Level.INFO, "[Gestion Alumnos] Insert realizada: " + "update insert " + alumn.toString() + " into /Alumnos");
                    } else {
                        JOptionPane.showMessageDialog(null, "Ya existe un alumno con ese dni", "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                    col.close();

                } else {
                    JOptionPane.showMessageDialog(null, "Faltan datos por rellenar", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Introduce un número, en teléfono por favor", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        });

        jbModify.addActionListener(e -> {

            try {

                if (!jtDni.getText().isEmpty() && !jtNombre.getText().isEmpty() && !jtApellidos.getText().isEmpty() && !jtEmail.getText().isEmpty() && !jtTef.getText().isEmpty()) {

                    int tef = Integer.parseInt(jtTef.getText());

                    ExistConnection conn = new ExistConnection();

                    Collection col = conn.Col();

                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

                    servicio.query("update value /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/nombre with data('" + jtNombre.getText() + "') ");
                    servicio.query("update value /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/apellidos with data('" + jtApellidos.getText() + "') ");
                    servicio.query("update value /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/direccion with data('" + jtDir.getText() + "') ");
                    servicio.query("update value /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/email with data('" + jtEmail.getText() + "') ");
                    servicio.query("update value /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/telefono with data('" + tef + "') ");

                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/nombre with data('" + jtNombre.getText() + "') ");
                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/apellidos with data('" + jtApellidos.getText() + "') ");
                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/direccion with data('" + jtDir.getText() + "') ");
                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/email with data('" + jtEmail.getText() + "') ");
                    logger.log(Level.INFO, "[Gestion Asignaturas] Update realizado: " + "update value /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']/telefono with data('" + tef + "') ");

                    col.close();

                } else {
                    JOptionPane.showMessageDialog(null, "Faltan datos por rellenar", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Introduce un número, en teléfono por favor", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        });

        jbDelete.addActionListener(e -> {

            try {

                if (!jtDni.getText().isEmpty()) {

                    String msgInputDialog = "¿Seguro que deseas borrar esta alumno?";
                    int input = JOptionPane.showConfirmDialog(null, msgInputDialog, "Borrar", JOptionPane.YES_NO_OPTION);

                    if (input == 0) {

                        ExistConnection conn = new ExistConnection();

                        Collection col = conn.Col();

                        XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                        servicio.query("update delete /Alumnos/Alumno[@DNI='" + jtDni.getText() + "']");

                        logger.log(Level.INFO, "[Gestion Asignaturas] Delete realizado: " + "update delete /Alumnos/Alumno[@DNI=" + jtDni.getText() + "]");

                        col.close();

                        jtDni.setText("");
                        jtNombre.setText("");
                        jtApellidos.setText("");
                        jtDir.setText("");
                        jtEmail.setText("");
                        jtTef.setText("");

                        jbModify.setEnabled(false);
                        jbDelete.setEnabled(false);

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Introduce un dni por favor.", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        });

        // otro panel de igual forma
        JPanel panel2 = new JPanel();
        panelDePestanas.addTab("Consulta de Alumnos", null, panel2, null);
        panel2.setLayout(null);

        // otra etiqueta ésta vez en el segundo panel
        JLabel lbl2 = new JLabel("BUSQUEDA POR CUALQUIER PARÁMETRO");
        lbl2.setBounds(20, 20, 480, 14);
        panel2.add(lbl2);

        // Dni Alumno
        JLabel jlDniVer = new JLabel("Dni:");
        jlDniVer.setBounds(90, 60, 200, 20);
        panel2.add(jlDniVer);
        JTextField jtDniVer = new JTextField();
        jtDniVer.setBounds(180, 60, 100, 20);
        panel2.add(jtDniVer);

        // Nombre Alumno
        JLabel jlNombreVer = new JLabel("Nombre:");
        jlNombreVer.setBounds(90, 90, 200, 20);
        panel2.add(jlNombreVer);
        JTextField jtNombreVer = new JTextField();
        jtNombreVer.setBounds(180, 90, 200, 20);
        panel2.add(jtNombreVer);

        // Apellidos Alumno
        JLabel jlApellidosVer = new JLabel("Apellidos:");
        jlApellidosVer.setBounds(90, 120, 200, 20);
        panel2.add(jlApellidosVer);
        JTextField jtApellidosVer = new JTextField();
        jtApellidosVer.setBounds(180, 120, 200, 20);
        panel2.add(jtApellidosVer);

        // Direccion Alumnos
        JLabel jlDirVer = new JLabel("Dirección:");
        jlDirVer.setBounds(90, 150, 200, 20);
        panel2.add(jlDirVer);
        JTextField jtDirVer = new JTextField();
        jtDirVer.setBounds(180, 150, 280, 20);
        panel2.add(jtDirVer);

        // Email Alumnos
        JLabel jlEmailVer = new JLabel("Email:");
        jlEmailVer.setBounds(90, 180, 200, 20);
        panel2.add(jlEmailVer);
        JTextField jtEmailVer = new JTextField();
        jtEmailVer.setBounds(180, 180, 250, 20);
        panel2.add(jtEmailVer);

        // Telefono Alumnos
        JLabel jlTefVer = new JLabel("Teléfono:");
        jlTefVer.setBounds(90, 210, 200, 20);
        panel2.add(jlTefVer);
        JTextField jtTefVer = new JTextField();
        jtTefVer.setBounds(180, 210, 100, 20);
        panel2.add(jtTefVer);

        // Resgistro
        JLabel jlReg = new JLabel("REG:");
        jlReg.setBounds(80, 240, 40, 20);
        panel2.add(jlReg);

        // Resgistro actual
        JTextField jtpag1 = new JTextField();
        jtpag1.setBounds(120, 240, 40, 20);
        jtpag1.setText("0");
        jtpag1.setHorizontalAlignment(JTextField.CENTER);
        jtpag1.setEditable(false);
        panel2.add(jtpag1);

        // Barra entre el numero de registros
        JLabel jlBarra = new JLabel("/");
        jlBarra.setBounds(170, 240, 10, 20);
        panel2.add(jlBarra);

        // Resgistros totales
        JTextField jtpag2 = new JTextField();
        jtpag2.setBounds(180, 240, 40, 20);
        jtpag2.setText("0");
        jtpag2.setHorizontalAlignment(JTextField.CENTER);
        jtpag2.setEditable(false);
        panel2.add(jtpag2);

        // Boton primer registro
        JButton jbFistReg = new JButton("|<<");
        jbFistReg.setBounds(240, 235, 55, 30);
        jbFistReg.setEnabled(false);
        panel2.add(jbFistReg);

        // Boton registro anterior
        JButton jbMesReg = new JButton("<<");
        jbMesReg.setBounds(300, 235, 55, 30);
        jbMesReg.setEnabled(false);
        panel2.add(jbMesReg);

        // Boton registro siguiente
        JButton jbSigReg = new JButton(">>");
        jbSigReg.setBounds(360, 235, 55, 30);
        jbSigReg.setEnabled(false);
        panel2.add(jbSigReg);

        // Boton ultimo registro
        JButton jbLastReg = new JButton(">>|");
        jbLastReg.setBounds(420, 235, 55, 30);
        jbLastReg.setEnabled(false);
        panel2.add(jbLastReg);

        // Boton ejecutar consulta
        JButton jbEjecuteCon = new JButton("Ejecutar Consulta");
        jbEjecuteCon.setBounds(200, 270, 280, 40);
        panel2.add(jbEjecuteCon);

        // Boton limpiar
        JButton jbLimpiarVer = new JButton("Limpiar");
        jbLimpiarVer.setBounds(80, 270, 100, 40);
        panel2.add(jbLimpiarVer);

        // Ejecución del boton consulta
        jbEjecuteCon.addActionListener(e -> {

            try {

                listaAlumnos = new ArrayList<>();
                regActual = 0;

                ExistConnection conn = new ExistConnection();

                Collection col = conn.Col();

                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

                String query = "for $alumn in /Alumnos/Alumno";

                if (!jtDniVer.getText().isEmpty()) {
                    query += " where data($alumn[@DNI='" + jtDniVer.getText() + "'])";

                } else {

                    if (!jtNombreVer.getText().isEmpty()) {
                        query += " where data($alumn/nombre) = data('" + jtNombreVer.getText() + "')";
                    }

                    if (!jtApellidosVer.getText().isEmpty() && jtNombreVer.getText().isEmpty()) {
                        query += " where data($alumn/apellidos) = data('" + jtApellidosVer.getText() + "')";

                    } else if (!jtApellidosVer.getText().isEmpty() && !jtNombreVer.getText().isEmpty()) {
                        query += " and data($alumn/apellidos) = data('" + jtApellidosVer.getText() + "')";
                    }

                    if (!jtDirVer.getText().isEmpty() && jtApellidosVer.getText().isEmpty()) {
                        query += " where data($alumn/direccion) = data('" + jtDirVer.getText() + "')";

                    } else if (!jtDirVer.getText().isEmpty() && !jtApellidosVer.getText().isEmpty()) {
                        query += " and data($alumn/direccion) = data('" + jtDirVer.getText() + "')";
                    }

                    if (!jtEmailVer.getText().isEmpty() && jtDirVer.getText().isEmpty()) {
                        query += " where data($alumn/email) = data('" + jtEmailVer.getText() + "')";

                    } else if (!jtEmailVer.getText().isEmpty() && !jtDirVer.getText().isEmpty()) {
                        query += " and data($alumn/email) = data('" + jtEmailVer.getText() + "')";
                    }

                    if (!jtTefVer.getText().isEmpty() && jtEmailVer.getText().isEmpty()) {
                        query += " where data($alumn/telefono) = data('" + jtTefVer.getText() + "')";

                    } else if (!jtTefVer.getText().isEmpty() && !jtEmailVer.getText().isEmpty()) {
                        query += " and data($alumn/telefono) = data('" + jtTefVer.getText() + "')";
                    }

                }

                query += " return <res>concat(|{data($alumn/[@DNI])}|{data($alumn/nombre)}|{data($alumn/apellidos)}|{data($alumn/direccion)}|{data($alumn/email)}|{data($alumn/telefono)}|)</res>";

                logger.log(Level.INFO, "[Gestion Alumnos] Consulta realizada: " + query);

                ResourceSet result = servicio.query(query);

                // recorrer los datos del recurso.
                ResourceIterator i = result.getIterator();
                if (!i.hasMoreResources())
                    JOptionPane.showMessageDialog(null, "No existe un alumno con esos parámetros.", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                while (i.hasMoreResources()) {
                    Resource r = i.nextResource();
                    String dato = (String) r.getContent();
                    String[] parts = dato.split("\\|");
                    listaAlumnos.add(new Alumno(parts[1], parts[2], parts[3], parts[4], parts[5], Integer.parseInt(parts[6])));
                }

                col.close();

                if (listaAlumnos.size() > 0) {

                    jtDniVer.setText(listaAlumnos.get(regActual).getDni());
                    jtNombreVer.setText(listaAlumnos.get(regActual).getNombre());
                    jtApellidosVer.setText(listaAlumnos.get(regActual).getApellidos());
                    jtDirVer.setText(listaAlumnos.get(regActual).getDireccion());
                    jtEmailVer.setText(listaAlumnos.get(regActual).getEmail());
                    jtTefVer.setText(listaAlumnos.get(regActual).getTelefono().toString());

                    jtpag1.setText(String.valueOf(regActual + 1));
                    jtpag2.setText(String.valueOf(listaAlumnos.size()));

                    if (listaAlumnos.size() > 1) {
                        jbSigReg.setEnabled(true);
                        jbLastReg.setEnabled(true);

                    } else if (listaAlumnos.size() == 1) {
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

                if (listaAlumnos.size() > 1 && listaAlumnos.size() < listaAlumnos.size() + 1) {

                    regActual++;

                    jtDniVer.setText(listaAlumnos.get(regActual).getDni());
                    jtNombreVer.setText(listaAlumnos.get(regActual).getNombre());
                    jtApellidosVer.setText(listaAlumnos.get(regActual).getApellidos());
                    jtDirVer.setText(listaAlumnos.get(regActual).getDireccion());
                    jtEmailVer.setText(listaAlumnos.get(regActual).getEmail());
                    jtTefVer.setText(listaAlumnos.get(regActual).getTelefono().toString());

                    jtpag1.setText(String.valueOf(regActual + 1));
                    jbFistReg.setEnabled(true);
                    jbMesReg.setEnabled(true);

                    if ((regActual + 1) == listaAlumnos.size()) {
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

                if (listaAlumnos.size() > 1 && listaAlumnos.size() < listaAlumnos.size() + 1) {

                    regActual = listaAlumnos.size() - 1;

                    jtDniVer.setText(listaAlumnos.get(listaAlumnos.size() - 1).getDni());
                    jtNombreVer.setText(listaAlumnos.get(listaAlumnos.size() - 1).getNombre());
                    jtApellidosVer.setText(listaAlumnos.get(listaAlumnos.size() - 1).getApellidos());
                    jtDirVer.setText(listaAlumnos.get(listaAlumnos.size() - 1).getDireccion());
                    jtEmailVer.setText(listaAlumnos.get(listaAlumnos.size() - 1).getEmail());
                    jtTefVer.setText(listaAlumnos.get(listaAlumnos.size() - 1).getTelefono().toString());

                    jtpag1.setText(String.valueOf(listaAlumnos.size()));

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

                    jtDniVer.setText(listaAlumnos.get(regActual).getDni());
                    jtNombreVer.setText(listaAlumnos.get(regActual).getNombre());
                    jtApellidosVer.setText(listaAlumnos.get(regActual).getApellidos());
                    jtDirVer.setText(listaAlumnos.get(regActual).getDireccion());
                    jtEmailVer.setText(listaAlumnos.get(regActual).getEmail());
                    jtTefVer.setText(listaAlumnos.get(regActual).getTelefono().toString());

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

                    jtDniVer.setText(listaAlumnos.get(regActual).getDni());
                    jtNombreVer.setText(listaAlumnos.get(regActual).getNombre());
                    jtApellidosVer.setText(listaAlumnos.get(regActual).getApellidos());
                    jtDirVer.setText(listaAlumnos.get(regActual).getDireccion());
                    jtEmailVer.setText(listaAlumnos.get(regActual).getEmail());
                    jtTefVer.setText(listaAlumnos.get(regActual).getTelefono().toString());

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
            jtDniVer.setText("");
            jtNombreVer.setText("");
            jtApellidosVer.setText("");
            jtDirVer.setText("");
            jtEmailVer.setText("");
            jtTefVer.setText("");

            jtpag1.setText("0");
            jtpag2.setText("0");

            jbSigReg.setEnabled(false);
            jbLastReg.setEnabled(false);
            jbFistReg.setEnabled(false);
            jbMesReg.setEnabled(false);

            listaAlumnos = new ArrayList<>();
        });

    }

}
