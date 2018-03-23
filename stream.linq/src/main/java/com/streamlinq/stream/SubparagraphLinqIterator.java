package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.Func_T_Boolean;
import main.java.com.streamlinq.core.Func_T_R;

import java.util.Iterator;
import java.util.List;

/**
 * Created by yuany on 2018/3/22.
 */

class SubparagraphLinqIterator<TSource> extends LinqIterator<Iterator<TSource>>
{
        private Iterable<TSource> source = null;
        private int rangelength = 0;

        private LinqStream<TSource> sourceIterator = null;

        public SubparagraphLinqIterator(Iterable<TSource> source, int rangelength) throws Exception
        {
                if(rangelength < 1)
                {
                        throw new Exception("rangelength参数应大于等于1");
                }

                this.source = source;
                this.rangelength = rangelength;
        }


        @Override
        public LinqIterator<Iterator<TSource>> clone()
        {
                try
                {
                        return new SubparagraphLinqIterator<>(this.source, this.rangelength);
                }
                catch(Exception ignored)
                {

                }
                return null;
        }

        @Override
        public void close()
        {
                this.sourceIterator = null;
                super.close();
        }

        @Override
        public boolean hasNext() throws RuntimeException
        {
                try
                {
                        switch(this.state)
                        {
                                case 1:
                                        this.sourceIterator = LinqStream.fromIterable(this.source);
                                        this.state = 2;

                                case 2:

                                        if(this.sourceIterator.ANY())
                                        {
                                                this.current = this.sourceIterator.TAKE(rangelength).iterator();
                                                this.sourceIterator = this.sourceIterator.SKIP(this.rangelength);
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
        public <TResult> LinqIterator<TResult> select(Func_T_R<Iterator<TSource>, TResult> selector)
        {
                return new SelectLinqIterator<>(this, selector);
        }

        @Override
        public LinqIterator<Iterator<TSource>> where(Func_T_Boolean<Iterator<TSource>> predicate)
        {
                return new WhereLinqIterator<>(this, predicate);
        }
}
