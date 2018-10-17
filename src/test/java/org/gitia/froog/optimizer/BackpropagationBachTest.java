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
package org.gitia.froog.optimizer;

import org.gitia.froog.Feedforward;
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
     * Test of train method, of class BackpropagationBach.
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
        instance.train(net, input, output);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

//    /**
//     * Test of computeGradient method, of class BackpropagationBach.
//     */
//    @Ignore
//    @Test
//    public void testCalcularGradientes() {
//        System.out.println("computeGradient");
//        Backpropagation instance = new Backpropagation();
//        double expResult = 0.0;
//        double result = instance.computeGradient();
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of updateParameters method, of class BackpropagationBach.
     */
    @Ignore
    @Test
    public void testActualizarParametros() {
//        System.out.println("actualizarParametros");
//        double m = 0.0;
//        Backpropagation instance = new Backpropagation();
//        instance.updateParameters(m);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of computeCost method, of class BackpropagationBach.
     */
//    @Ignore
//    @Test
//    public void testComputeCost_SimpleMatrix_SimpleMatrix() {
//        System.out.println("computeCost");
//        SimpleMatrix input = null;
//        SimpleMatrix Yobs = null;
//        Backpropagation instance = new Backpropagation();
//        double expResult = 0.0;
//        double result = instance.cost(input, Yobs);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of computeCost method, of class BackpropagationBach.
//     */
//    @Ignore
//    @Test
//    public void testComputeCost_doubleArr_doubleArr() {
//        System.out.println("computeCost");
//        double[] input = null;
//        double[] output = null;
//        Backpropagation instance = new Backpropagation();
//        double expResult = 0.0;
//        double result = instance.cost(input, output);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of computeCost method, of class BackpropagationBach.
     */
//    @Test
//    @Ignore
//    public void testDeltasZero() {
//        Backpropagation bach = new Backpropagation();
//
//        List<SimpleMatrix> deltaW = new ArrayList<>();
//        List<SimpleMatrix> deltaB = new ArrayList<>();
//
//        SimpleMatrix a = new SimpleMatrix(2, 2, true, 1.0, 1.1, 1.2, 1.3);
//        SimpleMatrix b = new SimpleMatrix(2, 2, true, 2.0, 2.1, 2.2, 2.3);
//
//        deltaW.add(a);
//        deltaW.add(new SimpleMatrix(a));
//        deltaB.add(b);
//        deltaB.add(new SimpleMatrix(b));
//
//        bach.deltasW = deltaW;
//        bach.deltasB = deltaB;
//
//        bach.deltasZero();
//
//        double[] expected = {0.0, 0.0, 0.0, 0.0};
//        assertArrayEquals(expected, bach.deltasB.get(0).getDDRM().getData(), 0);
//        assertArrayEquals(expected, bach.deltasB.get(1).getDDRM().getData(), 0);
//        assertArrayEquals(expected, bach.deltasW.get(0).getDDRM().getData(), 0);
//        assertArrayEquals(expected, bach.deltasW.get(1).getDDRM().getData(), 0);
//    }

}
