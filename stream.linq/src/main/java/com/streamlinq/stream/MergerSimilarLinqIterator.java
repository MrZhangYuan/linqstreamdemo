package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.Iterator;

/**
 * Created by ZhangYuan on 2018/1/14.
 */

class MergerSimilarLinqIterator<TSource, TKey, TResult> extends LinqIterator<TResult>
{
        private Iterable<TSource> source = null;
        private Func_T_R<TSource, TKey> similarKeySelector;
        private Func_T_R<Iterable<TSource>, TResult> mergerfunc;

        private Iterator<Grouping<TKey, TSource>> groupingterator = null;

        public MergerSimilarLinqIterator(Iterable<TSource> source, Func_T_R<TSource, TKey> similarKeySelector, Func_T_R<Iterable<TSource>, TResult> mergerfunc)
        {
                this.source = source;
                this.similarKeySelector = similarKeySelector;
                this.mergerfunc = mergerfunc;
        }

        @Override
        public LinqIterator<TResult> clone()
        {
                return new MergerSimilarLinqIterator<>(this.source, this.similarKeySelector, this.mergerfunc);
        }

        @Override
        public void close()
        {
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
                                        this.groupingterator = new GroupedLinqIterator_3<TSource, TKey, TSource>
                                                (
                                                        this.source,
                                                        this.similarKeySelector,
                                                        IdentityFunction.Instance()
                                                )
                                                .iterator();
                                        this.state = 2;
                                case 2:
                                        if(this.groupingterator.hasNext())
                                        {
                                                Grouping<TKey, TSource> current = this.groupingterator.next();
                                                TResult result = this.mergerfunc.Invoke(current);
                                                this.current = result;
                                                return true;
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
