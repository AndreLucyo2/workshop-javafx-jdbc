/**
 * @author Andre
 * 
 * @date 12/07/2020 12:40:32
 */
package model.dao;

import java.util.List;

import model.entities.Department;

//feito na seçao 21/JDBC do curso da Udemy
public interface DepartmentDao
{

	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id);
	List<Department> findAll();

}
