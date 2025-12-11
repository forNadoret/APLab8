package ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private static final Logger logger = LoggerFactory.getLogger(Menu.class);

    private final Map<String, Command> commands;
    private final Scanner scanner;
    protected final CreditService service;

    public Menu(CreditService service) {
        this.commands = new HashMap<>();
        this.scanner = new Scanner(System.in);
        this.service = service;
        init(service);
    }

    protected void init(CreditService service) {
    }

    public void registerCommand(String key, Command command) {
        commands.put(key, command);
    }

    public void run() {
        logger.info("Application session started.");
        System.out.println("System started. Type 'help' for commands.");
        boolean running = true;
        while (running) {
            System.out.print("> ");
            if (!scanner.hasNextLine())
            {
                break;
            }

            String line = scanner.nextLine().trim();
            if (line.isEmpty())
            {
                continue;
            }

            String[] parts = line.split(" ", 2);
            String key = parts[0].toLowerCase();
            String params = (parts.length > 1) ? parts[1] : "";

            logger.info("User command: '{}', Params: '{}'", key, params);

            try {
                switch (key) {
                    case "exit":
                        running = false;
                        logger.info("User initiated shutdown.");
                        break;
                    case "help":
                        handleHelp();
                        break;
                    default:
                        Command command = commands.get(key);
                        if (command != null) {
                            command.execute(params);
                        } else {
                            logger.warn("Unknown command entered: {}", key);
                            System.out.println("Unknown command.");
                        }
                        break;
                }
            } catch (Exception e) {
                logger.error("CRITICAL: Unexpected error in Menu execution", e);
                System.out.println("An internal error occurred. Please contact admin.");
            }
        }
        logger.info("Application stopped.");
    }

    private void handleHelp() {
        System.out.println("Available commands:");
        commands.forEach((k, v) -> System.out.printf("  %-10s - %s\n", k, v.getDesc()));
        System.out.println("  exit       - Exit program");
    }
}