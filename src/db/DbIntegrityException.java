package db;

//feito na se�ao 21/JDBC do curso da Udemy
public class DbIntegrityException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DbIntegrityException(String msg) {
		super(msg);
	}
}
