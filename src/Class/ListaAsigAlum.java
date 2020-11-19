package Class;

import java.util.ArrayList;
import java.util.List;

public class ListaAsigAlum {

    private final List<AsigAlum> lista = new ArrayList<>();

    public ListaAsigAlum(){
    }

    public void add(AsigAlum asigAlum) {
        lista.add(asigAlum);
    }

    public List<AsigAlum> getListaAsigAlum() {
        return lista;
    }

}
