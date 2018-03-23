package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 */

class SelectLinqIterator<TSource, TResult> extends LinqIterator<TResult>
{
        private Iterable<TSource> _source;
        private Func_T_R<TSource, TResult> _selector;
        private Iterator<TSource> _enumerator;

        public SelectLinqIterator(Iterable<TSource> source, Func_T_R<TSource, TResult> selector)
        {
                this._source = source;
                this._selector = selector;
        }

        @Override
        public LinqIterator<TResult> clone()
        {
                return new SelectLinqIterator<TSource, TResult>(this._source, this._selector);
        }

        @Override
        public void close()
        {
                this._enumerator = null;
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
                                        this._enumerator = this._source.iterator();
                                        this.state = 2;
                                case 2:
                                        if(this._enumerator.hasNext())
                                        {
                                                this.current = this._selector.Invoke(this._enumerator.next());
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
        public <TResult2> LinqIterator<TResult2> select(Func_T_R<TResult, TResult2> selector)
        {
                return new SelectLinqIterator<TSource, TResult2>
                        (
                                this._source,
                                LinqIteratorHelper.combineSelectors(this._selector, selector)
                        );
        }

        @Override
        public LinqIterator<TResult> where(Func_T_Boolean<TResult> predicate)
        {
                return new WhereLinqIterator<>(this, predicate);
        }
}