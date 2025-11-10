package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            // Inicializa la API de Telegram
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            // Registra tu bot
            botsApi.registerBot(new GymBot());

            System.out.println("🤖 Bot del Gym Smart iniciado correctamente...");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al iniciar el bot.");
        }
    }
}
