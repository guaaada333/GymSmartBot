package org.example.controlador;
import org.example.ProfesorData;
import org.example.model.Profesor;
import java.util.List;
import org.example.model.Profesor;

import java.util.ArrayList;
import java.util.List;

public class ControladorProfesor {

    // devolver todos los profes
    public List<Profesor> listarProfesores() {
        return ProfesorData.PROFESORES;
    }

    // buscar por nombre exacto
    public Profesor buscarPorNombre(String nombre) {
        for (Profesor p : ProfesorData.PROFESORES) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    // buscar por especialidad Yoga, Crossfit, etc.
    public List<Profesor> buscarPorEspecialidad(String especialidad) {
        List<Profesor> resultado = new ArrayList<>();

        for (Profesor p : ProfesorData.PROFESORES) {
            if (p.getEspecialidad().equalsIgnoreCase(especialidad)) {
                resultado.add(p);
            }
        }

        return resultado;
    }
}