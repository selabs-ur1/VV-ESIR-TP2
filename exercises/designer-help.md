# Use PMD designer

## Download JavaFX 
If JavaFX is not installed you can download it [here](https://gluonhq.com/products/javafx/).
Check the current version of your JDK by running ```java --version``` and download the corresponding version ("Minimum JDK" column).
* Java 21 => JavaFX 24
* Java 17 => JavaFX 21
* Java 11 => JavaDX 17

## Set up JavaFX
Before using `designer` you need to specify JavaFX path.

#### Linux/Unix
```shell
$ export JAVAFX_HOME=path/to/javafx-sdk-{version}
```

#### Windows
```shell
$ set JAVAFX_HOME=path\to\javafx-sdk-{version}
```

## Run designer

#### Linux/Unix
```shell
$ pmd designer
```

#### Windows
```shell
$ pmd.bat designer
```
