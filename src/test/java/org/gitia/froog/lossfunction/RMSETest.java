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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class RMSETest {

    public RMSETest() {
    }

    /**
     * Test of cost method, of class RMSE.
     */
    @Test
    public void testCost() {
        System.out.println("cost");
        double[] data = {0, 0, 1.0};
        double[] data1 = {0.1, 0.1, 0.8};
        SimpleMatrix Yobs = new SimpleMatrix(3, 1, true, data);
        SimpleMatrix Ycalc = new SimpleMatrix(3, 1, true, data1);
        //Ycalc.print();
        //Yobs.print();
        RMSE instance = new RMSE();
        double expResult = 0.34996355115805830;
        double result = instance.cost(Ycalc, Yobs);
//        System.out.println("cost: " + result);
//        System.out.println("costAll: " + instance.costAll(Ycalc, Yobs));
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of costAll method, of class RMSE.
     */
    @Test
    public void testCostAll() {
        System.out.println("costAll");
        double[] data = {
            0.1, 0.3, 0.1, 0.9,
            0.1, 0.5, 0.1, 0.05,
            0.8, 0.1, 0.20, 0.05};
        SimpleMatrix Ycalc = new SimpleMatrix(3, 4, true, data);
        double[] data1 = {
            0, 0, 0, 1,
            0, 1, 0, 0,
            1.0, 0, 1, 0};
        SimpleMatrix Yobs = new SimpleMatrix(3, 4, true, data1);
//        Ycalc.print();
//        Yobs.print();
        RMSE instance = new RMSE();
        double expResult = 0.360838144602255;
        double result = instance.costAll(Ycalc, Yobs);
//        System.out.println("result: " + result);
        assertEquals(expResult, result, 0.0);
    }

}
