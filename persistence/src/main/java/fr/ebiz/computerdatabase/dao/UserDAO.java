package fr.ebiz.computerdatabase.dao;

import fr.ebiz.computerdatabase.model.User;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class UserDAO implements IUserDAO {

    private static final Logger LOG = LoggerFactory.getLogger(UserDAO.class);

    private static final String QUERY_FIND = "from User where username = :username";

    private static final String QUERY_ROLES = "select userRole from UserRoles where username = :username";

    private SessionFactory sessionFactory;

    /**
     * Default constructor UserDAO.
     * @param sessionFactory handler for connections and creation of query.
     */
    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User findUserByName(String userName) {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(QUERY_FIND, User.class);
        return query.setParameter("username", userName).getResultList().get(0);
    }

    @Override
    public List<String> getUserRoles(String userName) {
        TypedQuery<String> query = sessionFactory.getCurrentSession().createQuery(QUERY_ROLES, String.class);
        return query.setParameter("username", userName).getResultList();
    }
}
