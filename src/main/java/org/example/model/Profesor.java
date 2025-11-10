package org.example.model;

import java.util.List;

public class Profesor {
    private String nombre;
    private String especialidad;
    private List<String> horarios; // NUEVO: lista de días y horas


    public Profesor(String nombre, String especialidad, List<String> horarios) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.horarios = horarios;

    }
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
