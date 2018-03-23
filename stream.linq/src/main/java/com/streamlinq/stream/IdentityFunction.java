package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;

/**
 * Created by ZhangYuan on 2018/1/13.
 */

public class IdentityFunction
{
        public static <TElement> Func_T_R<TElement, TElement> Instance()
        {
                return x -> x;
        }
}
