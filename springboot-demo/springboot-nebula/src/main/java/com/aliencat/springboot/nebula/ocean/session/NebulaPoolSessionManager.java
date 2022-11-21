package com.aliencat.springboot.nebula.ocean.session;

import com.aliencat.springboot.nebula.ocean.exception.NebulaException;
import com.vesoft.nebula.client.graph.exception.AuthFailedException;
import com.vesoft.nebula.client.graph.exception.ClientServerIncompatibleException;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.exception.NotValidConnectionException;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import org.apache.commons.lang3.StringUtils;


public class NebulaPoolSessionManager {

    private final NebulaPool nebulaPool;

    private final String userName;

    private final String password;

    private final boolean reconnect;

    public NebulaPoolSessionManager(NebulaPool nebulaPool, String userName, String password, boolean reconnect) {
        this.nebulaPool = nebulaPool;
        this.userName = userName;
        this.password = password;
        this.reconnect = reconnect;
    }

    public NebulaSessionWrapper getSession(String space) throws NotValidConnectionException, IOErrorException, AuthFailedException, NebulaException, ClientServerIncompatibleException {
        NebulaSessionWrapper nebulaSessionWrapper = new NebulaSessionWrapper(this.nebulaPool.getSession(this.userName, this.password, this.reconnect));
        if (StringUtils.isNotBlank(space)) {
            nebulaSessionWrapper.execute("use " + space);
        }
        return nebulaSessionWrapper;
    }

    public NebulaSessionWrapper getSession(String userName, String password, boolean reconnect) throws NotValidConnectionException,
            IOErrorException, AuthFailedException, NebulaException, ClientServerIncompatibleException {
        return new NebulaSessionWrapper(this.nebulaPool.getSession(userName, password, reconnect));
    }

    public NebulaSessionWrapper getSession(boolean reconnect) throws NotValidConnectionException,
            IOErrorException, AuthFailedException, NebulaException, ClientServerIncompatibleException {
        return new NebulaSessionWrapper(this.nebulaPool.getSession(this.userName, this.password, reconnect));
    }

}
