package hexlet.code.service;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsManager {
    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Override
    public void createUser(UserDetails userData) {
        var user = new User();
        user.setEmail(userData.getUsername());
        var hashedPassword = passwordEncoder.encode(userData.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Нереализованный метод 'updateUser'");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Нереализованный метод 'deleteUser'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Нереализованный метод 'changePassword'");
    }

    @Override
    public boolean userExists(String username) {
        throw new UnsupportedOperationException("Нереализованный метод 'userExists'");
    }
}
