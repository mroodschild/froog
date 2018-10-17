/*
 * Copyright 2018 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
