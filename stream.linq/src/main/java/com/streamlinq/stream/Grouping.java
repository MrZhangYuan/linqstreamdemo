package main.java.com.streamlinq.stream;

import java.util.Iterator;

/**
 * Created by yuany on 2018/1/12.
 */

public interface Grouping<TKey, TElement> extends Iterable<TElement>
{
        TKey getKey();
}
