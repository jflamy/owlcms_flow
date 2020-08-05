/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.components.elements.unload;

import com.vaadin.flow.component.ComponentEvent;

/**
 * Server-side event class associated with {@code beforeunload} event happening in the client-side. Can optionally
 * prompt the user before leaving the page.
 *
 * @author miki
 * @since 2020-04-29
 */
public class UnloadEvent extends ComponentEvent<UnloadObserver> {

    private final boolean becauseOfQuerying;

    /**
     * Creates a new event using the given source and indicator whether the event originated from the client side or the
     * server side.
     *
     * @param source    the source component
     * @param attempted when {@code true}, the event is fired in response to querying before unloading; {@code false}
     *                  otherwise.
     */
    public UnloadEvent(UnloadObserver source, boolean attempted) {
        super(source, true);
        this.becauseOfQuerying = attempted;
    }

    /**
     * Checks whether or not the event has been fired in response to querying the user on {@code beforeunload} browser
     * event.
     *
     * @return {@code true} when event is in response to querying the user on {@code beforeunload}, {@code false}
     *         otherwise.
     */
    public boolean isBecauseOfQuerying() {
        return becauseOfQuerying;
    }
}