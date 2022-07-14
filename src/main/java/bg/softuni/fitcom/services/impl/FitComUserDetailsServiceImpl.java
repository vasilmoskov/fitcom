package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.user.FitcomUserDetails;
import bg.softuni.fitcom.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FitComUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public FitComUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("No such user."));

        return mapToUserDetails(userEntity);
    }

    private UserDetails mapToUserDetails(UserEntity userEntity) {
        List<GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getRole().name()))
                .collect(Collectors.toList());

        return new FitcomUserDetails( userEntity.getPassword(), userEntity.getEmail(),
                userEntity.getFirstName(), userEntity.getLastName(), authorities);
//        return new User(userEntity.getEmail(),userEntity.getPassword(), authorities);
    }
}
