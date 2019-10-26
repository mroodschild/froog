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
public class Momentum implements Accelerate {

    private double beta;

    List<SimpleMatrix> vW = new ArrayList<>();
    List<SimpleMatrix> vB = new ArrayList<>();

    public Momentum() {
    }

    public Momentum(double beta) {
        this.beta = beta;
    }

    @Override
    public void grad(List<SimpleMatrix> dW, List<SimpleMatrix> db, double learningRate) {
        if (vW.isEmpty()) {
            init(vW, dW);
            init(vB, db);
        }
        
        for (int i = 0; i < dW.size(); i++) {
            SimpleMatrix vdW = vW.get(i);
            SimpleMatrix vdb = vB.get(i);
            
            vdW = vdW.scale(beta).plus(dW.get(i).scale(1-beta));
            vdb = vdb.scale(beta).plus(db.get(i).scale(1-beta));
            
            vW.set(i, vdW);
            vB.set(i, vdb);
            
            dW.set(i, vdW.scale(learningRate));
            db.set(i, vdb.scale(learningRate));
        }
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getBeta() {
        return beta;
    }
    
    /**
     * 
     * @param v
     * @param grad 
     */
    private void init(List<SimpleMatrix> v, List<SimpleMatrix> grad){
        for (int i = 0; i < grad.size(); i++) {
            SimpleMatrix vAux = grad.get(i).copy();
            vAux.zero();
            v.add(vAux);
        }
    }

}