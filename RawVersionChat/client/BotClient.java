package client;

import auxiliary.ConsoleHelper;
import auxiliary.Format;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;



public class BotClient extends Client{
    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);

            String[] split = message.split(": ");
            if (split.length != 2) return;

            String messageData = split[1];

            SimpleDateFormat format = null;
            switch (messageData) {
                case "дата":
                    format = Format.DATE_FORMAT;
                    break;
                case "день":
                    format = Format.DAYS_FORMAT;
                    break;
                case "месяц":
                    format = Format.MONTHS_FORMAT;
                    break;
                case "год":
                    format = Format.YEARS_FORMAT;
                    break;
                case "время":
                    format = Format.TIME_FORMAT;
                    break;
                case "час":
                    format = Format.HOURS_FORMAT;
                    break;
                case "минуты":
                    format = Format.MINUTES_FORMAT;
                    break;
                case "секунды":
                    format = Format.SECONDS_FORMAT;
                    break;
            }

            if (format != null)
                sendTextMessage("Информация для " + split[0] + ": " + format.format(Calendar.getInstance().getTime()));
        }
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        return "date_bot_" + ThreadLocalRandom.current().nextInt(0, 100);
    }

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }
}
