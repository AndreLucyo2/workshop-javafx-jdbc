package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

//feito na seçao 21/JDBC do curso da Udemy
//Fabrica de DAO
//Diminui o acoplamento
//Tem operações staticas para instanciar os DAOs
public class DaoFactory {

	/***
	 * Fabria de DAO com o uso de JDBC
	 * @return
	 */
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	/***
	 * Fabria de DAO com o uso de JDBC
	 * @return
	 */
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
	
}
//ADAPTER E PADRAO FACTORY