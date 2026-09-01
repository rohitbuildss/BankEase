package exception;
import java.lang.Exception;

public class InvalidAmountException extends Exception{

	public InvalidAmountException(String message){
		super(message);
	}
	
}
