package Class;

import java.util.ArrayList;
import java.util.List;

public class ListaMatriculas {

    private final List<Matricula> lista = new ArrayList<>();

    public ListaMatriculas(){
    }

    public void add(Matricula asigAlum) {
        lista.add(asigAlum);
    }

    public List<Matricula> getListaAsigAlum() {
        return lista;
    }

}
