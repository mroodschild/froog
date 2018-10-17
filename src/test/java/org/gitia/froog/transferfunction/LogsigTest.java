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
import org.junit.Ignore;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class LogsigTest {

    /**
     * Test of output method, of class Logsig.
     */
    @Test
    public void testOutput() {
        System.out.println("output");
        double data[] = {-0.25, -0.1, 0, 0.2, 0.5};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        Logsig instance = new Logsig();
        double[] esperado = {0.4378234991142,
            0.4750208125211, 0.5000000000000, 0.5498339973125,
            0.6224593312019};
        double[] salida = instance.output(a).getDDRM().getData();
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of derivative method, of class Logsig.
     */
    @Test
    public void testDerivative_SimpleMatrix() {
        System.out.println("derivative");
        double data[] = {-0.25, -0.1, 0, 0.2, 0.5};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        Logsig instance = new Logsig();
        double[] esperado = {-0.312500,
            -0.110000, 0.000000, 0.160000, 0.250000};
        double[] salida = instance.derivative(a).getDDRM().getData();
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of derivative method, of class Logsig.
     */
    @Test
    public void testDerivative_SimpleMatrix_double() {
        System.out.println("derivative");
        double data[] = {-0.25, -0.1, 0, 0.2, 0.5};
        SimpleMatrix a = new SimpleMatrix(1, 5, true, data);
        double b = 0.1;
        Logsig instance = new Logsig();
        double[] esperado = {-0.212500,
            -0.010000, 0.100000, 0.260000, 0.350000};
        double[] salida = instance.derivative(a, b).getDDRM().getData();
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of derivative method, of class Logsig.
     */
    @Ignore
    @Test
    public void testDerivative_SimpleMatrix_SimpleMatrix() {
        System.out.println("derivative");
        double yc[] = {-0.25, -0.1, 0, 0.2, 0.5};
        double yo[] = {-0.10, 0.05, 0.15, 0.35, 0.65};
        SimpleMatrix yCalc = new SimpleMatrix(1, 5, true, yc);
        SimpleMatrix yObs = new SimpleMatrix(1, 5, true, yo);
        Logsig instance = new Logsig();
        double[] esperado = {0.046875,
            0.0165, 0, -0.024, -0.0375};
        double[] salida = instance.derivative(yCalc, yObs).getDDRM().getData();
        assertArrayEquals(esperado, salida, 0.000000001);
    }

    /**
     * Test of toString method, of class Logsig.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Logsig instance = new Logsig();
        String expResult = TransferFunction.LOGSIG;
        String result = instance.toString();
        assertEquals(expResult, result);
    }

}
