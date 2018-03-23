package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

/**
 * Created by yuany on 2018/1/12.
 */


class WhereSelectArrayLinqIterator<TSource, TResult> extends LinqIterator<TResult>
{
        private TSource[] source;
        private Func_T_Boolean<TSource> predicate;
        private Func_T_R<TSource, TResult> selector;
        private int index;

        public WhereSelectArrayLinqIterator(TSource[] source, Func_T_Boolean<TSource> predicate, Func_T_R<TSource, TResult> selector)
        {
                this.source = source;
                this.predicate = predicate;
                this.selector = selector;
        }


        @Override
        public LinqIterator<TResult> clone()
        {
                return new WhereSelectArrayLinqIterator<TSource, TResult>(this.source, this.predicate, this.selector);
        }


        @Override
        public boolean hasNext()
        {
                try
                {
                        if(this.state == 1)
                        {
                                while(this.index < this.source.length)
                                {
                                        TSource source = this.source[this.index];
                                        this.index = this.index + 1;
                                        if(this.predicate == null || this.predicate.Invoke(source))
                                        {
                                                this.current = this.selector.Invoke(source);
                                                return true;
                                        }
                                }
                                this.close();
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
                return new WhereSelectArrayLinqIterator<TSource, TResult2>
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
