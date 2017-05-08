package jjc.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GifRenamer {

	static public final String DIR = "gifs";
	static public final String EXT = ".gif";
	static public final int RETRIES = 3;
	static public final int SLEEP = 2000;
	static public final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");

	public static void main(String[] args) throws IOException {
		File cwd = new File(new File(".").getCanonicalPath());
		renameGifs(cwd);
	}

	/**
	 * Rename all files in a "gifs" directory.<br>
	 * 1. Create a new "gifs.[timestamp]" directory<br>
	 * 2. Copy gifs from "gifs" to "gifs.[timestamp]" changing names<br>
	 * 3. Remove the old directory and rename the new one
	 * 
	 * @param source
	 *            - The parent directory. Source/gifs will get processed.
	 * @return
	 */
	static public void renameGifs(File source) {
		File initialDir = new File(source, DIR);

		if (initialDir.isDirectory()) {

			// 1. Create a new "gifs.[timestamp]" directory
			String timestamp = FORMAT.format(new Date());
			File newDir = new File(source, DIR + "." + timestamp);
			newDir.mkdir();

			// 2. Copy gifs from "gifs" to "gifs.[timestamp]" changing names
			File[] list = initialDir.listFiles();
			int count = 1;
			for (File file : list) {
				file.renameTo(new File(newDir, count + EXT));
				count++;
			}

			// 3. Remove the old directory and rename the new one
			if (initialDir.listFiles().length == 0) {
				initialDir.delete();

				int retry = 0;
				do {
					// System.out.println(">>> Sleep and try #" + (retry + 1));
					sleep(SLEEP);
				} while (!newDir.renameTo(new File(source, DIR)) && ++retry < RETRIES);
			}
		}

	}

	static private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}
}
