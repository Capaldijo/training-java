package fr.ebiz.computerdatabase.dao;

import fr.ebiz.computerdatabase.model.User;

import java.util.List;

public interface IUserDAO {

    /**
     * Find a user by his name.
     * @param userName Name of the user. String.
     * @return a User.
     */
    User findUserByName(String userName);

    /**
     * Get the user's roles by his name.
     * @param userName Name of the user. String.
     * @return the list of the user's roles.
     */
    List<String> getUserRoles(String userName);
}
