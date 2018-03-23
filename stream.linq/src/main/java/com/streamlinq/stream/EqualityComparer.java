package main.java.com.streamlinq.stream;

/**
 * Created by ZhangYuan on 2018/1/20.
 */

public interface EqualityComparer<T>
{
        boolean equals(T x, T y);

        int hashCode(T obj);
}
