package org.example;

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

public class JsonManager {
    private static final String FILE_PATH = "reservas.json";
    // Gson con formato legible
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Reserva> cargarReservas() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Reserva>>(){}.getType();
            List<Reserva> reservas = gson.fromJson(reader, listType);
            return reservas != null ? reservas : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void guardarReserva(Reserva reserva) {
        List<Reserva> reservas = cargarReservas();
        reservas.add(reserva);
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(reservas, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
