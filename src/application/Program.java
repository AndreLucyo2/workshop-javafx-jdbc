package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

//feito na seçao 21/JDBC do curso da Udemy
public class Program {

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		SellerDao sellerDao = DaoFactory.createSellerDao();

		System.out.println("=== TEST 1: seller metodo findById ========================================================");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);

		System.out.println("\n=== TEST 2: seller findByDepartment - lista vendedores por departamento =================");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		// Para cada seller obj na minha lista list imprime na tela o obj
		for (Seller obj : list)
		{
			System.out.println(obj);
		}

		System.out.println("\n=== TEST 3: seller findAll - Lista todos os VENDEDORES sem restrição ====================");
		list = sellerDao.findAll();
		// Para cada seller obj na minha lista list imprime na tela o obj
		for (Seller obj : list)
		{
			System.out.println(obj);
		}

		System.out.println("\n=== TEST 4: seller insert ===============================================================");
		// Cria um objeto vendedor de teste:
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		// Faz o insert:
		sellerDao.insert(newSeller);
		// Retorna o que foi inserido:
		System.out.println("Inserted! New id = " + newSeller.getId());

		System.out.println("\n=== TEST 5: seller update Alterando o nome.. ============================================");
		// Carrega um vendedor por exemplo:
		seller = sellerDao.findById(1);
		// altera o nome
		seller.setName("Martha Waine");
		sellerDao.update(seller);
		System.out.println("Update completed");

		System.out.println("\n=== TEST 6: seller delete ===============================================================");
		//Pede para informar o ID:
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		//Tnta deletar o id
		sellerDao.deleteById(id);
		System.out.println("Delete completed");

		sc.close();

	}
}
