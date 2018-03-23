package main.java.com.streamlinq.stream;

import main.java.com.streamlinq.core.*;
import main.java.com.streamlinq.core.Func_T_Boolean;

/**
 * Created by yuany on 2018/1/12.
 */

class LinqIteratorHelper
{
        public static <TSource> Func_T_Boolean<TSource> combinePredicates
                (
                        Func_T_Boolean<TSource> predicate1,
                        Func_T_Boolean<TSource> predicate2
                )
        {
                return x ->
                {
                        if(predicate1.Invoke(x))
                        {
                                return predicate2.Invoke(x);
                        }
                        return false;
                };
        }

        public static <TSource, TMiddle, TResult> Func_T_R<TSource, TResult> combineSelectors
                (
                        Func_T_R<TSource, TMiddle> selector1,
                        Func_T_R<TMiddle, TResult> selector2
                )
        {
                return x -> selector2.Invoke(selector1.Invoke(x));
        }

}
