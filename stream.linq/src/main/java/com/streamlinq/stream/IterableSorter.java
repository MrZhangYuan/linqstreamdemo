package main.java.com.streamlinq.stream;

/**
 * Created by yuany on 2018/1/12.
 */

abstract class IterableSorter<TElement>
{
        abstract void computeKeys(Object[] elements, int count) throws Exception;

        abstract int compareKeys(int index1, int index2);

        int[] sort(Object[] elements, int count) throws Exception
        {
                computeKeys(elements, count);
                int[] map = new int[count];
                for(int i = 0; i < count; i++)
                {
                        map[i] = i;
                }
                quickSort(map, 0, count - 1);
                return map;
        }

        void quickSort(int[] map, int left, int right)
        {
                do
                {
                        int i = left;
                        int j = right;
                        int x = map[i + ((j - i) >> 1)];
                        do
                        {
                                while(i < map.length && compareKeys(x, map[i]) > 0)
                                {
                                        i++;
                                }
                                while(j >= 0 && compareKeys(x, map[j]) < 0)
                                {
                                        j--;
                                }
                                if(i > j)
                                {
                                        break;
                                }
                                if(i < j)
                                {
                                        int temp = map[i];
                                        map[i] = map[j];
                                        map[j] = temp;
                                }
                                i++;
                                j--;
                        }
                        while(i <= j);
                        if(j - left <= right - i)
                        {
                                if(left < j)
                                {
                                        quickSort(map, left, j);
                                }
                                left = i;
                        }
                        else
                        {
                                if(i < right)
                                {
                                        quickSort(map, i, right);
                                }
                                right = j;
                        }
                }
                while(left < right);
        }
}
