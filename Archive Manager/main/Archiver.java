package main;

import auxilary.*;
import exception.*;
import command.*;

import java.io.IOException;

public class Archiver {
    public static void main(String[] args) throws Exception {
        Operation operation = null;
        while (operation != Operation.EXIT) {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (WrongZipFileException e) {
                ConsoleHelper.writeMessage("Вы не выбрали файл архива или выбрали неверный файл.");
            } catch (Exception e) {
                ConsoleHelper.writeMessage("Произошла ошибка. Проверьте введенные данные.");
            }
        }

        ConsoleHelper.close();
    }

    public static Operation askOperation() throws IOException {
        ConsoleHelper.writeMessage("Выберите одну из комманд: ");
        for (Operation i : Operation.values()) {
            ConsoleHelper.writeMessage(i.ordinal() + " " + i);
        }
        int choice = ConsoleHelper.readInt();
        for (Operation i : Operation.values()) {
            if (i.ordinal() == choice) return i;
        }
        return null;
    }
}
