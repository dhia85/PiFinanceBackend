package com.example.simulateurmarche.Iservices;

import com.example.simulateurmarche.entities.Role;

import java.util.List;

public interface IRoleService  {

    Role saveRole(Role role);
    Role updateRole(Role role);

    void deleteRole(Long id);

    List<Role> getRoles();
    public Role findID(Long id);
}
