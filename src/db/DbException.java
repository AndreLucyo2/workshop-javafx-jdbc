package db;

//feito na se�ao 21/JDBC do curso da Udemy
public class DbException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DbException(String msg) {
		super(msg);
	}
}
