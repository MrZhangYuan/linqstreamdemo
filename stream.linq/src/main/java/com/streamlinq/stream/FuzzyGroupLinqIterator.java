package main.java.com.streamlinq.stream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import jdk.nashorn.internal.runtime.Source;
import main.java.com.streamlinq.Ex.StringUlitis;
import main.java.com.streamlinq.core.Func_T_Boolean;
import main.java.com.streamlinq.core.Func_T_R;

class _Temp<T>
{
        public T Item;
        public String CompareKey;
        public String IdentityKey;

        public _Temp(T item, String idkey, String comkey)
        {
                this.Item = item;
                this.IdentityKey = idkey;
                this.CompareKey = comkey;
        }

        @Override
        public boolean equals(Object o)
        {
                _Temp<T> item = (_Temp<T>) o;
                if(item != null)
                {
                        return this.IdentityKey.equals(item.IdentityKey);
                }
                return false;
        }

        @Override
        public int hashCode()
        {
                return this.IdentityKey.hashCode();
        }
}


class FuzzyGroupLinqIterator<TSource, TKey> extends LinqIterator<Iterable<TSource>>
{
        private Iterable<TSource> source = null;
        Func_T_R<TSource, String> keyselector;
        Func_T_R<TSource, String> groupkeyselector;
        double coefficient;

        public FuzzyGroupLinqIterator
                (
                        Iterable<TSource> source,
                        Func_T_R<TSource, String> keyselector,
                        Func_T_R<TSource, String> groupkeyselector,
                        double coefficient
                )
        {
                this.source = source;
                this.keyselector = keyselector;
                this.groupkeyselector = groupkeyselector;
                this.coefficient = coefficient;

                if(this.coefficient < 0)
                {
                        this.coefficient = 0;
                }

                if(this.coefficient > 1)
                {
                        this.coefficient = 1;
                }
        }

        @Override
        public LinqIterator<Iterable<TSource>> clone()
        {
                try
                {
                        return new FuzzyGroupLinqIterator<>(this.source, this.keyselector, this.groupkeyselector, this.coefficient);
                }
                catch(Exception ignored)
                {

                }
                return null;
        }

        @Override
        public void close()
        {
                super.close();
        }

        private boolean _needNext = true;

        Iterator<_Temp<TSource>> parentsourcewrapperiterator = null;
        Iterator<_Temp<TSource>> childsourcewrapperiterator = null;

        _Temp<TSource> parentCurrent = null;

        Set<_Temp<TSource>> olditems = null;

        int outindex = 0;
        int innerindex = 0;

        @Override
        public boolean hasNext() throws RuntimeException
        {
                try
                {
                        while(_needNext)
                        {
                                switch(this.state)
                                {
                                        case 1:
                                                this.parentsourcewrapperiterator = LinqStream.fromIterable(this.source)
                                                        .SELECT
                                                                (
                                                                        _p -> new _Temp<TSource>
                                                                                (
                                                                                        _p,
                                                                                        this.keyselector.Invoke(_p),
                                                                                        this.groupkeyselector.Invoke(_p)
                                                                                )
                                                                )
                                                        .TOLIST()
                                                        .iterator();

                                                olditems = new Set<>();
                                                this.state = 2;

                                        case 2:

                                                while(this.parentsourcewrapperiterator.hasNext())
                                                {

                                                }

                                                break;

                                        case 3:

                                                List<TSource> groupitem = new ArrayList<>();

                                                while(this.childsourcewrapperiterator.hasNext())
                                                {
                                                        if(outindex == innerindex)
                                                        {
                                                                continue;
                                                        }

                                                        _Temp<TSource> currentitem = this.childsourcewrapperiterator.next();

                                                        double precent = StringUlitis.LevenshteinDistancePercent(parentCurrent.CompareKey, currentitem.CompareKey);

                                                        if(precent >= this.coefficient)
                                                        {
                                                                precent = Math.round(precent * 100000000) / 100000000;

                                                                if(olditems.add(currentitem))
                                                                {
                                                                        groupitem.add(currentitem.Item);
                                                                }
                                                        }
                                                }

                                                if(groupitem.size() > 0)
                                                {
                                                        this.current = groupitem;
                                                        return true;
                                                }
                                                break;
                                }
                        }
                }
                catch(Exception ex)
                {
                        throw new RuntimeException(ex);
                }
                return false;
        }

        @Override
        public <TResult> LinqIterator<TResult> select(Func_T_R<Iterable<TSource>, TResult> selector)
        {
                return new SelectLinqIterator<>(this, selector);
        }

        @Override
        public LinqIterator<Iterable<TSource>> where(Func_T_Boolean<Iterable<TSource>> predicate)
        {
                return new WhereLinqIterator<>(this, predicate);
        }
}
