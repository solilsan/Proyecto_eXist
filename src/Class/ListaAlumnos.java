package Class;

import java.util.ArrayList;
import java.util.List;

public class ListaAlumnos {

    private final List<Alumno> lista = new ArrayList<>();

    public ListaAlumnos(){
    }

    public void add(Alumno alumno) {
        lista.add(alumno);
    }

    public List<Alumno> getListaAlumnos() {
        return lista;
    }
}
