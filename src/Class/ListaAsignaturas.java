package Class;

import java.util.ArrayList;
import java.util.List;

public class ListaAsignaturas {

    private final List<Asignatura> lista = new ArrayList<>();

    public ListaAsignaturas(){
    }

    public void add(Asignatura asignatura) {
        lista.add(asignatura);
    }

    public List<Asignatura> getListaAsignaturas() {
        return lista;
    }

}
