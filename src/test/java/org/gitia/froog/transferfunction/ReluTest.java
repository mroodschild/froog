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

import org.gitia.froog.transferfunction.Relu;
import org.ejml.simple.SimpleMatrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class ReluTest {

    public ReluTest() {
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
     * Test of output method, of class Relu.
     */
    @Test
    public void testOutput() {
        System.out.println("output");
        SimpleMatrix z = new SimpleMatrix(1, 5, true, -1, 1, 0, .1, -.1);
        Relu instance = new Relu();
        double[] expResult = {0, 1, 0, 0.1, 0};
        SimpleMatrix result = instance.output(z);
        assertArrayEquals(expResult, result.getMatrix().getData(), 0);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of derivative method, of class Relu.
     */
    @Test
    public void testDerivative() {
        System.out.println("output");
        SimpleMatrix z = new SimpleMatrix(1, 5, true, -1, 1, 0, .1, -.1);
        Relu instance = new Relu();
        double[] expResult = {0, 1, 0, 1, 0};
        SimpleMatrix result = instance.derivative(z);
        assertArrayEquals(expResult, result.getMatrix().getData(), 0);
    }

    /**
     * Test of toString method, of class Relu.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Relu instance = new Relu();
        String expResult = "relu";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

}
