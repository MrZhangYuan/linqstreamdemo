package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

import java.util.Iterator;

/**
 * Created by yuany on 2018/1/16.
 */

class SkipLinqIterator<TSource> extends LinqIterator<TSource>
{
        private Iterable<TSource> source;
        private Func_T_Boolean<TSource> predicate = null;
        private Iterator<TSource> enumerator;
        private int count = 0;
        private int currentindex = 0;
        private boolean flag = false;

        public SkipLinqIterator(Iterable<TSource> source, Func_T_Boolean<TSource> predicate, int count) throws Exception
        {
                if(source == null
                        || count < 0)
                {
                        throw new Exception("SkipLinqIterator 参数异常。");
                }
                this.source = source;
                this.predicate = predicate;
                this.count = count;
        }


        @Override
        public LinqIterator<TSource> clone()
        {
                try
                {
                        return new SkipLinqIterator<TSource>(this.source, this.predicate, this.count);
                }
                catch(Exception e)
                {
                }
                return null;
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
                                        if(this.predicate == null)
                                        {
                                                while(this.enumerator.hasNext())
                                                {
                                                        if(this.currentindex >= this.count)
                                                        {
                                                                this.current = this.enumerator.next();
                                                                return true;
                                                        }
                                                        ++this.currentindex;
                                                }
                                        }
                                        else
                                        {
                                                while(this.enumerator.hasNext())
                                                {
                                                        TSource current = this.enumerator.next();
                                                        if(flag || this.predicate.Invoke(current))
                                                        {
                                                                this.current = current;
                                                                flag = true;
                                                                return true;
                                                        }
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
                return new SelectLinqIterator<>(this, selector);
        }

        @Override
        public LinqIterator<TSource> where(Func_T_Boolean<TSource> predicate)
        {
                return new WhereLinqIterator<>(this, predicate);
        }
}
