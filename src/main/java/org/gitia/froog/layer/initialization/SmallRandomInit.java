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
package org.gitia.froog.layer.initialization;

import java.util.Random;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class SmallRandomInit implements WeightInit {

    Random random = new Random();

    /**
     * <b>Small random numbers</b>: we still want the weights to be very close
     * to zero, but as we have argued above, not identically zero. As a
     * solution, it is common to initialize the weights of the neurons to small
     * numbers and refer to doing so as symmetry breaking. The idea is that the
     * neurons are all random and unique in the beginning, so they will compute
     * distinct updates and integrate themselves as diverse parts of the full
     * network. The implementation for one weight matrix might look like W =
     * 0.01 * random.nextGaussian(), where random samples from a zero mean, unit
     * standard deviation gaussian. With this formulation, every neuron’s weight
     * vector is initialized as a random vector sampled from a multi-dimensional
     * gaussian, so the neurons point in random direction in the input space. It
     * is also possible to use small numbers drawn from a uniform distribution,
     * but this seems to have relatively little impact on the final performance
     * in practice.
     * <br><br>
     * <b>Warning</b>: It’s not necessarily the case that smaller numbers will
     * work strictly better. For example, a Neural Network layer that has very
     * small weights will during backpropagation compute very small gradients on
     * its data (since this gradient is proportional to the value of the
     * weights). This could greatly diminish the “gradient signal” flowing
     * backward through a network, and could become a concern for deep networks.
     * <br><br>
     *
     * see:
     * <url>http://cs231n.github.io/neural-networks-2/#weight-initialization</url>
     *
     * @param matrix
     */
    @Override
    public void init(SimpleMatrix matrix) {
        for (int i = 0; i < matrix.getNumElements(); i++) {
            matrix.set(i, 0.01 * random.nextGaussian());
        }
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }
    
    @Override
    public String toString() {
        return "smallrandom";
    }

}
