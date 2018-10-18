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
package org.gitia.froog.optimizer.gradient;

import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Dense;
import org.gitia.froog.optimizer.updaterule.Update;
import org.gitia.froog.optimizer.updaterule.UpdateRule;
import org.gitia.froog.transferfunction.TransferFunction;
import org.junit.Test;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class UpdateTest {

    public UpdateTest() {
    }

    /**
     * Test of compute method, of class Gradient.
     */
    @Test
    public void testCompute() {
        double[][] w1 = {
            {-0.00615039, 0.0169021},
            {-0.02311792, 0.03137121},
            {-0.0169217, -0.01752545},
            {0.00935436, -0.05018221}};
        SimpleMatrix W1 = new SimpleMatrix(w1);

        double[][] b1 = {{-8.97523455e-07}, {8.15562092e-06}, {6.04810633e-07}, {-2.54560700e-06}};
        SimpleMatrix B1 = new SimpleMatrix(b1);

        double[][] w2 = {{-0.0104319, -0.04019007, 0.01607211, 0.04440255}};
        SimpleMatrix W2 = new SimpleMatrix(w2);

        double[][] b2 = {{9.14954378e-05}};
        SimpleMatrix B2 = new SimpleMatrix(b2);

        double[][] gW1 = {
            {0.00023322, -0.00205423},
            {0.00082222, -0.00700776},
            {-0.00031831, 0.0028636},
            {-0.00092857, 0.00809933}};

        double[][] gB1 = {{1.05570087e-07}, {-3.81814487e-06}, {-1.90155145e-07}, {5.46467802e-07}};
        double[][] gW2 = {{-1.75740039e-05, 3.70231337e-03, -1.25683095e-03, -2.55715317e-03}};
        double[][] gB2 = {{-1.08923140e-05}};

        List<SimpleMatrix> gradW = new ArrayList<>();
        gradW.add(new SimpleMatrix(gW1));
        gradW.add(new SimpleMatrix(gW2));
        List<SimpleMatrix> gradB = new ArrayList<>();
        gradB.add(new SimpleMatrix(gB1));
        gradB.add(new SimpleMatrix(gB2));

        Feedforward net = new Feedforward();
        net.addLayer(new Dense(W1, B1, TransferFunction.LOGSIG));
        net.addLayer(new Dense(W2, B2, TransferFunction.LOGSIG));

        UpdateRule update = new Update();
        update.updateParameters(net, 1, 0, 1.2, gradW, gradB);

        for (Dense l : net.getLayers()) {
            l.getW().print("%.8f");
            l.getB().print("%.8f");
        }

    }

}
