package me.specter.springbootdemo.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.specter.springbootdemo.role.AppRole.RoleName;

import java.util.Optional;
import java.util.List;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole,String>{
    Optional<AppRole> findByRoleName(RoleName roleName);

    List<AppRole> findAllByRoleNameIn(List<RoleName> roleName);
}
