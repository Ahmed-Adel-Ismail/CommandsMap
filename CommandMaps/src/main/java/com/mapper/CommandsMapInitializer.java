package com.mapper;

import com.annotations.CommandsMapFactory;

import java.lang.reflect.Constructor;

/**
 * a class that retrieves the {@link CommandsMap} Object related to the passed class
 * <p>
 * Created by Ahmed Adel Ismail on 9/9/2017.
 */
class CommandsMapInitializer {

    public static final String GENERATED_NAME = "$$CommandsMap";

    /**
     * create a {@link CommandsMap} from the passed instance
     *
     * @param commandsMapFactory an Object that is annotated with {@link CommandsMapFactory}
     * @return a {@link CommandsMap} that holds all the methods annotated with {@link com.annotations.Command}
     * @throws IllegalArgumentException if the passed instance is not annotated with {@link CommandsMapFactory}
     */
    public static CommandsMap of(Object commandsMapFactory) throws IllegalArgumentException {

        Class<?> commandsMapFactoryClass = commandsMapFactory.getClass();

        if (commandsMapFactoryClass.getAnnotation(CommandsMapFactory.class) == null) {
            throw new IllegalArgumentException("passed Object should be annotated with @"
                    + CommandsMapFactory.class.getSimpleName());
        }

        String commandsMapName = commandsMapFactoryClass.getName() + GENERATED_NAME;
        CommandsMap commandsMap = createCommandsMap(commandsMapName);
        commandsMap.setCommandsMapFactory(commandsMapFactory);
        return commandsMap;
    }

    private static CommandsMap createCommandsMap(String commandsMapName)
            throws IllegalArgumentException {
        try {
            Class<?> clazz = Class.forName(commandsMapName);
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (CommandsMap) constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to initialize " + commandsMapName
                    + ", try to re-build the project");
        }
    }

}
