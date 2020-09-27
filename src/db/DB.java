package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

// feito na seçao 21/JDBC do curso da Udemy
public class DB {

	private static Connection conn = null;

	//Metdo que carregar o arquivo db.properties em um objeto Properties
	//com esta na raiz do projeto, é so passar o arquivo 
	private static Properties loadProperties()
	{
		//Tenta fazer a leitura de todo o arquivo:
		try (FileInputStream fs = new FileInputStream("db.properties"))
		{
			//instancia o objeto Properties
			Properties props = new Properties();
			//carrega os dados para o objeto
			props.load(fs);
			
			return props;
		}
		catch (IOException e)
		{
			throw new DbException(e.getMessage());
		}
	}
	
	//Monta a connection, retorna uma conection do jdbc SQL
	public static Connection getConnection()
	{
		//Se tiver null, tenta carregar as propriedades da conexão
		if (conn == null)
		{
			try
			{
				//Pega as propriedades do objeto
				Properties props = loadProperties();
				//pega o campo especifico: atenção ao nome do campo
				String url = props.getProperty("dburl");
				//aqui é instanciada a conexão - ja abre a conection
				conn = DriverManager.getConnection(url, props);
			}
			catch (SQLException e)
			{
				throw new DbException(e.getMessage());
			}
		}
		
		//se a conexao ja estiver definida, so retorna ela
		return conn;
	}

	//Fecha a conexão caso tiver instanciada
	public static void closeConnection()
	{
		//testa se a conexão esta instanciada = aberta
		if (conn != null)
		{
			try
			{
				//Tenta fechar
				conn.close();
			}
			catch (SQLException e)
			{
				//Evia de ter que esta tratando com try
				throw new DbException(e.getMessage());
			}
		}
	}



	public static void closeStatement(Statement st)
	{
		if (st != null)
		{
			try
			{
				st.close();
			}
			catch (SQLException e)
			{
				throw new DbException(e.getMessage());
			}
		}
	}

	public static void closeResultSet(ResultSet rs)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (SQLException e)
			{
				throw new DbException(e.getMessage());
			}
		}
	}
}
