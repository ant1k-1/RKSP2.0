package org.example.pracs_mokin_anton.prac1.task3;

import java.util.Random;
import java.util.concurrent.BlockingQueue;


// структура файла
public class File {
    private String fileType;
    private int fileSize;
    public File(String fileType, int fileSize) {
        this.fileType = fileType;
        this.fileSize = fileSize;
    }
    public String getFileType() {
        return fileType;
    }
    public int getFileSize() {
        return fileSize;
    }
}

// имитация генерации файла с задержкой
class FileGenerator implements Runnable {
    // BlockingQueue нужна для безопасной работы очереди в многопоточке
    private final BlockingQueue<File> queue;
    public FileGenerator(BlockingQueue<File> queue) {
        this.queue = queue;
    }
    // создание файла
    @Override
    public void run() {
        Random random = new Random();
        String[] fileTypes = {"XML", "JSON", "XLS"};
        while (true) {
            try {
                Thread.sleep(random.nextInt(901) + 100);
                String randomFileType = fileTypes[random.nextInt(fileTypes.length)];
                int randomFileSize = random.nextInt(91) + 10;
                File file = new File(randomFileType, randomFileSize);
                // поток ждет, если очередь заполнена
                queue.put(file); // пушаем в очередь
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

// имитация обработки файла с задержкой
class FileProcessor implements Runnable {
    private BlockingQueue<File> queue;
    private String allowedFileType;
    public FileProcessor(BlockingQueue<File> queue, String allowedFileType) {
        this.queue = queue;
        this.allowedFileType = allowedFileType;
    }
    @Override
    public void run() {
        while (true) {
            try {
                // поток ждет, если очередь пуста
                File file = queue.take(); // берем файл из очереди
                if (file.getFileType().equals(allowedFileType)) {
                    long processingTime = file.getFileSize() * 7; // задержка обработки
                    Thread.sleep(processingTime);
                    System.out.println(file.getFileType() +
                            " type file processed with size " +
                            file.getFileSize() +
                            ". Processing time: " + processingTime + " ms.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}