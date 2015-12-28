package com.troshchiy.bubblesortvisualization.util;

import java.util.Random;

public class ArrayUtils {

    /** Avoid creating an instance of this class */
    private ArrayUtils() { throw new AssertionError(); }

    public static int[] generateUnsortedArray(int length, int maxValue) {
        int[] array = new int[length];
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            array[i] = r.nextInt(maxValue) + 1; // Avoid 0 value for better drawing
        }
        return array;
    }

    public static void swap(int[] array, int firstIndex, int secondIndex) {
        int temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    public static int getArrayMaxValue(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

}