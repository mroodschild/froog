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
package org.gitia.froog.lossfunction;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.layer.Dense;
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
        // SimpleMatrix Ycalc = null;
        // SimpleMatrix Yobs = null;
        // Logistic instance = new Logistic();
        // double expResult = 0.0;
        // double result = instance.cost(Ycalc, Yobs);
        //assertEquals(expResult, result, 0.0);
        // review the generated test code and remove the default call to fail.
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
        Dense l = new Dense(W, b, TransferFunction.LOGSIG);
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
