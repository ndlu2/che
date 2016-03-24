/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.api.user.server.dao;

import org.eclipse.che.api.core.BadRequestException;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;

import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;
import static org.eclipse.che.api.user.server.Constants.ID_LENGTH;
import static org.eclipse.che.api.user.server.Constants.PASSWORD_LENGTH;
import static org.eclipse.che.commons.lang.NameGenerator.generate;

/**
 * Facade for User related operations.
 *
 * @author Max Shaposhnik (mshaposhnik@codenvy.com)
 */
public class UserManager {

    private final UserDao userDao;
    private final UserProfileDao profileDao;
    private final PreferenceDao  preferenceDao;


    @Inject
    public UserManager(UserDao userDao,
                       UserProfileDao profileDao,
                       PreferenceDao preferenceDao) {
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.preferenceDao = preferenceDao;
    }


    /**
     * Creates new user.
     *
     * @param user
     *         - POJO representation of user entity
     * @throws ConflictException
     *         when given user cannot be created
     * @throws ServerException
     *         when any other error occurs
     */
    public void create(User user, boolean isTemporary) throws ConflictException, ServerException, BadRequestException,NotFoundException {
        user.withId(generate("user", ID_LENGTH))
            .withPassword(firstNonNull(user.getPassword(), generate("", PASSWORD_LENGTH)));
        checkPassword(user.getPassword());
        userDao.create(user);

        profileDao.create(new Profile(user.getId()));

        final Map<String, String> preferences = new HashMap<>(4);
        preferences.put("temporary", Boolean.toString(isTemporary));
        preferences.put("codenvy:created", Long.toString(System.currentTimeMillis()));
        preferenceDao.setPreferences(user.getId(), preferences);
    }


    /**
     * Gets user from persistent layer by his identifier
     *
     * @param id
     *         user identifier
     * @return user POJO
     * @throws NotFoundException
     *         when user doesn't exist
     * @throws ServerException
     *         when any other error occurs
     */
    public User getById(String id) throws NotFoundException, ServerException {
        return userDao.getById(id);
    }


    /**
     * Updates already present in persistent layer user.
     *
     * @param user
     *         POJO representation of user entity
     * @throws NotFoundException
     *         when user is not found
     * @throws ConflictException
     *         when given user cannot be updated
     * @throws ServerException
     *         when any other error occurs
     *
     */
    public void update(User user) throws NotFoundException, ServerException, ConflictException, BadRequestException {
        checkPassword(user.getPassword());
        userDao.update(user);
    }


    /**
     * Gets user from persistent layer by any of his aliases
     *
     * @param alias
     *         user name or alias
     * @return user POJO
     * @throws NotFoundException
     *         when user doesn't exist
     * @throws ServerException
     *         when any other error occurs
     */
    public User getByAlias(String alias) throws NotFoundException, ServerException {
        return userDao.getByAlias(alias);
    }


    /**
     * Gets user from persistent layer by his username
     *
     * @param userName
     *         user name
     * @return user POJO
     * @throws NotFoundException
     *         when user doesn't exist
     * @throws ServerException
     *         when any other error occurs
     */
    public User getByName(String userName) throws NotFoundException, ServerException {
        return userDao.getByName(userName);
    }


    /**
     * Removes user from persistent layer by his identifier.
     *
     * @param id
     *         user identifier
     * @throws ConflictException
     *         when given user cannot be deleted
     * @throws ServerException
     *         when any other error occurs
     */
    public void remove(String id) throws NotFoundException, ServerException, ConflictException {
        userDao.remove(id);
    }

    private void checkPassword(String password) throws BadRequestException {
        if (password == null) {
            throw new BadRequestException("Password required");
        }
        if (password.length() < 8) {
            throw new BadRequestException("Password should contain at least 8 characters");
        }
        int numOfLetters = 0;
        int numOfDigits = 0;
        for (char passwordChar : password.toCharArray()) {
            if (Character.isDigit(passwordChar)) {
                numOfDigits++;
            }
            if (Character.isLetter(passwordChar)) {
                numOfLetters++;
            }
        }
        if (numOfDigits == 0 || numOfLetters == 0) {
            throw new BadRequestException("Password should contain letters and digits");
        }
    }


}
