package command;

import auxilary.*;
import main.*;

import java.util.List;

public class ZipContentCommand extends ZipCommand{
    @Override
    public void execute() throws Exception {

        ConsoleHelper.writeMessage("Просмотр содержимого архива");

        ZipFileManager zipFileManager = getZipFileManager();

        ConsoleHelper.writeMessage("Содержимое архива");

        List<FileProperties> filePropertiesList = zipFileManager.getFilesList();

        filePropertiesList.forEach(System.out::println);

        ConsoleHelper.writeMessage("Содержимое архива прочитано");

    }
}
