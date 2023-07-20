package exceptions;

public class MyException extends Exception{

	private static final long serialVersionUID = -1069681133721849727L;
	
	public MyException() {
		super("Invalid configuration");
	}
	public MyException(String msg) {
		super(msg);
	}
}
