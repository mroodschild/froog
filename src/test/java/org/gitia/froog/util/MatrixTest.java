/*
 * Copyright 2019 Matías Roodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.util;

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
public class MatrixTest {

    public MatrixTest() {
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

    @Test
    public void testMean() {
        System.out.println("mean");
        double[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8.0};
        SimpleMatrix m = new SimpleMatrix(3, 3, true, data);
        double[] expResultHorizontal = {1, 4, 7};
        double[] expResultVertical = {3, 4, 5};
        SimpleMatrix resultVertical = Matrix.mean(m, 0);
        SimpleMatrix resultHorizontal = Matrix.mean(m, 1);
        resultHorizontal.print();
        resultVertical.print();
        assertArrayEquals(expResultVertical, resultVertical.getDDRM().getData(), 0.001);
        assertArrayEquals(expResultHorizontal, resultHorizontal.getDDRM().getData(), 0.001);
    }

}
