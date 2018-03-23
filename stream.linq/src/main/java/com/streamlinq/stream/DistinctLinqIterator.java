package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.Func_T_Boolean;
import main.java.com.streamlinq.core.Func_T_R;

import java.util.Iterator;

/**
 * Created by ZhangYuan on 2018/1/21.
 */

class DistinctLinqIterator<TSource, TKey> extends LinqIterator<TSource>
{
        private Iterable<TSource> sources;
        private Func_T_R<TSource, TKey> keyselector = null;
        private EqualityComparer<Object> equalityComparer = null;

        private Iterator<TSource> sourceIterator;
        private Set<Object> set = null;

        public DistinctLinqIterator(Iterable<TSource> sources, Func_T_R<TSource, TKey> keyselector, EqualityComparer<TSource> equalitycomparer)
        {
                this.sources = sources;
                this.keyselector = keyselector;
                this.equalityComparer = (EqualityComparer<Object>) equalitycomparer;
        }

        @Override
        public LinqIterator<TSource> clone()
        {
                return new DistinctLinqIterator<TSource, TKey>(this.sources, this.keyselector, (EqualityComparer<TSource>) this.equalityComparer);
        }

        @Override
        public void close()
        {
                this.sourceIterator = null;
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
                                        this.sourceIterator = this.sources.iterator();
                                        set = new Set<>(this.equalityComparer);
                                        this.state = 2;
                                case 2:
                                        while(this.sourceIterator.hasNext())
                                        {
                                                TSource current = this.sourceIterator.next();
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
