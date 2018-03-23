package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 */

class ConcatLinqIterator<TSource> extends LinqIterator<TSource>
{
        private Iterable<TSource> first;
        private Iterable<TSource> second;

        private Iterator<TSource> firstIterator;
        private Iterator<TSource> secondIterator;


        public ConcatLinqIterator(Iterable<TSource> first, Iterable<TSource> second)
        {
                this.first = first;
                this.second = second;
        }

        @Override
        public LinqIterator<TSource> clone()
        {
                return new ConcatLinqIterator<>(this.first, this.second);
        }

        @Override
        public void close()
        {
                this.firstIterator = null;
                this.secondIterator = null;
                super.close();
        }

        @Override
        public boolean hasNext() throws RuntimeException
        {
                switch(this.state)
                {
                        case 1:
                                this.firstIterator = this.first.iterator();
                                this.state = 2;
                        case 2:
                                if(this.firstIterator.hasNext())
                                {
                                        TSource current = this.firstIterator.next();
                                        this.current = current;
                                        return true;
                                }
                                this.state = 3;
                        case 3:
                                this.secondIterator = this.second.iterator();
                                this.state = 4;
                        case 4:
                                if(this.secondIterator.hasNext())
                                {
                                        TSource current = this.secondIterator.next();
                                        this.current = current;
                                        return true;
                                }
                                this.close();
                                break;
                }

                return false;
        }

        @Override
        public <TResult> LinqIterator<TResult> select(Func_T_R<TSource, TResult> selector)
        {
                return new SelectLinqIterator<>(this, selector);
        }

        @Override
        public LinqIterator<TSource> where(Func_T_Boolean<TSource> predicate)
        {
                return new WhereLinqIterator<>(this, predicate);
        }
}
