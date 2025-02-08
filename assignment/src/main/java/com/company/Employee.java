package com.company;

public class Employee {

    public Employee(int id, String firstName, String lastName, int salary, int managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }
    
    private int id;
    private String firstName;
    private String lastName;
    private int salary;
    private int managerId;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSalary() {
        return salary;
    }
    
    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getManagerId() {
        return managerId;
    }
    
    public void setManagerId(int id) {
        this.managerId = managerId;
    }

}
    