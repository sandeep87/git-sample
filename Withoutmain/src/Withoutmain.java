public class Withoutmain {

	private static String[] args;
	// private static final String[] args = null;

	static {
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		int z;
		z = x + y;
		System.out.println("Addition" + z);
	}

}