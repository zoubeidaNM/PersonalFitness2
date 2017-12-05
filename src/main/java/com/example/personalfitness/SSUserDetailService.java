package com.example.personalfitness;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public SSUserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
            FitnessUser user = userRepository.findByUsername(username);

            if(user == null){
                throw new UsernameNotFoundException("No user found with username: " + username);

            }

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));

     }

    private Set<GrantedAuthority> getAuthorities(FitnessUser user){
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        for(UserRole role: user.getRoles()){
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
            authorities.add(grantedAuthority);
        }
        return authorities;

    }
}
