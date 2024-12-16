package org.example.pracs_mokin_anton.prac2.task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
public class ReadTextFile {

    // метод для записи строк в файл
    private static void writeLinesToFile(String fileName, String[] lines) {
        Path filePath = Paths.get(fileName);
        try {
            Files.write(filePath, List.of(lines));
            System.out.println("File created: " + fileName);
        } catch (IOException e) {
            System.err.println("File writing error occurred: " + e.getMessage());
        }
    }

    //метод для чтения файла
    private static void readAndPrintFileContent(String fileName) {
        Path filePath = Paths.get(fileName);
        try {
            List<String> fileLines = Files.readAllLines(filePath);
            System.out.println("Content of " + fileName + ":");
            for (String line : fileLines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("File reading error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String fileName = "sample.txt";

        String[] lines = {
                "This is a sample text.",
                "This is a sample text next line.",
                "Made by Anton Mokin IKBO-20-21"
        };

        writeLinesToFile(fileName, lines);
        // Читаем содержимое файла и выводим его в стандартный поток вывода
        readAndPrintFileContent(fileName);
    }
}
