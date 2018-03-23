package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;

import java.util.Comparator;

/**
 * Created by yuany on 2018/1/12.
 */

class OrderedLinqIterator_2<TElement, TKey> extends OrderedLinqIterator_1<TElement>
{
        OrderedLinqIterator_1<TElement> parent;

        Func_T_R<TElement, TKey> keySelector;
        Comparator<TKey> comparer;
        boolean descending;

        public OrderedLinqIterator_2(Iterable<TElement> source, Func_T_R<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending) throws Exception
        {
                if(source == null
                        || keySelector == null)
                {
                        throw new Exception("source、keySelector不能为null");
                }

                this.source = source;
                this.parent = null;
                this.keySelector = keySelector;
                this.comparer = comparer != null ? comparer : new Comparator<TKey>()
                {
                        @Override
                        public int compare(TKey a, TKey b)
                        {
                                return DefaultComparator.instance.compare(a, b);
                        }
                };
                this.descending = descending;
        }


        @Override
        IterableSorter<TElement> getIterableSorter(IterableSorter<TElement> next)
        {
                IterableSorter<TElement> next1 = new IterableSorter_2<TElement, TKey>(this.keySelector, this.comparer, this.descending, next);
                if(this.parent != null)
                {
                        next1 = this.parent.getIterableSorter(next1);
                }
                return next1;
        }

        @Override
        public LinqIterator<TElement> clone()
        {
                try
                {
                        return new OrderedLinqIterator_2<>(this.source, this.keySelector, this.comparer, this.descending);
                }
                catch(Exception ignored)
                {
                }
                return null;
        }
}
