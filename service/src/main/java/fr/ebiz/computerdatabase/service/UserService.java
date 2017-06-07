package fr.ebiz.computerdatabase.service;


import fr.ebiz.computerdatabase.dao.UserDAO;
import fr.ebiz.computerdatabase.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional()
public class UserService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserDAO userDAO;

    /**
     * Default UserService constructor.
     * @param userDAO .
     */
    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findUserByName(username);

        if (user == null) {
            LOG.error("[Service] User: " + username + " was not found.");
            throw new RuntimeException("[Service] User: " + username + " was not found.");
        }

        List<String> roles = userDAO.getUserRoles(username);
        List<GrantedAuthority> grantList = new ArrayList<>();

        if (roles != null) {
            for (String role : roles) {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                grantList.add(authority);
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), grantList);
    }
}
