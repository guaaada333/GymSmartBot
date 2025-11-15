package org.example.controlador;
import org.example.Data.ProfesorData;
import org.example.model.Profesor;
import java.util.List;
import java.util.ArrayList;

//controlador se encarga de la logica de manejar los profesores
public class ControladorProfesor {

    // Devuelve todos los profesores cargados en ProfesorData,al utilizar la funcion /profes es utilizado
    public List<Profesor> listarProfesores() {
        return ProfesorData.PROFESORES;
    }

    // la clase principal gymBot usa este metodo para buscar por nombre exacto
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