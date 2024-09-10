# PMD

### Requirement 
* Java 8 or above

### Installation 
* download latest release https://github.com/pmd/pmd/releases/download/pmd_releases%2F7.5.0/pmd-dist-7.5.0-bin.zip
* unzip
* add the bin folder to your path

### Usage
(Instruction for linux use ```pmd.bat``` for windows)
* Do not hesitate to have a look on the help page ```pmd  -h```
* Same thing for the check help page```pmd check -h``` 
* Run the analysis ```pmd check -f text -R rulesets/java/quickstart.xml -d [PROJECT_PATH] -r [REPORT_FILE_PATH]```


For more information consult the instructions given in https://pmd.github.io/pmd/pmd_userdocs_installation.html