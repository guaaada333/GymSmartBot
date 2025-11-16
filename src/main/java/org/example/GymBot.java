package org.example;
import org.example.Data.JsonManager;
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

//la clase Gymbot se encarga de conectar todo,esta permite relizar reserva de una clase y mostrar los profesores disponibles
public class GymBot extends TelegramLongPollingBot {//libreria para usar metodos de telegram

    private final ControladorProfesor controladorProfesor = new ControladorProfesor();//utiliza el controladorProfesor cada vez q se inicia el bot

    @Override
    public String getBotUsername() {
        return "AsistenteEnergyBot";//nombre del bot de telegram
    }

    @Override
    public String getBotToken() {
        return "8402794640:AAFVC0kNprTIpNdk0T_wrjsVHMSqbz3FsL8";//clave del bot de telegram
    }

//Este método se ejecuta cada vez que alguien escribe algo o toca un botón
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            switch (text) {
                case "/start"://en el caso de usar este comando
                    sendMsg(chatId, "🏋️‍♀️ *Bienvenida al GymBot Smart!*\nUsá /profes para ver los profesores disponibles");//muestra este mensaje
                    break;

                case "/profes"://en el caso de usar este comando
                    mostrarProfes(chatId);//muestra los profesores disponibles usando la funcion "mostrarProfes"
                    break;

                default:
                    sendMsg(chatId, "❌ Comando no reconocido.\nUsá /profes o /start.");//en el caso de que el usuario escriba cualquier cosa,muestra este mensaje
                    break;
            }
            return;
        }
        //CALLBACK QUERIES (BOTONES)
        if (update.hasCallbackQuery()) {//Esto se ejecuta cuando el usuario toca un botón inline

            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();//Obtener datos del botón y del usuario
            String data = update.getCallbackQuery().getData();
            String usuario = update.getCallbackQuery().getFrom().getFirstName();//Saca el nombre del usuario de Telegram para guardar quién reservó

            // Usuario seleccionó un profesor
            if (data.startsWith("prof_")) {//el texto comienza con prof? entonces significa que el usuario tocó un profesor
                String nombreProfe = data.replace("prof_", "");//Saca el nombre del profesor
                Profesor profe = controladorProfesor.buscarPorNombre(nombreProfe);//busca el nombre seleccionado en la lista de profes
                mostrarHorarios(chatId, profe);//muestra los horarios de ese profesor seleccionado
                return;//termina la funcion
            }

            // Usuario seleccionó un horario
            if (data.startsWith("hora_")) {//el texto comienza con hora? entonces significa que el usuario tocó un horario

                String dataLimpia = data.replace("hora_", "");//saca el horario seleccionado
                String[] partes = dataLimpia.split("_");//divide la cadena con _
                String profe = partes[0];//primera parte el nombre del profesor
                String especialidad = partes[1].replace("_", " ");//segunda parte la especialiodad del profe

                // reconstruir el horario
                StringBuilder horarioSB = new StringBuilder();
                for (int i = 2; i < partes.length; i++) {//empieza desde la posicion 2 hacia adelante
                    horarioSB.append(partes[i]).append(" ");
                }
                String horario = horarioSB.toString().trim().replace("_", " ");//lo convierte en un string normal
                JsonManager.guardarReserva(new Reserva(usuario, profe, horario, especialidad));//guarda la reserva en el json con todos los datos

                sendMsg(chatId, "✅ *Reserva confirmada*\n\nProfesor: *" + profe +//manda este mensaje al finalizar con la reserva
                        "*\nEspecialidad: " + especialidad +
                        "\nHorario: " + horario);
                return;
            }

            // Botón volver
            if (data.equals("volver_profes")) {//si el usuario apreto el boton volver
                mostrarProfes(chatId);//muestra la lista de profes y lo que enseñan
            }
        }
    }

    //  MOSTRAR PROFESORES
    private void mostrarProfes(String chatId) {

        List<Profesor> lista = controladorProfesor.listarProfesores();//Llama al controlador → devuelve TODOS los profes cargados en ProfesorData
        List<List<InlineKeyboardButton>> botones = new ArrayList<>();//crea la lista de botones,Telegram usa una matriz de botones → cada fila es List<InlineKeyboardButton>.
       //Recorrer cada profesor para crear su botón
        for (Profesor p : lista) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(p.getNombre() + " (" + p.getEspecialidad() + ")");
            btn.setCallbackData("prof_" + p.getNombre());//cuando se toca un boton el bot recibe prof_Marcos
            botones.add(List.of(btn));//guarda cada boton como una fila en la matriz
        }
       //muestra en pantalla todos los profesores cada uno en un boton
        SendMessage msg = new SendMessage(chatId,
                "👥 *Seleccioná un profesor para ver sus horarios:*");//el texto que aparece arriba
        msg.enableMarkdown(true);
        msg.setReplyMarkup(new InlineKeyboardMarkup(botones));

        try {
            execute(msg);//Enviar el mensaje a Telegram
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // MOSTRAR HORARIOS
    private void mostrarHorarios(String chatId, Profesor profe) {
        List<List<InlineKeyboardButton>> botones = new ArrayList<>();//crea los botones de los horarios

        for (String h : profe.getHorarios()) {//busca el profesor que el usuario selecciono y obtiene sus horarios
            InlineKeyboardButton btn = new InlineKeyboardButton();//por cada horario crea un boton
            btn.setText(h);

            btn.setCallbackData("hora_" + profe.getNombre() + "_"//encierra toda la info ej:hora_Marcos_Entrenamiento_funcional_Lunes_9:00_-_11:00
                    + profe.getEspecialidad().replace(" ", "_") + "_"
                    + h.replace(" ", "_"));

            botones.add(List.of(btn));//Guardamos cada botón como una fila en la matriz
        }

        // botón volver
        InlineKeyboardButton volver = new InlineKeyboardButton();//crea un boton para volver hacia la lista de profesores
        volver.setText("🔙 Volver");
        volver.setCallbackData("volver_profes");//mostrara la lista de profesores de nuevo
        botones.add(List.of(volver));//se agrefa el boton

        SendMessage msg = new SendMessage(chatId,//mensaje que aparece al apretar un profesor
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
//sirve para enviar un mensaje en el chat
    private void sendMsg(String chatId, String text) {//obtiene el id del chat y el mensaje que yo decido pasar
        SendMessage msg = new SendMessage(chatId, text);//Crea un mensaje para Telegram con el texto que le paso.
        msg.enableMarkdown(true);

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
