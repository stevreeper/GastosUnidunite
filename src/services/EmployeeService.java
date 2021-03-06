package services;

import dao.Employees;
import logic.Employee;

import java.util.List;

public class EmployeeService {
    private static Employees employees;

    public EmployeeService() throws Exception{
        employees = new Employees();
    }

    public List<Employee> getAllEmployee() throws Exception {
        return employees.getAllObjects();
    }

    public void addEmployee(Employee e) throws Exception{
        employees.addObject(e);
    }

    public void deleteEmployee (Employee e) throws Exception {
        employees.deleteObject(e);
    }

    public void updateEmployee (Employee e) throws Exception {
        employees.updateObject(e);
    }
}
