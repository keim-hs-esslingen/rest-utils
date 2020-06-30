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

import java.util.function.Supplier;
import javax.validation.constraints.NotNull;

/**
 * Collection of generic methods for various purposes.
 *
 * @author ben
 */
public class RestUtils {

    private RestUtils() {
    }

    /**
     * Asserts that the given {@code obj} is not {@code null} by throwing an
     * exception from the given {@link java.util.function.Supplier} if that is
     * the case.
     *
     * @param <T>
     * @param <E>
     * @param obj
     * @param toThrowIfNull
     * @return
     * @throws E
     */
    @NotNull
    public static <T, E extends Exception> T assertNotNull(T obj, Supplier<E> toThrowIfNull) throws E {
        if (obj == null) {
            throw toThrowIfNull.get();
        }

        return obj;
    }

}
