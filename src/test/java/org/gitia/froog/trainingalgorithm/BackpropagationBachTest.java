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
package org.gitia.froog.trainingalgorithm;

import org.gitia.froog.trainingalgorithm.Backpropagation;
import org.gitia.froog.Feedforward;
import org.ejml.simple.SimpleMatrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Roodschild, Matias <mroodschild@gmail.com>
 */
public class BackpropagationBachTest {

    public BackpropagationBachTest() {
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
     * Test of entrenar method, of class BackpropagationBach.
     */
    @Ignore
    @Test
    public void testEntrenar() {
        System.out.println("entrenar");
        Feedforward net = null;
        double[][] input = null;
        double[][] output = null;
        int iteraciones = 0;
        Backpropagation instance = new Backpropagation();
        instance.setEpoch(iteraciones);
        instance.entrenar(net, input, output);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

//    /**
//     * Test of calcularGradientes method, of class BackpropagationBach.
//     */
//    @Ignore
//    @Test
//    public void testCalcularGradientes() {
//        System.out.println("calcularGradientes");
//        Backpropagation instance = new Backpropagation();
//        double expResult = 0.0;
//        double result = instance.calcularGradientes();
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of computeCost method, of class BackpropagationBach.
     */
    @Ignore
    @Test
    public void testComputeCost_SimpleMatrix_SimpleMatrix() {
        System.out.println("computeCost");
        SimpleMatrix input = null;
        SimpleMatrix Yobs = null;
        Backpropagation instance = new Backpropagation();
        double expResult = 0.0;
        double result = instance.cost(input, Yobs);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of computeCost method, of class BackpropagationBach.
     */
    @Ignore
    @Test
    public void testComputeCost_doubleArr_doubleArr() {
        System.out.println("computeCost");
        double[] input = null;
        double[] output = null;
        Backpropagation instance = new Backpropagation();
        double expResult = 0.0;
        double result = instance.cost(input, output);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
