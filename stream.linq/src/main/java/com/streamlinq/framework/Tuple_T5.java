package main.java.com.streamlinq.framework;

/**
 * Created by yuany on 2018/3/6.
 */

public class Tuple_T5<T1, T2, T3, T4, T5>
{
        public T1 item1 = null;
        public T2 item2 = null;
        public T3 item3 = null;
        public T4 item4 = null;
        public T5 item5 = null;

        public Tuple_T5()
        {
        }

        public Tuple_T5(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5)
        {
                this.item1 = t1;
                this.item2 = t2;
                this.item3 = t3;
                this.item4 = t4;
                this.item5 = t5;
        }
}
