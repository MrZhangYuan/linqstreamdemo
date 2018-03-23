package main.java.com.streamlinq.stream;

/**
 * Created by ZhangYuan on 2018/1/20.
 */

public final class DefaultEqualityComparer implements EqualityComparer<Object>
{
        public final static EqualityComparer<Object> instance = new DefaultEqualityComparer();

        private DefaultEqualityComparer()
        {

        }

        @Override
        public boolean equals(Object a, Object b)
        {
                return a != null ? a.equals(b) : b == null;
        }

        @Override
        public int hashCode(Object obj)
        {
                return obj == null ? 0 : obj.hashCode();
        }
}
