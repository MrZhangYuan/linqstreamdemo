package main.java.com.streamlinq.framework;

/**
 * Created by yuany on 2018/1/18.
 */
public class Tuple_T4<T1, T2, T3, T4>
{
        private T1 item1 = null;
        private T2 item2 = null;
        private T3 item3 = null;
        private T4 item4 = null;

        public T1 getItem1()
        {
                return item1;
        }

        public T2 getItem2()
        {
                return item2;
        }

        public T3 getItem3()
        {
                return item3;
        }

        public T4 getItem4()
        {
                return item4;
        }

        public Tuple_T4(T1 t1, T2 t2, T3 t3, T4 t4)
        {
                this.item1 = t1;
                this.item2 = t2;
                this.item3 = t3;
                this.item4 = t4;
        }
}
