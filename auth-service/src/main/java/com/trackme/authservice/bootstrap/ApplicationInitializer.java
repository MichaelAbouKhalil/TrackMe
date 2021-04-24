package com.trackme.authservice.bootstrap;

import com.trackme.authservice.repository.AuthorityRepository;
import com.trackme.authservice.repository.ClientRepository;
import com.trackme.authservice.repository.RoleRepository;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.models.enums.AuthorityEnum;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.security.AuthorityEntity;
import com.trackme.models.security.ClientDetailsEntity;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    private Map<AuthorityEnum, AuthorityEntity> authorities;
    private Map<RoleEnum, RoleEntity> roles;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadClients();
        loadAuthorities();
        loadRoles();
        loadDemoUsers();
    }

    private void loadClients() {

        ClientDetailsEntity client = ClientDetailsEntity.builder()
                .clientId("trackme-webapp")
                .clientSecret(passwordEncoder.encode("secret-trackme"))
                .authorizedGrantTypes("refresh_token,password,client_credentials")
                .scope("webclient")
                .build();

        if (clientRepository.count() == 0) {
            log.debug("No client already exists, loading client {} in database", client.getClientId());
            clientRepository.save(client);
        }
    }

    private void loadDemoUsers() {

        if (userRepository.count() == 0) {

            userRepository.save(UserEntity.builder()
                    .username("demo-admin")
                    .email("admin@demo.com")
                    .password(passwordEncoder.encode("demo-admin"))
                    .role(roles.get(RoleEnum.ADMIN))
                    .build());

            userRepository.save(UserEntity.builder()
                    .username("demo-pm")
                    .email("pm@demo.com")
                    .password(passwordEncoder.encode("demo-pm"))
                    .role(roles.get(RoleEnum.PM))
                    .build());

            userRepository.save(UserEntity.builder()
                    .username("demo-dev")
                    .email("dev@demo.com")
                    .password(passwordEncoder.encode("demo-dev"))
                    .role(roles.get(RoleEnum.DEV))
                    .build());

            userRepository.save(UserEntity.builder()
                    .username("demo-customer")
                    .email("customer@demo.com")
                    .password(passwordEncoder.encode("demo-customer"))
                    .role(roles.get(RoleEnum.CUSTOMER))
                    .build());

        }
    }

    private void loadRoles() {
        roles = new HashMap<>();
        if (roleRepository.count() == 0) {

            roles.put(RoleEnum.ADMIN,
                    roleRepository.save(
                            RoleEntity.builder()
                                    .roleName(RoleEnum.ADMIN.getRoleName())
                                    .authorities(Arrays.asList(
                                            authorities.get(AuthorityEnum.ADMIN_UPDATE),
                                            authorities.get(AuthorityEnum.ADMIN_READ),
                                            authorities.get(AuthorityEnum.ADMIN_PROMOTE),
                                            authorities.get(AuthorityEnum.ADMIN_DEMOTE),
                                            authorities.get(AuthorityEnum.ADMIN_DELETE),
                                            authorities.get(AuthorityEnum.ADMIN_EDIT)
                                    ))
                                    .build()));

            roles.put(RoleEnum.PM,
                    roleRepository.save(
                            RoleEntity.builder()
                                    .roleName(RoleEnum.PM.getRoleName())
                                    .authorities(Arrays.asList(
                                            authorities.get(AuthorityEnum.PM_WRITE),
                                            authorities.get(AuthorityEnum.PM_UPDATE),
                                            authorities.get(AuthorityEnum.PM_READ),
                                            authorities.get(AuthorityEnum.PM_DISMISS),
                                            authorities.get(AuthorityEnum.PM_DELETE),
                                            authorities.get(AuthorityEnum.PM_ASSIGN)
                                    ))
                                    .build()));

            roles.put(RoleEnum.DEV,
                    roleRepository.save(
                            RoleEntity.builder()
                                    .roleName(RoleEnum.DEV.getRoleName())
                                    .authorities(Arrays.asList(
                                            authorities.get(AuthorityEnum.DEV_WRITE),
                                            authorities.get(AuthorityEnum.DEV_UPDATE),
                                            authorities.get(AuthorityEnum.DEV_READ),
                                            authorities.get(AuthorityEnum.DEV_DELETE)
                                    ))
                                    .build()));

            roles.put(RoleEnum.CUSTOMER,
                    roleRepository.save(
                            RoleEntity.builder()
                                    .roleName(RoleEnum.CUSTOMER.getRoleName())
                                    .authorities(Arrays.asList(
                                            authorities.get(AuthorityEnum.CUSTOMER_WRITE),
                                            authorities.get(AuthorityEnum.CUSTOMER_UPDATE),
                                            authorities.get(AuthorityEnum.CUSTOMER_READ),
                                            authorities.get(AuthorityEnum.CUSTOMER_DELETE)
                                    ))
                                    .build()));

            roles.put(RoleEnum.PM_PENDING,
                    roleRepository.save(
                            RoleEntity.builder()
                                    .roleName(RoleEnum.PM_PENDING.getRoleName())
                                    .build()));

            roles.put(RoleEnum.DEV_PENDING,
                    roleRepository.save(
                            RoleEntity.builder()
                                    .roleName(RoleEnum.DEV_PENDING.getRoleName())
                                    .build()));

            roles.put(RoleEnum.CUSTOMER_PENDING,
                    roleRepository.save(
                            RoleEntity.builder()
                                    .roleName(RoleEnum.CUSTOMER_PENDING.getRoleName())
                                    .build()));
        }
    }

    private void loadAuthorities() {
        authorities = new HashMap<>();

        if (authorityRepository.count() == 0) {

            authorities.put(AuthorityEnum.ADMIN_READ,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.ADMIN_READ.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.ADMIN_EDIT,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.ADMIN_EDIT.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.ADMIN_DELETE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.ADMIN_DELETE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.ADMIN_DEMOTE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.ADMIN_DEMOTE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.ADMIN_PROMOTE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.ADMIN_PROMOTE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.ADMIN_UPDATE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.ADMIN_UPDATE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.PM_ASSIGN,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.PM_ASSIGN.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.PM_DELETE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.PM_DELETE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.PM_DISMISS,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.PM_DISMISS.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.PM_READ,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.PM_READ.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.PM_UPDATE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.PM_UPDATE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.PM_WRITE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.PM_WRITE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.DEV_DELETE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.DEV_DELETE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.DEV_READ,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.DEV_READ.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.DEV_UPDATE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.DEV_UPDATE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.DEV_WRITE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.DEV_WRITE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.CUSTOMER_DELETE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.CUSTOMER_DELETE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.CUSTOMER_READ,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.CUSTOMER_READ.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.CUSTOMER_UPDATE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.CUSTOMER_UPDATE.getAuthorityName()).build()));

            authorities.put(AuthorityEnum.CUSTOMER_WRITE,
                    authorityRepository.save(AuthorityEntity.builder()
                            .permission(AuthorityEnum.CUSTOMER_WRITE.getAuthorityName()).build()));
        }
    }
}
