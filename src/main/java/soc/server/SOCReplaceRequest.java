/**
 * Java Settlers - An online multiplayer version of the game Settlers of Catan
 * Copyright (C) 2003  Robert S. Thomas <thomas@infolab.northwestern.edu>
 * Portions of this file Copyright (C) 2017,2020-2024 Jeremy D Monin <jeremy@nand.net>.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * The maintainer of this program can be reached at jsettlers@nand.net
 **/
package soc.server;

import soc.message.SOCSitDown;

import soc.server.genericServer.Connection;


/**
 * This is a pair of connections, one is sitting at a game and the other is leaving;
 * the arriving connection might be taking over the leaving one's seat.
 * Gives server info about both for when dealing with messages/events from the other.
 */
/*package*/ class SOCReplaceRequest
{
    private final Connection arriving;
    private final Connection leaving;
    /**
     * Player number, from the {@link SOCSitDown} message from {@link #arriving}.
     * Before v2.7.00 we stored the entire message but only ever used its PN.
     * @since 2.7.00
     */
    private final int playerNumber;
    private final boolean isArrivingRobot;

    /**
     * Make a new request
     * @param arriv  the arriving connection; not null
     * @param leave  the leaving connection; not null
     * @param sm the SITDOWN message from {@code arriv}, for its {@link SOCSitDown#getPlayerNumber()}; not null
     * @throws IllegalArgumentException if {@code arriv}, {@code leave}, or {@code sm} is {@code null}
     */
    public SOCReplaceRequest(Connection arriv, Connection leave, SOCSitDown sm)
        throws IllegalArgumentException
    {
        if (arriv == null)
            throw new IllegalArgumentException("arriving");
        if (leave == null)
            throw new IllegalArgumentException("leaving");
        if (sm == null)
            throw new IllegalArgumentException("sm");

        arriving = arriv;
        leaving = leave;
        playerNumber = sm.getPlayerNumber();

        final SOCClientData arrivScd = (SOCClientData) arriv.getAppData();
        isArrivingRobot = (arrivScd != null) ? arrivScd.isRobot : false;
    }

    /**
     * @return the arriving connection; not null
     * @see #isArrivingRobot()
     */
    public Connection getArriving()
    {
        return arriving;
    }

    /**
     * Is the arriving connection's player a robot?
     * Set during constructor by checking {@link #getArriving()}'s {@link SOCClientData#isRobot} flag.
     * @return true if {@link #getArriving()} is a bot
     * @since 2.5.00
     */
    public boolean isArrivingRobot()
    {
        return isArrivingRobot;
    }

    /**
     * @return the leaving connection; not null
     */
    public Connection getLeaving()
    {
        return leaving;
    }

    /**
     * @return seat number/player number being replaced, from the SITDOWN message
     * @since 2.7.00
     */
    public int getPlayerNumber()
    {
        return playerNumber;
    }

}
