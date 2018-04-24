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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Roodschild, Matias <mroodschild@gmail.com>
 */
public class PurelimTest {

    public PurelimTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of output method, of class Purelim.
     */
    @Test
    public void testOutput() {
        TransferFunction purelim = FunctionFactory.getFunction("purelim");
        double[] data ={-3, -1, 0, 1, 3.0};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        double[] salida = purelim.output(a).getDDRM().getData();
        double[] esperado = {-3, -1, 0, 1, 3};
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of derivative method, of class Purelim.
     */
    @Test
    public void testDerivative() {
        TransferFunction purelim = FunctionFactory.getFunction("purelim");
        double[] data ={-3, -1, 0, 1, 3.0};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        double[] salida = purelim.derivative(a).getDDRM().getData();
        double[] esperado = {1, 1, 1, 1, 1};
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of toString method, of class Purelim.
     */
    @Test
    public void testToString() {
        TransferFunction tansig = FunctionFactory.getFunction("purelim");
        String expResult = "purelim";
        String result = tansig.toString();
        assertEquals(expResult, result);
    }

}
