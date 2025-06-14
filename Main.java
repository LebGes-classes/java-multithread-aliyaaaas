package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        String botUsername = "WB botyara";
        String botToken = "7999600443:AAFRQbGcK1VE_puEa1icNWBriRZ1SfC9VzM";

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new PriceParserBot(botUsername, botToken));
            System.out.println("Бот запущен!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}