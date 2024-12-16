package org.example.pracs_mokin_anton.prac1.task3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MultithreadedFileProcessing {
    public static void main(String[] args) {
        System.out.println("######## Made by Anton Mokin IKBO-20-21 ########");
        // Очередь вместимостью 5 файлов
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(5);

        Thread generatorThread = new Thread(new FileGenerator(queue));
        Thread jsonProcessorThread = new Thread(new FileProcessor(queue, "JSON"));
        Thread xmlProcessorThread = new Thread(new FileProcessor(queue, "XML"));
        Thread xlsProcessorThread = new Thread(new FileProcessor(queue, "XLS"));

        generatorThread.start();
        jsonProcessorThread.start();
        xmlProcessorThread.start();
        xlsProcessorThread.start();
    }
}