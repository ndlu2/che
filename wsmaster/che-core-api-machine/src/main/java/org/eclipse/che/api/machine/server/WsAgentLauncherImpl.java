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
package org.eclipse.che.api.machine.server;

import org.eclipse.che.api.core.ApiException;
import org.eclipse.che.api.core.BadRequestException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.model.machine.Machine;
import org.eclipse.che.api.core.rest.HttpJsonRequest;
import org.eclipse.che.api.core.rest.HttpJsonRequestFactory;
import org.eclipse.che.api.core.rest.HttpJsonResponse;
import org.eclipse.che.api.machine.server.exception.MachineException;
import org.eclipse.che.api.machine.server.model.impl.CommandImpl;
import org.eclipse.che.api.machine.shared.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Starts ws agent in the machine and waits until ws agent sends notification about its start
 *
 * @author Alexander Garagatyi
 */
@Singleton
public class WsAgentLauncherImpl implements WsAgentLauncher {
    public static final String WS_AGENT_PROCESS_START_COMMAND = "machine.ws_agent.run_command";
    public static final String WS_AGENT_PROCESS_NAME          = "CheWsAgent";

    private static final Logger LOG                             = LoggerFactory.getLogger(WsAgentLauncherImpl.class);
    private static final String WS_AGENT_PROCESS_OUTPUT_CHANNEL = "workspace:%s:ext-server:output";

    private final Provider<MachineManager> machineManagerProvider;
    private final HttpJsonRequestFactory   httpJsonRequestFactory;
    private final String                   wsAgentStartCommandLine;
    private final long                     wsAgentMaxStartTimeMs;
    private final long                     wsAgentPingDelayMs;
    private final int                      wsAgentPingConnectionTimeoutMs;
    private final String                   wsAgentPingPath;
    private final String                   pingTimedOutErrorMessage;

    @Inject
    public WsAgentLauncherImpl(Provider<MachineManager> machineManagerProvider,
                               HttpJsonRequestFactory httpJsonRequestFactory,
                               @Named(WS_AGENT_PROCESS_START_COMMAND) String wsAgentStartCommandLine,
                               @Named("machine.ws_agent.max_start_time_ms") long wsAgentMaxStartTimeMs,
                               @Named("machine.ws_agent.ping_delay_ms") long wsAgentPingDelayMs,
                               @Named("machine.ws_agent.ping_conn_timeout_ms") int wsAgentPingConnectionTimeoutMs,
                               @Named("machine.ws_agent.ping_timed_out_error_msg") String pingTimedOutErrorMessage,
                               @Named("machine.ws_agent.agent_api.path") String wsAgentApiPath) {
        this.machineManagerProvider = machineManagerProvider;
        this.httpJsonRequestFactory = httpJsonRequestFactory;
        this.wsAgentStartCommandLine = wsAgentStartCommandLine;
        this.wsAgentMaxStartTimeMs = wsAgentMaxStartTimeMs;
        this.wsAgentPingDelayMs = wsAgentPingDelayMs;
        this.wsAgentPingConnectionTimeoutMs = wsAgentPingConnectionTimeoutMs;
        this.pingTimedOutErrorMessage = pingTimedOutErrorMessage;
        // everrest respond 404 to path to rest without trailing slash
        this.wsAgentPingPath = wsAgentApiPath;
    }

    public static String getWsAgentProcessOutputChannel(String workspaceId) {
        return String.format(WS_AGENT_PROCESS_OUTPUT_CHANNEL, workspaceId);
    }

    @Override
    public void startWsAgent(String workspaceId) throws NotFoundException, MachineException, InterruptedException {
        final Machine devMachine = getMachineManager().getDevMachine(workspaceId);
        try {
            getMachineManager().exec(devMachine.getId(),
                                     new CommandImpl(WS_AGENT_PROCESS_NAME, wsAgentStartCommandLine, "Arbitrary"),
                                     getWsAgentProcessOutputChannel(workspaceId));

            final HttpJsonRequest wsAgentPingRequest = createPingRequest(devMachine);

            long pingStartTimestamp = System.currentTimeMillis();
            LOG.debug("Starts pinging ws agent. Workspace ID:{}. Url:{}. Timestamp:{}",
                      workspaceId,
                      wsAgentPingRequest,
                      pingStartTimestamp);

            while (System.currentTimeMillis() - pingStartTimestamp < wsAgentMaxStartTimeMs) {
                if (pingWsAgent(wsAgentPingRequest)) {
                    return;
                } else {
                    Thread.sleep(wsAgentPingDelayMs);
                }
            }
        } catch (BadRequestException wsAgentLaunchingExc) {
            throw new MachineException(wsAgentLaunchingExc.getLocalizedMessage(), wsAgentLaunchingExc);
        }
        throw new MachineException(pingTimedOutErrorMessage);
    }

    private HttpJsonRequest createPingRequest(Machine devMachine) {
        final String wsAgentPingUrl = UriBuilder.fromUri(devMachine.getRuntime()
                                                                   .getServers()
                                                                   .get(Constants.WS_AGENT_PORT)
                                                                   .getUrl())
                                                .replacePath(wsAgentPingPath)
                                                .build()
                                                .toString();
        return httpJsonRequestFactory.fromUrl(wsAgentPingUrl)
                                     .setMethod(HttpMethod.GET)
                                     .setTimeout(wsAgentPingConnectionTimeoutMs);
    }

    private boolean pingWsAgent(HttpJsonRequest wsAgentPingRequest) throws MachineException {
        try {
            final HttpJsonResponse pingResponse = wsAgentPingRequest.request();
            if (pingResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (ApiException | IOException ignored) {
        }
        return false;
    }

    private MachineManager getMachineManager() {
        return machineManagerProvider.get();
    }
}
