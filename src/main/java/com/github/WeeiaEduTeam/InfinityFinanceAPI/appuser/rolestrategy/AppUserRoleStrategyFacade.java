package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.rolestrategy;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserAdminDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.ReplaceAppUserAllDetailsDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/*
 * Class adds roles based on JSON input
 * the highest role is taken and the rest
 * of the roles are added for example:
 * - if we have ROLE_ADMIN in name of the
 *  json request it adds also ROLE_USER
 * - if we have ROLE_USER it adds just
 *  ROLE_USER.
 *
 *  If you want to add new roles just add
 *  another object cast in factory
 *  and add create new strategy file.
 */
@Component
@Slf4j
public class AppUserRoleStrategyFacade {

    private final RoleService roleService;

    public AppUserRoleStrategyFacade(RoleService roleService) {
        this.roleService = roleService;
    }

    public <T> void addRolesForUser(AppUser user, T objectDTO) {
        createStrategy(objectDTO).addRolesForUser(user);
    }

    private <T> AppUserRoleStrategy createStrategy(T objectDTO) {
        if(isAdmin(objectDTO)) {
            return AppUserAddAdminRole.getInstance(roleService);
        } else if(isDefaultUser(objectDTO)) {
            return AppUserAddUserRole.getInstance(roleService);
        }

        throw new IllegalArgumentException("Error during create strategy occurred.");
    }

    private <T> boolean isDefaultUser(T objectDTO) {
        return isCreateAppUserUserDTO(objectDTO);
    }


    private <T> boolean isAdmin(T objectDTO) {

        /* Also catches ReplaceAppUserAllDetailsDTO due to inheritance. */
        if(isReplaceAppUserAllDetailsDTO(objectDTO)) {
            return ((ReplaceAppUserAllDetailsDTO) objectDTO).hasRole(RoleType.ROLE_ADMIN.getName());
        } else if(isCreateAppUserAdminDTO(objectDTO)) {
            return ((CreateAppUserAdminDTO) objectDTO).hasRole(RoleType.ROLE_ADMIN.getName());
        }

        return false;
    }

    private <T> boolean isReplaceAppUserAllDetailsDTO(T objectDTO) {
        return objectDTO instanceof ReplaceAppUserAllDetailsDTO;
    }

    private <T> boolean isCreateAppUserUserDTO(T objectDTO) {
        return objectDTO instanceof CreateAppUserUserDTO;
    }

    private <T> boolean isCreateAppUserAdminDTO(T objectDTO) {
        return objectDTO instanceof CreateAppUserAdminDTO;
    }

    public void removeRoles(AppUser user) {
        AppUserRoleRemover.getInstance(roleService).deleteRolesForUser(user);
    }
}
