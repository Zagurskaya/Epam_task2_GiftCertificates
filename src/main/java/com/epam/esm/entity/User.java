package com.epam.esm.entity;

/**
 * User with characteristics <b>id</b>, <b>login</b>, <b>password</b>, <b>fullName</b> и <b>role</b>.
 */
public class User {
    /**
     * Identifier
     */
    private Long id;
    /**
     * Login
     */
    private String login;

    /**
     * Full name
     */
    private String fullName;
    /**
     * Role
     */
    private RoleType role;

    /**
     * Get field value {@link User#id}
     *
     * @return identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Get field value {@link User#login}
     *
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Get field value {@link User#fullName}
     *
     * @return full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Get field value {@link User#role}
     *
     * @return role
     */
    public RoleType getRole() {
        return role;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{")
                .append("id=")
                .append(id)
                .append(", login=")
                .append(login)
                .append(", fullName=")
                .append(fullName)
                .append(", role=")
                .append(role)
                .append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (fullName != null ? !fullName.equals(user.fullName) : user.fullName != null) return false;
        return role == user.role;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    /**
     * Construction user.
     */
    public static class Builder {
        private User newUser;

        /**
         * Constructor
         */
        public Builder() {
            newUser = new User();
        }

        /**
         * definition identifier {@link User#id}
         *
         * @param id - identifier
         * @return Builder
         */
        public Builder addId(Long id) {
            newUser.id = id;
            return this;
        }

        /**
         * Login definition {@link User#login}
         *
         * @param login - login
         * @return Builder
         */
        public Builder addLogin(String login) {
            newUser.login = login;
            return this;
        }

        /**
         * Full name definition {@link User#fullName}
         *
         * @param fullName - full name
         * @return Builder
         */
        public Builder addFullName(String fullName) {
            newUser.fullName = fullName;
            return this;
        }

        /**
         * Role definition {@link User#role}
         *
         * @param role - role
         * @return Builder
         */
        public Builder addRole(RoleType role) {
            newUser.role = role;
            return this;
        }

        /**
         * Role definition {@link User#role}
         *
         * @param role - role
         * @return Builder
         */
        public Builder addRole(String role) {
            newUser.role = RoleType.valueOf(role.toUpperCase());
            return this;
        }

        /**
         * Returns the constructed user
         *
         * @return user
         */
        public User build() {
            return newUser;
        }
    }
}
