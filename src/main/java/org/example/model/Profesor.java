package org.example.model;
import java.util.List;
//modelo de el objeto profesor
public class Profesor {
    private String nombre;
    private String especialidad;
    private List<String> horarios; // lista de días y horas


    public Profesor(String nombre, String especialidad, List<String> horarios) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.horarios = horarios;

    }
    //getters and setters
    public String getNombre() {
        return nombre;
    }
    public String getEspecialidad() {
        return especialidad;
    }
    public List<String> getHorarios() {
        return horarios;
    }
}
