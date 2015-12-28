package com.troshchiy.bubblesortvisualization.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ArrayUtilsTest {

    @Test public void testGenerateUnsortedArray() throws Exception {
        int length = 8;
        int[] array = ArrayUtils.generateUnsortedArray(length, 999);

        Assert.assertNotNull(array);
        Assert.assertEquals(8, array.length);
    }

    @Test public void testSwap() throws Exception {
        int a = 5;
        int b = 10;

        int[] array = {a, b};
        ArrayUtils.swap(array, 0, 1);

        Assert.assertEquals(a, array[1]);
        Assert.assertEquals(b, array[0]);
    }

    @Test public void testGetArrayMaxValue() throws Exception {
        int[] array = ArrayUtils.generateUnsortedArray(8, 999);
        System.out.println(Arrays.toString(array));

        int max = ArrayUtils.getArrayMaxValue(array);
        System.out.println("max: " + max);

        //FIXME:
//        System.out.println(Arrays.asList(array).contains(max));
//        Assert.assertTrue(Arrays.asList(array).contains(max));

        for (int i : array) {
            Assert.assertTrue(max >= i);
        }
    }

}