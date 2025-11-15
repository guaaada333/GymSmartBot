package org.example.model;
//modelo de reserva
public class Reserva {
    private String usuario;//el nombre del usuario que realiza la reserva
    private String profesor;//profe con el que realiza la reserva
    private String horario;//horario que elige el usuario
    private String especialidad;//el tipo de clase que eligio el usuario

    public Reserva(String usuario, String profesor, String horario, String especialidad) {
        this.usuario = usuario;
        this.profesor = profesor;
        this.horario = horario;
        this.especialidad=especialidad;
    }
//getters
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
