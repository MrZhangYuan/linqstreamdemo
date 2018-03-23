package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.Func_T_Boolean;
import main.java.com.streamlinq.core.Func_T_R;

import java.util.Iterator;

/**
 * Created by ZhangYuan on 2018/1/21.
 */

class UnionLinqIterator<TSource, TKey> extends LinqIterator<TSource>
{
        private Iterable<TSource> first;
        private Iterable<TSource> second;
        private Func_T_R<TSource, TKey> keyselector = null;
        private EqualityComparer<Object> equalityComparer = null;

        private Iterator<TSource> firstenumerator;
        private Iterator<TSource> secondenumerator;
        private Set<Object> set = null;

        public UnionLinqIterator(Iterable<TSource> first, Iterable<TSource> second, Func_T_R<TSource, TKey> keyselector, EqualityComparer<TSource> equalitycomparer)
        {
                this.first = first;
                this.second = second;
                this.keyselector = keyselector;
                this.equalityComparer = (EqualityComparer<Object>) equalitycomparer;
        }

        @Override
        public LinqIterator<TSource> clone()
        {
                return new UnionLinqIterator<TSource, TKey>(this.first, this.second, this.keyselector, (EqualityComparer<TSource>) this.equalityComparer);
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
                                        while(this.firstenumerator.hasNext())
                                        {
                                                TSource current = this.firstenumerator.next();
                                                if(this.set.add(keyselector == null ? current : keyselector.Invoke(current)))
                                                {
                                                        this.current = current;
                                                        return true;
                                                }
                                        }
                                        this.state = 3;
                                case 3:
                                        while(this.secondenumerator.hasNext())
                                        {
                                                TSource current = this.secondenumerator.next();
                                                if(set.add(keyselector == null ? current : keyselector.Invoke(current)))
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
