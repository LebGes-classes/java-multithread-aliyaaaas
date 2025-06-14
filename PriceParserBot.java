package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PriceParserBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public PriceParserBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Передаем обработку в поток
            executor.submit(() -> handleCommand(chatId, messageText));
        }
    }

    private void handleCommand(long chatId, String messageText) {
        if (messageText.startsWith("https://www.wildberries.ru/"))  {
            sendMessage(chatId, "Парсю данные...");
            parseWildberriesProduct(messageText, chatId);
        } else {
            sendMessage(chatId, "Отправь ссылку на товар с Wildberries.");
        }
    }

    private void parseWildberriesProduct(String url, long chatId) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();

            // Извлекаем название товара
            String title = doc.selectFirst("h1.product-page__title").text();

            // Извлекаем цену
            String priceText = doc.selectFirst("span.price-block__price").text()
                    .replaceAll("\\D+", ""); // убираем всё, кроме цифр
            int price = Integer.parseInt(priceText);

            sendMessage(chatId, String.format("Название: %s\nЦена: %d руб.", title, price));

        } catch (IOException e) {
            sendMessage(chatId, "Ошибка при загрузке страницы.");
        } catch (NumberFormatException | NullPointerException e) {
            sendMessage(chatId, "Не удалось извлечь цену или название.");
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}