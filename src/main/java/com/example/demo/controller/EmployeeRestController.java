package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.response.ResponseFactory;
import com.example.demo.response.ResponseWrapper;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private final EmployeeService employeeService;
    private final ResponseFactory response;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService, ResponseFactory response) {
        this.employeeService = employeeService;this.response = response;
    }

    @GetMapping("/employees")
    public ResponseEntity<ResponseWrapper<List<Employee>>> findAll() {
        List<Employee> employees = employeeService.findAll();
        return response.ok("Zoznam zamestnancov úspešne načítaný", employees);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<ResponseWrapper<Employee>> getEmployee(@PathVariable Integer id) {
        Employee employee = employeeService.findById(id);

        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zamestnanec s ID " + id + " nebol nájdený");
        }
        return response.ok("Zamestnanec nájdený", employee);
    }

    @PostMapping("/employees")
    public ResponseEntity<ResponseWrapper<Employee>> addEmployee(@Valid @RequestBody Employee employee) {
        employee.setId(null);
        Employee savedEmployee = employeeService.save(employee);
        return response.created("Zamestnanec úspešne pridaný", savedEmployee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<ResponseWrapper<Employee>> updateEmployee(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
        Employee existingEmployee = employeeService.findById(id);
        if (existingEmployee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zamestnanec s ID " + id + " nebol nájdený");
        }

        Employee updatedEmployee = employeeService.update(id, employee);
        return response.ok("Zamestnanec úspešne aktualizovaný", updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteEmployee(@PathVariable Integer id) {
        Employee tempEmployee = employeeService.findById(id);

        if (tempEmployee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zamestnanec s ID " + id + " nebol nájdený");
        }

        employeeService.deleteById(id);
        return response.ok("Zamestnanec úspešne vymazaný");
    }
}