package org.architecturemining.interactions.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Tijmen
 *
 *         This class contains utility methods for working with Interaction
 *         Logs. They are designed only to deal with the kind of string
 *         representations that occur in logs created by AJPOLog.
 */
public class InteractionStringUtils {

	/**
	 * @param name
	 *            i.e. java.util.ArrayList.CallerPsuedoId: 1873859565
	 * @param includeId
	 *            whether to include the object ID
	 * @return shorter version, i.e. ArrayList(1873859565)
	 */
	public static String abbreviateName(String name) {
		/*
		 * This was originally done using regular expressions, but Java's
		 * terrible regex implementation broke my spirit and made me rewrite it
		 * using Apache Commons StringUtils
		 */
		String[] elements = StringUtils.split(name, '.');

		String className = elements[elements.length - 2];
		String identifier = StringUtils.removeStart(elements[elements.length - 1], "CallerPseudoId: ");

		if (identifier.equals("Static")) {
			return className;
		} else {
			return className + "(" + identifier + ")";
		}
	}

	/**
	 * @param name
	 * @return Same as abbreviateName but without the identifier
	 */
	public static String abbreviateNameWithoutIdentifier(String name) {

		String[] elements = StringUtils.split(name, '.');

		String className = elements[elements.length - 2];

		return className;
	}

	/**
	 * @param name
	 *            i.e. execution(void
	 *            org.architecturemining.program.example.band.BandPractice.main(String[]))
	 * @param includeArguments
	 *            whether to include the arguments
	 * @return shorter version, i.e. main(String[])
	 */
	public static String abbreviateCall(String call) {
		// There might be dots in the arguments
		String[] elements = StringUtils.split(call, '(');
		// Take the part after the opening parenthesis after 'execution',
		// but before the arguments
		elements = StringUtils.split(elements[0], '.');

		String method = elements[elements.length - 1];

		return method;
	}
}
