package com.example.simulateurmarche.services;


import com.example.simulateurmarche.Iservices.IRoleService;
import com.example.simulateurmarche.entities.Role;
import com.example.simulateurmarche.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class RoleService implements IRoleService {
    @Autowired
    RoleRepository roleRepo;
    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public Role updateRole(Role role) {

        return roleRepo.save(role);

    }
    public Role findID(Long id) {
        return roleRepo.findById(id).get();
    }
    @Override
    public void deleteRole(Long id) {
        roleRepo.deleteById(id);
    }

    @Override
    public List<Role> getRoles() {
        return (List<Role>) roleRepo.findAll();
    }
}
