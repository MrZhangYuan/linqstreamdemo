import java.util.ArrayList;
import java.util.List;

import main.java.com.streamlinq.stream.LinqStream;

class SelectDemo
{
        public static void run() throws Exception
        {
                System.out.println("---------------------------------------SelectDemo Started!---------------------------------------");

                ArrayList<Teacher> testdata = testData.getTeachers(100);


                LinqStream.fromIterable(testdata)
                        .SELECT
                                (
                                        _p -> _p.Name
                                )
                        .FOREACH
                                (
                                        System.out::println
                                );


                LinqStream.fromIterable(testdata)
                        .SELECTMANY
                                (
                                        _p -> _p.Students
                                )
                        .SELECT
                                (
                                        //boxed,age is int
                                        _p->_p.Age
                                )
                        .FOREACH
                                (
                                        _p -> System.out.println(_p)
                                );

                System.out.println("---------------------------------------SelectDemo Finished!---------------------------------------");
        }
}
