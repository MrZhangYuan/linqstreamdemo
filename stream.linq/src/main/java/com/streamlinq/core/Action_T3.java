package main.java.com.streamlinq.core;

/**
 * Created by yuany on 2018/1/18.
 */
@FunctionalInterface
public interface Action_T3<T1, T2, T3>
{
        void Invoke(T1 var1, T2 val2, T3 val3) throws Exception;
}
