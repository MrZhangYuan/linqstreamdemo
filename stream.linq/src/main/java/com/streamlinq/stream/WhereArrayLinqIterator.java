package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

/**
 * Created by yuany on 2018/1/12.
 */

class WhereArrayLinqIterator<TSource> extends LinqIterator<TSource>
{
        private TSource[] source;
        private Func_T_Boolean<TSource> predicate;
        private int index;

        public WhereArrayLinqIterator(TSource[] source, Func_T_Boolean<TSource> predicate)
        {
                this.source = source;
                this.predicate = predicate;
        }

        @Override
        public LinqIterator<TSource> clone()
        {
                return new WhereArrayLinqIterator<TSource>(this.source, this.predicate);
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
                                        if(this.predicate.Invoke(source))
                                        {
                                                this.current = source;
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
        public <TResult> LinqIterator<TResult> select(Func_T_R<TSource, TResult> selector)
        {
                return new WhereSelectArrayLinqIterator<TSource, TResult>
                        (
                                this.source,
                                this.predicate,
                                selector
                        );
        }

        @Override
        public LinqIterator<TSource> where(Func_T_Boolean<TSource> predicate)
        {
                return new WhereArrayLinqIterator<TSource>
                        (
                                this.source,
                                LinqIteratorHelper.combinePredicates(this.predicate, predicate)
                        );
        }
}
