package de.pfannekuchen.jlw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import net.lingala.zip4j.ZipFile;

/**
 * All sorts of things
 * @author Pancake
 */
public final class Utils {
	public enum OS {
		WINDOWS, LINUX, MACOS
	};

	// Cache
	private static OS os = null;

	/**
	 * Try to get the Operating System set via Java
	 * @return OS Operating System
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
		copyURLToFile(new URL(url), temporaryFile);
		ZipFile zipFile = new ZipFile(temporaryFile);
		zipFile.extractAll(destination);
	}
	
	/**
	 * Downloads a File
	 */
    public static final void copyURLToFile(final URL source, final File destination) throws IOException {
        try (final InputStream stream = source.openStream()) {
        	destination.createNewFile();
        	try (final OutputStream outStream = new FileOutputStream(destination)) {
        		byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                while ((bytesRead = stream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                outStream.close();
                stream.close();
        	}
        }
    }
	
}