package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T2_R;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 */

class GroupedLinqIterator_4<TSource, TKey, TElement, TResult> extends LinqIterator<TResult>
{
        private Iterable<TSource> source;
        private Iterator<TSource> iterator;
        private Func_T_R<TSource, TKey> keySelector;
        private Func_T_R<TSource, TElement> elementSelector;

        private Func_T2_R<TKey, Iterable<TElement>, TResult> resultSelector;

        private HashMap<TKey, GroupingItem<TKey, TElement>> buffer = null;
        private Iterator<GroupingItem<TKey, TElement>> bufferiterator;

        public GroupedLinqIterator_4
                (
                        Iterable<TSource> source,
                        Func_T_R<TSource, TKey> keySelector,
                        Func_T_R<TSource, TElement> elementSelector,
                        Func_T2_R<TKey, Iterable<TElement>, TResult> resultSelector
                )
                throws Exception
        {
                if(source == null
                        || keySelector == null
                        || elementSelector == null
                        || resultSelector == null)
                {
                        throw new Exception("source、keySelector、elementSelector、resultSelector 等参数不能为null");
                }
                this.source = source;
                this.keySelector = keySelector;
                this.elementSelector = elementSelector;
                this.resultSelector = resultSelector;
        }
        @Override
        public void close()
        {
                this.iterator = null;
                this.buffer=null;
                this.bufferiterator=null;
                super.close();
        }
        @Override
        public LinqIterator<TResult> clone()
        {
                try
                {
                        return new GroupedLinqIterator_4<TSource, TKey, TElement, TResult>(this.source, this.keySelector, this.elementSelector, this.resultSelector);
                }
                catch(Exception e)
                {

                }
                return null;
        }

        @Override
        public boolean hasNext()
        {
                try
                {
                        switch(this.state)
                        {
                                case 1:
                                        this.iterator = this.source.iterator();
                                        this.buffer = new HashMap<>();
                                        while(this.iterator.hasNext())
                                        {
                                                TSource current = this.iterator.next();
                                                TKey key = this.keySelector.Invoke(current);
                                                TElement element = this.elementSelector.Invoke(current);

                                                if(!buffer.containsKey(key))
                                                {
                                                        buffer.put(key, new GroupingItem<>(key));
                                                }

                                                buffer.get(key).add(element);
                                        }
                                        this.bufferiterator = this.buffer.values().iterator();
                                        this.state = 2;
                                case 2:
                                        if(this.bufferiterator.hasNext())
                                        {
                                                Grouping<TKey, TElement> current = this.bufferiterator.next();
                                                this.current = resultSelector.Invoke(current.getKey(), current);
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

        @Override
        public <TResult1> LinqIterator<TResult1> select(Func_T_R<TResult, TResult1> selector)
        {
                return new SelectLinqIterator<>(this, selector);
        }

        @Override
        public LinqIterator<TResult> where(Func_T_Boolean<TResult> predicate)
        {
                return new WhereLinqIterator<>(this, predicate);
        }
}
