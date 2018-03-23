import java.util.ArrayList;
import java.util.List;

import main.java.com.streamlinq.stream.LinqStream;

class SelectDemo
{
        final static String[] data = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};

        public static void run() throws Exception
        {
                System.out.println("---------------------------------------SelectDemo Started!---------------------------------------");

                LinqStream.fromArray(data)
                        .SELECT
                                (
                                        _p -> _p + _p
                                )
                        .FOREACH
                                (
                                        _p -> System.out.println(_p)
                                );

                System.out.println("---------------------------------------SelectDemo Finished!---------------------------------------");
        }
}
