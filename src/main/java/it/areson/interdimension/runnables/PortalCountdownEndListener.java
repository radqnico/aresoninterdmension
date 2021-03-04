package it.areson.interdimension.runnables;

import it.areson.interdimension.portals.Portal;

/**
 * Registrable listener for portal close countdowns.
 */
public interface PortalCountdownEndListener {

    /**
     * This method is called when a portal countdowns finishes.
     *
     * @param portal Portal attached to the countdown.
     */
    void notifyCountdownFinish(Portal portal);

}
