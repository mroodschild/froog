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
package org.gitia.froog.optimizer.accelerate;

import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Adam implements Accelerate {

    private double beta1;
    private double beta2;
    private double epsilon;
    private double t;

    List<SimpleMatrix> vW = new ArrayList<>();
    List<SimpleMatrix> vB = new ArrayList<>();
    List<SimpleMatrix> sW = new ArrayList<>();
    List<SimpleMatrix> sB = new ArrayList<>();

    public Adam() {
    }

    public Adam(double beta1, double beta2, double epsilon, double t) {
        this.beta1 = beta1;
        this.beta2 = beta2;
        this.epsilon = epsilon;
        this.t = t;
    }

    @Override
    public void grad(List<SimpleMatrix> dW, List<SimpleMatrix> db, double learningRate) {
        initWeigth(dW, db);
        double beta1_t = Math.pow(beta1, t);
        double beta2_t = Math.pow(beta2, t);
        //System.out.println("vw: " + vW.size() + " vb: " + vB.size() + "sw: " + sW.size() + "sb: " + sB.size());
        for (int i = 0; i < dW.size(); i++) {
            SimpleMatrix vdW = vW.get(i).scale(beta1).plus(dW.get(i).scale(1 - beta1));
            SimpleMatrix vdb = vB.get(i).scale(beta1).plus(db.get(i).scale(1 - beta1));

            vW.set(i, vdW);
            vB.set(i, vdb);

            SimpleMatrix vW_corrected = vdW.divide(1 - beta1_t);
            SimpleMatrix vB_corrected = vdb.divide(1 - beta1_t);

            SimpleMatrix sdW = sW.get(i).scale(beta2).plus(dW.get(i).elementPower(2).scale(1 - beta2));
            SimpleMatrix sdb = sB.get(i).scale(beta2).plus(db.get(i).elementPower(2).scale(1 - beta2));

            sW.set(i, sdW);
            sB.set(i, sdb);

            SimpleMatrix sW_corrected = sdW.divide(1 - beta2_t);
            SimpleMatrix sB_corrected = sdb.divide(1 - beta2_t);
            
            dW.set(i, vW_corrected.elementDiv(sW_corrected.elementPower(0.5).plus(epsilon)).scale(learningRate));
            db.set(i, vB_corrected.elementDiv(sB_corrected.elementPower(0.5).plus(epsilon)).scale(learningRate));
        }

    }

    private void initWeigth(List<SimpleMatrix> dW, List<SimpleMatrix> db) {
        if (vW.isEmpty()) {
            init(vW, dW);
            init(sW, dW);
            init(vB, db);
            init(sB, db);
        }
    }

    /**
     *
     * @param v
     * @param grad
     */
    private void init(List<SimpleMatrix> v, List<SimpleMatrix> grad) {
        for (int i = 0; i < grad.size(); i++) {
            SimpleMatrix vAux = grad.get(i).copy();
            vAux.zero();
            v.add(vAux);
        }
    }

}
