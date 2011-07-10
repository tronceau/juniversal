package juniversal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Util {

	/**
	 * Count number of times specified character appears in string.
	 * 
	 * @param string
	 *            string in question
	 * @param character
	 *            character to count
	 * @return count of occurrences of character in string
	 */
	public static int countChars(String string, int character) {
		int count = 0;
		int currIndex = 0;
		for (;;) {
			currIndex = string.indexOf(character, currIndex);
			if (currIndex == -1)
				break;
			++currIndex; // Start next search from next character
			++count;
		}
		return count;
	}

	/**
	 * Recursively get a list of all files in the specified directory, having the given suffix. The
	 * resulting files are added to the files list.
	 * 
	 * @param directory
	 *            starting directory
	 * @param suffix
	 *            suffix (e.g. ".java") to match on; pass "" if want to match all files
	 * @param files
	 *            list to add files to, which caller should create
	 * @throws FileNotFoundException
	 */
	static public void getFilesRecursive(File directory, String suffix, List<File> files) throws FileNotFoundException {
		File[] filesList = directory.listFiles();
		if (filesList == null)
			throw new FileNotFoundException(directory.getPath());

		for (File file : directory.listFiles()) {
			if (file.isDirectory())
				getFilesRecursive(file, suffix, files);
			else {
				if (file.getName().endsWith(suffix))
					files.add(file);
			}
		}
	}
}
