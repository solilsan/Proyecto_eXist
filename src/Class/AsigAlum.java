package Class;

public class AsigAlum {

    private String dniAlumno;
    private String idAsignatura;
    private Double nota;

    public AsigAlum() {}

    public AsigAlum(String dniAlumno, String idAsignatura, Double nota) {
        this.dniAlumno = dniAlumno;
        this.idAsignatura = idAsignatura;
        this.nota = nota;
    }

    public String getDniAlumno() {
        return dniAlumno;
    }

    public void setDniAlumno(String dniAlumno) {
        this.dniAlumno = dniAlumno;
    }

    public String getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(String idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
}
