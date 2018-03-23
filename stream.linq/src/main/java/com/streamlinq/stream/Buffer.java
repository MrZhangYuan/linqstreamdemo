package main.java.com.streamlinq.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by yuany on 2018/1/12.
 */

class Buffer<TElement>
{
        Object[] items;
        int count;

        Buffer(Iterable<TElement> source)
        {
                Object[] array = (TElement[]) null;
                int length = 0;

                if(source instanceof Collection<?>)
                {
                        Collection<TElement> elements = (Collection<TElement>) source;
                        length = elements.size();

                        if(length > 0)
                        {
                                array = elements.toArray();
                        }
                }
                else
                {
                        for(TElement element : source)
                        {
                                if(array == null)
                                {
                                        array = new Object[4];
                                }
                                else if(array.length == length)
                                {
                                        Object[] newarray = new Object[length * 2];
                                        System.arraycopy(array, 0, newarray, 0, length);
                                        array = newarray;
                                }

                                array[length] = element;
                                ++length;
                        }
                }

                this.items = array;
                this.count = length;
        }

        Object[] ToArray()
        {
                if(this.count == 0)
                {
                        return new Object[0];
                }
                if(this.items.length == this.count)
                {
                        return this.items;
                }
                Object[] elementArray = new Object[this.count];
                System.arraycopy(this.items, 0, elementArray, 0, this.count);
                return elementArray;
        }
}
