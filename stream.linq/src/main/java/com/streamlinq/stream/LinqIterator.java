package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 *
 */

public abstract class LinqIterator<TSource> implements Iterable<TSource>, Iterator<TSource>, AutoCloseable
{
        private long threadId;
        protected int state;
        protected TSource current;

        public LinqIterator()
        {
                this.threadId = Thread.currentThread().getId();
        }

        @Override
        public abstract LinqIterator<TSource> clone();

        @Override
        public void close()
        {
                this.current = null;
                this.state = -1;
        }

        @Override
        public Iterator<TSource> iterator()
        {
                if(this.state == 0
                        && this.threadId == Thread.currentThread().getId())
                {
                        this.state = 1;
                        return this;
                }
                LinqIterator<TSource> iterator = this.clone();
                iterator.state = 1;
                return iterator;
        }

        @Override
        public abstract boolean hasNext() throws RuntimeException;

        @Override
        public TSource next()
        {
                return this.current;
        }

        public abstract <TResult> LinqIterator<TResult> select(Func_T_R<TSource, TResult> selector);

        public abstract LinqIterator<TSource> where(Func_T_Boolean<TSource> predicate);
}
