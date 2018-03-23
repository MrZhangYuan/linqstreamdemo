package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.Func_T2_R;
import main.java.com.streamlinq.core.Func_T_Boolean;
import main.java.com.streamlinq.core.Func_T_R;

import java.util.Iterator;

/**
 * Created by ZhangYuan on 2018/1/21.
 */

class ZipLinqIterator<TFirst, TSecond, TResult> extends LinqIterator<TResult>
{
        private Iterable<TFirst> firsts = null;
        private Iterable<TSecond> seconds = null;
        private Func_T2_R<TFirst, TSecond, TResult> resultSelector = null;

        private Iterator<TFirst> firstIterator;
        private Iterator<TSecond> secondIterator;

        ZipLinqIterator(Iterable<TFirst> firsts, Iterable<TSecond> seconds, Func_T2_R<TFirst, TSecond, TResult> resultselector)
        {
                this.firsts = firsts;
                this.seconds = seconds;
                this.resultSelector = resultselector;
        }

        @Override
        public LinqIterator<TResult> clone()
        {
                return new ZipLinqIterator<>(this.firsts, this.seconds, this.resultSelector);
        }

        @Override
        public void close()
        {
                this.firstIterator = null;
                this.secondIterator = null;
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
                                        this.firstIterator = this.firsts.iterator();
                                        this.secondIterator = this.seconds.iterator();
                                        this.state = 2;
                                case 2:
                                        if(this.firstIterator.hasNext() && this.secondIterator.hasNext())
                                        {
                                                TFirst first = this.firstIterator.next();
                                                TSecond second = this.secondIterator.next();

                                                this.current = this.resultSelector.Invoke(first, second);
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
