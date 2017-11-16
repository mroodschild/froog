/*
 * The MIT License
 *
 * Copyright 2017 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.gitia.froog.statistics;

import java.util.Calendar;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Clock {

    Calendar i = Calendar.getInstance();
    Calendar f = Calendar.getInstance();

    public Clock() {
    }

    public void start() {
        i = Calendar.getInstance();
    }

    public void stop() {
        f = Calendar.getInstance();
    }

    public double time() {
        return f.getTimeInMillis() - i.getTimeInMillis();
    }

    public void printTime() {
        System.out.printf("Time:\t%.5f\ts.\n", timeSec());
    }

    public void printTime(String message) {
        System.out.printf("%s\tTime:\t%.5f\ts.\n", message, timeSec());
    }

    /**
     * devuelve el tiempo en segundos
     *
     * @return
     */
    public double timeSec() {
        return time() / 1000.0;
    }

}
