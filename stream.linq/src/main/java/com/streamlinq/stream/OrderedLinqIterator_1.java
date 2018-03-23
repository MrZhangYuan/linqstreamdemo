package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.Comparator;

/**
 * Created by yuany on 2018/1/12.
 */

abstract class OrderedLinqIterator_1<TElement> extends LinqIterator<TElement>
{
        Iterable<TElement> source;
        Buffer<TElement> buffer = null;
        int index = 0;
        int[] map = null;

        @Override
        public void close()
        {
                this.buffer = null;
                this.map=null;
                this.index=0;
                super.close();
        }
        @Override
        public boolean hasNext()
        {
                try
                {
                        switch(this.state)
                        {
                                case 1:
                                        this.buffer = new Buffer<>(this.source);

                                        if(buffer.count > 0)
                                        {
                                                IterableSorter<TElement> sorter = getIterableSorter(null);
                                                map = sorter.sort(buffer.items, buffer.count);
                                        }
                                        this.state = 2;
                                case 2:
                                        if(this.index < buffer.count)
                                        {
                                                TElement current = (TElement) buffer.items[map[this.index]];
                                                this.current = current;
                                                index++;
                                                return true;
                                        }
                                        this.close();
                                        break;
                        }
                }
                catch(Exception ex)
                {
                        throw new RuntimeException(ex);
                }
                return false;
        }

        abstract IterableSorter<TElement> getIterableSorter(IterableSorter<TElement> next);

        @Override
        public <TResult> LinqIterator<TResult> select(Func_T_R<TElement, TResult> selector)
        {
                return new SelectLinqIterator<>(this, selector);
        }

        @Override
        public LinqIterator<TElement> where(Func_T_Boolean<TElement> predicate)
        {
                return new WhereLinqIterator<>(this, predicate);
        }

        public <TKey> OrderedLinqIterator_1<TElement> createOrderedEnumerable
                (
                        Func_T_R<TElement, TKey> keySelector,
                        Comparator<TKey> comparer,
                        boolean descending
                )
                throws Exception
        {
                OrderedLinqIterator_2 neworder = new OrderedLinqIterator_2<TElement, TKey>
                        (
                                this.source,
                                keySelector,
                                comparer,
                                descending
                        );
                neworder.parent = this;
                return neworder;
        }

}
