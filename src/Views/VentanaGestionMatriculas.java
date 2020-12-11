package Views;

import Class.Alumno;
import Class.Asignatura;
import Class.ExistConnection;
import Class.Matricula;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XPathQueryService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentanaGestionMatriculas extends JFrame {

    private List<Matricula> listaMatriculas = new ArrayList<>();
    private List<Asignatura> listaAsignaturas = new ArrayList<>();
    private List<Alumno> listaAlumnos = new ArrayList<>();
    private int regActual;
    Logger logger;

    JComboBox<String> comboAsig = new JComboBox<>();
    JComboBox<String> comboAlumn = new JComboBox<>();

    JTextField jtNota = new JTextField();

    JButton jbEjecuteCon = new JButton("Matricular");
    JButton jbCambiarNota = new JButton("Cambiar Nota");

    public VentanaGestionMatriculas(Logger logger) {

        JPanel panel1 = new JPanel();
        panel1.setLayout(null);

        JLabel jlTitulo = new JLabel("Matricula de alumnos en asignaturas");
        jlTitulo.setBounds(180, 20, 348, 20);
        panel1.add(jlTitulo);

        // Nombre Asignatura
        JLabel jlCod = new JLabel("Asignatura:");
        jlCod.setBounds(140, 100, 200, 20);
        panel1.add(jlCod);

        // Combo Asignaturas
        comboAsig.setBounds(250, 100, 200, 20);

        datosAsig();

        for (Asignatura listaA : listaAsignaturas) {
            comboAsig.addItem(listaA.getNombre());
        }

        panel1.add(comboAsig);

        // Dni Alumnos
        JLabel jlDni = new JLabel("Alumno:");
        jlDni.setBounds(140, 140, 200, 20);
        panel1.add(jlDni);

        // Combo Asignaturas
        comboAlumn.setBounds(250, 140, 200, 20);

        datosAlumn();

        for (Alumno listaAl : listaAlumnos) {
            comboAlumn.addItem(listaAl.getDni());
        }

        panel1.add(comboAlumn);

        // Nota Alumno
        JLabel jlNota = new JLabel("Nota:");
        jlNota.setBounds(140, 180, 200, 20);
        panel1.add(jlNota);
        jtNota.setBounds(250, 180, 50, 20);

        panel1.add(jtNota);

        // Boton matricular
        jbEjecuteCon.setBounds(320, 250, 150, 40);
        jbEjecuteCon.setEnabled(false);
        panel1.add(jbEjecuteCon);

        // Boton cambiar nota
        jbCambiarNota.setBounds(120, 250, 150, 40);
        jbCambiarNota.setEnabled(false);
        panel1.add(jbCambiarNota);

        add(panel1);
        setResizable(false);
        setTitle("Gestión de Matriculas");
        setBounds(100, 100, 600, 400);

        this.logger = logger;

        listarNota();

        comboAlumn.addActionListener (e -> listarNota());
        comboAsig.addActionListener (e -> listarNota());

        jbEjecuteCon.addActionListener(e -> {

            try {

                Matricula matri = new Matricula(listaAlumnos.get(comboAlumn.getSelectedIndex()).getDni(), listaAsignaturas.get(comboAsig.getSelectedIndex()).getId(), 0.0);

                ExistConnection conn = new ExistConnection();

                Collection col = conn.Col();

                XPathQueryService busqueda = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                ResourceSet result = busqueda.query("/Matriculas/Matricula[dniAlumno='" + listaAlumnos.get(comboAlumn.getSelectedIndex()).getDni() + "' and idAsignatura='" + listaAsignaturas.get(comboAsig.getSelectedIndex()).getId() + "']");

                ResourceIterator i = result.getIterator();
                if (!i.hasMoreResources()) {
                    XPathQueryService insert = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    insert.query("update insert " + matri.toString() + " into /Matriculas");

                    listarNota();

                    logger.log(Level.INFO, "[Gestion Matriculas] Insert realizado: " + "update insert " + matri.toString() + " into /Matriculas");

                } else {
                    JOptionPane.showMessageDialog(null, "Ese alumno ya esta matricula en ese asignatura", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                col.close();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        });

        jbCambiarNota.addActionListener(e -> {

            try {

                if (!jtNota.getText().isEmpty()) {

                    double nota = Double.parseDouble(jtNota.getText());

                    ExistConnection conn = new ExistConnection();

                    Collection col = conn.Col();

                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

                    servicio.query("update value /Matriculas/Matricula[dniAlumno='" + listaAlumnos.get(comboAlumn.getSelectedIndex()).getDni() + "' and idAsignatura='" + listaAsignaturas.get(comboAsig.getSelectedIndex()).getId() + "']/nota with data('" + nota + "')");

                    logger.log(Level.INFO, "[Gestion Matriculas] Update realizado: " + "update value /Matriculas/Matricula[dniAlumno='" + listaAlumnos.get(comboAlumn.getSelectedIndex()).getDni() + "' and idAsignatura='" + listaAsignaturas.get(comboAsig.getSelectedIndex()).getId() + "']/nota with data('" + nota + "')");

                    col.close();

                } else {
                    JOptionPane.showMessageDialog(null, "Introduce una nota por favor", "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Introduce un número, en nota por favor", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        });

    }

    private void datosAsig() {

        try {

            ExistConnection conn = new ExistConnection();

            Collection col = conn.Col();

            XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

            String query = "for $asig in /Asignaturas/Asignatura return <res>concat(|{data($asig/[@ID])}|{data($asig/nombre)}|{data($asig/profesor)}|{data($asig/horas)}|)</res>";

            ResourceSet result = servicio.query(query);

            ResourceIterator i = result.getIterator();

            while (i.hasMoreResources()) {
                Resource r = i.nextResource();
                String dato = (String) r.getContent();
                String[] parts = dato.split("\\|");
                listaAsignaturas.add(new Asignatura(parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
            }

            col.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void datosAlumn() {

        try {

            ExistConnection conn = new ExistConnection();

            Collection col = conn.Col();

            XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

            String query = "for $alumn in /Alumnos/Alumno return <res>concat(|{data($alumn/[@DNI])}|{data($alumn/nombre)}|{data($alumn/apellidos)}|{data($alumn/direccion)}|{data($alumn/email)}|{data($alumn/telefono)}|)</res>";

            ResourceSet result = servicio.query(query);

            ResourceIterator i = result.getIterator();

            while (i.hasMoreResources()) {
                Resource r = i.nextResource();
                String dato = (String) r.getContent();
                String[] parts = dato.split("\\|");
                listaAlumnos.add(new Alumno(parts[1], parts[2], parts[3], parts[4], parts[5], Integer.parseInt(parts[6])));
            }

            col.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void datosMatri() {

        try {

            ExistConnection conn = new ExistConnection();

            Collection col = conn.Col();

            XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

            String query = "for $matri in /Matriculas/Matricula return <res>concat(|{data($matri/dniAlumno)}|{data($matri/idAsignatura)}|{data($matri/nota)}|)</res>";

            ResourceSet result = servicio.query(query);

            ResourceIterator i = result.getIterator();

            while (i.hasMoreResources()) {
                Resource r = i.nextResource();
                String dato = (String) r.getContent();
                String[] parts = dato.split("\\|");
                listaMatriculas.add(new Matricula(parts[1], parts[2], Double.parseDouble(parts[3])));
            }

            col.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void listarNota() {

        try {

            datosMatri();

            ExistConnection conn = new ExistConnection();

            Collection col = conn.Col();

            XPathQueryService busqueda = (XPathQueryService) col.getService("XPathQueryService", "1.0");
            ResourceSet result = busqueda.query("/Matriculas/Matricula[dniAlumno='" + listaAlumnos.get(comboAlumn.getSelectedIndex()).getDni() + "' and idAsignatura='" + listaAsignaturas.get(comboAsig.getSelectedIndex()).getId() + "']");

            ResourceIterator i = result.getIterator();
            if (i.hasMoreResources()) {

                jbCambiarNota.setEnabled(true);
                jbEjecuteCon.setEnabled(false);

                for (Matricula listaM : listaMatriculas) {

                    if (listaM.getDniAlumno().equals(listaAlumnos.get(comboAlumn.getSelectedIndex()).getDni()) && listaM.getIdAsignatura().equals(listaAsignaturas.get(comboAsig.getSelectedIndex()).getId())) {
                        jtNota.setText(listaM.getNota().toString());
                    }

                }

            } else {
                jbEjecuteCon.setEnabled(true);
                jbCambiarNota.setEnabled(false);
                jtNota.setText("");
            }

            col.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
