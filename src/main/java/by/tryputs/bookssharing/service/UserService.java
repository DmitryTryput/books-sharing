package by.tryputs.bookssharing.service;

import by.tryputs.bookssharing.converter.UserConverter;
import by.tryputs.bookssharing.dto.UserDto;
import by.tryputs.bookssharing.entity.Role;
import by.tryputs.bookssharing.entity.User;
import by.tryputs.bookssharing.repository.RoleRepository;
import by.tryputs.bookssharing.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private UserConverter userConverter;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private static final String USER_ROLE_NAME = "USER";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        by.tryputs.bookssharing.entity.User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User doesn't exist!");
        }
        return new org.springframework.security.core.userdetails.User(email, user.getPassword(), generateAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> generateAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
    }

    public void signUp(UserDto userToSave) {
        final Role userRole = roleRepository.findByName(USER_ROLE_NAME);

        final User user = userConverter.convertToDbo(userToSave);
        user.setRoles(Collections.singletonList(userRole));

        final String password = userToSave.getPassword();
        final String encodedPassword = passwordEncoder.encode(password);

        user.setPassword(encodedPassword);

        userRepository.save(user);
    }
}