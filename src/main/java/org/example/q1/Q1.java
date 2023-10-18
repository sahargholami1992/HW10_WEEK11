package org.example.q1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Q1 {

    private static List<Integer> sharedList = new ArrayList<>();

    private static Object lock = new Object();

    private static boolean isEven = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an integer:");
        int n = scanner.nextInt();


        Thread oddThread = new Thread(() -> {
            for (int i = 1; i <= n ; i+=2) {
                synchronized (lock) {
                    while (isEven) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sharedList.add(i);
                    isEven = true;
                    lock.notify();
                }
            }
        });
        Thread evenThread = new Thread(() -> {
            for (int i = 0; i <= n; i += 2) {
                synchronized (lock) {
                    while (!isEven) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sharedList.add(i);
                    isEven = false;
                    lock.notify();
                }
            }
        });

        evenThread.start();
        oddThread.start();

        try {
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("The shared list is: " + sharedList);
    }
}