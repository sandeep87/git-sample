
public class Takevalues {
	public static void main(String args[]) {
	
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		Sumvalues s = new Sumvalues(x, y);
		System.out.println(s);
		
	}
}
class Student
{
String name;
int rollno;
public static void main(String[] args)
{
Student s1 = new Student();
Student s2 = new Student();
Student s3 = new Student();
Student s4 = new Student();
s1.name = "Malli";s1.rollno = 101;
s2.name = "Sankar";s2.rollno = 102;
s3.name = "Kiran";s3.rollno = 103;
s4.name = "Sai";s4.rollno = 104;
System.out.println(s1.name+"---"+s1.rollno);
System.out.println(s2.name+"---"+s2.rollno);
System.out.println(s3.name+"---"+s3.rollno);
System.out.println(s4.name+"---"+s4.rollno);
}
}
/*To over come this type of burden constructor was introduced.
Ex:-*/
class Student
{
String name;
int rollno;
Student(String name, int rollno)
{
this.name = name;
this.rollno = rollno;
}
public static void main(String[] args)
{
Student s1 = new Student("raju",101);
Student s2 = new Student("mani",102);
System.out.println(s1.name+"---"+s1.rollno);
System.out.println(s2.name+"---"+s2.rollno);
}
}