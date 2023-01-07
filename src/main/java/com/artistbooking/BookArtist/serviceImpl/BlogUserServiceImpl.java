package com.artistbooking.BookArtist.serviceImpl;

import com.artistbooking.BookArtist.model.Authority;
import com.artistbooking.BookArtist.model.BlogUser;
import com.artistbooking.BookArtist.repository.AuthorityRepository;
import com.artistbooking.BookArtist.repository.BlogUserRepository;
import com.artistbooking.BookArtist.service.BlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class BlogUserServiceImpl implements BlogUserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private final BCryptPasswordEncoder bcryptEncoder;
    private final BlogUserRepository blogUserRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public BlogUserServiceImpl(BCryptPasswordEncoder bcryptEncoder, BlogUserRepository blogUserRepository, AuthorityRepository authorityRepository) {
        this.bcryptEncoder = bcryptEncoder;
        this.blogUserRepository = blogUserRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BlogUser> blogUser = Optional.ofNullable(blogUserRepository.findByUsername(username));
        if (blogUser.isPresent()) {
            return blogUser.get();
        } else {
            throw new UsernameNotFoundException("No user found with username " + username);
        }
    }

    @Override
    public Optional<BlogUser> findByUsername(String username) {

        return Optional.ofNullable(blogUserRepository.findByUsername(username));
    }

    @Override
    public BlogUser saveNewBlogUser(BlogUser blogUser) throws RoleNotFoundException {
        System.err.println("saveNewBlogUser: " + blogUser);
        blogUser.setPassword(this.bcryptEncoder.encode(blogUser.getPassword()));
        blogUser.setEnabled(true);
        Optional<Authority> optionalAuthority = this.authorityRepository.findByAuthority(DEFAULT_ROLE);
        System.err.println("optionalAuthority: " + optionalAuthority);
        if (optionalAuthority.isPresent()) {
            Authority authority = optionalAuthority.get();
            Collection<Authority> authorities = Collections.singletonList(authority);
            blogUser.setAuthorities(authorities);
            System.err.println("blogUser after Roles: " + blogUser);
//            return blogUserRepository.save(blogUser);
            return this.blogUserRepository.saveAndFlush(blogUser);
        } else {
            throw new RoleNotFoundException("Default role not found for blog user with username " + blogUser.getUsername());
        }
    }
}
