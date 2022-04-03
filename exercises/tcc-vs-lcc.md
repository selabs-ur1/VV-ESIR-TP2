# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

If TCC=LCC=1, it means that all the methods are connected. If the value of TCC=LCC is between 0 and 1, it means that there are different parts that can be separated in at least 2 classes. If TCC=LCC=0, it means that none of the methods are connected.

Class example :
```Java
public class Word {

    private String word;
    
    public Word(String w) {
        word = w;
    }
    
    public String getWord() {
        return word;
    }
    
    public void setWord(String w) {
        word=w;
    }
}
```
