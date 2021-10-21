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
        double[] data ={-1, 1.0, 0, .1, -.1};
        SimpleMatrix z = new SimpleMatrix(1, 5, true, data);
        Relu instance = new Relu();
        double[] expResult = {0, 1, 0, 0.1, 0};
        SimpleMatrix result = instance.output(z);
        assertArrayEquals(expResult, result.getDDRM().getData(), 0);
        // review the generated test code and remove the default call to fail.
    }

    /**
     * Test of derivative method, of class Relu.
     */
    @Test
    public void testDerivative() {
        System.out.println("output");
        double[] data = {-1, 1.0, 0, .1, -.1};
        SimpleMatrix z = new SimpleMatrix(1, 5, true, data);
        Relu instance = new Relu();
        double[] expResult = {0, 1, 1, 1, 0};
        SimpleMatrix result = instance.derivative(z);
        assertArrayEquals(expResult, result.getDDRM().getData(), 0);
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
        // review the generated test code and remove the default call to fail.
    }

}
