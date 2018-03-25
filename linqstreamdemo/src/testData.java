import java.util.ArrayList;

class testData
{
        public static ArrayList<Book> getBooks(int num)
        {
                ArrayList<Book> students = new ArrayList<>(num);

                for(int i = 0; i < num; i++)
                {
                        students.add
                                (
                                        new Book
                                                (
                                                        "Book_" + String.valueOf(i),
                                                        i % 80
                                                )
                                );
                }

                return students;
        }

        public static ArrayList<Student> getStudents(int num)
        {
                ArrayList<Student> students = new ArrayList<>(num);

                for(int i = 0; i < num; i++)
                {
                        students.add
                                (
                                        new Student
                                                (
                                                        "Student_" + String.valueOf(i),
                                                        i % 80, i % 3 == 0 ? Sex.Boy : Sex.Girl,
                                                        getBooks(i%30)
                                                )
                                );
                }

                return students;
        }

        public static ArrayList<Teacher> getTeachers(int num)
        {
                ArrayList<Teacher> teachers = new ArrayList<>(num);

                for(int i = 0; i < num; i++)
                {
                        teachers.add
                                (
                                        new Teacher
                                                (
                                                        "Teacher_" + String.valueOf(i),
                                                        getStudents(i % 300)
                                                )
                                );
                }

                return teachers;
        }
}

enum Sex
{
        Boy,
        Girl
}

class  Book
{
        public String BookName;
        public int Pages;

        public Book(String bookName, int pages)
        {
                BookName = bookName;
                Pages = pages;
        }
}

class Student
{
        public String Name;
        public int Age;
        public Sex Sex;
        public ArrayList<Book> Books;

        public Student()
        {
        }

        public Student(String name, int age, Sex sex, ArrayList<Book> books)
        {
                Name = name;
                Age = age;
                Sex = sex;
                Books = books;
        }
}

class Teacher
{
        public String Name;

        public ArrayList<Student> Students;

        public Teacher()
        {
        }

        public Teacher(String name, ArrayList<Student> students)
        {
                Name = name;
                Students = students;
        }
}