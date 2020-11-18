package Class;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.*;

public class GenerarXML {

    static XStream xstream = new XStream(new DomDriver("UTF-8"));

    public static Writer xml(String url) throws IOException {

        FileOutputStream xml = new FileOutputStream(url);
        Writer writer = new OutputStreamWriter(xml, "UTF-8");
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        return writer;

    }

    public static void generar() {

        try {

            generarAlumnos();

        }
        catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }

    }

    public static void generarAlumnos() throws IOException {

        Alumno alumno = new Alumno("12345678Z", "Alex", "Ruiz", "Calle Alfonso Nª1", "alex@gmail.com", 610342952);
        ListaAlumnos listaAlumnos = new ListaAlumnos();

        listaAlumnos.add(alumno);

        xstream.alias("Alumnos", ListaAlumnos.class);
        xstream.processAnnotations(Alumno.class);
        xstream.addImplicitCollection(ListaAlumnos.class, "lista");
        xstream.toXML(listaAlumnos, xml("./ficheros/Alumnos.xml"));

    }

}
