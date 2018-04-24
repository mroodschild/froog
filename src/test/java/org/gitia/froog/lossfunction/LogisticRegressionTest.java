/*
 * The MIT License
 *
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
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
import org.gitia.froog.layer.Layer;
import org.gitia.froog.transferfunction.TransferFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class LogisticRegressionTest {

    public LogisticRegressionTest() {
    }

    /**
     * Test of cost method, of class Logistic.
     */
    @Ignore
    @Test
    public void testCost() {
        System.out.println("cost");
        SimpleMatrix Ycalc = null;
        SimpleMatrix Yobs = null;
        Logistic instance = new Logistic();
        double expResult = 0.0;
        double result = instance.cost(Ycalc, Yobs);
        //assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of costAll method, of class Logistic.
     */
    @Test
    public void testCostAll() {
        System.out.println("costAll");
        double[] data = {1, 0, 1.0};
        double[] data1 = {1.0, 2, -1, 3, 4, -3.2};
        double[] data2 = {1.0, 2};
        double[] data3 = {2.0};
        
        SimpleMatrix Y = new SimpleMatrix(1, 3, true, data);
        Logistic instance = new Logistic();
        SimpleMatrix X = new SimpleMatrix(2, 3, true, data1);
        SimpleMatrix W = new SimpleMatrix(1, 2, true, data2);
        SimpleMatrix b = new SimpleMatrix(1, 1, true, data3);
        Layer l = new Layer(W, b, TransferFunction.LOGSIG);
        double expResult = 5.801545319394553;
        double result = instance.costAll(l.output(X), Y);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of toString method, of class Logistic.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Logistic instance = new Logistic();
        String expResult = "logistic";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

}
