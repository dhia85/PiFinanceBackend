package com.example.simulateurmarche.controllers;

import com.example.simulateurmarche.entities.Role;
import com.example.simulateurmarche.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping("/getroles")
    public List<Role> afficher()
    {
        return roleService.getRoles();
    }

    @PostMapping("/save")
    public Role add(@RequestBody Role role)
    {
        return roleService.saveRole(role);
    }

    @PutMapping("/update")
    public Role modifier(@RequestBody Role role)
    {
        return  roleService.updateRole(role);
    }
    @DeleteMapping("/delete/{id}")
    public void supprimer(@PathVariable(value = "id") Long id)
    {
        roleService.deleteRole(id);
    }
}
