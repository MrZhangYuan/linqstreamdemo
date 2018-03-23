package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 */

class IntersectLinqIterator<TSource, TKey> extends LinqIterator<TSource>
{
        private Iterable<TSource> first;
        private Iterable<TSource> second;
        private Iterator<TSource> firstenumerator;
        private Iterator<TSource> secondenumerator;
        private Func_T_R<TSource, TKey> keyselector = null;
        private Set<Object> set = null;
        private EqualityComparer<Object> equalityComparer = null;

        public IntersectLinqIterator(Iterable<TSource> first, Iterable<TSource> second, Func_T_R<TSource, TKey> keyselector, EqualityComparer<TSource> equalitycomparer)
        {
                this.first = first;
                this.second = second;
                this.keyselector = keyselector;

                this.equalityComparer = (EqualityComparer<Object>)equalitycomparer;
        }

        @Override
        public LinqIterator<TSource> clone()
        {
                return new IntersectLinqIterator<TSource, TKey>(this.first, this.second, this.keyselector, (EqualityComparer<TSource>)this.equalityComparer);
        }

        @Override
        public void close()
        {
                this.firstenumerator = null;
                this.secondenumerator = null;
                this.set = null;
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
                                        this.firstenumerator = this.first.iterator();
                                        this.secondenumerator = this.second.iterator();
                                        set = new Set<>(this.equalityComparer);
                                        this.state = 2;
                                case 2:
                                        while(this.secondenumerator.hasNext())
                                        {
                                                set.add(keyselector == null ? this.secondenumerator.next() : keyselector.Invoke(this.secondenumerator.next()));
                                        }

                                        while(this.firstenumerator.hasNext())
                                        {
                                                TSource current = this.firstenumerator.next();

                                                if(set.remove(keyselector == null ? current : keyselector.Invoke(current)))
                                                {
                                                        this.current = current;
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
        public <TResult> LinqIterator<TResult> select(Func_T_R<TSource, TResult> selector)
        {
                return new SelectLinqIterator<TSource, TResult>(this, selector);
        }

        @Override
        public LinqIterator<TSource> where(Func_T_Boolean<TSource> predicate)
        {
                return new WhereLinqIterator<TSource>(this, predicate);
        }
}