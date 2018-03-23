package main.java.com.streamlinq.stream;

import java.util.Comparator;

/**
 * Created by ZhangYuan on 2018/1/20.
 */

public final class DefaultComparator implements Comparator<Object>
{
        public static final DefaultComparator instance = new DefaultComparator();

        private DefaultComparator()
        {

        }

        @Override
        public int compare(Object a, Object b)
        {
                if(a == b)
                {
                        return 0;
                }
                if(a == null)
                {
                        return -1;
                }
                if(b == null)
                {
                        return 1;
                }

                if(a instanceof Comparable<?>)
                {
                        return ((Comparable) a).compareTo(b);
                }

                if(b instanceof Comparable<?>)
                {
                        return -((Comparable) b).compareTo(a);
                }

                return 0;
        }
}
