package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employees")
public class EmployeeMvcController {

    private final EmployeeService employeeService;

    @Value("${employee.job.titles}")
    private List<String> jobTitles;

    @Value("#{${employee.employment.types}}")
    private Map<Integer, String> employmentTypes;

    @Autowired
    public EmployeeMvcController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("employmentTypes", employmentTypes); // pre preklad ID na text v tabuľke
        return "list-employees";
    }

    @GetMapping("/details")
    public String showDetails(@RequestParam("employeeId") Integer id, Model model) {
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        model.addAttribute("employmentTypeStr", employmentTypes.get(employee.getFullTime()));
        return "employee-details";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        model.addAttribute("jobTitles", jobTitles);
        model.addAttribute("employmentTypes", employmentTypes);
        return "employee-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") Integer id, Model model) {
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        model.addAttribute("jobTitles", jobTitles);
        model.addAttribute("employmentTypes", employmentTypes);
        return "employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("jobTitles", jobTitles);
            model.addAttribute("employmentTypes", employmentTypes);
            return "employee-form";
        }
        employeeService.save(employee);
        return "redirect:/employees/list";
    }

    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam("employeeId") Integer id) {
        employeeService.deleteById(id);
        return "redirect:/employees/list";
    }
}