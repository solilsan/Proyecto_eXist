package Views;

import Class.Alumno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaListaAlumnosAsignatura extends JFrame {

    private JPanel panel1;
    private JTable tableAlumnos;

    private List<Alumno> listaAlumnos;

    public VentanaListaAlumnosAsignatura(List<Alumno> listaAlumnos) {

        add(panel1);
        setResizable(false);
        setTitle("Lista de alumnos ");
        setBounds(100, 100, 1000, 600);

        this.listaAlumnos = listaAlumnos;
        datosTabla();

    }

    private void datosTabla() {

        DefaultTableModel modeloTablaCliente = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloTablaCliente.setColumnIdentifiers(new Object[]{
                "Dni",
                "Nombre",
                "Apellidos",
                "Direccion",
                "Email",
                "Teléfono"
        });

        for (Alumno listaAlumn : listaAlumnos) {

            modeloTablaCliente.addRow(new Object[]{
                    listaAlumn.getDni(),
                    listaAlumn.getNombre(),
                    listaAlumn.getApellidos(),
                    listaAlumn.getDireccion(),
                    listaAlumn.getEmail(),
                    listaAlumn.getTelefono()
            });

            tableAlumnos.setModel(modeloTablaCliente);
        }

    }

}
