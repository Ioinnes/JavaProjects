package main;

import auxilary.*;
import exception.*;
import command.*;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private final Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception {
        if (Files.notExists(zipFile.getParent()))
            Files.createDirectories(zipFile.getParent());
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            if (Files.isDirectory(source)) {
                FileManager fileManager = new FileManager(source);
                fileManager.getFileList().forEach(x -> {
                    try {
                        addNewZipEntry(zipOutputStream, source, x);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (Files.isRegularFile(source)) {
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else
                throw new PathIsNotFoundException();
        }
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        try (InputStream inputStream = Files.newInputStream(filePath.resolve(fileName))) {
            ZipEntry entry = new ZipEntry(fileName.toString());
            zipOutputStream.putNextEntry(entry);
            copyData(inputStream, zipOutputStream);
            zipOutputStream.closeEntry();
        }
    }

    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[8 * 1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }

    public List<FileProperties> getFilesList() throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();

        List<FileProperties> fileProperties = new ArrayList<>(100);
        try (ZipInputStream inputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

            ZipEntry zipEntry;
            ByteArrayOutputStream byteArrayOutputStream;

            while ((zipEntry = inputStream.getNextEntry()) != null) {
                byteArrayOutputStream = new ByteArrayOutputStream();
                copyData(inputStream, byteArrayOutputStream);

                fileProperties.add(new FileProperties(zipEntry.getName(), zipEntry.getSize(), zipEntry.getCompressedSize(), zipEntry.getMethod()));
                inputStream.closeEntry();
            }

            return fileProperties;
        }
    }

    public void extractAll(Path outputFolder) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();

        if (Files.notExists(outputFolder))
            Files.createDirectories(outputFolder);

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                Path fileFullName = outputFolder.resolve(fileName);

                Path parent = fileFullName.getParent();
                if (Files.notExists(parent))
                    Files.createDirectories(parent);

                try (OutputStream outputStream = Files.newOutputStream(fileFullName)) {
                    copyData(zipInputStream, outputStream);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        }
    }

        public void removeFiles(List<Path> pathList) throws Exception {
            if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();


            Path tempZipFile = Files.createTempFile(null, null);

            try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
                try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

                    ZipEntry zipEntry;

                    while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                        String fileName = zipEntry.getName();

                        if (!pathList.contains(Paths.get(fileName))) {
                            zipOutputStream.putNextEntry(new ZipEntry(fileName));
                            copyData(zipInputStream, zipOutputStream);
                            zipOutputStream.closeEntry();
                            zipInputStream.closeEntry();
                        } else {
                            ConsoleHelper.writeMessage(String.format("Файл '%s' удален из архива.", fileName));
                        }
                    }
                }
            }
            Files.move(tempZipFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
        }


    public void addFiles(List<Path> pathList) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();

        Path tempZipFile = Files.createTempFile(null, null);
        List<Path> localPathList = new ArrayList<>(100);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

                ZipEntry zipEntry;

                while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                    String fileName = zipEntry.getName();
                    localPathList.add(Paths.get(fileName)); //??

                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    copyData(zipInputStream, zipOutputStream);

                    zipOutputStream.closeEntry();
                    zipInputStream.closeEntry();
                }
            }

            for (Path path : pathList) {
                if (Files.notExists(path) || !Files.isRegularFile(path)) throw new PathIsNotFoundException();
                if (!localPathList.contains(path.getFileName())) {
                    addNewZipEntry(zipOutputStream, path.getParent(), path.getFileName());
                    ConsoleHelper.writeMessage(String.format("Файла %s был добавлен в архив.", path));
                } else
                    ConsoleHelper.writeMessage(String.format("Файл %s уже существвует в архиве", path.getFileName()));
            }
        }
        Files.move(tempZipFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }


    public void addFile(Path absolutePath) throws Exception {
        addFiles(Collections.singletonList(absolutePath));
    }


        public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }
    
}
