# ProblemSolutionSpaceDivergenceDetectorAnalysis
The Problem-Solution-Space (PSS) Divergence Detector is an analysis plug-in, which identifies unintended divergences between artifacts in the problem space and the solution space of a Software Product Line (SPL). This identification is based on a problem-solution-space mapping as provided by the [Problem-Solution-Space Mapper](https://github.com/KernelHaven/ProblemSolutionSpaceMapperAnalysis). Please refer to the website of that plug-in for more information on the provided mapping.

The PSS Divergence Detector identifies the following types of divergences:
1. Detection of single-mapping divergences, which result from the information of a single key-value-pair of a problem-solution-space mapping. Therefore the plug-in checks the information of each feature in the mapping, e.g., whether its state is either UNUSED or UNDEFINED. If it detects a feature with one of these states, it creates a new divergence information object and adds all features (or similarly named variables), source files, and code elements of the key-value-pair, which are involved in that divergence.
2.	Detection of multi-mapping divergences, which result from combining the information of two or more key-value-pairs. This is future work.

## KernelHaven Setup
In order to detect unintended divergences, the analysis plug-in must be combined with the [Problem-Solution-Space Mapper analysis plug-in](https://github.com/KernelHaven/ProblemSolutionSpaceMapperAnalysis). A particular configuration file for executing this analysis plug-in should contain the following information:

```
#####################
#     Directores    #
#####################
resource_dir = res/
output_dir = output/
plugins_dir = plugins/
cache_dir = cache/
archive = false
#archive.dir = /some/dir

source_tree = <TODO: PATH_TO_SPL>
arch = x86

##################
#     Logging    #
##################
log.dir = log/
log.console = true
log.file = true
log.level = INFO

################################
#     Code Model Parameters    #
################################

code.provider.timeout = 0
code.provider.cache.write = false
code.provider.cache.read = false
code.extractor.class =  net.ssehub.kernel_haven.undertaker.UndertakerExtractor
code.extractor.files = main
# Undertaker parses header and code files separately
code.extractor.file_regex = .*\.(c|h)
code.extractor.threads = 2
code.extractor.add_linux_source_include_dirs = false
code.extractor.parse_to_ast = false


################################
#    Build Model Parameters    #
################################

build.provider.timeout = 0
build.provider.cache.write = false
build.provider.cache.read = false
build.extractor.class = net.ssehub.kernel_haven.kbuildminer.KbuildMinerExtractor
build.extractor.top_folders = main


#######################################
#     Variability Model Parameters    #
#######################################

variability.provider.timeout = 0
variability.provider.cache.write = false
variability.provider.cache.read = false
variability.extractor.class = net.ssehub.kernel_haven.kconfigreader.KconfigReaderExtractor

##############################
#     Analysis Parameters    #
##############################

analysis.class = net.ssehub.kernel_haven.analysis.ConfiguredPipelineAnalysis
analysis.pipeline = net.ssehub.kernel_haven.pss_divergence_detector.ProblemSolutionSpaceDivergenceDetector(net.ssehub.kernel_haven.pss_mapper.ProblemSolutionSpaceMapper(cmComponent(), bmComponent(), vmComponent()))
analysis.output.intermediate_results = ProblemSolutionSpaceMapper
analysis.output.type = xlsx
analysis.pss_mapper.variable_regex = CONFIG_.*
```
Please note that the PSS Divergence Detector plug-in is currently under development and, hence, tested only with this particular configuration.

## Usage
The PSS Divergence Detector can only be used as part of an analysis pipeline as it requires a [problem-solution-space mapping](https://github.com/KernelHaven/ProblemSolutionSpaceMapperAnalysis) as input (see KernelHaven Setup above). In such a setup, it will only provide a possibly empty set of divergences. This setup can also be extended by the [Problem-Solution-Space Divergence Corrector](https://github.com/KernelHaven/ProblemSolutionSpaceDivergenceCorrectorAnalysis) to provide (proposals for) corrections of detected divergences. 
