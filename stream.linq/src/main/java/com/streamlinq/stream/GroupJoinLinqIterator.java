package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.Func_T2_R;
import main.java.com.streamlinq.core.Func_T_Boolean;
import main.java.com.streamlinq.core.Func_T_R;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by yuany on 2018/1/23.
 */

class GroupJoinLinqIterator<TSource, TInner, TKey, TResult> extends LinqIterator<TResult>
{
        private Iterable<TSource> outer;
        private Iterable<TInner> inner;
        private Func_T_R<TSource, TKey> outerKeySelector;
        private Func_T_R<TInner, TKey> innerKeySelector;
        private Func_T2_R<TSource, Iterable<TInner>, TResult> resultSelector;
        private EqualityComparer<TKey> keyequalityComparer;

        private HashMap<TKey, GroupingItem<TKey, TInner>> innerbuffer = null;
        private Iterator<TSource> outeriterator;

        public GroupJoinLinqIterator
                (
                        Iterable<TSource> outer,
                        Iterable<TInner> inner,
                        Func_T_R<TSource, TKey> outerKeySelector,
                        Func_T_R<TInner, TKey> innerKeySelector,
                        Func_T2_R<TSource, Iterable<TInner>, TResult> resultSelector,
                        EqualityComparer<TKey> keyequalityComparer
                )
                throws Exception
        {
                if(outer == null
                        || inner == null
                        || outerKeySelector == null
                        || innerKeySelector == null
                        || resultSelector == null)
                {
                        throw new Exception("参数不可为null。");
                }

                this.outer = outer;
                this.inner = inner;
                this.outerKeySelector = outerKeySelector;
                this.innerKeySelector = innerKeySelector;
                this.resultSelector = resultSelector;
                this.keyequalityComparer = keyequalityComparer;
        }

        @Override
        public LinqIterator<TResult> clone()
        {
                try
                {
                        return new GroupJoinLinqIterator<>(this.outer, this.inner, this.outerKeySelector, this.innerKeySelector, this.resultSelector, this.keyequalityComparer);
                }
                catch(Exception ignored)
                {

                }
                return null;
        }

        @Override
        public void close()
        {
                this.innerbuffer = null;
                this.outeriterator = null;
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
                                        this.innerbuffer = new HashMap<>();
                                        for(TInner current : this.inner)
                                        {
                                                TKey key = this.innerKeySelector.Invoke(current);
                                                if(!innerbuffer.containsKey(key))
                                                {
                                                        innerbuffer.put(key, new GroupingItem<>(key));
                                                }
                                                innerbuffer.get(key).add(current);
                                        }

                                        this.outeriterator = this.outer.iterator();
                                        this.state = 2;
                                case 2:
                                        while(this.outeriterator.hasNext())
                                        {
                                                TSource outer = this.outeriterator.next();
                                                TKey key = this.outerKeySelector.Invoke(outer);

                                                if(this.innerbuffer.containsKey(key))
                                                {
                                                        this.current = resultSelector.Invoke(outer, this.innerbuffer.get(key));
                                                        return true;
                                                }
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
