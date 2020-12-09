package Class;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.XMLResource;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GenerarXML {

    static XStream xstream = new XStream(new DomDriver("UTF-8"));

    public static Boolean comprobarXML() {

        boolean existen = true;

        File alumnos = new File("./ficheros/Alumnos.xml");
        File asignaturas = new File("./ficheros/Asignaturas.xml");
        File asigAlum = new File("./ficheros/AsigAlum.xml");

        if (!alumnos.exists() || !asignaturas.exists() || !asigAlum.exists()) {
            existen = false;
        }

        return existen;

    }

    public static Writer xml(String url) throws IOException {

        FileOutputStream xml = new FileOutputStream(url);
        Writer writer = new OutputStreamWriter(xml, StandardCharsets.UTF_8);
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        return writer;

    }

    public static String generarCargarCol() {

        try {

            ExistConnection conn = new ExistConnection();

            generarAlumnos();
            cargarEnColeccion(conn, "./ficheros/Alumnos.xml", "Alumnos.xml");
            generarAsinaturas();
            cargarEnColeccion(conn, "./ficheros/Asignaturas.xml", "Asignaturas.xml");
            generarAsigAlum();
            cargarEnColeccion(conn, "./ficheros/AsigAlum.xml", "AsigAlum.xml");

            return("exito");

        }
        catch (Exception e) {
            return("Error general: " + e.getMessage());
        }

    }

    public static void generarAlumnos() throws IOException {

        ListaAlumnos listaAlumnos = new ListaAlumnos();

        listaAlumnos.add(new Alumno("12345678Z", "Alex", "Ruiz", "Calle Alfonso Nª1", "alex@gmail.com", 610342952));
        listaAlumnos.add(new Alumno("12345678A", "Pepe", "Calvo", "Calle Medron Nª4", "pepe@gmail.com", 647384829));
        listaAlumnos.add(new Alumno("12345678Q", "Juan", "Mendoza", "Calle Holy Nª2", "juan@gmail.com", 945364738));
        listaAlumnos.add(new Alumno("12345678X", "Lucas", "Federico", "Calle Fuelles Nª10", "lucas@gmail.com", 645829301));
        listaAlumnos.add(new Alumno("12345678D", "Marcos", "Garcia", "Calle Nonas Nª9", "marcos@gmail.com", 945839402));

        xstream.alias("Alumnos", ListaAlumnos.class);
        xstream.processAnnotations(Alumno.class);
        xstream.addImplicitCollection(ListaAlumnos.class, "lista");
        xstream.toXML(listaAlumnos, xml("./ficheros/Alumnos.xml"));

    }

    public static void generarAsinaturas() throws IOException {

        ListaAsignaturas listaAsignaturas = new ListaAsignaturas();

        listaAsignaturas.add(new Asignatura("1", "Matemáticas", "Álvaro", 210));
        listaAsignaturas.add(new Asignatura("2", "Lenguaje", "María", 180));
        listaAsignaturas.add(new Asignatura("3", "Ciencias", "Gorka", 200));
        listaAsignaturas.add(new Asignatura("4", "Programación", "Ekaitz", 220));

        xstream.alias("Asignaturas", ListaAsignaturas.class);
        xstream.processAnnotations(Asignatura.class);
        xstream.addImplicitCollection(ListaAsignaturas.class, "lista");
        xstream.toXML(listaAsignaturas, xml("./ficheros/Asignaturas.xml"));

    }

    public static void generarAsigAlum() throws IOException {

        ListaAsigAlum listaAsigAlum = new ListaAsigAlum();

        listaAsigAlum.add(new AsigAlum("12345678Z", "4", 9.5));
        listaAsigAlum.add(new AsigAlum("12345678X", "2", 7.0));
        listaAsigAlum.add(new AsigAlum("12345678D", "1", 8.3));

        xstream.alias("AsigAlum", ListaAsigAlum.class);
        xstream.processAnnotations(AsigAlum.class);
        xstream.addImplicitCollection(ListaAsigAlum.class, "lista");
        xstream.toXML(listaAsigAlum, xml("./ficheros/AsigAlum.xml"));

    }

    public static void cargarEnColeccion(ExistConnection conn, String url, String nombreFichero) {

        try {

            Collection col = conn.Col();

            XMLResource res = (XMLResource) col.createResource(nombreFichero, "XMLResource");

            File f = new File(url);

            res.setContent(f);
            col.storeResource(res);

            col.close();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static Collection listaRecursos() {

        try {

            ExistConnection conn = new ExistConnection();
            return conn.Col();

        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

}
