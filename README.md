# Nerunerune project template

This is a project template for a greenfield Java project. It's named after the Java mascot _Duke_. Given below are
instructions on how to use it.

## Documentation

**[User Guide](docs/README.md)** - Learn how to use all features

## Quick Start

1. Download the latest release
2. Run `java -jar nerunerune.jar`
3. See the **[User Guide](docs/README.md)** for detailed commands

## Setting up in Intellij

Prerequisites: JDK 17, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project
   first)
2. Open the project into Intellij as follows:
    1. Click `Open`.
    2. Select the project directory, and click `OK`.
    3. If there are any further prompts, accept the defaults.
3. Configure the project to use **JDK 17** (not other versions) as explained
   in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
4. After that, locate the `src/main/java/nerunerune/Nerunerune.java` file, right-click it, and choose
   `Run Nerunerune.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is
   correct, you should see something like the below as the output:

![Nerunerune launch image](docs/images/launch.png)

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move
Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle)
expect to find Java files.
<br>

- Duke is released under an MIT license.
- Duke is part of https://se-education.org