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
package org.gitia.froog.r;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class rFeedforwardTest {

    public rFeedforwardTest() {
    }

    /**
     * Test of out method, of class rFeedforward.
     */
    @Test
    public void testOut() {
        System.out.println("out");
        double[] matrix = {0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        int row = 2;
        int col = 3;
        rFeedforward net = new rFeedforward();
        net.addLayer(2, 2, "tansig");
        net.addLayer(2, 1, "purelim");

        double[] expResult = net.out(matrix, 2, 3);
        for (int i = 0; i < expResult.length; i++) {
            double d = expResult[i];
            System.out.printf("%.2f\t", d);
        }

        System.out.println("");
    }

    @Test
    public void testOneHot() {
        System.out.println("org.gitia.froog.r.rFeedforwardTest.testOneHot()");
        rFeedforward instance = new rFeedforward();
        String[] words = {"hola", "mundo", "matias", "hola"};
        double[] resultados = instance.oneHot(words);

        System.out.println("Clases: "+ instance.getNumClasses());
        for (int i = 0; i < resultados.length; i++) {
            double resultado = resultados[i];
            System.out.println(i+": "+resultado);
        }
    }

}
