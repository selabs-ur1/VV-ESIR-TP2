
# Using PMD


1. Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)).

2. Describe below an issue found by PMD that you think should be solved (true positive)

3. Include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.


## Answer

### 1. Chosen Java Project

[Here](https://github.com/Astri2/IdleBot) is the Java Project we have chosen. It is one of our classmates' project.

We executed the command below to run PMD on it and obtain a report named `pmd-report.txt` :

```
pmd check -f text -R rulesets/java/quickstart.xml -d ./IdleBot/ -r ./pmd-report.txt
```

You can fin this report in the code repository.

### 2. True Positive

Here is an issue found by PMD that could be solved. (line 83 in the report)

``` text
.\IdleBot
    \app
        \src
            \main
                \java
                    \me
                        \astri
                            \idleBot
                                \GameBot
                                    \commands
                                        \__debug
                                            \DebugCommands.java:31:    
SwitchStmtsShouldHaveDefault: Switch statements should be exhaustive, add a default case (or missing enum branches)
```

The problem reported by PMD is in the file ``DebugCommands.java``. It concerns the absence of a default case in a switch instruction, which is therefore not exhaustive. This means that if none of the specific cases are encountered, the program will not know how to handle the situation.

We can easily correct this mistake and it is important to make the code more robust, maintainable and conform to good practices. In fact, we have to add a default case at this place of the code.

```java
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().toLowerCase().split("\\s+");
        if(!event.getAuthor().getId().equals(Config.get("BOT_OWNER_ID"))) return;

        if(!event.getMessage().getMentionedUsers().contains(event.getJDA().getSelfUser())) return;
        switch (args[0]) {
            case "i!give" -> give(event);
            case "i!load" -> load();
            case "i!save" -> save();
            case "i!lprices" -> prices();
            case "i!ping" -> ping(event);
            case "i!emote" -> emote(event);
            case "i!shutdown" -> shutdown(event);
            // add a default case here and maybe an error message to tell that the command is unknown by the system
        }
    }
```

### 3. False Positive

PMD has reported an warning called `UnusedPrivateMethod`, it means avoid unused private methods such as `add(int, int)`.

The file and associated line are : 

```
.\commons-collections
    \src
        \main
            \java
                \org
                    \apache
                        \commons
                            \collections4
                                \bloomfilter
                                    \ArrayCountingBloomFilter.java:121
```


So when we look at the code, we see that add is called but weirdly. When we look at the public version of ``add``, we see ``this::add``. In this case, it will be the private method that will be provided, because ``processCells`` is asking for a ``Runnable`` with two int arguments. It is really tricky, even for us. We do not know why Java accepts that in the language.  

``` java
@Override
public boolean add(final CellExtractor other) {
    Objects.requireNonNull(other, "other");
    other.processCells(this::add);
    return isValid();
}

private boolean add(final int idx, final int addend) {
    try {
        final int updated = cells[idx] + addend;
        state |= updated;
        cells[idx] = updated;
        return true;
    } catch (final IndexOutOfBoundsException e) {
        throw new IllegalArgumentException(
            String.format("Filter only accepts values in the [0,%d) range", getShape().getNumberOfBits()), e);
    }
}
```
