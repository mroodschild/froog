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
package org.gitia.froog.transferfunction;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class LogsigTest {

    /**
     * Test of output method, of class Logsig.
     */
    @Test
    public void testOutput() {
        System.out.println("output");
        double data[] = {-0.25, -0.1, 0, 0.2, 0.5};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        Logsig instance = new Logsig();
        double[] esperado = {0.4378234991142,
            0.4750208125211, 0.5000000000000, 0.5498339973125,
            0.6224593312019};
        double[] salida = instance.output(a).getDDRM().getData();
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of derivative method, of class Logsig.
     */
    @Test
    public void testDerivative_SimpleMatrix() {
        System.out.println("derivative");
        double data[] = {-0.25, -0.1, 0, 0.2, 0.5};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        Logsig instance = new Logsig();
        double[] esperado = {-0.312500,
            -0.110000, 0.000000, 0.160000, 0.250000};
        double[] salida = instance.derivative(a).getDDRM().getData();
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of derivative method, of class Logsig.
     */
    @Test
    public void testDerivative_SimpleMatrix_double() {
        System.out.println("derivative");
        double data[] = {-0.25, -0.1, 0, 0.2, 0.5};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        double b = 0.1;
        Logsig instance = new Logsig();
        double[] esperado = {-0.212500,
            -0.010000, 0.100000, 0.260000, 0.350000};
        double[] salida = instance.derivative(a, b).getDDRM().getData();
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of derivative method, of class Logsig.
     */
    @Ignore
    @Test
    public void testDerivative_SimpleMatrix_SimpleMatrix() {
        System.out.println("derivative");
        double yc[] = {-0.25, -0.1, 0, 0.2, 0.5};
        double yo[] = {-0.10, 0.05, 0.15, 0.35, 0.65};
        SimpleMatrix yCalc = new SimpleMatrix(1, 5, true, yc);
        SimpleMatrix yObs = new SimpleMatrix(1, 5, true, yo);
        Logsig instance = new Logsig();
        double[] esperado = {0.046875,
            0.0165, 0, -0.024, -0.0375};
        double[] salida = instance.derivative(yCalc, yObs).getDDRM().getData();
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of toString method, of class Logsig.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Logsig instance = new Logsig();
        String expResult = TransferFunction.LOGSIG;
        String result = instance.toString();
        assertEquals(expResult, result);
    }

}
