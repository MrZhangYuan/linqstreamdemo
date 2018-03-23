package main.java.com.streamlinq.core;

/**
 * Created by yuany on 2018/1/18.
 */
@FunctionalInterface
public interface Action_T4<T1, T2, T3, T4>
{
        void Invoke(T1 var1, T2 val2, T3 val3, T4 val4) throws Exception;
}