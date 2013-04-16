

public class Add {
		
	 
		/*static  int sum = 100;*/
	public static void main(String args[]) {
		System.out.println("Addition of two numbers!");
		  int a = Integer.parseInt(args[0]);
		  int b = Integer.parseInt(args[1]);
		  System.out.println(a);
		  System.out.println(b);
		  int sum =0;
		  try {
		 sum =a + b;
		  System.out.println("Sum: " + sum);
		  } catch (Exception e) {
			System.out.println(e);
		}
		
		  System.out.println(a);
		  System.out.println(b);
		
	}

}
