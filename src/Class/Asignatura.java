package Class;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Asignatura")
public class Asignatura {

    @XStreamAsAttribute
    @XStreamAlias("ID")
    private String id;

    private String nombre;
    private String profesor;
    private Integer horas;

    public Asignatura() {}

    public Asignatura(String id, String nombre, String profesor, Integer horas) {
        this.id = id;
        this.nombre = nombre;
        this.profesor = profesor;
        this.horas = horas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }
}
