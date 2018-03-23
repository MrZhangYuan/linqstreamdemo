package main.java.com.streamlinq.core;

/**
 * Created by yuany on 2018/1/18.
 */
@FunctionalInterface
public interface Action_T<T>
{
        void Invoke(T val1) throws Exception;
}
