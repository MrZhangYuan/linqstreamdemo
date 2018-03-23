package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 */

class WhereSelectLinqIterator<TSource, TResult> extends LinqIterator<TResult>
{
        private Iterable<TSource> source;
        private Func_T_Boolean<TSource> predicate;
        private Func_T_R<TSource, TResult> selector;
        private Iterator<TSource> enumerator;

        public WhereSelectLinqIterator(Iterable<TSource> source, Func_T_Boolean<TSource> predicate, Func_T_R<TSource, TResult> selector)
        {
                this.source = source;
                this.predicate = predicate;
                this.selector = selector;
        }

        @Override
        public LinqIterator<TResult> clone()
        {
                return new WhereSelectLinqIterator<TSource, TResult>(this.source, this.predicate, this.selector);
        }


        @Override
        public void close()
        {
                this.enumerator = null;
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
                                        this.enumerator = this.source.iterator();
                                        this.state = 2;
                                case 2:
                                        while(this.enumerator.hasNext())
                                        {
                                                TSource current = this.enumerator.next();
                                                if(this.predicate == null || this.predicate.Invoke(current))
                                                {
                                                        this.current = this.selector.Invoke(current);
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
        public <TResult2> LinqIterator<TResult2> select(Func_T_R<TResult, TResult2> selector)
        {
                return new WhereSelectLinqIterator<TSource, TResult2>
                        (
                                this.source,
                                this.predicate,
                                LinqIteratorHelper.combineSelectors(this.selector, selector)
                        );
        }

        @Override
        public LinqIterator<TResult> where(Func_T_Boolean<TResult> predicate)
        {
                return new WhereLinqIterator<TResult>(this, predicate);
        }
}
