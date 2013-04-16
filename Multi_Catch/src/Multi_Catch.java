
public class Multi_Catch {
	public static void main(String[] args) {
		try {
			int len = args.length;
			System.out.println("lenght :"+len);
			if (len == 0) {
				int i = 1 / 1;
			}
			if (len == 1) {
				int j[] = new int[3];
				j[10] = 4343;
			}
		} catch (ArithmeticException e) {
			System.out.println("you have not " +
					"entered any argument");
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
