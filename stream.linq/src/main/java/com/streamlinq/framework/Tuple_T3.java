package main.java.com.streamlinq.framework;

/**
 * Created by yuany on 2018/1/18.
 */
public class Tuple_T3<T1, T2, T3>
{
        public T1 Item1 = null;
        public T2 Item2 = null;
        public T3 Item3 = null;

        public Tuple_T3()
        {
        }

        public Tuple_T3(T1 t1, T2 t2, T3 t3)
        {
                this.Item1 = t1;
                this.Item2 = t2;
                this.Item3 = t3;
        }
}
