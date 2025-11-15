package com.golden.config;

/**
 * This class defines the constraints and constants of the Chatbot app.
 * static = the attributes are shared across all instances
 * final = the values cannot be changed
 */

public final class StaticConfig {
    // #constraint: sets the hard limit for TaskList
    public static final int MAX_TASKS = 100;

    // #config: setup and change the name of the chatbot/app
    public static final String APP_NAME = "Golden";

    // #config: sets the path of initial file to load
    public static final String INIT_FILEPATH = "src/main/data/test1.txt";
}

