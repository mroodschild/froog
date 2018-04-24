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

/**
 *
 * @author Roodschild, Matias <mroodschild@gmail.com>
 */
public class TansigTest {

    /**
     * Test of output method, of class Tansig. -1 + 2 / (1 + e^(-2 . x))
     */
    @Test
    public void testOutput() {
        TransferFunction tansig = FunctionFactory.getFunction("tansig");
        double[] data = {-0.25, -0.1, 0, 0.2, 0.5};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        double[] salida = tansig.output(a).getDDRM().getData();
        double[] esperado = {-0.2449186624037, -0.0996679946250,
            0.0000000000000, 0.1973753202249, 0.4621171572600};
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of derivative method, of class Tansig.
     */
    @Test
    public void testDerivative() {
        TransferFunction tansig = FunctionFactory.getFunction("tansig");
        double[] data = {-0.25, -0.1, 0, 0.2, 0.5};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        double[] salida = tansig.derivative(a).getDDRM().getData();
        double[] esperado = {0.9375, 0.99, 1, 0.96, 0.75};
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of derivative method, of class Tansig.
     */
    @Test
    public void testDerivativeParam() {
        TransferFunction tansig = FunctionFactory.getFunction("tansig");
        double[] data = {-0.25, -0.1, 0, 0.2, 0.5};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        double[] salida = tansig.derivative(a, 0.1).getDDRM().getData();
        double[] esperado = {0.94375000, 0.99100000, 1.00000000, 0.96400000, 0.77500000};
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    @Test
    public void testTanh() {
        double[][] z1 = {{1.7386459, 1.74687437, 1.74830797},
        {-0.81350569, -0.73394355, -0.78767559},
        {0.29893918, 0.32272601, 0.34788465},
        {-0.2278403, -0.2632236, -0.22336567}};
        SimpleMatrix Z1 = new SimpleMatrix(z1);
        Tansig tansig = new Tansig();
//        System.out.println("tanh");
//        tansig.output(Z1).print("%.8f");
    }

    /**
     * Test of toString method, of class Tansig.
     */
    @Test
    public void testToString() {
        TransferFunction tansig = FunctionFactory.getFunction("tansig");
        String expResult = "tansig";
        String result = tansig.toString();
        assertEquals(expResult, result);
    }

}
