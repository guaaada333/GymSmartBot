package org.example.Data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.model.Reserva;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//✔ Crear el archivo reservas.json si no existe
public class JsonManager {
    private static final String FILE_PATH = "reservas.json";//Indica el archivo donde se guardan las reservas
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();//hace que el JSON quede bonito, con saltos de línea

    public static List<Reserva> cargarReservas() {//abre el archivo reservas.json
        try (FileReader reader = new FileReader(FILE_PATH)) {//lee todo el contenido
            Type listType = new TypeToken<List<Reserva>>(){}.getType();
            List<Reserva> reservas = gson.fromJson(reader, listType);//Convierte el JSON a List<Reserva>
            return reservas != null ? reservas : new ArrayList<>();//si el archivo esta vacio devuelve lista vacia
        } catch (IOException e) {//si el archivo no existe devuelve lista vacia
            return new ArrayList<>();//✔ Si hay un error → también devuelve una lista vacía
        }
    }

    public static void guardarReserva(Reserva reserva) {
        List<Reserva> reservas = cargarReservas();//lee las reservas ya hechas
        reservas.add(reserva);//agrega nueva reserva al json
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(reservas, writer);// Escribir todo el JSON
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
