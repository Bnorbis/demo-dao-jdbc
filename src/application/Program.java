package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        //Declarar scanner para leitura do delete
        Scanner sc = new Scanner(System.in);

        //Para encontrar vendedores pelo seus respectivos ID's.
        System.out.println("=== TEST 1: seller findById ===");
        //Injeção de dependencia
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        //Para encontrar vendedores pelos seus respectivos ID's de departamento
        System.out.println("=== TEST 2: seller findByDepartmentId ===");
        sellerDao = DaoFactory.createSellerDao();
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);
        for(Seller obj: list){
            System.out.println(obj);
        }

        //Para encontrar todos os vendedores
        System.out.println("=== TEST 3: seller findAll ===");
        list = sellerDao.findAll();
        for(Seller obj: list){
            System.out.println(obj);
        }

        //INSERT implementação
        System.out.println("=== TEST 4: Seller insert ===");
        Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId());

        //UPDATE Implementação
        System.out.println("=== TEST 5: Seller update ===");
        seller = sellerDao.findById(1);
        seller.setName("Gabriel Marcelino");
        seller.setEmail("gabriel_marcelino@gmail.com");
        sellerDao.update(seller);
        System.out.println("Update completed");

        //DELETE Implementação
        System.out.println("=== TEST 6: Seller delete ===");
        System.out.println("Enter id for delete test:");
        int id = sc.nextInt();
        sellerDao.deleteById(id);
        System.out.println("Delete completed!");

        sc.close();
    }
}
