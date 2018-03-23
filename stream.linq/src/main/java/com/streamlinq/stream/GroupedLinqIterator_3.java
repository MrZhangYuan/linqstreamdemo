package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 */

class GroupedLinqIterator_3<TSource, TKey, TElement> extends LinqIterator<Grouping<TKey, TElement>>
{
        private Iterable<TSource> source;
        private Iterator<TSource> iterator;

        private Func_T_R<TSource, TKey> keySelector;
        private Func_T_R<TSource, TElement> elementSelector;
        private HashMap<TKey, GroupingItem<TKey, TElement>> buffer = null;
        private Iterator<GroupingItem<TKey, TElement>> bufferiterator;

        public GroupedLinqIterator_3
                (
                        Iterable<TSource> source,
                        Func_T_R<TSource, TKey> keySelector,
                        Func_T_R<TSource, TElement> elementSelector
                )
                throws Exception
        {
                if(source == null
                        || keySelector == null
                        || elementSelector == null)
                {
                        throw new Exception("source、keySelector、elementSelector 等参数不能为null");
                }

                this.source = source;
                this.keySelector = keySelector;
                this.elementSelector = elementSelector;
        }

        @Override
        public LinqIterator<Grouping<TKey, TElement>> clone()
        {
                try
                {
                        return new GroupedLinqIterator_3<TSource, TKey, TElement>(this.source, this.keySelector, this.elementSelector);
                }
                catch(Exception e)
                {

                }
                return null;
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
                                                this.current = current;
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
        public <TResult> LinqIterator<TResult> select(Func_T_R<Grouping<TKey, TElement>, TResult> selector)
        {
                return new SelectLinqIterator<>(this, selector);
        }

        @Override
        public LinqIterator<Grouping<TKey, TElement>> where(Func_T_Boolean<Grouping<TKey, TElement>> predicate)
        {
                return new WhereLinqIterator<>(this, predicate);
        }
}
