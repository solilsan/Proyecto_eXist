package Views;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class VentanaPrincipal extends JFrame {

    private JPanel panel1;
    private JLabel back;

    public VentanaPrincipal() {

        //ImageIcon imgIcon = new ImageIcon("resources/ventanaprincipalbg.png");
        //back.setIcon(imgIcon);

        add(panel1);

        createMenuBar();
        setTitle("Gestión del Colegio");
        setResizable(false);
        setSize(1024, 720);

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

        var proveedoresMenu = new JMenu("Asignaturas");
        menuBar.add(proveedoresMenu);

        var proveedoresGestionItem = new JMenuItem("Gestión de Asignaturas");
        proveedoresGestionItem.addActionListener(
                (event) -> {
                    VentanaGestionAsignaturas vga = new VentanaGestionAsignaturas();
                    vga.setLocationRelativeTo(null);
                    vga.setVisible(true);
                }
        );

        proveedoresMenu.add(proveedoresGestionItem);

        setJMenuBar(menuBar);
    }

}
