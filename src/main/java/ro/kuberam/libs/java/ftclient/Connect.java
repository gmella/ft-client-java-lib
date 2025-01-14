/*
 *  Copyright (C) 2011 Claudius Teodorescu
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  $Id$
 */
package ro.kuberam.libs.java.ftclient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Implements a method for opening a remote connection.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */


public class Connect {
    
    private static final Logger log = LogManager.getLogger(Connect.class);
    
    public static <X> X connect(URI remoteHostUri, String options) throws Exception {
     
        X connection = null;

        // get the connection details
        String scheme = remoteHostUri.getScheme();
        String username = "anonymous";
        String password = "anonymous";
        String userInfo = remoteHostUri.getUserInfo();
        if (userInfo != null) {
            if (userInfo.indexOf(":") == -1) {
                username =  userInfo;
            } else {
                username =  userInfo.substring(0, userInfo.indexOf(":"));
                password = userInfo.substring(userInfo.indexOf(":") + 1);
            }
        }
        String remoteHost = remoteHostUri.getHost();

        int remotePort = remoteHostUri.getPort();
        
        String protocol = scheme.toUpperCase();
        
        if (!ExpathFTClientModule.SUPPORTED_PROTOCOLS.contains(" " + protocol + " ")) {
            log.debug("err:FTC006: The protocol is not supported. Details: the protocol '" + protocol + "' is not implemented.");
            throw new Exception("err:FTC006: The protocol is not supported.");
        }

        if (!ExpathFTClientModule.LOADED_PROTOCOLS.get(protocol)) {
            log.debug("err:FTC006: The protocol is not supported. Details: the protocol jar is not loaded.");
            throw new Exception("err:FTC006: The protocol is not supported.");
        }
        
        Class<?> clazz = Class.forName("ro.kuberam.libs.java.ftclient." + protocol + "." + protocol);
        Method method = clazz.getMethod("connect", new Class<?>[] {URI.class, String.class, String.class, String.class, int.class, String.class});
        try {
            connection = (X) method.invoke(clazz.newInstance(), new Object[] {remoteHostUri, username, password, remoteHost, remotePort, options});
        } catch(InvocationTargetException ex) {
            throw new Exception(ex.getCause().getMessage());
        }
        
        return connection;
    }
}