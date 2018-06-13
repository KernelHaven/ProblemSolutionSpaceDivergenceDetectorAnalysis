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
 * This class defines a divergence denoting a variable to be used to control the presence or absence of
 * {@link SourceFile}s or {@link CodeElement}s, but not to be defined in the {@link VariabilityModel}. Hence, these
 * source files or code elements cannot be configured.
 * 
 * @author Christian Kröher
 *
 */
public class UndefinedVariableDivergence extends Divergence {
    
    /**
     * The name of the undefined variable. As this variable is undefined, there is no further information about it
     * except for the name used in the source files or code elements.
     */
    private @NonNull String undefinedVariableName;

    /**
     * Creates a {@link UndefinedVariableDivergence} instance.
     * 
     * @param mappingElement the {@link MappingElement} containing the undefined variable
     */
    public UndefinedVariableDivergence(@NonNull MappingElement mappingElement) {
        // There cannot be a VariabilityVariable if it is an undefined variable
        undefinedVariableName = mappingElement.getVariableName();
        /*
         * TODO although being not defined, we pass the variable as such to the corrector in this way. However, we only
         * know the name.
         */
        addInvolvedVariable(new VariabilityVariable(undefinedVariableName, ""));
        addInvolvedSourceFiles(mappingElement.getBuildMapping());
        addInvolvedCodeElements(mappingElement.getCodeMapping());
    }

    @Override
    public @NonNull String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public @NonNull String getProblemSpaceSymptom() {
        return toDescriptionString(undefinedVariableName) + " not defined in variability model";
    }

    @Override
    public @NonNull String getSolutionSpaceSymptom() {
        StringBuilder symptomBuilder = new StringBuilder();
        String involvedSourceFilesString = toDescriptionString(getInvolvedSourceFilesString());
        String involvedCodeElementsString = toDescriptionString(getInvolvedCodeElementsString());
        symptomBuilder.append(toDescriptionString(undefinedVariableName) + " used to constrain presence of ");
        if (!involvedSourceFilesString.isEmpty()) {
            symptomBuilder.append("file(s) " + involvedSourceFilesString);
        }
        if (!involvedCodeElementsString.isEmpty()) {
            if (!involvedSourceFilesString.isEmpty()) {
                symptomBuilder.append(" as well as ");
            }
            symptomBuilder.append("code element(s) " + involvedCodeElementsString);
        }
        return symptomBuilder.toString();
    }

}
