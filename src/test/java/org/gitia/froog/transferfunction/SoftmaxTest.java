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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class SoftmaxTest {

    /**
     * Test of output method, of class Softmax.
     */
    @Test
    public void testOutput() {
        TransferFunction softmax = FunctionFactory.getFunction("softmax");
        double[] data = {-3, -1, 0, 1.0, 3};
        SimpleMatrix z = new SimpleMatrix(5, 1, true, data);
        double[] salida = softmax.output(z).getDDRM().getData();
        double[] esperado = {
            0.002055492,
            0.015188145,
            0.04128566,
            0.112226059,
            0.829244644};
        //softmax.output(z).print();
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of output method, of class Softmax.
     */
    @Test
    public void testOutput2() {
        TransferFunction softmax = new Softmax();
        double[] data = {1, 1,
            2, 3.0,
            3, 2};
        SimpleMatrix z = new SimpleMatrix(3, 2, true, data);
        double[] expResult = {
            0.090030573, 0.090030573,
            0.244728471, 0.665240956,
            0.665240956, 0.244728471};
        softmax.output(z).getDDRM().print();
        double[] result = softmax.output(z).getDDRM().getData();
        assertArrayEquals(expResult, result, 0.000000001);
    }

    /**
     * Test of derivative method, of class Softmax.
     */
    @Test
    public void testDerivative() {

    }

    /**
     * Test of toString method, of class Softmax.
     */
    @Test
    public void testToString() {
        TransferFunction tansig = FunctionFactory.getFunction("softmax");
        String expResult = "softmax";
        String result = tansig.toString();
        assertEquals(expResult, result);
    }

}
