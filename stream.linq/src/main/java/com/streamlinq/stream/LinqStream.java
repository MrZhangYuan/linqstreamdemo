package main.java.com.streamlinq.stream;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import main.java.com.streamlinq.Ex.NumberEx;
import main.java.com.streamlinq.core.*;

/*
 * Created by yuany on 2017/12/25.
 *
 */

/**
 * Linq
 *
 * @param <T>
 */

public final class LinqStream<T> implements Iterable<T>
{
        //------------------------------------------------------------------------------------------------------------
        //
        //  Private Class
        //
        //------------------------------------------------------------------------------------------------------------
        private static class SimpleLinqIterator<TSource> extends LinqIterator<TSource>
        {
                private Iterable<TSource> source;
                private Iterator<TSource> enumerator;

                SimpleLinqIterator(Iterable<TSource> source)
                {
                        this.source = source;
                }

                @Override
                public LinqIterator<TSource> clone()
                {
                        return new SimpleLinqIterator<>(this.source);
                }

                @Override
                public boolean hasNext()
                {
                        switch(this.state)
                        {
                                case 1:
                                        this.enumerator = this.source.iterator();
                                        this.state = 2;
                                case 2:
                                        if(this.enumerator.hasNext())
                                        {
                                                this.current = this.enumerator.next();
                                                return true;
                                        }
                                        this.close();
                                        break;
                        }
                        return false;
                }

                public <TResult> LinqIterator<TResult> select(Func_T_R<TSource, TResult> selector)
                {
                        return new SelectLinqIterator<>(this.source, selector);
                }

                public LinqIterator<TSource> where(Func_T_Boolean<TSource> predicate)
                {
                        return new WhereLinqIterator<>(this.source, predicate);
                }
        }

        private static class SimpleListLinqIterator<TSource> extends LinqIterator<TSource>
        {
                private List<TSource> source;
                private ListIterator<TSource> enumerator;

                SimpleListLinqIterator(List<TSource> source)
                {
                        this.source = source;
                }

                @Override
                public LinqIterator<TSource> clone()
                {
                        return new SimpleListLinqIterator<>(this.source);
                }

                @Override
                public boolean hasNext()
                {
                        switch(this.state)
                        {
                                case 1:
                                        this.enumerator = this.source.listIterator();
                                        this.state = 2;
                                case 2:
                                        if(this.enumerator.hasNext())
                                        {
                                                this.current = this.enumerator.next();
                                                return true;
                                        }
                                        this.close();
                                        break;
                        }
                        return false;
                }

                public <TResult> LinqIterator<TResult> select(Func_T_R<TSource, TResult> selector)
                {
                        return new WhereSelectListLinqIterator<>(this.source, null, selector);
                }

                public LinqIterator<TSource> where(Func_T_Boolean<TSource> predicate)
                {
                        return new WhereListLinqIterator<>(this.source, predicate);
                }
        }

        private static class SimpleArrayLinqIterator<TSource> extends LinqIterator<TSource>
        {
                private TSource[] source;
                private int index;

                SimpleArrayLinqIterator(TSource[] source)
                {
                        this.source = source;
                }

                @Override
                public LinqIterator<TSource> clone()
                {
                        return new SimpleArrayLinqIterator<>(this.source);
                }

                @Override
                public boolean hasNext()
                {
                        if(this.state == 1)
                        {
                                if(this.index < this.source.length)
                                {
                                        this.current = this.source[this.index];
                                        this.index = this.index + 1;
                                        return true;
                                }
                                this.close();
                        }
                        return false;
                }

                public <TResult> LinqIterator<TResult> select(Func_T_R<TSource, TResult> selector)
                {
                        return new WhereSelectArrayLinqIterator<>(this.source, null, selector);
                }

                public LinqIterator<TSource> where(Func_T_Boolean<TSource> predicate)
                {
                        return new WhereArrayLinqIterator<>(this.source, predicate);
                }
        }


        //------------------------------------------------------------------------------------------------------------
        //
        //  Fields & Constractors
        //
        //------------------------------------------------------------------------------------------------------------
        private LinqIterator<T> linqIterator;

        private LinqStream(LinqIterator<T> linqIterator) throws Exception
        {
                if(linqIterator == null)
                {
                        throw new Exception("初始化LinqStream异常，linqIterator 不能为null");
                }

                this.linqIterator = linqIterator;
        }

        private LinqStream(Iterable<T> iterable) throws Exception
        {
                if(iterable == null)
                {
                        throw new Exception("初始化LinqStream异常，iterable 不能为null");
                }

                if(iterable instanceof List<?>)
                {
                        this.linqIterator = new SimpleListLinqIterator<>((List<T>) iterable);
                }
                else
                {
                        this.linqIterator = new SimpleLinqIterator<>(iterable);
                }
        }

        private LinqStream(T[] array) throws Exception
        {
                if(array == null)
                {
                        throw new Exception("初始化LinqStream异常，array 不能为null");
                }
                this.linqIterator = new SimpleArrayLinqIterator<>(array);
        }

        @Override
        public Iterator<T> iterator()
        {
                return linqIterator.clone().iterator();
        }

        //------------------------------------------------------------------------------------------------------------
        //
        //  Static Methods
        //
        //------------------------------------------------------------------------------------------------------------
        public static <M> LinqStream<M> fromIterable(Iterable<M> iterable) throws Exception
        {
                return new LinqStream<M>(iterable);
        }

        public static <M> LinqStream<M> fromArray(M[] array) throws Exception
        {
                return new LinqStream<M>(array);
        }


        //------------------------------------------------------------------------------------------------------------
        //
        //  Query Methods
        //
        //------------------------------------------------------------------------------------------------------------
        public LinqStream<T> WHERE(Func_T_Boolean<T> predicate) throws Exception
        {
                return new LinqStream<>(this.linqIterator.where(predicate));
        }

        public <K> LinqStream<K> SELECT(Func_T_R<T, K> predicate) throws Exception
        {
                return new LinqStream<K>(this.linqIterator.select(predicate));
        }


        public void FOREACH(Action_T<T> action) throws Exception
        {
                if(action == null)
                {
                        throw new Exception("action 不能为null。");
                }

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                action.Invoke(item);
                        }
                }
        }

        public boolean CONTAINS(T item)
        {
                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T inner : iterator)
                        {
                                if(inner != null)
                                {
                                        if(inner.equals(item))
                                        {
                                                return true;
                                        }
                                }
                                else
                                {
                                        if(item == null)
                                        {
                                                return true;
                                        }
                                }
                        }
                        return false;
                }
        }

        public boolean CONTAINS
                (
                        Func_T_Boolean<T> compairkey
                )
                throws Exception
        {
                if(compairkey == null)
                {
                        throw new Exception("比较键选择器compairkey不能为null。");
                }

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T inner : iterator)
                        {
                                if(compairkey.Invoke(inner))
                                {
                                        return true;
                                }
                        }
                        return false;
                }
        }

        public int COUNT()
        {
                int i = 0;
                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T inner : iterator)
                        {
                                i++;
                        }
                }
                return i;
        }

        public int COUNT(Func_T_Boolean<T> predicate) throws Exception
        {
                if(predicate == null)
                {
                        throw new Exception("predicate 不能为null。");
                }

                int i = 0;
                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T inner : iterator)
                        {
                                if(predicate.Invoke(inner))
                                {
                                        i++;
                                }
                        }
                }
                return i;
        }

        public boolean ANY()
        {
                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T inner : iterator)
                        {
                                return true;
                        }
                        return false;
                }
        }

        public boolean ANY(Func_T_Boolean<T> keyselector) throws Exception
        {
                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                if(keyselector.Invoke(item))
                                {
                                        return true;
                                }
                        }
                        return false;
                }
        }

        public boolean ALL(Func_T_Boolean<T> keyselector) throws Exception
        {
                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                if(!keyselector.Invoke(item))
                                {
                                        return false;
                                }
                        }
                        return true;
                }
        }

        public int SUM_INT(Func_T_Int<T> keyselector) throws Exception
        {
                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        int sum = 0;
                        for(T item : iterator)
                        {
                                sum += keyselector.Invoke(item);
                        }
                        return sum;
                }
        }

        public BigDecimal SUM_BIGDECIMAL(Func_T_R<T, BigDecimal> keyselector) throws Exception
        {
                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        BigDecimal sum = BigDecimal.ZERO;
                        for(T item : iterator)
                        {
                                sum = sum.add(NumberEx.getValueOrDefault(keyselector.Invoke(item)));
                        }
                        return sum;
                }
        }

        public int MAX_INT(Func_T_Int<T> keyselector) throws Exception
        {
                int max = 0;
                boolean flag = false;

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                int current = keyselector.Invoke(item);
                                if(flag)
                                {
                                        if(current > max)
                                        {
                                                max = current;
                                        }
                                }
                                else
                                {
                                        max = current;
                                        flag = true;
                                }
                        }
                }

                if(flag)
                {
                        return max;
                }
                throw new Exception("当前没有元素。");
        }

        public int MIN_INT(Func_T_Int<T> keyselector) throws Exception
        {
                int min = 0;
                boolean flag = false;

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                int current = keyselector.Invoke(item);
                                if(flag)
                                {
                                        if(current < min)
                                        {
                                                min = current;
                                        }
                                }
                                else
                                {
                                        min = current;
                                        flag = true;
                                }
                        }
                }

                if(flag)
                {
                        return min;
                }
                throw new Exception("当前没有元素。");
        }

        public <R extends Comparable<R>> R MAX(Func_T_R<T, R> keyselector) throws Exception
        {
                R max = null;
                boolean flag = false;

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                R current = keyselector.Invoke(item);
                                if(current == null)
                                {
                                        continue;
                                }

                                if(flag)
                                {
                                        if(current.compareTo(max) > 0)
                                        {
                                                max = current;
                                        }
                                }
                                else
                                {
                                        max = current;
                                        flag = true;
                                }
                        }
                }
                if(flag)
                {
                        return max;
                }
                throw new Exception("当前没有元素。");
        }

        public T MAX(Comparator<T> comparator) throws Exception
        {
                if(comparator == null)
                {
                        throw new Exception("comparator 不能为null。");
                }

                T max = null;
                boolean flag = false;

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                if(item == null)
                                {
                                        continue;
                                }

                                if(flag)
                                {
                                        if(comparator.compare(max, item) < 0)
                                        {
                                                max = item;
                                        }
                                }
                                else
                                {
                                        max = item;
                                        flag = true;
                                }
                        }
                }

                if(flag)
                {
                        return max;
                }
                throw new Exception("当前没有元素。");
        }

        public <R extends Comparable<R>> R MIN(Func_T_R<T, R> keyselector) throws Exception
        {
                R min = null;
                boolean flag = false;

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                R current = keyselector.Invoke(item);
                                if(current == null)
                                {
                                        continue;
                                }

                                if(flag)
                                {
                                        if(current.compareTo(min) < 0)
                                        {
                                                min = current;
                                        }
                                }
                                else
                                {
                                        min = current;
                                        flag = true;
                                }
                        }
                }

                if(flag)
                {
                        return min;
                }
                throw new Exception("当前没有元素。");
        }

        public T MIN(Comparator<T> comparator) throws Exception
        {
                if(comparator == null)
                {
                        throw new Exception("comparator 不能为null。");
                }

                T min = null;
                boolean flag = false;

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                if(item == null)
                                {
                                        continue;
                                }

                                if(flag)
                                {
                                        if(comparator.compare(min, item) > 0)
                                        {
                                                min = item;
                                        }
                                }
                                else
                                {
                                        min = item;
                                        flag = true;
                                }
                        }
                }
                if(flag)
                {
                        return min;
                }
                throw new Exception("当前没有元素。");
        }


        public <TAccumulate> TAccumulate AGGREGATE
                (
                        TAccumulate seed,
                        Func_T2_R<TAccumulate, T, TAccumulate> func
                )
                throws Exception
        {
                if(func == null)
                {
                        throw new Exception("累加函数不能为null");
                }

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                func.Invoke(seed, item);
                        }
                }

                return seed;
        }


        public <TAccumulate, TResult> TResult AGGREGATE
                (
                        TAccumulate seed,
                        Func_T2_R<TAccumulate, T, TAccumulate> func,
                        Func_T_R<TAccumulate, TResult> resultSelector
                )
                throws Exception
        {
                if(func == null)
                {
                        throw new Exception("累加函数不能为null");
                }

                if(resultSelector == null)
                {
                        throw new Exception("结果筛选器resultSelector不能为null。");
                }

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                func.Invoke(seed, item);
                        }
                }
                return resultSelector.Invoke(seed);
        }

        public LinqStream<T> EXCEPT(Iterable<T> secondstream) throws Exception
        {
                return new LinqStream<>
                        (
                                new ExceptLinqIterator<>
                                        (
                                                this.linqIterator,
                                                secondstream,
                                                null,
                                                null
                                        )
                        );
        }

        public LinqStream<T> EXCEPT
                (
                        Iterable<T> second,
                        EqualityComparer<T> equalityComparer
                )
                throws Exception
        {
                if(second == null)
                {
                        return new LinqStream<>(this.linqIterator);
                }

                if(this.equals(second))
                {
                        return new LinqStream<>(new ArrayList<T>());
                }

                return new LinqStream<>
                        (
                                new ExceptLinqIterator<>
                                        (
                                                this.linqIterator,
                                                second,
                                                null,
                                                equalityComparer
                                        )
                        );
        }

        public <K> LinqStream<T> EXCEPT
                (
                        Iterable<T> secondstream,
                        Func_T_R<T, K> keyselector
                )
                throws Exception
        {
                if(secondstream == null)
                {
                        return new LinqStream<>(this.linqIterator);
                }

                if(this.equals(secondstream))
                {
                        return new LinqStream<>(new ArrayList<T>());
                }

                return new LinqStream<>
                        (
                                new ExceptLinqIterator<>
                                        (
                                                this.linqIterator,
                                                secondstream,
                                                keyselector,
                                                null
                                        )
                        );
        }


        public <K> LinqStream<K> SELECTMANY(Func_T_R<T, Iterable<K>> predicate) throws Exception
        {
                if(predicate == null)
                {
                        throw new Exception("predicate不能为null。");
                }
                return new LinqStream<>
                        (
                                new SelectManyLinqIterator<>
                                        (
                                                this.linqIterator,
                                                predicate
                                        )
                        );
        }

        public LinqStream<T> INTERSECT(Iterable<T> secondstream) throws Exception
        {
                return new LinqStream<>
                        (
                                new IntersectLinqIterator<>
                                        (
                                                this.linqIterator,
                                                secondstream,
                                                null,
                                                null
                                        )
                        );
        }

        public LinqStream<T> INTERSECT
                (
                        Iterable<T> second,
                        EqualityComparer<T> equalityComparer
                )
                throws Exception
        {
                if(equalityComparer == null)
                {
                        throw new Exception("比较器 equalityComparer 不能为null。");
                }

                if(second == null)
                {
                        return LinqStream.fromIterable(new ArrayList<>());
                }

                if(this.equals(second))
                {
                        return new LinqStream<>(this);
                }

                return new LinqStream<>
                        (
                                new IntersectLinqIterator<>
                                        (
                                                this.linqIterator,
                                                second,
                                                null,
                                                equalityComparer
                                        )
                        );
        }

        public <K> LinqStream<T> INTERSECT
                (
                        Iterable<T> secondstream,
                        Func_T_R<T, K> keyselector
                )
                throws Exception
        {
                if(secondstream == null)
                {
                        return LinqStream.fromIterable(new ArrayList<>());
                }

                if(this.equals(secondstream))
                {
                        return new LinqStream<>(this);
                }

                return new LinqStream<>
                        (
                                new IntersectLinqIterator<>
                                        (
                                                this.linqIterator,
                                                secondstream,
                                                keyselector,
                                                null
                                        )
                        );
        }

        public LinqStream<T> DISTINCT() throws Exception
        {
                return new LinqStream<T>
                        (
                                new DistinctLinqIterator<>
                                        (
                                                this.linqIterator,
                                                null,
                                                null
                                        )
                        );
        }

        public <TKey> LinqStream<T> DISTINCT(Func_T_R<T, TKey> keyselector) throws Exception
        {
                return new LinqStream<T>
                        (
                                new DistinctLinqIterator<>
                                        (
                                                this.linqIterator,
                                                keyselector,
                                                null
                                        )
                        );
        }

        public LinqStream<T> DISTINCT(EqualityComparer<T> equalityComparer) throws Exception
        {
                return new LinqStream<T>
                        (
                                new DistinctLinqIterator<>
                                        (
                                                this.linqIterator,
                                                null,
                                                equalityComparer
                                        )
                        );
        }


        public <TKey> LinqStream<Grouping<TKey, T>> GROUPBY(Func_T_R<T, TKey> keySelector) throws Exception
        {
                return new LinqStream<>
                        (
                                new GroupedLinqIterator_3<>
                                        (
                                                this.linqIterator,
                                                keySelector,
                                                IdentityFunction.<T>Instance()
                                        )
                        );
        }

        public <TKey, TElement> LinqStream<Grouping<TKey, TElement>> GROUPBY
                (
                        Func_T_R<T, TKey> keySelector,
                        Func_T_R<T, TElement> elementSelector
                )
                throws Exception
        {
                return new LinqStream<>
                        (
                                new GroupedLinqIterator_3<>
                                        (
                                                this.linqIterator,
                                                keySelector,
                                                elementSelector
                                        )
                        );
        }

        public <TKey, TElement, TResult> LinqStream<TResult> GROUPBY
                (
                        Func_T_R<T, TKey> keySelector,
                        Func_T_R<T, TElement> elementSelector,
                        Func_T2_R<TKey, Iterable<TElement>, TResult> resultSelector
                )
                throws Exception
        {
                return new LinqStream<>
                        (
                                new GroupedLinqIterator_4<>
                                        (
                                                this.linqIterator,
                                                keySelector,
                                                elementSelector,
                                                resultSelector
                                        )
                        );
        }

        public <TKey, TResult> LinqStream<TResult> MERGERSIMILAR
                (
                        Func_T_R<T, TKey> similarKeySelector,
                        Func_T_R<Iterable<T>, TResult> mergerfunc
                )
                throws Exception
        {
                return new LinqStream<>
                        (
                                new MergerSimilarLinqIterator<T, TKey, TResult>
                                        (
                                                this.linqIterator,
                                                similarKeySelector,
                                                mergerfunc
                                        )
                        );
        }

        public <TKey> LinqStream<T> ORDERBY(Func_T_R<T, TKey> keySelector) throws Exception
        {
                return this.ORDERBY(keySelector, null);
        }

        public <TKey> LinqStream<T> ORDERBY
                (
                        Func_T_R<T, TKey> keySelector,
                        Comparator<TKey> comparator
                )
                throws Exception
        {
                return new LinqStream<>
                        (
                                new OrderedLinqIterator_2<>
                                        (
                                                this,
                                                keySelector,
                                                comparator,
                                                false
                                        )
                        );
        }

        public <TKey> LinqStream<T> THENBY(Func_T_R<T, TKey> keySelector) throws Exception
        {
                return this.THENBY(keySelector, null);
        }

        public <TKey> LinqStream<T> THENBY
                (
                        Func_T_R<T, TKey> keySelector,
                        Comparator<TKey> comparator
                )
                throws Exception
        {
                if(!(this.linqIterator instanceof OrderedLinqIterator_1<?>))
                {
                        throw new Exception("应首先调用orderBy或orderByDescending排序。");
                }

                return new LinqStream<>
                        (
                                ((OrderedLinqIterator_1<T>) this.linqIterator).createOrderedEnumerable
                                        (
                                                keySelector,
                                                comparator,
                                                false
                                        )
                        );
        }


        public <TKey> LinqStream<T> ORDERBYDESCENDING(Func_T_R<T, TKey> keySelector) throws Exception
        {
                return this.ORDERBYDESCENDING(keySelector, null);
        }

        public <TKey> LinqStream<T> ORDERBYDESCENDING
                (
                        Func_T_R<T, TKey> keySelector,
                        Comparator<TKey> comparator
                )
                throws Exception
        {
                return new LinqStream<>
                        (
                                new OrderedLinqIterator_2<>
                                        (
                                                this,
                                                keySelector,
                                                comparator,
                                                true
                                        )
                        );
        }

        public <TKey> LinqStream<T> THENBYDESCENDING(Func_T_R<T, TKey> keySelector) throws Exception
        {
                return this.THENBYDESCENDING(keySelector, null);
        }

        public <TKey> LinqStream<T> THENBYDESCENDING
                (
                        Func_T_R<T, TKey> keySelector,
                        Comparator<TKey> comparator
                )
                throws Exception
        {
                if(!(this.linqIterator instanceof OrderedLinqIterator_1<?>))
                {
                        throw new Exception("应首先调用orderBy或orderByDescending排序。");
                }

                return new LinqStream<>
                        (
                                ((OrderedLinqIterator_1<T>) this.linqIterator).createOrderedEnumerable
                                        (
                                                keySelector,
                                                comparator,
                                                true
                                        )
                        );
        }


        public LinqStream<T> CONCAT(Iterable<T> second) throws Exception
        {
                if(second == null)
                {
                        return new LinqStream<>(this.linqIterator);
                }

                return new LinqStream<>(new ConcatLinqIterator<>(this.linqIterator, second));
        }

        public T FIRSTORDEFAULT() throws Exception
        {
                return FIRSTORDEFAULT(null);
        }

        public T FIRSTORDEFAULT(Func_T_Boolean<T> filter) throws Exception
        {
                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        T result = null;
                        for(T item : iterator)
                        {
                                if(filter == null
                                        || filter.Invoke(item))
                                {
                                        result = item;
                                        break;
                                }
                        }
                        return result;
                }
        }

        public T LASTORDEFAULT() throws Exception
        {
                return this.LASTORDEFAULT(null);
        }

        public T LASTORDEFAULT(Func_T_Boolean<T> filter) throws Exception
        {
                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        T result = null;
                        for(T item : iterator)
                        {
                                if(filter == null
                                        || filter.Invoke(item))
                                {
                                        result = item;
                                }
                        }
                        return result;
                }
        }


        public LinqStream<T> TAKE(int count) throws Exception
        {
                return new LinqStream<T>
                        (
                                new TakeLinqIterator<>
                                        (
                                                this.linqIterator,
                                                null,
                                                count
                                        )
                        );
        }


        public LinqStream<T> TAKEWHILE(Func_T_Boolean<T> predicate) throws Exception
        {
                return new LinqStream<T>
                        (
                                new TakeLinqIterator<>
                                        (
                                                this.linqIterator,
                                                predicate,
                                                0
                                        )
                        );
        }


        public LinqStream<T> SKIP(int count) throws Exception
        {
                return new LinqStream<T>
                        (
                                new SkipLinqIterator<>
                                        (
                                                this.linqIterator,
                                                null,
                                                count
                                        )
                        );
        }


        public LinqStream<T> SKIPWHILE(Func_T_Boolean<T> predicate) throws Exception
        {
                return new LinqStream<T>
                        (
                                new SkipLinqIterator<>
                                        (
                                                this.linqIterator,
                                                predicate,
                                                0
                                        )
                        );
        }


        public <TInner, TKey, TResult> LinqStream<TResult> JOIN
                (
                        Iterable<TInner> inner,
                        Func_T_R<T, TKey> outerKeySelector,
                        Func_T_R<TInner, TKey> innerKeySelector,
                        Func_T2_R<T, TInner, TResult> resultSelector
                )
                throws Exception
        {
                return new LinqStream<TResult>
                        (
                                new JoinLinqIterator<>
                                        (
                                                this.linqIterator,
                                                inner,
                                                outerKeySelector,
                                                innerKeySelector,
                                                resultSelector,
                                                null
                                        )
                        );
        }

        public <TInner, TKey, TResult> LinqStream<TResult> JOIN
                (
                        Iterable<TInner> inner,
                        Func_T_R<T, TKey> outerKeySelector,
                        Func_T_R<TInner, TKey> innerKeySelector,
                        Func_T2_R<T, TInner, TResult> resultSelector,
                        EqualityComparer<TKey> keyequalityComparer
                )
                throws Exception
        {
                return new LinqStream<TResult>
                        (
                                new JoinLinqIterator<>
                                        (
                                                this.linqIterator,
                                                inner,
                                                outerKeySelector,
                                                innerKeySelector,
                                                resultSelector,
                                                keyequalityComparer
                                        )
                        );
        }

        public <TInner, TKey, TResult> LinqStream<TResult> GROUPJOIN
                (
                        Iterable<TInner> inner,
                        Func_T_R<T, TKey> outerKeySelector,
                        Func_T_R<TInner, TKey> innerKeySelector,
                        Func_T2_R<T, Iterable<TInner>, TResult> resultSelector
                )
                throws Exception
        {
                return new LinqStream<TResult>
                        (
                                new GroupJoinLinqIterator<>
                                        (
                                                this.linqIterator,
                                                inner,
                                                outerKeySelector,
                                                innerKeySelector,
                                                resultSelector,
                                                null
                                        )
                        );
        }

        public <TInner, TKey, TResult> LinqStream<TResult> GROUPJOIN
                (
                        Iterable<TInner> inner,
                        Func_T_R<T, TKey> outerKeySelector,
                        Func_T_R<TInner, TKey> innerKeySelector,
                        Func_T2_R<T, Iterable<TInner>, TResult> resultSelector,
                        EqualityComparer<TKey> keyequalityComparer
                )
                throws Exception
        {
                return new LinqStream<TResult>
                        (
                                new GroupJoinLinqIterator<>
                                        (
                                                this.linqIterator,
                                                inner,
                                                outerKeySelector,
                                                innerKeySelector,
                                                resultSelector,
                                                keyequalityComparer
                                        )
                        );
        }

        public LinqStream<Iterator<T>> SUBPARAGRAPH(int rangelength) throws Exception
        {
                if(rangelength < 1)
                {
                        throw new Exception("rangelength参数应大于等于1");
                }

                return new LinqStream<Iterator<T>>
                        (
                                new SubparagraphLinqIterator<T>
                                        (
                                                this.linqIterator,
                                                rangelength
                                        )
                        );
        }


        public boolean SEQUENCEEQUAL
                (
                        Iterable<T> second
                )
                throws Exception
        {
                return this.SEQUENCEEQUALCORE
                        (
                                second,
                                null,
                                null
                        );
        }

        public <TKey> boolean SEQUENCEEQUAL
                (
                        Iterable<T> second,
                        Func_T_R<T, TKey> keyselector
                )
                throws Exception
        {
                return this.SEQUENCEEQUALCORE
                        (
                                second,
                                keyselector,
                                null
                        );
        }

        public boolean SEQUENCEEQUAL
                (
                        Iterable<T> second,
                        EqualityComparer<T> comparer
                )
                throws Exception
        {
                return this.SEQUENCEEQUALCORE
                        (
                                second,
                                null,
                                comparer
                        );
        }

        private <TKey> boolean SEQUENCEEQUALCORE
                (
                        Iterable<T> second,
                        Func_T_R<T, TKey> keyselector,
                        EqualityComparer<T> comparer
                )
                throws Exception
        {
                if(second == null)
                {
                        throw new Exception("second 不能为null。");
                }

                if(keyselector == null && comparer == null)
                {
                        comparer = new EqualityComparer<T>()
                        {
                                @Override
                                public boolean equals(T x, T y)
                                {
                                        return DefaultEqualityComparer.instance.equals(x, y);
                                }

                                @Override
                                public int hashCode(T obj)
                                {
                                        return DefaultEqualityComparer.instance.hashCode(obj);
                                }
                        };
                }

                Iterator<T> firstenumerator = this.iterator();
                Iterator<T> secondenumerator = second.iterator();

                if(firstenumerator instanceof LinqIterator)
                {
                        firstenumerator = ((LinqIterator) firstenumerator).clone();
                }
                if(secondenumerator instanceof LinqIterator)
                {
                        secondenumerator = ((LinqIterator) secondenumerator).clone();
                }

                try
                {
                        while(firstenumerator.hasNext())
                        {
                                T firstcurrent = firstenumerator.next();

                                if(!secondenumerator.hasNext()
                                        || !(keyselector == null ? comparer.equals(firstcurrent, secondenumerator.next()) : DefaultEqualityComparer.instance.equals(keyselector.Invoke(firstcurrent), keyselector.Invoke(secondenumerator.next()))))
                                {
                                        return false;
                                }
                        }
                        if(secondenumerator.hasNext())
                        {
                                return false;
                        }
                        return true;
                }
                finally
                {
                        if(firstenumerator instanceof LinqIterator)
                        {
                                ((LinqIterator) firstenumerator).close();
                        }
                        if(secondenumerator instanceof LinqIterator)
                        {
                                ((LinqIterator) secondenumerator).close();
                        }
                }
        }


        public LinqStream<T> UNION
                (
                        Iterable<T> second
                )
                throws Exception
        {
                return new LinqStream<T>
                        (
                                new UnionLinqIterator<>
                                        (
                                                this.linqIterator,
                                                second,
                                                null,
                                                null
                                        )
                        );
        }

        public <TKey> LinqStream<T> UNION
                (
                        Iterable<T> second,
                        Func_T_R<T, TKey> keyselector
                )
                throws Exception
        {
                return new LinqStream<T>
                        (
                                new UnionLinqIterator<>
                                        (
                                                this.linqIterator,
                                                second,
                                                keyselector,
                                                null
                                        )
                        );
        }

        public LinqStream<T> UNION
                (
                        Iterable<T> second,
                        EqualityComparer<T> comparer
                )
                throws Exception
        {
                return new LinqStream<T>
                        (
                                new UnionLinqIterator<>
                                        (
                                                this.linqIterator,
                                                second,
                                                null,
                                                comparer
                                        )
                        );
        }

        public LinqStream<T> REVERSE() throws Exception
        {
                return new LinqStream<T>
                        (
                                new ReverseLinqIterator<>
                                        (
                                                this.linqIterator
                                        )
                        );
        }

        public <TSecond, TResult> LinqStream<TResult> ZIP
                (
                        Iterable<TSecond> second,
                        Func_T2_R<T, TSecond, TResult> resultSelector
                )
                throws Exception
        {
                return new LinqStream<TResult>
                        (
                                new ZipLinqIterator<>
                                        (
                                                this.linqIterator,
                                                second,
                                                resultSelector
                                        )
                        );
        }

        public <Tkey, Tvalue> HashMap<Tkey, ArrayList<Tvalue>> GROUPTOMAP
                (
                        Func_T_R<T, Tkey> keyselector,
                        Func_T_R<T, Tvalue> valueselector
                )
                throws Exception
        {
                HashMap<Tkey, ArrayList<Tvalue>> maplist = new HashMap<Tkey, ArrayList<Tvalue>>();

                if(valueselector == null)
                {
                        valueselector = p -> (Tvalue) p;
                }

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                Tkey key = keyselector.Invoke(item);
                                Tvalue value = valueselector.Invoke(item);

                                if(maplist.containsKey(key))
                                {
                                        maplist.get(key).add(value);
                                }
                                else
                                {
                                        ArrayList<Tvalue> list = new ArrayList<>();
                                        list.add(value);
                                        maplist.put(key, list);
                                }
                        }
                }

                return maplist;
        }


        public LinqStream<T> TOLISTSTREAM() throws Exception
        {
                return LinqStream.fromIterable(this.TOLIST());
        }

        public ArrayList<T> TOLIST()
        {
                ArrayList<T> list = new ArrayList<>();

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                list.add(item);
                        }
                }

                return list;
        }

        public Object[] TOARRAY()
        {
                ArrayList<T> list = new ArrayList<>();

                try(LinqIterator<T> iterator = this.linqIterator.clone())
                {
                        for(T item : iterator)
                        {
                                list.add(item);
                        }
                }

                return list.toArray();
        }
}
