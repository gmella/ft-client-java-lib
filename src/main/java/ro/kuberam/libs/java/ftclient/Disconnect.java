/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2011 The eXist Project
 *  http://exist-db.org
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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Implements a method for closing a remote connection.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */
public class Disconnect {
	private static final Logger log = LogManager.getLogger(Disconnect.class);
	private static final String moduleName = ExpathFTClientModule.MODULE_NAME;

	public static Boolean disconnect(Object remoteConnection) throws Exception {
		Boolean result = true;

		String protocol = ExpathFTClientModule.PROTOCOL_CLASS_CODES
				.get(remoteConnection.getClass().getName());
		Class<?> clazz = Class.forName("ro.kuberam.libs.java.ftclient." + protocol + "."
				+ protocol);
		Method method = clazz.getMethod("disconnect",
				new Class<?>[] { Object.class });
		try {
			result = (Boolean) method.invoke(clazz.newInstance(),
					new Object[] { remoteConnection });
			log.info(moduleName + " is now disconnected.");
		} catch (InvocationTargetException ex) {
			throw new Exception(ex.getCause().getMessage());
		}

		return result;
	}
}