import java.util.ArrayList;

class testData
{
        public static ArrayList<Student> getStudents(int num)
        {
                ArrayList<Student> students = new ArrayList<>(num);

                for(int i = 0; i < num; i++)
                {
                        students.add(new Student("Student_" + String.valueOf(i), i % 80, i % 3 == 0 ? Sex.Boy : Sex.Girl));
                }

                return students;
        }

        public static ArrayList<Teacher> getTeachers(int num)
        {
                ArrayList<Teacher> teachers = new ArrayList<>(num);

                for(int i = 0; i < num; i++)
                {
                        teachers.add(new Teacher("Teacher_" + String.valueOf(i), getStudents(i % 300)));
                }

                return teachers;
        }
}

enum Sex
{
        Boy,
        Girl
}

class Student
{
        public String Name;
        public int Age;
        public Sex Sex;

        public Student(String name, int age, Sex sex)
        {
                Name = name;
                Age = age;
                Sex = sex;
        }
}

class Teacher
{
        public String Name;

        public ArrayList<Student> Students;

        public Teacher(String name, ArrayList<Student> students)
        {
                Name = name;
                Students = students;
        }
}