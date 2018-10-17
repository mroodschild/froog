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
