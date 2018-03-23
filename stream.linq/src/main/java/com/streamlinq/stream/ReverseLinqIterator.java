package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.Func_T_Boolean;
import main.java.com.streamlinq.core.Func_T_R;

import java.util.Iterator;

/**
 * Created by ZhangYuan on 2018/1/21.
 */

class ReverseLinqIterator<TSource> extends LinqIterator<TSource>
{
        private Iterable<TSource> sources;
        private Iterator<TSource> sourceIterator = null;
        private Buffer<TSource> buffer = null;
        private int index;

        public ReverseLinqIterator(Iterable<TSource> iterable)
        {
                this.sources = iterable;
        }

        @Override
        public LinqIterator<TSource> clone()
        {
                return new ReverseLinqIterator<TSource>(this.sources);
        }

        @Override
        public void close()
        {
                this.sourceIterator = null;
                this.buffer = null;
                this.index = 0;
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
                                        this.buffer = new Buffer<>(this.sources);
                                        this.index = this.buffer.count - 1;
                                        this.state = 2;
                                case 2:
                                        if(this.index >= 0)
                                        {
                                                this.current = (TSource) this.buffer.items[this.index];
                                                --this.index;
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

