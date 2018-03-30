import java.util.ArrayList;
import java.util.List;

import main.java.com.streamlinq.stream.LinqStream;

class SelectWhereDemo
{
        static class _tempBook1
        {
                public String BookName1;
                public int Pages1;

                public _tempBook1(String bookName, int pages)
                {
                        BookName1 = bookName;
                        Pages1 = pages;
                }
        }

        static class _tempBook2
        {
                public String BookName2;
                public int Pages2;

                public _tempBook2(String bookName, int pages)
                {
                        BookName2 = bookName;
                        Pages2 = pages;
                }
        }

        public static void run() throws Exception
        {
                System.out.println("---------------------------------------SelectWhereDemo Started!---------------------------------------");

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
                        .WHERE
                                (
                                        _p -> _p < 25 && _p > 10
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
                                                                __p -> LinqStream.fromIterable(__p.Books)
                                                                        .WHERE
                                                                                (
                                                                                        ___p -> ___p.BookName.length() > 7
                                                                                )
                                                                        .SELECT
                                                                                (
                                                                                        ___p -> new _tempBook1
                                                                                                (
                                                                                                        ___p.BookName,
                                                                                                        ___p.Pages
                                                                                                )
                                                                                )
                                                        )
                                                .WHERE
                                                        (
                                                                __p -> __p.Pages1 > 100
                                                        )
                                                .SELECT
                                                        (
                                                                __p -> new _tempBook2
                                                                        (
                                                                                __p.BookName1,
                                                                                __p.Pages1
                                                                        )
                                                        )
                                )
                        //_tempBook2
                        .SELECT
                                (
                                        _p -> _p.BookName2
                                )
                        //_tempBook2 booknames
                        .FOREACH
                                (
                                        _p -> System.out.println(_p)
                                );

                System.out.println("---------------------------------------SelectWhereDemo Finished!---------------------------------------");
        }
}
