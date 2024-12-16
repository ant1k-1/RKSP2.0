package org.example.pracs_mokin_anton.prac1.task1;

import java.util.*;
import java.util.concurrent.*;

public class Task1 {
    public static List<Integer> generateArray(int size) {
        Random random = new Random();
        List<Integer> array = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            array.add(random.nextInt());
        }
        return array;
    }

    public static int findMaxSingleton(List<Integer> array) throws InterruptedException {
        int max = Integer.MIN_VALUE;
        for (var elem : array) {
            Thread.sleep(1);
            if (elem > max) {
                max = elem;
            }
        }
        return max;
    }

    public static int findMaxMultiThread(List<Integer> array) throws InterruptedException, ExecutionException {
        // Определяем количество доступных процессоров
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Количество тредов в многопотоке: " + numberOfThreads);

        // Создаем пул потоков для выполнения задач
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        // Создаем список задач для каждого подсписка
        List<Callable<Integer>> tasks = new ArrayList<>();
        int batchSize = array.size() / numberOfThreads;

        // Разбиваем список на подсписки и создаем задачи для каждого подсписка
        for (int i = 0; i < numberOfThreads; i++) {
            final int start = i * batchSize;
            final int end = (i == numberOfThreads - 1) ? array.size() : (i + 1) * batchSize;

            tasks.add(() -> findMaxSingleton(array.subList(start, end)));
        }

        // Запускаем все задачи и получаем Future объекты для получения результатов
        List<Future<Integer>> futures = executorService.invokeAll(tasks);

        // Инициализируем переменную для хранения максимального значения
        int max = Integer.MIN_VALUE;

        // Обходим результаты каждой задачи и находим минимальное значение
        for (var fut : futures) {
            int partialMax = fut.get();
            Thread.sleep(1);
            if (partialMax > max) {
                max = partialMax;
            }
        }
        executorService.shutdown();
        return max;
    }

    public static int findMaxForkJoin(List<Integer> array, int THRESHOLD) {
        // Создаем пул потоков ForkJoin
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        // Создаем корневую задачу MaxFinderTask для всего списка
        MaxFinderTask task = new MaxFinderTask(array, 0, array.size(), THRESHOLD);
        // Выполняем корневую задачу и получаем результат
        return forkJoinPool.invoke(task);
    }

    // Внутренний класс MaxFinderTask, расширяющий RecursiveTask для многопоточного выполнения
    static class MaxFinderTask extends RecursiveTask<Integer> {
        private final List<Integer> list;
        private final int start;
        private final int end;
        private final int THRESHOLD; // Порог для прямого вычисления

        // Конструктор MaxFinderTask для создания задачи для подсписка
        MaxFinderTask(List<Integer> list, int start, int end, int THRESHOLD) {
            this.list = list;
            this.start = start;
            this.end = end;
            this.THRESHOLD = THRESHOLD;
        }

        // Метод compute(), выполняющий вычисления для задачи
        @Override
        protected Integer compute() {
            // Если в подсписке только один элемент, вернем его
            if (end - start <= THRESHOLD) {
                try {
                    return findMaxSingleton(list.subList(start, end));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // Найдем середину подсписка
            int middle = start + (end - start) / 2;

            // Создаем две подзадачи для левой и правой половин подсписка
            MaxFinderTask leftTask = new MaxFinderTask(list, start, middle, THRESHOLD);
            MaxFinderTask rightTask = new MaxFinderTask(list, middle, end, THRESHOLD);

            // Запускаем подзадачу для правой половины параллельно
            leftTask.fork();
            // Вычисляем максимальные значения в левой и правой половинах подсписка
            int rightResult = rightTask.compute();
            int leftResult = leftTask.join();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Возвращаем максимальное значение из левой и правой половин
            return Math.max(leftResult, rightResult);
        }
    }
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("######## Made by Anton Mokin IKBO-20-21 ########");
        int n = 1_000_0;
        List<Integer> testList = generateArray(n);
        System.out.println("n = " + n);

        // 1 поток
        long startTime = System.nanoTime();
        int result = findMaxSingleton(testList);
        long endTime = System.nanoTime();
        long durationInMilliseconds = (endTime - startTime) / 1_000_000;
        System.out.println("Sequential function execution time: " +
                durationInMilliseconds + " ms. Result - " + result);

        // многопоточка
        startTime = System.nanoTime();
        result = findMaxMultiThread(testList);
        endTime = System.nanoTime();
        durationInMilliseconds = (endTime - startTime) / 1_000_000;
        System.out.println("Multi-threaded function execution time: " +
                durationInMilliseconds + " ms. Result - " + result);

        // forkjoin
        int THRESHOLD = 100_0; // предел дробления массива
        System.out.println("Предел дробления массива для forkjoin = " + THRESHOLD);
        startTime = System.nanoTime();
        result = findMaxForkJoin(testList, THRESHOLD);
        endTime = System.nanoTime();
        durationInMilliseconds = (endTime - startTime) / 1_000_000;
        System.out.println("Fork function execution time: " +
                durationInMilliseconds + " ms. Result - " + result);
    }
}
