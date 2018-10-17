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
package org.gitia.froog.optimizer.conjugategradient.beta;

import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class PolakRebiere implements BetaRule {

    /**
     * β<sub>k</sub> = (g<sub>k</sub><sup>t</sup> . y<sub>k-1</sub>) /
     * ||g<sub>k-1</sub>||²
     *
     * @param g
     * @param gPrev
     * @return
     */
    @Override
    public double compute(SimpleMatrix g, SimpleMatrix gPrev) {
        SimpleMatrix y_prev = g.minus(gPrev);
        //NormOps_DDRM.normP2(gPrev.getDDRM());
        //return g.minus(gPrev).transpose().mult(g).get(0) / gPrev.transpose().mult(gPrev).get(0);
        //return g.transpose().mult(y_prev).get(0) / gPrev.transpose().mult(gPrev).get(0);
        return g.transpose().mult(y_prev).get(0) / NormOps_DDRM.normP2(gPrev.getDDRM());
    }

}
