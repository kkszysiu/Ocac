/*
Yaaic - Yet Another Android IRC Client

Copyright 2009-2011 Sebastian Kaspari

This file is part of Yaaic.

Yaaic is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Yaaic is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Yaaic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.yaaic;

import org.yaaic.model.Server;

/**
 * Global Master Class :)
 * 
 * @author Sebastian Kaspari <sebastian@yaaic.org>
 */
public class Yaaic
{
    public static Yaaic             instance;
    public static Server            server;
    private final boolean           serversLoaded = false;

    /**
     * Private constructor, you may want to use static getInstance()
     */
    private Yaaic()
    {
        server = new Server();
    }

    /**
     * Get global Yaaic instance
     * 
     * @return the global Yaaic instance
     */
    public static Yaaic getInstance()
    {
        if (instance == null) {
            instance = new Yaaic();
        }

        return instance;
    }

    /**
     * Get server by id
     * 
     * @return Server object with given unique id
     */
    public Server getServer()
    {
        return server;
    }

}
