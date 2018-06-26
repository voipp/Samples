package base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSortExample {

    public static int performReorder(List<Integer> array) {
        int pivotal = array.get(array.size() / 2);

        int leftIndex = 0;
        int rightIndex = array.size() - 1;

        while (leftIndex < rightIndex) {
            while (array.get(leftIndex) < pivotal)
                leftIndex++;
            while (array.get(rightIndex) > pivotal)
                rightIndex--;

            int l = array.get(leftIndex);

            array.set(leftIndex, array.get(rightIndex));
            array.set(rightIndex, l);
        }

        return leftIndex;
    }

    public static List<Integer> hoarthQuickSort(List<Integer> array) {
        if (array.size() == 1)
            return array;

        int pivotal = performReorder(array);

        List<Integer> array1 = hoarthQuickSort(new ArrayList(array.subList(0, pivotal)));
        List<Integer> array2 = hoarthQuickSort(new ArrayList(array.subList(pivotal, array.size())));

        array1.addAll(array2);

        return array1;
    }

    public static void main(String[] args) {
        System.out.println(hoarthQuickSort(Arrays.asList(1, 8, 5, 4, 2, 3, 7, 6)));
    }
}
