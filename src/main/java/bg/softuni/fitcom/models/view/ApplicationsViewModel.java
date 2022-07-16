package bg.softuni.fitcom.models.view;

import bg.softuni.fitcom.models.enums.RoleEnum;

import java.util.List;

public class ApplicationsViewModel {
    private long userId;
    private String user;
    private List<RoleEnum> roles;

    public String getUser() {
        return user;
    }

    public ApplicationsViewModel setUser(String user) {
        this.user = user;
        return this;
    }

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public ApplicationsViewModel setRoles(List<RoleEnum> roles) {
        this.roles = roles;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public ApplicationsViewModel setUserId(long userId) {
        this.userId = userId;
        return this;
    }
}
