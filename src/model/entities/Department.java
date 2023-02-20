package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Department implements Serializable {

    //Implementar serializable
    private static final long serialVersionUID = 1L;

    //Declarar as variáveis
    private Integer id;
    private String name;

    //Declarar os construtores
    public Department(){

    }

    public Department(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    //Declarar getters e setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Gerar hashCode e equals


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Gerar toString


    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
