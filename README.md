# Try

Try is an implementation of a monad for dealing with exceptions in Java language.

Try helps handling of checked exceptions in functional code.

## Example

Given the program:

	public class TryMain {
		public static void main(String[] args) {
			System.out.println(Try.of(e -> e, () -> "X".charAt(1)).getOrElse('Y'));
		}
	}

The output will be:

	Y
	