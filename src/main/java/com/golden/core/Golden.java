package com.golden.core;

import com.golden.commands.Command;
import com.golden.exceptions.BotException;
import com.golden.parser.CommandParser;
import com.golden.storage.Storage;

// Golden == ChatBot
public class Golden {
    private final CustomList tasks;        //list belongs to the chatbot
    private Storage storage;
    private BotActions actions;
    private static Ui ui;


    public Golden() throws BotException {
        ui = new Ui();
        storage = new Storage();
        tasks = new CustomList(storage.loadFile());     //error on loadFile
        actions = new BotActions(tasks, storage);
    }

    /**
     *  The logic to start up the chatbot, when and how to interact with the UI, and when to end the process.
     */
    public void run(){
        ui.greet();
//        Scanner input = new Scanner(System.in);
        boolean isGoodbye = false;
        while (!isGoodbye){
            try {
                String fullCommand = ui.readCommand();
                Command c = CommandParser.parseCommand(fullCommand);
                c.execute(actions, ui);
                isGoodbye = c.isGoodbye();
            } catch (BotException e){
                ui.showError(e.toString());
            }
        }
    }

    public static void main(String[] args) throws BotException {
        try {
            new Golden().run();
        } catch (BotException e) {
            ui.showError(e.toString());
        }
    }
}