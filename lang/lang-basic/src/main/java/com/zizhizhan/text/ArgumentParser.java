package com.zizhizhan.text;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ArgumentParser {

    private Map<String, String> m_inputMap = new HashMap<String, String>();
    private Map<String, String> m_outputMap = new HashMap<String, String>();

    /**
     * Adds a valid command line argument.
     *
     * @param commandName  The name of the command to add.
     * @param argumentName The argument name.
     */
    public void addInputArgument(String commandName, String argumentName) {
        m_inputMap.put(commandName.toLowerCase(Locale.getDefault()), argumentName.toLowerCase(Locale.getDefault()));
    }

    /**
     * Retrieves a parsed argument value.
     *
     * @param argumentName The argument name.
     * @return Returns the parsed value from the output map.
     */
    public String get(String argumentName) {
        return m_outputMap.get(argumentName.toLowerCase(Locale.getDefault()));
    }

    /**
     * Parses the command line arguments based on the valid arguments specified.
     *
     * @param args The command line arguments.
     */
    public void parse(String[] args) throws IllegalArgumentException {
        for (int i = 0; i < args.length; i++) {
            String argumentName = args[i];

            String outputName = m_inputMap.get(argumentName.toLowerCase(Locale.getDefault()));
            if (null == outputName) {
                throw new IllegalArgumentException("argument " + i + " (" + args[i]
                        + ") is not a valid argument. It should be an option flag (e.g. -h).");
            }

            if (args.length < i + 2) {
                throw new IllegalArgumentException("argument " + i + " (" + args[i]
                        + ") requires a matching value (i.e. an even number of arguments)");
            }
            String value = args[++i];

            m_outputMap.put(outputName.toLowerCase(Locale.getDefault()), value);
        }
    }

    /**
     * Shows the usage example.
     *
     * @param stream The PrintStream to write to.
     * @param prefix The command prefix.
     */
    public void showUsage(PrintStream stream, String prefix) {
        for (Map.Entry<String, String> entry : m_inputMap.entrySet()) {
            stream.println(prefix + entry.getKey() + " " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser();

        parser.addInputArgument("ok", "baozha");
        parser.addInputArgument("cc", "tv");

        //parser.showUsage(System.out, "prefix: ");

        parser.parse(new String[]{"cc", "xxxxx", "ok", "hello", "cc", "tt"});

        Map<String, String> map = parser.m_outputMap;

        for (String key : map.keySet()) {
            System.out.println(key + " : " + map.get(key));
        }

        System.out.println();
    }

}
