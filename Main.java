package ru.corruptzero;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipFile;

public class Main {
    /*
        TODO
            Любой spring проект с контроллерами сервисами и БД

     */

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        String fileName = "downloads/zipFile.zip";      //путь до файла
        String url = "";      //ссылка для скачивания zip
        try {
            download(url, fileName);
            List<FileInfo> fileInfos = unzipFile(fileName);
            deleteFile(fileName);
            for (FileInfo file : fileInfos) {
                System.out.println(file);
            }
            System.out.println("Прошло времени: " + (System.currentTimeMillis() - time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void download(String urlStr, String file) throws IOException {
        System.out.println("Идет загрузка файла...");
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = bis.read(buffer, 0, 1024)) != -1) {
            fis.write(buffer, 0, bytesRead);
        }
        fis.close();
        bis.close();
        System.out.println("Успешно!");
    }

    private static List<FileInfo> unzipFile(String fileName) throws IOException {
        System.out.println("Идет распаковка файла...");
        List<FileInfo> fileInfos = new ArrayList<>();
        try (var file = new ZipFile(fileName)) {
            var entries = file.entries();
            var uncompressedDirectory = new File(file.getName()).getParent() + File.separator;
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                if (entry.isDirectory()) {
                    var newDirectory = uncompressedDirectory + entry.getName();
                    System.out.println("Создание папки: " + newDirectory);
                    var directory = new File(newDirectory);
                    if (!directory.exists()) {
                        if (directory.mkdirs()) {
                            fileInfos.add(new FileInfo(directory.getName(), "folder", directory.length()));
                            System.out.println("Папка создана");
                        } else {
                            System.out.println("Папка не создана");
                        }
                    }

                } else {
                    try (var is = file.getInputStream(entry); var bis = new BufferedInputStream(is)) {
                        var uncompressedFileName = uncompressedDirectory + entry.getName();
                        try (var os = new FileOutputStream(uncompressedFileName); var bos = new BufferedOutputStream(os)) {
                            while (bis.available() > 0) {
                                bos.write(bis.read());
                            }
                        }
                    }
                    fileInfos.add(new FileInfo(entry.getName(), getFileExtension(entry.getName()).orElse(""), entry.getSize()));
                    System.out.println("Записан файл: " + entry.getName());
                }
            }
        }
        System.out.println("Успешно!");
        return fileInfos;
    }

    private static void deleteFile(String fileName) throws IOException {
        System.out.println(Files.deleteIfExists(Path.of(fileName)) ? "Файл успешно удален" : "Файл не удален");
    }

    private static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}