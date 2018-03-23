import java.util.ArrayList;
import java.util.List;

import main.java.com.streamlinq.stream.LinqStream;

class SelectDemo
{
        public static void run() throws Exception
        {
                System.out.println("---------------------------------------SelectDemo Started!---------------------------------------");

                ArrayList<Teacher> testteacher = testData.getTeachers(100);
                ArrayList<Student> teststudent = testData.getStudents(300);


                LinqStream.fromIterable(testteacher)
                        .SELECT
                                (
                                        _p -> _p.Name
                                )
                        .FOREACH
                                (
                                        System.out::println
                                );


                LinqStream.fromIterable(testteacher)
                        .SELECTMANY
                                (
                                        _p -> _p.Students
                                )
                        .SELECT
                                (
                                        //boxed,age is int
                                        _p -> _p.Age
                                )
                        .FOREACH
                                (
                                        _p -> System.out.println(_p)
                                );


                LinqStream.fromIterable(testteacher)
                        .SELECTMANY
                                (
                                        _p -> LinqStream.fromIterable(_p.Students)
                                                .SELECTMANY
                                                        (
                                                                __p -> __p.Books
                                                        )
                                )
                        //books
                        .SELECT
                                (
                                        _p -> _p.BookName
                                )
                        //booknames
                        .FOREACH
                                (
                                        _p -> System.out.println(_p)
                                );

                System.out.println("---------------------------------------SelectDemo Finished!---------------------------------------");
        }
}
