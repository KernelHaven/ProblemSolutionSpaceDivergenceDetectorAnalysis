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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.ssehub.kernel_haven.code_model.CodeElement;
import net.ssehub.kernel_haven.code_model.SourceFile;
import net.ssehub.kernel_haven.util.io.ITableRow;
import net.ssehub.kernel_haven.util.null_checks.NonNull;
import net.ssehub.kernel_haven.util.null_checks.Nullable;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * This class represents an abstract problem-solution-space divergence. It provides the common attributes and methods
 * for specific divergence types (subclasses) as well as the required methods for saving divergences as a KernelHaven
 * result.
 * 
 * @author Christian Kröher
 *
 */
public abstract class Divergence implements ITableRow {
    
    /**
     * The set of {@link VariabilityVariable}s involved in this divergence. For example, this set may contain variables
     * defined in the variability model, but not used in build and code artifacts
     * (see {@link UnusedVariableDivergence}.<br><br>
     * 
     * This set is initialized during object construction. Hence, its value is never <code>null</code>, but the set may
     * be <i>empty</i>.
     */
    protected @NonNull Set<VariabilityVariable> involvedVariables;
    
    /**
     * The set of {@link SourceFile}s involved in this divergence. For example, this set may contain source files, whose
     * presence or absence in a final product are controlled by a variable, which is not defined in the variability
     * model (see {@link UndefinedVariableDivergence}).<br><br>
     * 
     * This set is initialized during object construction. Hence, its value is never <code>null</code>, but the set may
     * be <i>empty</i>. 
     */
    protected @NonNull Set<SourceFile> involvedSourceFiles;
    
    /**
     * The set of {@link CodeElement}s involved in this divergence. For example, this set may contain code elements
     * within a specific {@link SourceFile}, whose presence or absence in a final product are controlled by a variable,
     * which is not defined in the variability model (see {@link UndefinedVariableDivergence}).<br><br>
     * 
     * This set is initialized during object construction. Hence, its value is never <code>null</code>, but the set may
     * be <i>empty</i>. 
     */
    protected @NonNull Set<CodeElement> involvedCodeElements;
    
    /**
     * Create an {@link Divergence} instance and initializes the {@link #involvedVariables},
     * {@link #involvedSourceFiles}, and {@link #involvedCodeElements} sets.
     */
    public Divergence() {
        involvedVariables = new HashSet<VariabilityVariable>();
        involvedSourceFiles = new HashSet<SourceFile>();
        involvedCodeElements = new HashSet<CodeElement>();
    } 
    
    /**
     * Adds the given {@link VariabilityVariable} to this divergence as it is involved in it. For example, the given
     * variable is part of the symptom or the cause of this divergence.
     * 
     * @param variable the {@link VariabilityVariable} involved in this divergence
     */
    public void addInvolvedVariable(@NonNull VariabilityVariable variable) {
        involvedVariables.add(variable);
    }
    
    /**
     * Adds the given collection of {@link VariabilityVariable} to this divergence as they are involved in it. For
     * example, the given variables are part of the symptom or the cause of this divergence.
     * 
     * @param variables the collection of {@link VariabilityVariable}s involved in this divergence
     */
    public void addInvolvedVariables(@NonNull Collection<VariabilityVariable> variables) {
        involvedVariables.addAll(variables);
    }
    
    /**
     * Adds the given {@link SourceFile} to this divergence as it is involved in it. For example, the given source file
     * is part of the symptom or the cause of this divergence.
     * 
     * @param sourceFile the {@link SourceFile} involved in this divergence
     */
    public void addInvolvedSourceFile(@NonNull SourceFile sourceFile) {
        involvedSourceFiles.add(sourceFile);
    }
    
    /**
     * Adds the given collection of {@link SourceFile}s to this divergence as they are involved in it. For example, the
     * given source files are part of the symptom or the cause of this divergence.
     * 
     * @param sourceFiles the collection of {@link SourceFile}s involved in this divergence
     */
    public void addInvolvedSourceFiles(@NonNull Collection<SourceFile> sourceFiles) {
        involvedSourceFiles.addAll(sourceFiles);
    }
    
    /**
     * Adds the given {@link CodeElement} to this divergence as it is involved in it. For example, the given code
     * element is part of the symptom or the cause of this divergence.
     * 
     * @param codeElement the {@link CodeElement} involved in this divergence
     */
    public void addInvolvedCodeElement(@NonNull CodeElement codeElement) {
        involvedCodeElements.add(codeElement);
    }
    
    /**
     * Adds the given collection of {@link CodeElement}s to this divergence as they are involved in it. For example, the
     * given code elements are part of the symptom or the cause of this divergence.
     * 
     * @param codeElements the collection of {@link CodeElement}s involved in this divergence
     */
    public void addInvolvedCodeElements(@NonNull Collection<CodeElement> codeElements) {
        involvedCodeElements.addAll(codeElements);
    }

    /**
     * Returns the set of {@link VariabilityVariable}s involved in this divergence.
     * 
     * @return the set of involved {@link VariabilityVariable}s; can be <i>empty</i>, if no variable is involved in this
     *         divergence
     */
    public @NonNull Set<VariabilityVariable> getInvolvedVariables() {
        return involvedVariables;
    }
    
    /**
     * Returns the names of all {@link VariabilityVariable}s involved in this divergence as a single string. The names
     * are separated by a single whitespace.
     *  
     * @return the names of all involved {@link VariabilityVariable}s or an <i>empty</i> string, if no variable is
     *         involved in this divergence
     */
    public @NonNull String getInvolvedVariablesString() {
        String involvedVariablesString = "";
        if (!involvedVariables.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (VariabilityVariable variable : involvedVariables) {
                stringBuilder.append(variable.getName());
                stringBuilder.append(' ');
            }
            involvedVariablesString = stringBuilder.toString().trim();
        }
        return involvedVariablesString;
    }
    
    /**
     * Returns the set of {@link SourceFile}s involved in this divergence.
     * 
     * @return the set of involved {@link SourceFile}s; can be <i>empty</i>, if no source file is involved in this
     *         divergence
     */
    public @NonNull Set<SourceFile> getInvolvedSourceFiles() {
        return involvedSourceFiles;
    }
    
    /**
     * Returns the paths of all {@link SourceFile}s involved in this divergence as a single string. The paths are
     * separated by a single whitespace.
     *  
     * @return the paths of all involved {@link SourceFile}s or an <i>empty</i> string, if no source file is involved
     *         in this divergence
     */
    public @NonNull String getInvolvedSourceFilesString() {
        String involvedSourceFilesString = "";
        if (!involvedSourceFiles.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (SourceFile sourceFile : involvedSourceFiles) {
                stringBuilder.append(sourceFile.getPath().getPath());
                stringBuilder.append(' ');
            }
            involvedSourceFilesString = stringBuilder.toString().trim();
        }
        return involvedSourceFilesString;
    }
    
    /**
     * Returns the set of {@link CodeElement}s involved in this divergence.
     * 
     * @return the set of involved {@link CodeElement}s; can be <i>empty</i>, if no code element is involved in this
     *         divergence
     */
    public @NonNull Set<CodeElement> getInvolvedCodeElements() {
        return involvedCodeElements;
    }
    
    /**
     * Returns the start and end line numbers as well as the parent {@link SourceFile}'s paths of all
     * {@link CodeElement}s involved in this divergence as a single string. The information for each element is
     * separated by a single whitespace.
     *  
     * @return the start and end line numbers as well as the parent {@link SourceFile}'s paths of all involved
     *         {@link CodeElement}s or an <i>empty</i> string, if no code element is involved in this divergence
     */
    public @NonNull String getInvolvedCodeElementsString() {
        String involvedCodeElementsString = "";
        if (!involvedCodeElements.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (CodeElement codeElement : involvedCodeElements) {
                stringBuilder.append(codeElement.getSourceFile().getPath() + "[" + codeElement.getLineStart() + "-" 
                    + codeElement.getLineEnd() + "]");
                stringBuilder.append(' ');
            }
            involvedCodeElementsString = stringBuilder.toString().trim();
        }
        return involvedCodeElementsString;
    }
    
    /**
     * Returns the name of the type of this divergence. The returned value will be used to write this divergence as a
     * result into an Excel-sheet.
     *  
     * @return the name of the type of this divergence
     */
    public abstract @NonNull String getType();
    
    /**
     * Returns the problematic information in the problem space (artifacts), which causes this divergence in combination
     * with the solution space symptom. The returned value will be used to write this divergence as a result into an
     * Excel-sheet.
     *  
     * @return the problematic information in the problem space causing this divergence
     * @see #getSolutionSpaceSymptom()
     */
    public abstract @NonNull String getProblemSpaceSymptom();
    
    /**
     * Returns the problematic information in the solution space (artifacts), which causes this divergence in
     * combination with the problem space symptom. The returned value will be used to write this divergence as a result
     * into an Excel-sheet.
     * 
     * @return the problematic information in the solution space causing this divergence
     * @see #getProblemSpaceSymptom()
     */
    public abstract @NonNull String getSolutionSpaceSymptom();
    
    @Override
    public @Nullable Object @NonNull [] getHeader() {
        // For writing the Excel-sheet headers
        String[] headers = {"Type", "Problem Space Symptom", "Solution Space Symptom"};
        return headers;
    }

    @Override
    public @Nullable Object @NonNull [] getContent() {
        // For writing each divergence as a single row and in accordance to the headers defined above to the Excel-sheet
        String[] content = {getType(), getProblemSpaceSymptom(), getSolutionSpaceSymptom()};
        return content;
    }
    
    @Override
    public @NonNull String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Divergence Type = ");
        stringBuilder.append(getType());
        stringBuilder.append('\t');
        stringBuilder.append("Problem Space Symptom = ");
        stringBuilder.append(getProblemSpaceSymptom());
        stringBuilder.append('\t');
        stringBuilder.append("Solution Space Symptom = ");
        stringBuilder.append(getSolutionSpaceSymptom());
        return stringBuilder.toString();
    }
    
    /**
     * Converts the given string containing whitespace-separated elements involved in a {@link Divergence} into a string
     * appropriate for including it into a description. The result will be a formatted listing, e.g.:
     * <li>
     * <ul>Input parameter value = "VariableA VariableB VariableC"</ul>
     * <ul>Return value = ""VariableA", "VariableB", and "VariableC""</ul>
     * <li>
     * 
     * @param involvedElements the string containing whitespace-separated elements involved in a {@link Divergence}
     * @return a string containing the involved elements properly formatted for a description; can be <i>empty</i> if
     *         the given string does not contain any characters
     */
    protected @NonNull String toDescriptionString(@NonNull String involvedElements) {
        StringBuilder descriptionStringBuilder = new StringBuilder("");
        if (!involvedElements.isEmpty()) {            
            String[] splittedInvolvedElements = involvedElements.split("\\s+");
            int involvedElementsNumber = splittedInvolvedElements.length;
            if (involvedElementsNumber > 0) {
                descriptionStringBuilder.append("\"" + splittedInvolvedElements[0] + "\"");
                if (involvedElementsNumber == 2) {
                    // Exactly 2 elements: return ""Elem1" and "Elem2""
                    descriptionStringBuilder.append(" and ");
                    descriptionStringBuilder.append("\"" + splittedInvolvedElements[1] + "\"");
                } else if (involvedElementsNumber > 2) {
                    // More than 2 elements: return ""Elem1, Elem2, [...], and "ElemX""
                    int i;
                    for (i = 1; i < involvedElementsNumber - 1; i++) {
                        descriptionStringBuilder.append(", ");
                        descriptionStringBuilder.append(splittedInvolvedElements[i]);
                    }
                    descriptionStringBuilder.append(", and ");
                    descriptionStringBuilder.append("\"" + splittedInvolvedElements[i + 1] + "\"");
                }
            }
        }
        return descriptionStringBuilder.toString();
    }
}
