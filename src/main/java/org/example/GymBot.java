package org.example;

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

public class GymBot extends TelegramLongPollingBot {// significa que hereda el comportamiento de un bot que escucha mensajes de Telegram (librería telegrambots).

    private final List<Profesor> profesores = List.of(//LISTA DE PROFESORES CARGADOS
            new Profesor("Marcos", "Entrenamiento funcional",
                    List.of("Lunes 9:00 - 11:00", "Miércoles 14:00 - 16:00", "Viernes 18:00 - 20:00")),
            new Profesor("Luna", "Yoga y estiramiento",
                    List.of("Martes 10:00 - 12:00", "Jueves 17:00 - 19:00")),
            new Profesor("Tomás", "Musculación y fuerza",
                    List.of("Lunes 15:00 - 17:00", "Miércoles 9:00 - 11:00", "Viernes 10:00 - 12:00")),
            new Profesor("Mariano", "Crossfit",
                    List.of("Lunes 18:00-21:00 " ," Martes 18:00-21:00 ","Miercoles 18:00-21:00","Jueves 18:00-21:00","Viernes 18:00-21:00")),
            new Profesor("Sofía", "Pilates",
                    List.of("Lunes 8:00 - 10:00", "Miércoles 17:00 - 19:00", "Viernes 9:00 - 11:00"))


    );

    @Override
    public String getBotUsername() {
        return "AsistenteEnergyBot";
    }//NOMBRE DEL BOT

    @Override
    public String getBotToken() {
        return "CLAVE DEL BOT DE TELGRAM ";
    }//CLAVE

    @Override
    public void onUpdateReceived(Update update) {//METODO PRINCIPAL CADA VEZ QUE ALGUIEN MANDA UN MENSAJE SE EJECUTA
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();//IDENTIFICA AL USUARIO
            String text = update.getMessage().getText();//CONTIENE LO QUE MANDA EL USUARIO

            switch (text) {
                case "/start":
                    sendMsg(chatId, "🏋️‍♀️ ¡Bienvenida al GymBot Smart , Usá /profes para ver los profesores disponibles");
                    break;
                case "/profes":
                    mostrarProfes(chatId);
                    break;
                default:
                    sendMsg(chatId, "Comando no reconocido 😅\nUsá /profes o /start para comenzar.");
            }
        }
        else if (update.hasCallbackQuery()) {//SI EL USUARIO APRETA UN BOTON
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String data = update.getCallbackQuery().getData();
            String usuario = update.getCallbackQuery().getFrom().getFirstName();

            if (data.startsWith("prof_")) {// SI EMPIEZA CON PROF MUESTRA LOS PROFES -MUESTRA LOS HORARIOS
                // El usuario eligió un profesor → mostrar horarios
                String nombreProfe = data.replace("prof_", "");
                Profesor profe = buscarProfesor(nombreProfe);
                if (profe != null) {
                    mostrarHorarios(chatId, profe);
                }
            } else if (data.startsWith("hora_")) {// SI EMPIEZA CON HORA MUESTRA LOS HORARIOS DEL PROFE SELECCIONADO
                // El usuario eligió un horario → guardar reserva
                String[] partes = data.replace("hora_", "").split("_", 4);
                String nombreProfe = partes[0];
                String especialidad = partes[1].replace("_", " ");
                String horario = partes[2] + " " + partes[3];
                horario = horario.replace("_", " ");

                JsonManager.guardarReserva(new Reserva(usuario, nombreProfe, horario, especialidad));
                sendMsg(chatId, "✅ Reserva confirmada con *" + nombreProfe + "* (" + especialidad + ") el *" + horario + "* 🗓️");

            } else if (data.equals("volver_profes")) {
                // Botón para volver al menú anterior
                mostrarProfes(chatId);
            }
        }
    }

    private Profesor buscarProfesor(String nombre) {//BUSCA UN PROFESOR EN LA LISTA DE PROFESOR SEGUN SU NOMBRE
        for (Profesor p : profesores) {
            if (p.getNombre().equals(nombre)) return p;
        }
        return null;
    }

    private void mostrarProfes(String chatId) {//CREA UNA LISTA DE BOTONES INLINE CON LOS NOMBRES Y ESPECIALIDADES DE TODOS LOS PROFESORES
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (Profesor p : profesores) {//Por cada profesor en la lista, creá un botón con su nombre y especialidad.
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(p.getNombre() + " (" + p.getEspecialidad() + ")");
            btn.setCallbackData("prof_" + p.getNombre());
            buttons.add(List.of(btn));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        SendMessage message = new SendMessage(chatId, "👥 Seleccioná un profesor para ver sus horarios disponibles:");
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void mostrarHorarios(String chatId, Profesor profe) {//Muestra los horarios del profesor seleccionado.
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (String h : profe.getHorarios()) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(h);
            btn.setCallbackData("hora_" + profe.getNombre() + "_"
                    + profe.getEspecialidad().replace(" ", "_") + "_"
                    + h.replace(" ", "_"));

            buttons.add(List.of(btn));
        }
//  Crea botones con cada horario disponible y agrega al final un botón de “🔙 Volver”.
        // 🔙 Agregar botón de volver
        InlineKeyboardButton volverBtn = new InlineKeyboardButton();
        volverBtn.setText("🔙 Volver a Profesores");
        volverBtn.setCallbackData("volver_profes");
        buttons.add(List.of(volverBtn));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        SendMessage message = new SendMessage(chatId,
                "🧑‍🏫 *" + profe.getNombre() + "* - " + profe.getEspecialidad() +
                        "\n\nSeleccioná un horario para reservar o volvé al menú anterior:");
        message.setReplyMarkup(markup);
        message.enableMarkdown(true);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        message.enableMarkdown(true);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
