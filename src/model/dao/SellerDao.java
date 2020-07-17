package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

//feito na seçao 21/JDBC do curso da Udemy
public interface SellerDao
{

	void insert(Seller obj);
	void update(Seller obj);
	void deleteById(Integer id);
	
	//Busca pelo ID - retorna um registro
	Seller findById(Integer id);
	
	//busca todos
	List<Seller> findAll();
	
	//Lista departamentos do vendedor - Assinatura do metodos
	List<Seller> findByDepartment(Department department);
	
}
