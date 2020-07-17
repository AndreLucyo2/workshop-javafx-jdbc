package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll()
	{
		//Vai no DAO e retorna os dados do banco: feito na seçao 21/JDBC do curso da Udemy
		return dao.findAll();
	}
}
