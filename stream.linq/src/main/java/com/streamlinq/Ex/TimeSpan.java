package main.java.com.streamlinq.Ex;

/**
 * Created by ZhangYuan on 2017/12/23.
 */

public class TimeSpan
{
        private long yyyy = 0;
        private long MM = 0;
        private long dd = 0;
        private long HH = 0;
        private long mm = 0;
        private long ss = 0;
        private long ms = 0;
        private long us = 0;
        private long ns = 0;

        private TimeSpan(long naseconds)
        {

        }


        public static TimeSpan fromSeconds(long seconds)
        {
                return new TimeSpan(seconds * 1000000);
        }

        public static TimeSpan fromNanoSeconds(long naseconds)
        {
                return new TimeSpan(naseconds);
        }

        @Override
        public String toString()
        {
                String template = "yyyy-MM-dd HH:mm:ss ms us ns";

                return template.replace("yyyy", StringUlitis.padLeft(yyyy, '0', 4))
                        .replace("MM", StringUlitis.padLeft(MM, '0', 2))
                        .replace("dd", StringUlitis.padLeft(dd, '0', 2))
                        .replace("HH", StringUlitis.padLeft(HH, '0', 2))
                        .replace("mm", StringUlitis.padLeft(mm, '0', 2))
                        .replace("ss", StringUlitis.padLeft(ss, '0', 2))
                        .replace("ms", StringUlitis.padLeft(ms, '0', 4))
                        .replace("us", StringUlitis.padLeft(us, '0', 4))
                        .replace("ns", StringUlitis.padLeft(ns, '0', 4));
        }
}
