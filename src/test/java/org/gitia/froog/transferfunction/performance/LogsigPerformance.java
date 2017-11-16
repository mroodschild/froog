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
package org.gitia.froog.transferfunction.performance;

import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.transferfunction.Logsig;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class LogsigPerformance {

    public static void main(String[] args) {
        int datos = 200000;
        int it = 1;
        SimpleMatrix a = SimpleMatrix.random(1, datos, 0, 1, new Random(1));
        //SimpleMatrix b = SimpleMatrix.random(1, 500000, 0, 1, new Random(1));
        Logsig l = new Logsig();
        Clock c = new Clock();

        c.start();
        for (int i = 0; i < it; i++) {
            l.output(a);
        }
        c.stop();
        c.printTime();

        System.out.println("");

        c.start();
        for (int i = 0; i < it; i++) {
           // l.output2(a);
        }
        c.stop();
        c.printTime();
        
        System.out.println("");
        
        c.start();
        for (int i = 0; i < it*datos; i++) {
            Math.pow(1, -2);
        }
        c.stop();
        c.printTime();
        
        System.out.println("");
        
        c.start();
        for (int i = 0; i < it*datos; i++) {
            double r = (5.1/2.3);
        }
        c.stop();
        c.printTime();
        
    }

}
