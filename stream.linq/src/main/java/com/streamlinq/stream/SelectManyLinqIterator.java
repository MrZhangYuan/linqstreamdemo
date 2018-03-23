package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 */

class SelectManyLinqIterator<TSource, TResult> extends LinqIterator<TResult>
{
        private Iterable<TSource> source;
        private Func_T_R<TSource, Iterable<TResult>> selector;
        private Iterator<TSource> sourceIterator;
        private Iterator<TResult> currentiterator;
        private boolean flag = false;
        private boolean needNext = true;

        public SelectManyLinqIterator(Iterable<TSource> source, Func_T_R<TSource, Iterable<TResult>> selector)
        {
                this.source = source;
                this.selector = selector;
        }

        @Override
        public LinqIterator<TResult> clone()
        {
                return new SelectManyLinqIterator(this.source, this.selector);
        }

        @Override
        public void close()
        {
                this.sourceIterator = null;
                this.currentiterator = null;
                super.close();
        }

        @Override
        public boolean hasNext()
        {
                try
                {
                        while(needNext)
                        {
                                switch(this.state)
                                {
                                        case 1:
                                                sourceIterator = source.iterator();
                                                this.state = 2;

                                        case 2:
                                                if(sourceIterator.hasNext())
                                                {
                                                        TSource current = sourceIterator.next();
                                                        this.currentiterator = this.selector.Invoke(current).iterator();
                                                        this.state = 3;
                                                        flag = true;
                                                }
                                                if(!flag)
                                                {
                                                        needNext = false;
                                                        this.close();
                                                        break;
                                                }
                                        case 3:
                                                if(this.currentiterator.hasNext())
                                                {
                                                        TResult current = this.currentiterator.next();
                                                        this.current = current;
                                                        return true;
                                                }
                                                flag = false;
                                                this.state = 2;
                                                break;
                                }
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
