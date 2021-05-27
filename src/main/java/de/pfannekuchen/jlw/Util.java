package de.pfannekuchen.jlw;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import net.lingala.zip4j.ZipFile;

/**
 * All sorts of things
 * @author Pancake
 */
public final class Util {
	public enum OS {
		WINDOWS, LINUX, MACOS
	};

	// Cache
	private static OS os = null;

	/**
	 * Try to get the Operating System set via Java
	 * @return OS
	 */
	public static final OS getOS() {
		if (os == null) {
			final String osname = System.getProperty("os.name").toLowerCase();
			if (osname.contains("win")) {
				os = OS.WINDOWS;
			} else if (osname.contains("nix") || osname.contains("nux") || osname.contains("aix")) {
				os = OS.LINUX;
			} else if (osname.contains("mac")) {
				os = OS.MACOS;
			}
		}
		return os;
	}

	/**
	 * Used to quickly download and Unzip a ZIP Archive
	 */
	public static final void downloadZip(final String url, final String destination) throws MalformedURLException, IOException {
		final File temporaryFile = File.createTempFile("jvmdownload", ".zip");
		temporaryFile.deleteOnExit();
		FileUtils.copyURLToFile(new URL(url), temporaryFile);
		ZipFile zipFile = new ZipFile(temporaryFile);
		zipFile.extractAll(destination);
	}
}