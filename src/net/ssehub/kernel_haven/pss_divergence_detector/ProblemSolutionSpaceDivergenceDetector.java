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
package net.ssehub.kernel_haven.pss_divergence_detector;

import java.util.ArrayList;
import java.util.List;

import net.ssehub.kernel_haven.analysis.AnalysisComponent;
import net.ssehub.kernel_haven.config.Configuration;
import net.ssehub.kernel_haven.pss_divergence_detector.divergences.Divergence;
import net.ssehub.kernel_haven.pss_divergence_detector.divergences.UndefinedVariableDivergence;
import net.ssehub.kernel_haven.pss_divergence_detector.divergences.UnusedVariableDivergence;
import net.ssehub.kernel_haven.pss_mapper.MappingElement;
import net.ssehub.kernel_haven.pss_mapper.ProblemSolutionSpaceMapping;
import net.ssehub.kernel_haven.util.null_checks.NonNull;

/**
 * This class detects unintended {@link Divergence}s between problem and solution space artifacts based on a given (set
 * of) {@link MappingElement}s.
 * 
 * @author Christian Kröher
 *
 */
public class ProblemSolutionSpaceDivergenceDetector extends AnalysisComponent<Divergence> {
    
    /**
     * The {@link AnalysisComponent} providing the set of {@link MappingElement}s, which represent the
     * {@link ProblemSolutionSpaceMapping}.
     */
    private @NonNull AnalysisComponent<MappingElement> pssMapper;
    
    /**
     * The list of {@link MappingElement}s received from the {@link #pssMapper}.
     */
    private @NonNull List<MappingElement> mappingElements;
    
    /**
     * The list of detected {@link Divergence}s between problem and solution space artifacts.
     */
    private @NonNull List<Divergence> divergences;

    /**
     * Creates a {@link ProblemSolutionSpaceDivergenceDetector} instance.
     * 
     * @param config the global {@link Configuration} 
     * @param pssMapper the {@link AnalysisComponent} providing the {@link MappingElement}s
     */
    public ProblemSolutionSpaceDivergenceDetector(@NonNull Configuration config,
            @NonNull AnalysisComponent<MappingElement> pssMapper) {
        super(config);
        this.pssMapper = pssMapper;
        mappingElements = new ArrayList<MappingElement>();
        divergences = new ArrayList<Divergence>();
    }

    @Override
    protected void execute() {
        if (pssMapper != null) {
            MappingElement receivedMappingElement;
            while ((receivedMappingElement = pssMapper.getNextResult()) != null) {
                LOGGER.logDebug2("Received mapping element: " + receivedMappingElement);
                mappingElements.add(receivedMappingElement);
                // Detect single-mapping-divergences, e.g., unused of undefined variables, immediately
                detectSingleMappingDivergences(receivedMappingElement);
            }
            LOGGER.logInfo2("Mapping with " + mappingElements.size() + " elements received");
            if (!mappingElements.isEmpty()) {
                detectMultiMappingDivergences();
            } else {
                LOGGER.logWarning2("Mapping is empty - no divergence detection possible");
            }
        } else {
            LOGGER.logWarning2("No mapping creator specified - no divergence detection possible");
        }
        
        /*
         * As divergences may be detected by checking mapping elements both in isolate and in combination, we can add
         * the final results only after all mapping elements are processed.
         */
        for (Divergence divergence : divergences) {
            addResult(divergence);
        }
        LOGGER.logInfo2(divergences.size() + " divergences detected");
    }
    
    /**
     * Detects {@link Divergence}s based on a single {@link MappingElement}.
     * 
     * @param mappingElement the {@link MappingElement} to be investigated for divergences
     */
    private void detectSingleMappingDivergences(@NonNull MappingElement mappingElement) {
        switch(mappingElement.getVariableState()) {
        case UNUSED:
            divergences.add(new UnusedVariableDivergence(mappingElement));
            break;
        case UNDEFINED:
            divergences.add(new UndefinedVariableDivergence(mappingElement));
            break;
        default:
            // Do nothing
            break;
        }
    }
    
    /**
     * Detects {@link Divergence}s based on multiple {@link MappingElement}s.
     */
    private void detectMultiMappingDivergences() {
        // TODO implement multi-mapping divergence detection
    }

    @Override
    public @NonNull String getResultName() {
        return "PSS_Divergences";
    }

}
