package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.Func_T_R;

/*
 *
 * Created by yuany on 2018/1/24.
 *
 */


/*


interface ILookup<TKey, TElement> extends Iterable<Grouping<TKey, TElement>>
{
        int COUNT();

        Iterable<TElement> get(TKey key);

        boolean CONTAINS(TKey key);
}

public class Lookup<TKey, TElement> implements Iterable<Grouping<TKey, TElement>>, ILookup<TKey, TElement>
{
        private EqualityComparer<TKey> comparer;
        private GroupingItem[] groupings;
        private GroupingItem lastGrouping;
        private int COUNT;

        private Lookup(EqualityComparer<TKey> comparer)
        {
                //                if (comparer == null)
                //                        comparer = (IEqualityComparer<TKey>) EqualityComparer<TKey>.Default;
                this.comparer = comparer;
                this.groupings = new GroupingItem[7];
        }

        static <TSource, TKey, TElement> Lookup<TKey, TElement> Create
                (
                        Iterable<TSource> source,
                        Func_T_R<TSource, TKey> keySelector,
                        Func_T_R<TSource, TElement> elementSelector,
                        EqualityComparer<TKey> comparer
                )
                throws Exception
        {
                Lookup<TKey, TElement> lookup = new Lookup<TKey, TElement>(comparer);
                for(TSource source1 : source)
                {
                        lookup.GetGrouping(keySelector.Invoke(source1), true).add(elementSelector.Invoke(source1));
                }
                return lookup;
        }

        GroupingItem GetGrouping(TKey key, boolean create)
{
        int hashCode = this.InternalGetHashCode(key);
        for (GroupingItem grouping = this.groupings[hashCode % this.groupings.length]; grouping != null; grouping = grouping.hashNext)
        {
                if (grouping.hashCode == hashCode && this.comparer.Equals(grouping.key, key))
                        return grouping;
        }
        if (!create)
                return null;

        if (this.COUNT == this.groupings.length)
                this.Resize();

        int index = hashCode % this.groupings.length;

        Lookup<TKey, TElement>.Grouping grouping1 = new Lookup<TKey, TElement>.Grouping();
        grouping1.key = key;
        grouping1.hashCode = hashCode;
        grouping1.elements = new TElement[1];
        grouping1.hashNext = this.groupings[index];
        this.groupings[index] = grouping1;
        if (this.lastGrouping == null)
        {
                grouping1.next = grouping1;
        }
        else
        {
                grouping1.next = this.lastGrouping.next;
                this.lastGrouping.next = grouping1;
        }
        this.lastGrouping = grouping1;
        this.COUNT = this.COUNT + 1;
        return grouping1;
}

        private void Resize()
        {
                int length =this.COUNT * 2 + 1;
                GroupingItem[] groupingArray = new GroupingItem[length];
                GroupingItem grouping = this.lastGrouping;
                do
                {
                        grouping = grouping.next;
                        int index = grouping.hashCode % length;
                        grouping.hashNext = groupingArray[index];
                        groupingArray[index] = grouping;
                }
                while (grouping != this.lastGrouping);
                this.groupings = groupingArray;
        }

         int InternalGetHashCode(TKey key)
{
        if (key != null)
                return this.comparer.hashCode(key) & Integer.MaxValue;
        return 0;
}

        @Override
        public int COUNT()
        {
                return 0;
        }

        @Override
        public Iterable<TElement> get(TKey tKey)
        {
                return null;
        }

        @Override
        public boolean CONTAINS(TKey tKey)
        {
                return false;
        }

        @Override
        public Iterator<Grouping<TKey, TElement>> iterator()
        {
                return null;
        }
}
 */