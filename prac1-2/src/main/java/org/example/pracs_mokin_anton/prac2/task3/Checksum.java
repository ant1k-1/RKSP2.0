package org.example.pracs_mokin_anton.prac2.task3;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Checksum {
    public static short calculateChecksum(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
            FileChannel fileChannel = fileInputStream.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(2); // Создаем буфер для хранения 2 байт
            short checksum = 0;
            while (fileChannel.read(buffer) != -1) {
                buffer.flip(); // Переключаем буфер в режим чтения
                while (buffer.hasRemaining()) {
                    checksum ^= buffer.get(); // Выполняем XOR над байтами
                }
                buffer.clear(); // Очищаем буфер для следующего чтения
            }
            return checksum;
        }
    }
    public static void main(String[] args) {
        System.out.println("######## Made by Anton Mokin IKBO-20-21 ########");

        String filePath = "sample.txt"; // Путь к файлу, для которого нужно вычислить контрольную сумму
        try {
            short checksum = calculateChecksum(filePath);
            System.out.printf("Контрольная сумма файла %s: 0x%04X%n", filePath,
                    checksum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
