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
package org.gitia.froog.lossfunction;

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
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class CrossEntropyLossTest {

  public CrossEntropyLossTest() {
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
     * Test of cost method, of class CrossEntropyLoss.
     */
    @Test
    public void testCost() {
        System.out.println("org.gitia.neuralnetwork.lossfunction.CrossEntropyLossTest.testCost()");
        double[] data = {0.1, 0.10, 0.8};
        double[] data1 = {0, 0, 1.0};
        SimpleMatrix Ycalc = new SimpleMatrix(1, 3, true, data);
        //Ycalc.print();

        SimpleMatrix Yobs = new SimpleMatrix(1, 3, true, data1);
        //Yobs.print();
        CrossEntropyLoss instance = new CrossEntropyLoss();
        double expResult = 0.22314355131421;
        double result = instance.cost(Ycalc, Yobs);
        System.out.println("result: " + result + "\t Expected: " + expResult);
        assertEquals(expResult, result, 0.00000000000001);
    }

    /**
     * Test of costAll method, of class CrossEntropyLoss.
     */
    @Ignore
    @Test
    public void testCostAll() {
        System.out.println("org.gitia.neuralnetwork.lossfunction.CrossEntropyLossTest.testCostAll()");
        double[] data = {0.1, 0.3, 0.1, 0.9,
            0.1, 0.5, 0.1, 0.05,
            0.8, 0.1, 0.20, 0.05};
        double[] data1 = {0, 0, 0, 1,
            0, 1, 0, 0,
            1.0, 0, 1, 0};
        SimpleMatrix Ycalc = new SimpleMatrix(3, 4, true, data);
        SimpleMatrix Yobs = new SimpleMatrix(3, 4, true, data1);
//        Ycalc.print();
//        Yobs.print();
        CrossEntropyLoss instance = new CrossEntropyLoss();
        double expResult = 2.63108915996608;
        double result = instance.costAll(Ycalc.transpose(), Yobs.transpose());
        System.out.println("result: " + result + "\t Expected: " + expResult);
        assertEquals(expResult, result, 0.00000000000001);
    }

}
