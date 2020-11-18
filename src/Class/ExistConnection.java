package Class;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

import java.lang.reflect.InvocationTargetException;

public class ExistConnection {
    
    final String driver = "org.exist.xmldb.DatabaseImpl";
    final String URI = "xmldb:exist://localhost:8083/exist/xmlrpc/db/proyectoExist";
    final String usu = "admin";
    final String usuPwd = "admin";

    public Collection Col() {

        try {
            Class<?> cl = Class.forName(driver);
            Database database = (Database) cl.getDeclaredConstructor().newInstance();
            DatabaseManager.registerDatabase(database);
            return DatabaseManager.getCollection(URI, usu, usuPwd);
        }
        catch (XMLDBException e) {
            System.out.println("Error al inicializar la BD eXist: " + e.getMessage());
        }
        catch (ClassNotFoundException e) {
            System.out.println("Error en el driver.");
        }
        catch (InstantiationException | IllegalAccessException e) {
            System.out.println("Error al instanciar la BD: " + e.getMessage());
        }
        catch (InvocationTargetException | NoSuchMethodException e) {
            System.out.println("Error: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
        return null;

    }

}
