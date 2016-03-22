package agh.sr.kurtz.Command;

public class HelpCommand implements Command {
    public void execute() {
        CommandRouter.printAvailableCommands();
    }
}
