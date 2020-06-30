/*
 * MIT License
 * 
 * Copyright (c) 2020 Hochschule Esslingen
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. 
 */
package de.hsesslingen.keim.restutils;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Stores items of type {@link T} in a map by an ID.
 *
 * @author boesch
 * @param <T>
 * @param <ID>
 */
public class Cache<T, ID> {

    private Duration expiryDuration;
    private Map<ID, Item> map = new HashMap<>();

    /**
     * Returns the item expiry duration set for this cache.
     *
     * @return
     */
    protected Duration getExpiryDuration() {
        return expiryDuration;
    }

    /**
     * Sets the item expiry duration for this cache.
     *
     * @param expiryDuration
     */
    protected void setExpiryDuration(Duration expiryDuration) {
        this.expiryDuration = expiryDuration;
    }

    /**
     * Sets a value for the given id.
     *
     * @param id
     * @param item
     */
    public void set(ID id, T item) {
        if (map.containsKey(id)) {
            map.get(id).setItem(item);
        } else {
            map.put(id, new Item(item));
        }
    }

    /**
     * Gets the value of the given id.
     *
     * @param id
     * @return
     */
    public Optional<T> get(ID id) {
        if (map.containsKey(id)) {
            return Optional.of(map.get(id).getItem());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Removes expired items from the cache.
     */
    protected void cleanUp() {
        if (expiryDuration != null) {
            Set<ID> ids = map.keySet();
            var now = Instant.now();

            for (var id : ids) {
                var item = map.get(id);

                if (item.getLastUpdated().plus(expiryDuration).isBefore(now)) {
                    map.remove(id);
                }
            }
        }
    }

    /**
     * Wrapper class to store an element in the cache, containing meta
     * information (update timestamp).
     */
    private class Item {

        private T item;
        private Instant lastUpdated;

        public Item(T item) {
            this.setItem(item);
        }

        public T getItem() {
            return item;
        }

        public final void setItem(T item) {
            this.item = item;
            this.lastUpdated = Instant.now();
        }

        public Instant getLastUpdated() {
            return lastUpdated;
        }

    }
}
