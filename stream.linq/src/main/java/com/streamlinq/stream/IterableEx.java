package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * Created by yuany on 2017/12/20.
 */

public class IterableEx
{
        public static <T> T firstOrDefault(Iterable<T> iterable, Func_T_R<T, Boolean> predicate) throws Exception
        {
                if(iterable == null)
                {
                        throw new Exception("iterable不能为null。");
                }

                for(T item : iterable)
                {
                        if(predicate.Invoke(item))
                        {
                                return item;
                        }
                }

                return null;
        }

        public static <T> T firstOrDefault(T[] array, Func_T_R<T, Boolean> predicate) throws Exception
        {
                if(array == null)
                {
                        throw new Exception("iterable不能为null。");
                }

                T value = null;

                for(int i = 0; i < array.length; i++)
                {
                        if(predicate.Invoke(array[i]))
                        {
                                value = (T) array[i];
                                break;
                        }
                }

                return value;
        }
}
