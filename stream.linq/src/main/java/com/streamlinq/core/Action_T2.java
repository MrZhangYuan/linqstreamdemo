package main.java.com.streamlinq.core;

/**
 * Created by yuany on 2018/1/18.
 */
@FunctionalInterface
public interface Action_T2<T, T2>
{
        void Invoke(T var1, T2 val2) throws Exception;
}
