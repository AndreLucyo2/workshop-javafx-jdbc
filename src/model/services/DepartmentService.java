package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

	private DepartmentDao dao = DaoFactory.createDepartmentDao();

	public List<Department> findAll()
	{
		// Vai no DAO e retorna os dados do banco: feito na seçao 21/JDBC do curso da Udemy
		return dao.findAll();
	}

	//Faz o serviço de validar se esta inserindo ou atualizando
	// se o objeto esta carregado, esta alterando, caso null, esta inserindo
	public void saveOrUpdate(Department obj)
	{
		if (obj.getId() == null)
		{
			dao.insert(obj);
		}
		else
		{
			dao.update(obj);
		}
	}
}
