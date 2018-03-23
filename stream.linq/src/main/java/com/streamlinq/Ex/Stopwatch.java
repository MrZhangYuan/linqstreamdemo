package main.java.com.streamlinq.Ex;

/**
 * Created by ZhangYuan on 2017/12/23.
 */

/**
 * 简单的计时封装
 */
public class Stopwatch
{
        public static Stopwatch startNew()
        {
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start();
                return stopwatch;
        }

        private long begintime = 0;
        private long endtime = 0;

        public void start()
        {
                begintime = System.nanoTime();
                endtime = 0;
        }


        public void stop()
        {
                //运算代码
                endtime = System.nanoTime();
        }

        public void reStart()
        {
                this.start();
        }

        public TimeSpan getTimeSpan()
        {
                return TimeSpan.fromNanoSeconds(this.getNanoTime());
        }

        public long getNanoTime()
        {
                return endtime - begintime;
        }

        public long getMillisTime()
        {
                return this.getMicroSecondsTime() / 1000;
        }

        public long getSecondsTime()
        {
                return this.getMillisTime() / 1000;
        }

        public long getMicroSecondsTime()
        {
                return this.getNanoTime() / 1000;
        }
}
