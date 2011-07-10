package juniversal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class JUniversal {
	private List<File> javaProjectDirectories;
	private File outputDirectory;
	private int preferredIndent = 4;

	public static void main(String[] args) {
		JUniversal jUniversal = new JUniversal(args);

		new juniversal.cplusplus.Translator(jUniversal).translate();

		System.out.println("Translation complete; output is in " + jUniversal.outputDirectory);
	}

	public JUniversal(String[] args) {
		this.javaProjectDirectories = new ArrayList<File>();

		for (int i = 0; i < args.length; ++i) {
			String arg = args[i];

			if (arg.startsWith("-")) {
				if (arg.equals("-o")) {
					++i;
					if (i >= args.length)
						usageError();
					arg = args[i];

					this.outputDirectory = new File(arg);
				} else
					usageError();
			} else
				this.javaProjectDirectories.add(new File(arg));
		}

		// Ensure that there's at least one input directory & the output directory is specified
		if (this.javaProjectDirectories.size() == 0 || this.outputDirectory == null)
			usageError();
	}

	private void usageError() {
		System.err.println("Usage: juniversal <java-project-directories>... -o <output-directory>");
		System.exit(1);
	}

	public List<File> getJavaProjectDirectories() {
		return javaProjectDirectories; 
	}

	public File getOutputDirectory() {
		return outputDirectory; 
	}

	public int getPreferredIndent() { 
		return preferredIndent;
	}

	/**
	 * Get all the source files in the specified Java project directories.
	 * 
	 * @return list of all files in the project directories, in project directory order specified on
	 *         command line
	 */
	public String[] getJavaFiles() {
		ArrayList<File> files = new ArrayList<File>();

		for (File directory : javaProjectDirectories) {
			System.out.println(directory);
			try {
				Util.getFilesRecursive(directory, ".java", files);
			} catch (FileNotFoundException e) {
				throw new UserViewableException("Java project directory " + directory + " not found or not an accessible directory");
			}
		}

		int length = files.size();
		String[] filePathsArray = new String[length];
		for (int i = 0; i < length; ++i)
			filePathsArray [i] = files.get(i).getPath(); 

		return filePathsArray;
	}
}
