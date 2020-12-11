package Views;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Logger;

public class VentanaPrincipal extends JFrame {

    private JPanel panel1;
    private JLabel back;
    Logger logger;

    public VentanaPrincipal(Logger logger) {

        ImageIcon imgIcon = new ImageIcon("resources/x.png");
        back.setIcon(imgIcon);

        add(panel1);

        createMenuBar();
        setTitle("Gestión del Colegio");
        setResizable(false);
        setSize(1024, 720);

        this.logger = logger;

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

    }

    private void createMenuBar() {

        var menuBar = new JMenuBar();

        var baseDatosMenu = new JMenu("Cerrar"); //Nombre del menu
        baseDatosMenu.setMnemonic(KeyEvent.VK_F); //Acceder al menu mediante el teclado alt+f

        var baseDatosMenuItem = new JMenuItem("Salir"); //Nombre del boton
        baseDatosMenuItem.setMnemonic(KeyEvent.VK_E); //Acceder al menu mediante el teclado alt+f
        baseDatosMenuItem.setToolTipText("Exit application"); //Hover del boton
        baseDatosMenuItem.addActionListener((event) -> System.exit(0)); //Funcion que ejecuta el boton

        baseDatosMenu.add(baseDatosMenuItem);
        menuBar.add(baseDatosMenu);

        var asignaturasMenu = new JMenu("Asignaturas");
        menuBar.add(asignaturasMenu);

        var asignaturasGestionItem = new JMenuItem("Gestión de Asignaturas");
        asignaturasGestionItem.addActionListener(
                (event) -> {
                    VentanaGestionAsignaturas vga = new VentanaGestionAsignaturas(logger);
                    vga.setLocationRelativeTo(null);
                    vga.setVisible(true);
                }
        );

        asignaturasMenu.add(asignaturasGestionItem);

        var alumnosMenu = new JMenu("Alumnos");
        menuBar.add(alumnosMenu);

        var alumnosGestionItem = new JMenuItem("Gestión de Alumnos");
        alumnosGestionItem.addActionListener(
                (event) -> {
                    VentanaGestionAlumnos vga = new VentanaGestionAlumnos(logger);
                    vga.setLocationRelativeTo(null);
                    vga.setVisible(true);
                }
        );

        alumnosMenu.add(alumnosGestionItem);

        var logMenu = new JMenu("Logs");
        menuBar.add(logMenu);

        var verLogItem = new JMenuItem("Ver logs");
        verLogItem.addActionListener(
                (event) -> {

                    try {

                        File f = new File("ficheros/log_actividad.txt");

                        if (!f.exists() || f.length() == 0) {
                            JOptionPane.showMessageDialog(null, "El archivo no existe o esta vacío.", "Información",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            BufferedReader b = new BufferedReader(new FileReader(f));
                            VentanaLog vl = new VentanaLog(b);
                            vl.setLocationRelativeTo(null);
                            vl.setVisible(true);
                        }

                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                }
        );

        logMenu.add(verLogItem);

        setJMenuBar(menuBar);
    }

}
