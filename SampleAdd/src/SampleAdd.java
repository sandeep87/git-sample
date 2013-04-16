
public class SampleAdd {
	
	int x;
	int y;
	public static void main(String args[]) {
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		Addition a = new Addition(x, y);
		System.out.println(a);
	}

}
