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
package org.gitia.froog.optimizer;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.optimizer.conjugategradient.beta.BetaRule;
import org.gitia.froog.optimizer.conjugategradient.beta.PolakRebiere;

public class CG extends TrainingAlgorithm {

    double rho = 0.01; // usado en la condición del paso
    double beta = 0.1;
    double f; // error de la función J
    SimpleMatrix pPrev; //= new SimpleMatrix();
    SimpleMatrix gPrev;// = new SimpleMatrix();
    double pBeta;
    BetaRule betaRule = new PolakRebiere();
    SimpleMatrix p;// = new SimpleMatrix();
    SimpleMatrix g;
    List<SimpleMatrix> Ak = new ArrayList<>();
    int L;//Number of last Dense (size - 1)
    
    public CG() {
    }
    
    //Training Algorithm
    /**
     *
     * @param net neural network to train
     * @param input every row is a feature and every column is a register
     * @param output every row is a feature and every column is a register
     */
    public void train(Feedforward net, SimpleMatrix input, SimpleMatrix output) {
        //Clock ck = new Clock();
        this.net = net;
        this.input = new SimpleMatrix(input);
        this.output = new SimpleMatrix(output);
        init();
        Ak = net.activations(input);
        L = Ak.size()-1;
        
        double costOverall = lossFunction.costAll(Ak.get(L), output);//modificacion
        this.cost.add(costOverall);
        gradient.compute(net, Ak, gradW, gradB, input, output);
        f = costOverall;//asignamos el f
        g = getGradients();//asignamos el g
        pPrev = g.negative();
        gPrev = g.copy();
        pBeta = 0;
        for (int i = 0; i < this.epoch; i++) {
            Clock ckIt = new Clock();
            ckIt.start();
            SimpleMatrix x = net.getParameters();
            //pbeta = 0;
            if (i > 0) {
                gradient.compute(net, Ak, gradW, gradB, input, output);
                g = getGradients();//asignamos el g
                pBeta = betaRule.compute(g, gPrev);
            }

            //ck.start();
            p = pPrev.scale(pBeta).minus(g);//-g+pbeta*ppr

            double norm = NormOps_DDRM.normPInf(g.getDDRM());
            if (norm < 1e-6) {//     % Condición de mínimo
                break;
            }
            double alpha = 1;
            SimpleMatrix xnew;
            double fxnew = 0;
            //double k_it = 0;
            for (int k = 0; k < 10; k++) {
                //k_it = k;
                xnew = x.plus(p.scale(alpha).transpose());
                net.setParameters(xnew);
                Ak = net.activations(input);
                fxnew = lossFunction.costAll(Ak.get(L), output);//modificacion
                //fxnew = loss(Ak.get(Ak.size() - 1), output);
                if (fxnew <= f + alpha * rho * g.transpose().mult(p).get(0)) {
                    break;
                } else {
                    alpha = alpha * beta;
                }
            }
            f = fxnew;

            pPrev = p;
            gPrev = g;
            ckIt.stop();
            //System.out.println("It: \t" + i + "\tk_it:\t" + k_it + "\tMSE:\t" + f + "\tnorm:\t" + norm + "\ttime:\t" + ckIt.timeSec() + "\ts");
            System.out.println("It: \t" + i + "\ttrain:\t" + f + "\ttime:\t" + ckIt.timeSec() + "\ts");
        }
    }

    @Override
    public SimpleMatrix getGradients() {
        if (net.layers().isEmpty()) {
            System.err.println("Inicialice los gradientes primero");
            return null;
        }
        double[] aux = new double[0];
        for (int i = 0; i < gradW.size(); i++) {
            aux = ArrayUtils.addAll(aux, gradW.get(i).getDDRM().getData());
        }
        for (int i = 0; i < gradB.size(); i++) {
            aux = ArrayUtils.addAll(aux, gradB.get(i).getDDRM().getData());
        }
        return new SimpleMatrix(aux.length, 1, true, aux);
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public void setBetaRule(BetaRule betaRule) {
        this.betaRule = betaRule;
    }
}