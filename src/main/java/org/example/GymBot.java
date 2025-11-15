package org.example;
import org.example.controlador.ControladorProfesor;
import org.example.model.Profesor;
import org.example.model.Reserva;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class GymBot extends TelegramLongPollingBot {

    private final ControladorProfesor controladorProfesor = new ControladorProfesor();

    @Override
    public String getBotUsername() {
        return "AsistenteEnergyBot";
    }

    @Override
    public String getBotToken() {
        return "8402794640:AAFVC0kNprTIpNdk0T_wrjsVHMSqbz3FsL8";
    }


    @Override
    public void onUpdateReceived(Update update) {

        // ------------------ MENSAJES NORMALES ------------------
        if (update.hasMessage() && update.getMessage().hasText()) {

            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            switch (text) {
                case "/start":
                    sendMsg(chatId, "🏋️‍♀️ *Bienvenida al GymBot Smart!*\nUsá /profes para ver los profesores disponibles");
                    break;

                case "/profes":
                    mostrarProfes(chatId);
                    break;

                default:
                    sendMsg(chatId, "❌ Comando no reconocido.\nUsá /profes o /start.");
                    break;
            }
            return;
        }


        // ------------------ CALLBACK QUERIES (BOTONES) ------------------
        if (update.hasCallbackQuery()) {

            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String data = update.getCallbackQuery().getData();
            String usuario = update.getCallbackQuery().getFrom().getFirstName();

            // Usuario seleccionó un profesor
            if (data.startsWith("prof_")) {
                String nombreProfe = data.replace("prof_", "");
                Profesor profe = controladorProfesor.buscarPorNombre(nombreProfe);
                mostrarHorarios(chatId, profe);
                return;
            }

            // Usuario seleccionó un horario
            if (data.startsWith("hora_")) {

                String dataLimpia = data.replace("hora_", "");
                String[] partes = dataLimpia.split("_");

                String profe = partes[0];
                String especialidad = partes[1].replace("_", " ");

                // reconstruir el horario (todos los elementos restantes)
                StringBuilder horarioSB = new StringBuilder();
                for (int i = 2; i < partes.length; i++) {
                    horarioSB.append(partes[i]).append(" ");
                }
                String horario = horarioSB.toString().trim().replace("_", " ");

                JsonManager.guardarReserva(new Reserva(usuario, profe, horario, especialidad));

                sendMsg(chatId, "✅ *Reserva confirmada*\n\nProfesor: *" + profe +
                        "*\nEspecialidad: " + especialidad +
                        "\nHorario: " + horario);
                return;
            }


            // Botón volver
            if (data.equals("volver_profes")) {
                mostrarProfes(chatId);
            }
        }
    }

    // ------------------ MOSTRAR PROFESORES ------------------
    private void mostrarProfes(String chatId) {

        List<Profesor> lista = controladorProfesor.listarProfesores();
        List<List<InlineKeyboardButton>> botones = new ArrayList<>();

        for (Profesor p : lista) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(p.getNombre() + " (" + p.getEspecialidad() + ")");
            btn.setCallbackData("prof_" + p.getNombre());
            botones.add(List.of(btn));
        }

        SendMessage msg = new SendMessage(chatId,
                "👥 *Seleccioná un profesor para ver sus horarios:*");
        msg.enableMarkdown(true);
        msg.setReplyMarkup(new InlineKeyboardMarkup(botones));

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // ------------------ MOSTRAR HORARIOS ------------------
    private void mostrarHorarios(String chatId, Profesor profe) {

        List<List<InlineKeyboardButton>> botones = new ArrayList<>();

        for (String h : profe.getHorarios()) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(h);

            btn.setCallbackData("hora_" + profe.getNombre() + "_"
                    + profe.getEspecialidad().replace(" ", "_") + "_"
                    + h.replace(" ", "_"));

            botones.add(List.of(btn));
        }

        // botón volver
        InlineKeyboardButton volver = new InlineKeyboardButton();
        volver.setText("🔙 Volver");
        volver.setCallbackData("volver_profes");
        botones.add(List.of(volver));

        SendMessage msg = new SendMessage(chatId,
                "🧑‍🏫 *" + profe.getNombre() + "* - " + profe.getEspecialidad() +
                        "\n\nElegí un horario:");

        msg.enableMarkdown(true);
        msg.setReplyMarkup(new InlineKeyboardMarkup(botones));

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String chatId, String text) {
        SendMessage msg = new SendMessage(chatId, text);
        msg.enableMarkdown(true);

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
