package main.java.com.streamlinq.core;

/**
 * Created by yuany on 2018/1/18.
 */
@FunctionalInterface
public interface Func_T_Long<T>
{
        long Invoke(T var1) throws Exception;
}
