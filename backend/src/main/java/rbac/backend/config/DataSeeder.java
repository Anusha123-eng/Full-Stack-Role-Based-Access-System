
package rbac.backend.config;

import rbac.backend.entity.User;
import rbac.backend.entity.Role;
import rbac.backend.entity.Permission;
import rbac.backend.entity.Resource;

import rbac.backend.repository.UserRepository;
import rbac.backend.repository.RoleRepository;
import rbac.backend.repository.PermissionRepository;
import rbac.backend.repository.ResourceRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            ResourceRepository resourceRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Skip seeding if data already exists
            if (userRepository.count() > 0
                    || roleRepository.count() > 0
                    || resourceRepository.count() > 0
                    || permissionRepository.count() > 0) {

                System.out.println("Seed skipped: data already exists.");
                return;
            }

            System.out.println("Starting RBAC database seed...");

            // 1. Create resources
            List<Resource> resources = List.of(
                    new Resource("users", "endpoint", "/api/users", "User management"),
                    new Resource("roles", "endpoint", "/api/roles", "Role management"),
                    new Resource("permissions", "endpoint", "/api/permissions", "Permission management"),
                    new Resource("resources", "endpoint", "/api/resources", "Resource management"),
                    new Resource("audit", "endpoint", "/api/audit", "Audit logs"),
                    new Resource("dashboard", "page", "/dashboard", "Dashboard page"),
                    new Resource("reports", "page", "/reports", "Reports page")
            );

            resourceRepository.saveAll(resources);

            // 2. Create permissions
            String[][] permissionData = {
                    {"create:users", "create", "users", "Create users", "0"},
                    {"read:users", "read", "users", "View users", "0"},
                    {"update:users", "update", "users", "Update users", "0"},
                    {"delete:users", "delete", "users", "Delete users", "0"},
                    {"create:roles", "create", "roles", "Create roles", "1"},
                    {"read:roles", "read", "roles", "View roles", "1"},
                    {"update:roles", "update", "roles", "Update roles", "1"},
                    {"delete:roles", "delete", "roles", "Delete roles", "1"},
                    {"create:permissions", "create", "permissions", "Create permissions", "2"},
                    {"read:permissions", "read", "permissions", "View permissions", "2"},
                    {"update:permissions", "update", "permissions", "Update permissions", "2"},
                    {"delete:permissions", "delete", "permissions", "Delete permissions", "2"},
                    {"read:audit", "read", "audit", "View audit logs", "4"},
                    {"read:dashboard", "read", "dashboard", "View dashboard", "5"},
                    {"read:reports", "read", "reports", "View reports", "6"}
            };

            List<Permission> permissions = new ArrayList<>();

            for (String[] data : permissionData) {

                Resource resource = resources.get(Integer.parseInt(data[4]));

                Permission permission = new Permission(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        resource
                );

                permissions.add(permission);
            }

            permissionRepository.saveAll(permissions);

            // 3. Create roles
            Role adminRole = new Role("Admin", "Full system access", 100);
            Role managerRole = new Role("Manager", "Manage users and view reports", 50);
            Role hrRole = new Role("HR", "Manage employees", 40);
            Role employeeRole = new Role("Employee", "Basic access", 10);

            // Admin gets all permissions
            adminRole.setPermissions(new HashSet<>(permissions));

            managerRole.setPermissions(new HashSet<>(List.of(
                    permissions.get(1),
                    permissions.get(2),
                    permissions.get(5),
                    permissions.get(12),
                    permissions.get(13),
                    permissions.get(14)
            )));

            hrRole.setPermissions(new HashSet<>(List.of(
                    permissions.get(0),
                    permissions.get(1),
                    permissions.get(2),
                    permissions.get(13)
            )));

            employeeRole.setPermissions(new HashSet<>(List.of(
                    permissions.get(1),
                    permissions.get(13)
            )));

            roleRepository.saveAll(List.of(
                    adminRole,
                    managerRole,
                    hrRole,
                    employeeRole
            ));

            // 4. Create users and assign roles
            User admin = new User(
                    "admin",
                    "admin@rbac.com",
                    passwordEncoder.encode("Admin@123"),
                    "System Administrator",
                    true
            );
            admin.setRoles(new HashSet<>(List.of(adminRole)));

            User manager = new User(
                    "manager",
                    "manager@rbac.com",
                    passwordEncoder.encode("Manager@123"),
                    "John Manager",
                    true
            );
            manager.setRoles(new HashSet<>(List.of(managerRole)));

            User hr = new User(
                    "hruser",
                    "hr@rbac.com",
                    passwordEncoder.encode("HR@123"),
                    "Sarah HR",
                    true
            );
            hr.setRoles(new HashSet<>(List.of(hrRole)));

            User employee = new User(
                    "employee",
                    "employee@rbac.com",
                    passwordEncoder.encode("Employee@123"),
                    "Mike Employee",
                    true
            );
            employee.setRoles(new HashSet<>(List.of(employeeRole)));

            userRepository.saveAll(List.of(
                    admin,
                    manager,
                    hr,
                    employee
            ));

            System.out.println("RBAC database seeded successfully.");
        };
    }
}
