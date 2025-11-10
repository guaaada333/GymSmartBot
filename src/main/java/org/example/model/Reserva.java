package org.example;

public class Reserva {
    private String usuario;
    private String profesor;
    private String horario;
    private String especialidad;

    public Reserva(String usuario, String profesor, String horario, String especialidad) {
        this.usuario = usuario;
        this.profesor = profesor;
        this.horario = horario;
        this.especialidad=especialidad;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getProfesor() {
        return profesor;
    }

    public String getHorario() {
        return horario;
    }
    public String getEspecialidad() {
        return especialidad;
    }
}
