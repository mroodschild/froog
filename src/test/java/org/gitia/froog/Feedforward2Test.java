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

import org.gitia.froog.layer.Dense;
import org.ejml.simple.SimpleMatrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Feedforward2Test {

    public Feedforward2Test() {
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
    public void testLayer() {
        Feedforward net = new Feedforward();
        net.addLayer(new Dense(4, 2, "tansig"));
        net.addLayer(new Dense(2, 2, "purelim"));

        assertEquals("Dimensiones Entrada", 8, net.layers().get(0).getW().getNumElements());
        assertEquals("Dimensiones Entrada", 4, net.layers().get(1).getW().getNumElements());
    }

    @Test
    public void testSalida() {
        double[] data = {0.1, 0.2, 0.3, 0.4};
        double[] data1 = {0.5, 0.1};
        double[] data2 = {0.5, 0.6, 0.7, 0.8};
        double[] data3 = {0.2, 0.4};
        SimpleMatrix w1 = new SimpleMatrix(2, 2, true, data);
        SimpleMatrix b1 = new SimpleMatrix(2, 1, true, data1);
        SimpleMatrix w2 = new SimpleMatrix(2, 2, true, data2);
        SimpleMatrix b2 = new SimpleMatrix(2, 1, true, data3);

        Feedforward net = new Feedforward();
        net.addLayer(new Dense(w1, b1, "tansig"));
        net.addLayer(new Dense(w2, b2, "purelim"));
        double[] entrada = {0.2, 0.6};
        double[] esperado = {0.7104, 1.0993};
        assertArrayEquals(esperado, net.output(entrada).getDDRM().getData(), 0.0001);
    }

    @Test
    public void testSalida2() {
        double[] data={0.1, 0.2, 0.3, 0.4};
        double[] data1={0.5, 0.1};
        double[] data2={0.1, 0.2, 0.3, 0.4, 0.5, 0.6};
        double[] data3={0.5, 0.1, 0.2};
        double[] data4={0.5, 0.6, 0.9, 0.7, 0.8, 1.0};
        double[] data5={0.2, 0.4};
        SimpleMatrix w1 = new SimpleMatrix(2, 2, true, data);
        SimpleMatrix b1 = new SimpleMatrix(2, 1, true, data1);
        SimpleMatrix w2 = new SimpleMatrix(3, 2, true, data2);
        SimpleMatrix b2 = new SimpleMatrix(3, 1, true, data3);
        SimpleMatrix w3 = new SimpleMatrix(2, 3, true, data4);
        SimpleMatrix b3 = new SimpleMatrix(2, 1, true, data5);

        Feedforward net = new Feedforward();
        net.addLayer(new Dense(w1, b1, "tansig"));
        net.addLayer(new Dense(w2, b2, "tansig"));
        net.addLayer(new Dense(w3, b3, "purelim"));

        double[] entrada = {0.2, 0.6};
        double[] esperado = {1.2686148090686, 1.7212904026294};
//        List<Layer> layers = net.getLayers();
//        for (int i = 0; i < layers.size(); i++) {
//            Dense l = layers.get(i);
//            System.out.println("Dense: " + i + " tipo: " + l.toString());
//            l.getW().print();
//        }
//        System.out.println("Salida:");
//        net.output(entrada).print("%.4f");
        assertArrayEquals(esperado, net.output(entrada).getDDRM().getData(), 0.000000000001);
    }

}
