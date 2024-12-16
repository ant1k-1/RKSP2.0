package org.example.pracs_mokin_anton.prac1.task2;

import java.util.*;
import java.util.concurrent.*;

public class Task2 {
    // функция вычисления степени
    private static int calculateSquare(int number) {
        int delayInSeconds = ThreadLocalRandom.current().nextInt(1, 6);
        try {
            Thread.sleep(delayInSeconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return number * number;
    }
    public static void main(String[] args) {
        System.out.println("######## Made by Anton Mokin IKBO-20-21 ########");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a number (or 'e' to exit):");
        // main поток для ввода чисел
        while (true) {
            try {
                String userInput = scanner.nextLine();
                if ("e".equalsIgnoreCase(userInput)) break;
                int number = Integer.parseInt(userInput);
                // Отправляем задачу на выполнение и сохраняем CompletableFuture в список
                CompletableFuture.supplyAsync(() -> calculateSquare(number), executorService)
                        .thenAccept(result -> System.out.println(number + "^2" + " = " + result));
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format. Please enter an integer.");
            }
        }
        executorService.shutdown();
    }
}

