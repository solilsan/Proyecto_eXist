
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import Class.*;

public class PruebasEmpleYDepart {

    public static final String nombrefichero = "AleatorioEmple.dat"; //Si tuviéramos que usar un fichero
    static Scanner teclado = new Scanner(System.in);
    static String driver = "org.exist.xmldb.DatabaseImpl"; //Driver para eXist
    static String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/ColeccionPruebas"; //URI colección   
    static String usu = "admin"; //Usuario
    static String usuPwd = "admin"; //Clave
    static Collection col = null;

    public static void main(String[] args) throws XMLDBException {

        GenerarXML.generar();

        ExistConnection a = new ExistConnection();
        col = a.Col();
        prueba();
        col.close();

        int op = 0, n = 0;
        do {
            dibujamenu();
            System.out.println("TECLEA OPERACIÓN: ");
            op = teclado.nextInt();
            switch (op) {
                case 1:
                    listardep();
                    break;
                case 2:
                	//Inserta uno fijo
                    insertardep(21, "Departamento 21","Vitoria-Gasteiz");
                    break;
                case 3: // consultar un departamento
                    System.out.println("Teclea el número de departamento a consultar: ");
                    n = teclado.nextInt();
                    consultardep(n);
                    break;
                case 4: //Modificar
                    System.out.println("Teclea el número de departamento a modificar: ");
                    n = teclado.nextInt();
                    modificardep(n);
                    break;
                case 5: // borrar un departamento
                    System.out.println("Teclea el número de departamento a borrar: ");
                    n = teclado.nextInt();
                    borrarregistrodep(n);
                    break;
                case 6: // ver empleados
                    System.out.println("Consultas de ejemplo");
                    consulta_ejemplo();
                    break;
                case 7:
                    System.out.println("Cargamos el fichero xml a nuestra colección");
                    cargar_en_coleccion();
                    break;
            }//cierra Sub menu

        } while (op != 0);

    }//fin main
////////////////////////////////////////////////////////////////////////////////////

    private static void dibujamenu() {
        System.out.println("............................................................\n"
                + ".  1 Listar todos los departamentos. \n"
                + ".  2 Insertar un departamento (inserta 21, 'Departamento 21','Vitoria-Gasteiz'.  \n"
                + ".  3 Consultar un departamento.\n"
                + ".  4 Modificar un departamento (cambia el nombre por 'NUEVO NOMBRE').\n"
                + ".  5 Borrar un departamento.\n"
                + ".  6 Consultas de ejemplo.\n"
                + ".  7 Cargar fichero xml en colección.\n"
                + ".  0 SALIR.\n"
                + "............................................................\n");

    } // fin dibujamenu

    public static Collection conectar() {

        try {
            Class<?> cl = Class.forName(driver); //Cargar del driver
            Database database = (Database) cl.getDeclaredConstructor().newInstance(); //Instancia de la BD
            DatabaseManager.registerDatabase(database); //Registro del driver
            col = DatabaseManager.getCollection(URI, usu, usuPwd);
            return col;
        } catch (XMLDBException e) {
            System.out.println("Error al inicializar la BD eXist.");
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error en el driver.");
            //e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("Error al instanciar la BD.");
            //e.printStackTrace();
        }
        catch (InvocationTargetException | NoSuchMethodException e) {
            System.out.println("Error: " + e.getMessage());
            //e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
        return null;
    }

    private static void listardep() {
        if (conectar() != null) {
            try {
                XPathQueryService servicio;
                servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Preparamos la consulta
                ResourceSet result = servicio.query("for $dep in /departamentos/DEP_ROW return $dep");
                // recorrer los datos del recurso.  
                ResourceIterator i;
                i = result.getIterator();
                if (!i.hasMoreResources()) {
                    System.out.println(" LA CONSULTA NO DEVUELVE NADA O ESTÁ MAL ESCRITA");
                }
                while (i.hasMoreResources()) {
                    Resource r = i.nextResource();
                    System.out.println("--------------------------------------------");
                    System.out.println((String) r.getContent());
                }
                col.close();
            } catch (XMLDBException e) {
                System.out.println(" ERROR AL CONSULTAR DOCUMENTO.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }

    }

    private static void insertardep(int dep, String dnombre, String loc) {

        //Caso concreto: sabemos cuáles son los nodos
        String nuevodep = "<DEP_ROW><DEPT_NO>" + dep + "</DEPT_NO>"
                + "<DNOMBRE>" + dnombre + "</DNOMBRE><LOC>" + loc + "</LOC>"
                + "</DEP_ROW>";

        if (conectar() != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                System.out.printf("Inserto: %s \n", nuevodep);
                //Consulta para insertar --> update insert ... into
                ResourceSet result = servicio.query("update insert " + nuevodep + " into /departamentos");
                col.close(); //borramos 
                System.out.println("Dep insertado.");
            } catch (Exception e) {
                System.out.println("Error al insertar empleado.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
    }

    private static void consultardep(int dep) {
        if (comprobardep(dep)) {
            if (conectar() != null) {
                try {
                    XPathQueryService servicio;
                    servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    //Preparamos la consulta
                    ResourceSet result = servicio.query("/departamentos/DEP_ROW[DEPT_NO=" + dep + "]");
                    // recorrer los datos del recurso.  
                    ResourceIterator i;
                    i = result.getIterator();
                    if (!i.hasMoreResources()) {
                        System.out.println(" LA CONSULTA NO DEVUELVE NADA O ESTÁ MAL ESCRITA");
                    } else {
                        Resource r = i.nextResource();
                        System.out.println("--------------------------------------------");
                        System.out.println((String) r.getContent());
                    }
                    col.close();
                } catch (XMLDBException e) {
                    System.out.println(" ERROR AL CONSULTAR DOCUMENTO.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("El departamento NO EXISTE.");
        }

    }

    private static void modificardep(int dep) {

        if (comprobardep(dep)) {

            if (conectar() != null) {
                try {
                    System.out.printf("Actualizo el departamento: %s\n", dep);
                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    //Consulta para modificar/actualizar un valor --> update value
                    ResourceSet result = servicio.query(
                            "update value /departamentos/DEP_ROW[DEPT_NO=" + dep + "]/DNOMBRE with data('NUEVO NOMBRE') ");

                    col.close();
                    System.out.println("Dep actualizado.");
                } catch (Exception e) {
                    System.out.println("Error al actualizar.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("El departamento NO EXISTE.");
        }
    }

    private static void borrarregistrodep(int dep) {
        if (comprobardep(dep)) {
            if (conectar() != null) {
                try {
                    System.out.printf("Borro el departamento: %s\n", dep);
                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    //Consulta para borrar un departamento --> update delete
                    ResourceSet result = servicio.query(
                            "update delete /departamentos/DEP_ROW[DEPT_NO=" + dep + "]");
                    col.close();
                    System.out.println("Dep borrado.");
                } catch (Exception e) {
                    System.out.println("Error al borrar.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("El departamento NO EXISTE.");
        }

    }

    private static boolean comprobardep(int dep) {
        //Devuelve true si el dep existe
        if (conectar() != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                ResourceSet result = servicio.query("/departamentos/DEP_ROW[DEPT_NO=" + dep + "]");
                ResourceIterator i;
                i = result.getIterator();
                col.close();
                if (!i.hasMoreResources()) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Error al consultar.");
                // e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }

        return false;

    }// comprobardep

    public static void consulta_ejemplo() throws XMLDBException  {
        String driver = "org.exist.xmldb.DatabaseImpl"; //Driver para eXist
        Collection col = null; // Colección
        String URI="xmldb:exist://localhost:8083/exist/xmlrpc/db/ColeccionPruebas"; //URI colección
        String usu="admin"; //Usuario
        String usuPwd="admin"; //Clave

        String nuevodep = "<DEP_ROW><DEPT_NO>" + "212" + "</DEPT_NO>"
                + "<DNOMBRE>" + "DepNuevo" + "</DNOMBRE><LOC>" + "LocNueva" + "</LOC>"
                + "</DEP_ROW>";
        try {
            Class<?> cl = Class.forName(driver); //Cargar del driver
            Database database = (Database) cl.getDeclaredConstructor().newInstance(); //Instancia de la BD
            DatabaseManager.registerDatabase(database); //Registro del driver

            col = DatabaseManager.getCollection(URI, usu, usuPwd);
            if(col == null)
                System.out.println(" *** LA COLECCION NO EXISTE. ***");

            XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
            //ResourceSet result = servicio.query ("for $em in /EMPLEADOS/EMP_ROW[DEPT_NO=10] return $em");
            //String query="for $em in doc("file:///C:/users/usuario/Desktop/empleados.xml")/EMPLEADOS/EMP_ROW[DEPT_NO=10] return $em";
            String query = "for $emp in /EMPLEADOS/EMP_ROW let $nom:=$emp/APELLIDO, $ofi:=$emp/OFICIO order by $emp/OFICIO return <APE_OFI> {($nom, ' ', $ofi)} </APE_OFI>";
            System.out.println(query);
            ResourceSet result = servicio.query (query);
            System.out.println("Se han obtenido " + result.getSize() + " elementos.");
            // recorrer los datos del recurso.
            ResourceIterator i;
            i = result.getIterator();
            if (!i.hasMoreResources())
                System.out.println(" LA CONSULTA NO DEVUELVE NADA O ESTÁ MAL ESCRITA");
            while (i.hasMoreResources()) {
                Resource r = i.nextResource();
                System.out.println((String) r.getContent());}

            col.close(); //cerramos
        } catch (Exception e) {
            System.out.println("Error al inicializar la BD eXist");
            e.printStackTrace();    }
    }// FIN consulta_ejemplo

    public static void cargar_en_coleccion() throws XMLDBException  {
        //Devuelve true si el dep existe
        if (conectar() != null) {
            try {
                System.out.printf("Conecta");
                // Inicializamos el recurso
                XMLResource res = null;

                // Creamos el recurso -> recibe 2 parámetros tipo String:
                // s: nombre.xml (si lo dejamos null, pondrá un nombre aleatorio)
                // s1: tipo recurso (en este caso, siempre será XMLResource)
                res = (XMLResource)col.createResource("dep_cargado.xml", "XMLResource");

                // Elegimos el fichero .xml que queremos añadir a la colección
                File f = new File(".\\ficheros\\dep.xml");

                // Fijamos como contenido ese archivo .xml elegido
                res.setContent(f);
                col.storeResource(res); // lo añadimos a la colección

                // Listamos la colección para ver que en efecto se ha añadido
                for (String colRe: col.listResources())
                    System.out.println(colRe);

                col.close();
            } catch (Exception e) {
                System.out.println("Error al consultar.");
                // e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
    }// FIN cargar_en_coleccion()

    public static void prueba() {

            try {
                XPathQueryService servicio;
                servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Preparamos la consulta
                ResourceSet result = servicio.query("for $dep in /departamentos/DEP_ROW return $dep");
                // recorrer los datos del recurso.
                ResourceIterator i;
                i = result.getIterator();
                if (!i.hasMoreResources()) {
                    System.out.println(" LA CONSULTA NO DEVUELVE NADA O ESTÁ MAL ESCRITA");
                }
                while (i.hasMoreResources()) {
                    Resource r = i.nextResource();
                    System.out.println("--------------------------------------------");
                    System.out.println((String) r.getContent());
                }
                col.close();
            } catch (XMLDBException e) {
                System.out.println(" ERROR AL CONSULTAR DOCUMENTO.");
                e.printStackTrace();
            }


    }

}// fin clase

