package main.java.com.streamlinq.stream;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 */

class GroupingItem<TKey, TElement> implements Grouping<TKey, TElement>
{
        private TKey key = null;

        private final ArrayList<TElement> elements = new ArrayList<>();

        GroupingItem(TKey key)
        {
                this.key = key;
        }

        public void add(TElement element)
        {
                this.elements.add(element);
        }

        @Override
        public TKey getKey()
        {
                return key;
        }

        @Override
        public Iterator<TElement> iterator()
        {
                return elements.iterator();
        }
}
