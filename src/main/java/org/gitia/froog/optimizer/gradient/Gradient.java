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

import java.util.List;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public interface Gradient {
    
    public static String STANDARD = "standard";
    public static String NOISE = "noise";
    public static String DROPOUT = "dropout";
    
    public void compute(Feedforward net, List<SimpleMatrix> Activations, List<SimpleMatrix> gradW, List<SimpleMatrix> gradB, SimpleMatrix X, SimpleMatrix Y);
    
}
