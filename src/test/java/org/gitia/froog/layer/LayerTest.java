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
package org.gitia.froog.layer;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.transferfunction.TransferFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class LayerTest {

    /**
     * Test of output method, of class Layer.
     */
    @Test
    public void testOutput_doubleArr() {
        System.out.println("output");
        double[] data = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6};
        double[] data1 = {0.1, 0.2};
        SimpleMatrix w = new SimpleMatrix(2, 3, true, data);
        SimpleMatrix b = new SimpleMatrix(2, 1, true, data1);
        double[] input = {0.2, 0.4, 0.6};
        Layer instance = new Layer(w, b, TransferFunction.LOGSIG);
        double[] expResult = {0.5938731029341, 0.6984652160025};
        double[] result = instance.output(input).getDDRM().getData();
        assertArrayEquals(expResult, result, 0.0000000000001);
    }

    /**
     * Test of output method, of class Layer.
     */
    @Test
    public void testOutput_SimpleMatrix() {
        System.out.println("output");
        double[] data = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6};
        double[] data1 = {0.1, 0.2};
        double[] data2 = {0.2, 0.4, 0.6};
        SimpleMatrix w = new SimpleMatrix(2, 3, true, data);
        SimpleMatrix b = new SimpleMatrix(2, 1, true, data1);
        SimpleMatrix input = new SimpleMatrix(3, 1, true, data2);
        Layer instance = new Layer(w, b, TransferFunction.LOGSIG);
        double[] expResult = {0.5938731029341, 0.6984652160025};
        double[] result = instance.output(input).getDDRM().getData();
        instance.output(input).print();
        assertArrayEquals(expResult, result, 0.0000000000001);
    }

    /**
     * Test of output method, of class Layer.
     */
    @Test
    public void testOutput_SimpleMatrixAll() {
        System.out.println("output");
        double[] data = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6};
        double[] data1 = {0.1, 0.2};
        double[] data2 = {0.2, 0.4, 0.6, 0.2, 0.4, 0.6};
        SimpleMatrix w = new SimpleMatrix(2, 3, true, data);
        SimpleMatrix b = new SimpleMatrix(2, 1, true, data1);
        SimpleMatrix input = new SimpleMatrix(3, 2, false, data2);
        Layer instance = new Layer(w, b, TransferFunction.LOGSIG);
        double[] expResult = {0.5938731029341, 0.5938731029341, 0.6984652160025, 0.6984652160025};
        double[] result = instance.output(input).getDDRM().getData();
        assertArrayEquals(expResult, result, 0.0000000000001);
    }

    /**
     * Test of outputN method, of class Layer.
     */
    @Test
    @Ignore
    public void testOutputN() {
        System.out.println("outputN");
        SimpleMatrix z = null;
        Layer instance = new Layer();
        SimpleMatrix expResult = null;
        SimpleMatrix result = instance.outputN(z);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of outputZ method, of class Layer.
     */
    @Test
    @Ignore
    public void testOutputZ() {
        System.out.println("outputZ");
        SimpleMatrix a = null;
        Layer instance = new Layer();
        SimpleMatrix expResult = null;
        SimpleMatrix result = instance.outputZ(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
