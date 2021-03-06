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
public interface WeightInit {

    /**
     * <b>Pitfall</b>: all zero initialization. Lets start with what we should
     * not do. Note that we do not know what the final value of every weight
     * should be in the trained network, but with proper data normalization it
     * is reasonable to assume that approximately half of the weights will be
     * positive and half of them will be negative. A reasonable-sounding idea
     * then might be to set all the initial weights to zero, which we expect to
     * be the “best guess” in expectation. This turns out to be a mistake,
     * because if every neuron in the network computes the same output, then
     * they will also all compute the same gradients during backpropagation and
     * undergo the exact same parameter updates. In other words, there is no
     * source of asymmetry between neurons if their weights are initialized to
     * be the same.
     */
    public static String PITFALL = "pitfall";
    
     /**
     * Inicializamos una matriz de pesos con las filas y columnas dadas<br>
     * r = sqrt(6)/(sqrt(s + n + 1)) <br>
     * val = 2 * rand * r - r
     */
    public static String DEFAULT = "default";
    
    /**
     *
     * for ReLU layers
     * random matrix * sqrt(2/ inputs.size)
     */
    public static String HE = "he";
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
     */
    public static String SMALLRANDOM = "smallrandom";
    public static String POSITIVERANDOM = "positiverandom";

    public void init(SimpleMatrix matrix);

    public void setRandom(Random random);

}
