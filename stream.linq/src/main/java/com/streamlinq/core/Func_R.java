package main.java.com.streamlinq.core;

/**
 * Created by yuany on 2018/1/18.
 */
@FunctionalInterface
public interface Func_R<R>
{
        R Invoke() throws Exception;
}
