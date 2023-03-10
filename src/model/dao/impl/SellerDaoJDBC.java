package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    //Dependencia com atributo
    private Connection conn;
    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    //Esqueleto para implementações
    //Implementações de fato
    public void insert (Seller obj){
        PreparedStatement st = null;

        try{
            if (conn.isClosed()){
                conn = DB.getConnection();
            }
            st = conn.prepareStatement(
                    "INSERT INTO seller " +
                            "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                            "VALUES " +
                            "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setString(3, String.valueOf(new Date(obj.getBirthDate().getTime())));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            //Para executar
            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }else {
                throw new DbException("Unexpected error! No rows affected");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);

        }
    }

    public void update (Seller obj){
        PreparedStatement st = null;

        try{
            if (conn.isClosed()){
                conn = DB.getConnection();
            }
            st = conn.prepareStatement(
                    "UPDATE seller " +
                            "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                            "WHERE Id = ?");
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setString(3, String.valueOf(new Date(obj.getBirthDate().getTime())));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            //Para executar
            st.executeUpdate();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);

        }

    }

    public void deleteById(Integer id){
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        }catch (Exception e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }

    }
    public Seller findById(Integer id){
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE seller.Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            //Transformação em objetos associados Seller + dados do Departamento
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
                Seller obj = instantiateSeller(rs, dep);
                return obj;
            }
            return null;
        }catch (Exception e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }

    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException{
        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setDepartment(dep);
        return obj;
    }

    //Find all para buscar todos os vendedores e organizar por nome
    public List<Seller> findAll(){
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            if (conn.isClosed()){
                conn = DB.getConnection();
            }
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY Name");

            //Para executar a query
            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();

            //Criar uma estrutura map vazia
            Map<Integer, Department> map = new HashMap<>();

            //Transformação em objetos associados Seller + dados do Departamento
            //Não pode ser um if, precisa ser um while para percorrer os objetos
            //While serve perfeitamente nessa ocasião também
            while(rs.next()){

                //Para testar se o departamento já existe
                Department dep = map.get(rs.getInt("DepartmentId"));

                //Não permitir confusão com Id do departament ou seller
                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        }catch (Exception e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }

    }
    //Encontrar por Id (findById)
    @Override
    public List<Seller> findByDepartment(Department department) {

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            if (conn.isClosed()){
                conn = DB.getConnection();
            }
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE DepartmentId = ? " +
                            "ORDER BY Name");
            st.setInt(1, department.getId());

            //Para executar a query
            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();

            //Criar uma estrutura map vazia
            Map<Integer, Department> map = new HashMap<>();

            //Transformação em objetos associados Seller + dados do Departamento
            //Não pode ser um if, precisa ser um while para percorrer os objetos
            while(rs.next()){

                //Para testar se o departamento já existe
                Department dep = map.get(rs.getInt("DepartmentId"));

                //Não permitir confusão com Id do departament ou seller
                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        }catch (Exception e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }

    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

}
