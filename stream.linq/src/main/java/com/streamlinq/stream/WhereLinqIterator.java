package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 */

class WhereLinqIterator<TSource> extends LinqIterator<TSource>
{
        private Iterable<TSource> source;
        private Func_T_Boolean<TSource> predicate;
        private Iterator<TSource> enumerator;

        public WhereLinqIterator(Iterable<TSource> source, Func_T_Boolean<TSource> predicate)
        {
                this.source = source;
                this.predicate = predicate;
        }

        @Override
        public LinqIterator<TSource> clone()
        {
                return new WhereLinqIterator<TSource>(this.source, this.predicate);
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
                                                if(this.predicate.Invoke(current))
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
                return new WhereSelectLinqIterator<TSource, TResult>
                        (
                                this.source,
                                this.predicate,
                                selector
                        );
        }

        @Override
        public LinqIterator<TSource> where(Func_T_Boolean<TSource> predicate)
        {
                return new WhereLinqIterator<TSource>
                        (
                                this.source,
                                LinqIteratorHelper.combinePredicates(this.predicate, predicate)
                        );
        }
}
