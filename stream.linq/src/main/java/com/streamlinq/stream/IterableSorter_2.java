package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;

import java.util.Comparator;

/**
 * Created by yuany on 2018/1/12.
 */

class IterableSorter_2<TElement, TKey> extends IterableSorter<TElement>
{
        Func_T_R<TElement, TKey> keySelector;
        Comparator<TKey> comparer;
        boolean descending;
        IterableSorter<TElement> next;
        Object[] keys;

        IterableSorter_2(Func_T_R<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending, IterableSorter<TElement> next)
        {
                this.keySelector = keySelector;
                this.comparer = comparer;
                this.descending = descending;
                this.next = next;
        }

        @Override
        void computeKeys(Object[] elements, int count) throws Exception
        {
                keys = new Object[count];
                for(int i = 0; i < count; i++)
                {
                        keys[i] = keySelector.Invoke((TElement) elements[i]);
                }
                if(next != null)
                {
                        next.computeKeys(elements, count);
                }
        }

        @Override
        int compareKeys(int index1, int index2)
        {
                int c = comparer.compare((TKey) keys[index1], (TKey) keys[index2]);
                if(c == 0)
                {
                        if(next == null)
                        {
                                return index1 - index2;
                        }
                        return next.compareKeys(index1, index2);
                }
                return descending ? -c : c;
        }
}
