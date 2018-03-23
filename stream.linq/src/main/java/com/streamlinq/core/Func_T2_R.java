package main.java.com.streamlinq.core;

/**
 * Created by yuany on 2018/1/18.
 */
@FunctionalInterface
public interface Func_T2_R<T1, T2, R>
{
        R Invoke(T1 var1, T2 val2) throws Exception;
}
