# Try

Try is an implementation of a monad for dealing with exceptions in Java language.

Try helps to handle checked or unchecked exceptions in functional code.

## Example

Given the program:

	public class TryMain {
		public static void main(String[] args) {
			// "X".charAt(1) will throw an unchecked exception !
			System.out.println(Try.of(() -> "X".charAt(1)).getOrElse('Y'));
	
			// new FileInputStream("") will throw a checked exception !
			System.out.println(Try.of(() -> new FileInputStream("")).getOrElse(null));
		}
	}

The output will be:

	Y
	null
	