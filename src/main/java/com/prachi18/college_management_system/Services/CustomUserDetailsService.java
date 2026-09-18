package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.Entities.User;
import com.prachi18.college_management_system.Exceptions.ResourceNotFoundException;
import com.prachi18.college_management_system.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
     private final UserRepository userRepository;

     @Override
     public UserDetails loadUserByUsername(String email) {

         User user = userRepository.findByEmail(email)
                 .orElseThrow(() ->
                         new UsernameNotFoundException("User not found with email: " + email)
                 );

         return org.springframework.security.core.userdetails.User
                 .withUsername(user.getEmail())
                 .password(user.getPassword())
                 .roles(user.getRole().name())
                 .build();
     }

     public UserDetails loadUserById(long id) {
         User user =userRepository.findById(id)
                 .orElseThrow(()->
                         new ResourceNotFoundException("user not found with id: "+id));
         return org.springframework.security.core.userdetails.User
                 .withUsername(user.getEmail())
                 .password(user.getPassword())
                 .roles(user.getRole().name())
                 .build();
     }
}
