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
