package com.nt.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nt.model.Employee;
import com.nt.model.Role;
import com.nt.repo.EmployeeRepository;
import com.nt.repo.RoleRepository;

@Service
public class EmployeeService implements UserDetailsService{
    @Autowired
    private EmployeeRepository empRepo;
    
    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder pe;
    
    //this for save employe from exteranal
    public String addEmployee1(Employee emp) {
    	emp.setPassword(pe.encode(emp.getPassword()));
    	String s= empRepo.save(emp).getName();
    	return s + " is save succefully";
    }
    
    //Adding new employee
    public String addEmployee(Employee emp) {
    	
    	
    	//chechiki weather employee register or not
    	if(empRepo.findByName(emp.getName()).isPresent()) {
    		return emp.getName()+":: is already present ";
    	}
        // Fetch roles from the database
        Set<Role> managedRoles = emp.getRole().stream()
                .map(role -> roleRepo.findById(role.getId())
                        .orElse(roleRepo.save(role)))
                .collect(Collectors.toSet());
        

        // Set the managed roles back to the employee
        emp.setRole(managedRoles);
        //emp.setPassword(pe.encode(emp.getPassword()));
        // Persist the employee
        empRepo.save(emp);
        return emp.getId()+ "Employee added successfully!";
    }
    
    //inject UserDetailsService interface and AuthenticationManager bean. 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("UserName verification");
		Employee emp= empRepo.findByName(username).orElse(null);
		System.out.println("employee details are    "+ emp);
		Set<GrantedAuthority> authorities=emp.getRole().stream().map((role)-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
		System.out.println("UserName verification  "+authorities);
		return new org.springframework.security.core.userdetails.User(
                username,
                emp.getPassword(),
                authorities
        );
	}
	
	}


