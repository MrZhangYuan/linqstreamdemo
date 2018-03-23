package main.java.com.streamlinq.framework;

/**
 * Created by yuany on 2018/1/18.
 */
public class Tuple_T2<T1, T2>
{
        public T1 Item1 = null;
        public T2 Item2 = null;

        public T1 getItem1()
        {
                return Item1;
        }

        public T2 getItem2()
        {
                return Item2;
        }

        public Tuple_T2(T1 t1, T2 t2)
        {
                this.Item1 = t1;
                this.Item2 = t2;
        }

        @Override
        public boolean equals(Object o)
        {
                if(o == null)
                {
                        return false;
                }

                if(!(o instanceof Tuple_T2<?, ?>))
                {
                        return false;
                }

                Tuple_T2<T1, T2> other = (Tuple_T2<T1, T2>) o;

                return this.Item1.equals(other.getItem1()) && this.Item2.equals(other.getItem2());
        }
}
