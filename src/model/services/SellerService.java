package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	//Injeção de dependencia do DaoFactory
	private SellerDao dao = DaoFactory.createSellerDao();

	public List<Seller> findAll()
	{
		// Vai no DAO e retorna os dados do banco: feito na seçao 21/JDBC do curso da Udemy
		return dao.findAll();
	}

	// Faz o serviço de validar se esta inserindo ou atualizando
	// se o objeto esta carregado, esta alterando, caso null, esta inserindo
	public void saveOrUpdate(Seller obj)
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

	//Faz o serviço de deletar do banco de dados
	public void remove(Seller obj)
	{
		dao.deleteById(obj.getId());
	}
}
