package Class;

import java.util.ArrayList;
import java.util.List;

public class ListaMatriculas {

    private final List<Matricula> lista = new ArrayList<>();

    public ListaMatriculas(){
    }

    public void add(Matricula matricula) {
        lista.add(matricula);
    }

    public List<Matricula> getMatriculas() {
        return lista;
    }

}
