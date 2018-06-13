/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.ssehub.kernel_haven.pss_divergence_detector.divergences;

import net.ssehub.kernel_haven.code_model.CodeElement;
import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.pss_mapper.MappingElement;
import net.ssehub.kernel_haven.util.null_checks.NonNull;
import net.ssehub.kernel_haven.variability_model.VariabilityModel;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * This class defines a divergence denoting a {@link VariabilityVariable} to be defined in the {@link VariabilityModel},
 * but not used to control the presence or absence of a {@link SourceFile} or a {@link CodeElement}. Hence, this
 * variable is useless.
 * 
 * @author Christian Kröher
 *
 */
public class UnusedVariableDivergence extends Divergence {

    /**
     * Creates a {@link UnusedVariableDivergence} instance.
     * 
     * @param mappingElement the {@link MappingElement} containing the unused variable
     */
    public UnusedVariableDivergence(@NonNull MappingElement mappingElement) {
        // There cannot be any SourceFiles or CodeElements if it is an unused variable
        addInvolvedVariable(mappingElement.getVariable());
    }
    
    @Override
    public @NonNull String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public @NonNull String getProblemSpaceSymptom() {
        return toDescriptionString(getInvolvedVariablesString()) + " defined in variability model";
    }

    @Override
    public @NonNull String getSolutionSpaceSymptom() {
        return toDescriptionString(getInvolvedVariablesString()) + " not referenced by any build or code artifact";
    }
    
}
