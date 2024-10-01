
# Using PMD


1. Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)).

2. Describe below an issue found by PMD that you think should be solved (true positive)

3. Include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.


## Answer

### 1. Chosen Java Project

[Here](https://github.com/Astri2/IdleBot) is the Java Project we have chosen.

### 2. True Positive

Here is an issue found by PMD that could be solved. 

```java
    // .\IdleBot\app\src\main\java\me\astri\idleBot\GameBot\commands\__debug\DebugCommands.java:31:    
    // SwitchStmtsShouldHaveDefault: Switch statements should be exhaustive, add a default case (or missing enum branches)
```

The problem reported by PMD is in the file DebugCommands.java. It concerns the absence of a default case in a switch instruction, which is therefore not exhaustive. This means that if none of the specific cases are encountered, the program will not know how to handle the situation.

We can easily correct this mistake and it is important to make the code more robust, maintainable and conform to good practices.

In fact, we have to add a default case at this place of the code.

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

### True Negative

FIND ONE 