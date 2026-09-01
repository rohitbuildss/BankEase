package exception;
import java.lang.Exception;

public class AccountNotFoundException extends Exception {
 
	public AccountNotFoundException(String message) {
		super(message);
	}
}
