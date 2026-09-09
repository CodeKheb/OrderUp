package com.orderup.Models;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A dish from the fixed in-game menu that a customer can order.
 *
 * <p>Customers are only abstract scheduling processes (arrival time, burst time)
 * so the actual food they "tell" the player is cosmetic flavor. Each customer is
 * assigned one random {@link MenuItem} when they spawn, which is then displayed
 * in their thought bubble in the waiting line scene.</p>
 */
public enum MenuItem {

    BURGER("Burger"),
    PIZZA("Pizza"),
    FRIES("Fries"),
    COFFEE("Coffee");

    /** Human-friendly name shown in the thought bubble. */
    private final String displayName;

    MenuItem(String displayName) {
        this.displayName = displayName;
    }

    /** Returns the name of this dish as it should be displayed to the player. */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Picks a random dish from the menu.
     *
     * @return a randomly selected menu item
     */
    public static MenuItem random() {
        MenuItem[] menu = values();
        return menu[ThreadLocalRandom.current().nextInt(menu.length)];
    }
}
