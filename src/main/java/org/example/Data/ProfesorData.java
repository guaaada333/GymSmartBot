package org.example.Data;
import org.example.model.Profesor;
import java.util.List;

//aca es donde se carga la lista de profesores y sus horarios
public class ProfesorData {

    public static final List<Profesor> PROFESORES = List.of(
            new Profesor("Marcos", "Entrenamiento funcional",
                    List.of("Lunes 9:00 - 11:00", "Miércoles 14:00 - 16:00", "Viernes 18:00 - 20:00")),
            new Profesor("Luna", "Yoga y estiramiento",
                    List.of("Martes 10:00 - 12:00", "Jueves 17:00 - 19:00")),
            new Profesor("Tomás", "Musculación y fuerza",
                    List.of("Lunes 15:00 - 17:00", "Miércoles 9:00 - 11:00", "Viernes 10:00 - 12:00")),
            new Profesor("Mariano", "Crossfit",
                    List.of("Lunes 18:00-21:00", "Martes 18:00-21:00",
                            "Miércoles 18:00-21:00", "Jueves 18:00-21:00",
                            "Viernes 18:00-21:00")),
            new Profesor("Sofía", "Pilates",
                    List.of("Lunes 8:00 - 10:00", "Miércoles 17:00 - 19:00", "Viernes 9:00 - 11:00"))
    );
}
