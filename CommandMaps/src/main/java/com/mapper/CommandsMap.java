package com.mapper;

import com.annotations.CommandsMapFactory;

import java.util.LinkedHashMap;

/**
 * a map of commands that can be executed based on a key for each one, to create an instance of
 * this class, you should invoke the static {@link #of(Object)} method, where it will scan for all methods annotated
 * with {@link com.annotations.Command}, and will store the {@link com.annotations.Command#value()}
 * as a key, and the annotated method as a value to be executed through {@link #execute(Object)},
 * {@link #execute(Object, Object)}, or {@link #execute(Object, Object, Object)} (based on the number of
 * arguments for the method)
 * <p>
 * notice that the instance that holds the annotated methods should be annotated with
 * {@link CommandsMapFactory}, also notice that all this is done in compile time (not reflections),
 * so you should build the project before invoking {@link #of(Object)} for the generated class to
 * be available
 * <p>
 * Created by Ahmed Adel Ismail on 9/9/2017.
 */
public class CommandsMap extends CommandsMapInitializer {

    private Object commandsMapFactory;
    private final LinkedHashMap<Object, Command> commands = new LinkedHashMap<>();
    private final LinkedHashMap<Object, BiCommand> biCommands = new LinkedHashMap<>();

    /**
     * set the instance that is annotated with {@link CommandsMapFactory}
     *
     * @param commandsMapFactory the instance that holds the methods to be added in the
     *                           {@link CommandsMap}
     */
    public final void setCommandsMapFactory(Object commandsMapFactory) {
        this.commandsMapFactory = commandsMapFactory;
    }

    @SuppressWarnings("unchecked")
    protected final <T> T getHostObject() {
        if (commandsMapFactory != null) {
            return (T) commandsMapFactory;
        } else {
            return null;
        }
    }

    protected void addCommand(Object key, Command command) {
        commands.put(key, command);
    }

    protected void addBiCommand(Object key, BiCommand biCommand) {
        biCommands.put(key, biCommand);
    }

    /**
     * execute a method that was annotated with {@link com.annotations.Command} and has no parameters
     *
     * @param key the value that was set in the {@link com.annotations.Command} annotation
     */
    public final void execute(Object key) {
        execute(key, null);
    }

    /**
     * execute a method that was annotated with {@link com.annotations.Command} and has one parameter
     *
     * @param key       the value that was set in the {@link com.annotations.Command} annotation
     * @param parameter the parameter to pass to the target method
     * @throws IllegalArgumentException if the key passed is not mapped to any methods (
     *                                  not mentioned in the {@link com.annotations.Command} annotation
     */
    public final void execute(Object key, Object parameter)
            throws IllegalArgumentException {
        Command command = commands.get(key);
        if (command != null) {
            command.accept(parameter);
        } else {
            System.err.println("no command assigned to the passed key " + key);
        }
    }

    /**
     * execute a method that was annotated with {@link com.annotations.Command} and has two parameter
     *
     * @param key          the value that was set in the {@link com.annotations.Command} annotation
     * @param parameterOne the first parameter to pass to the target method
     * @param parameterTwo the second parameter to pass to the target method
     * @throws IllegalArgumentException if the key passed is not mapped to any methods (
     *                                  not mentioned in the {@link com.annotations.Command} annotation
     */
    public final void execute(Object key, Object parameterOne, Object parameterTwo)
            throws IllegalArgumentException {
        BiCommand command = biCommands.get(key);
        if (command != null) {
            command.accept(parameterOne, parameterTwo);
        } else {
            System.err.println("no command assigned to the passed key " + key);
        }
    }

    /**
     * clear the current {@link CommandsMap}, this method is important to clear all the references
     */
    public final void clear() {
        commands.clear();
        biCommands.clear();
        commandsMapFactory = null;
    }
}
