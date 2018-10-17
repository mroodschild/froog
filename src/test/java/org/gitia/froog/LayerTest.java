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
package org.gitia.froog;

import org.gitia.froog.layer.Layer;
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
public class LayerTest {

    public LayerTest() {
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
     * Test of outputZ method, of class Layer.
     */
    @Test
    public void testOutputZ() {
        double[] data = {0.1, 0.2, 0.3, 0.4};
        double[] data1 = {0.5, 0.1};
        SimpleMatrix w1 = new SimpleMatrix(2, 2, true, data);
        SimpleMatrix b1 = new SimpleMatrix(2, 1, true, data1);

        Layer layer = new Layer(w1, b1, "tansig");

        double[] data2 = {0.2, 0.6};
        SimpleMatrix entrada = new SimpleMatrix(2, 1, true, data2);
        double[] esperado = {0.64, 0.4};
        assertArrayEquals(esperado, layer.outputZ(entrada).getDDRM().getData(), 0.0001);
    }

    /**
     * Test of output method, of class Layer.
     */
    @Test
    public void testOutput_SimpleMatrix() {
        double[] data = {0.1, 0.2, 0.3, 0.4};
        double[] data1 = {0.5, 0.1};
        double[] data2 = {0.2, 0.6};
        SimpleMatrix w1 = new SimpleMatrix(2, 2, true, data);
        SimpleMatrix b1 = new SimpleMatrix(2, 1, true, data1);
        SimpleMatrix a = new SimpleMatrix(2, 1, true, data2);

        Layer instance = new Layer(w1, b1, "tansig");

        double[] esperado = {0.5648995528462, 0.3799489622552};
        assertArrayEquals(esperado, instance.output(a).getDDRM().getData(), 0.0000001);
    }

    /**
     * Test of output method, of class Layer.
     */
    @Test
    public void testOutput_doubleArr() {
        double[] data = {0.1, 0.2, 0.3, 0.4};
        double[] data1 = {0.5, 0.1};
        SimpleMatrix w1 = new SimpleMatrix(2, 2, true, data);
        SimpleMatrix b1 = new SimpleMatrix(2, 1, true, data1);
        double[] a = {0.2, 0.6};
        
        Layer instance = new Layer(w1, b1, "tansig");

        double[] esperado = {0.5648995528462, 0.3799489622552};
        assertArrayEquals(esperado, instance.output(a).getDDRM().getData(), 0.0000001);
    }

}
