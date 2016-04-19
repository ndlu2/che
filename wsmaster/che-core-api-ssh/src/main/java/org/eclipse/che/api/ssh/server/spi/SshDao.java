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
package org.eclipse.che.api.ssh.server.spi;


import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.ssh.server.model.impl.SshPairImpl;

import java.util.List;

/**
 * Defines data access object contract for {@link SshPairImpl}.
 *
 * @author Sergii Leschenko
 */
public interface SshDao {

    /**
     * Creates new ssh pair for specified user.
     *
     * @param owner
     *         the id of the user who will be the owner of the ssh pair
     * @param sshPair
     *         ssh pair to create
     * @throws ConflictException
     *         when specified user already has ssh pair with given service and name
     * @throws ServerException
     *         when any other error occurs during ssh pair creating
     */
    void create(String owner, SshPairImpl sshPair) throws ServerException, ConflictException;

    /**
     * Returns ssh pairs by owner and service.
     *
     * @param owner
     *         the id of the user who is the owner of the ssh pairs
     * @param service
     *         service name of ssh pair
     * @return list of ssh pair with given service and owned by given service.
     * @throws ServerException
     *         when any other error occurs during ssh pair fetching
     */
    List<SshPairImpl> get(String owner, String service) throws ServerException;

    /**
     * Returns ssh pair by owner, service and name.
     *
     * @param owner
     *         the id of the user who is the owner of the ssh pair
     * @param service
     *         service name of ssh pair
     * @param name
     *         name of ssh pair
     * @return ssh pair instance
     * @throws NotFoundException
     *         when ssh pair is not found
     * @throws ServerException
     *         when any other error occurs during ssh pair fetching
     */
    SshPairImpl get(String owner, String service, String name) throws ServerException, NotFoundException;

    /**
     * Removes ssh pair by owner, service and name.
     *
     * @param owner
     *         the id of the user who is the owner of the ssh pair
     * @param service
     *         service name of ssh pair
     * @param name
     *         of ssh pair
     * @throws NotFoundException
     *         when ssh pair is not found
     * @throws ServerException
     *         when any other error occurs during ssh pair removing
     */
    void remove(String owner, String service, String name) throws ServerException, NotFoundException;
}
