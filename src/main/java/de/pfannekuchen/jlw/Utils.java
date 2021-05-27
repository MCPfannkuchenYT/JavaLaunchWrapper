package de.pfannekuchen.jlw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	public static final void downloadZip(final String url, final String destination) throws Exception {
		final File temporaryFile = File.createTempFile("jvmdownload", ".zip");
		temporaryFile.deleteOnExit();
		copyURLToFile(new URL(url), temporaryFile);
		unzipFile(temporaryFile.getAbsolutePath(), new File(destination));
		//ZipFile zipFile = new ZipFile(temporaryFile);
		//zipFile.extractAll(destination);
	}
	
	public static final void unzipFile(final String fileZip, final File destDir) throws Exception {
		byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip))) {
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File newFile = newFile(destDir, zipEntry);
			    if (zipEntry.isDirectory()) {
			        if (!newFile.isDirectory() && !newFile.mkdirs()) {
			            throw new IOException("Failed to create directory " + newFile);
			        }
			    } else {
			        // fix for Windows-created archives
			        File parent = newFile.getParentFile();
			        if (!parent.isDirectory() && !parent.mkdirs()) {
			            throw new IOException("Failed to create directory " + parent);
			        }
			        
			        // write file content
			        FileOutputStream fos = new FileOutputStream(newFile);
			        int len;
			        while ((len = zis.read(buffer)) > 0) {
			            fos.write(buffer, 0, len);
			        }
			        fos.close();
			    }
			zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		}
	}
	
	private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
	    File destFile = new File(destinationDir, zipEntry.getName());

	    String destDirPath = destinationDir.getCanonicalPath();
	    String destFilePath = destFile.getCanonicalPath();

	    if (!destFilePath.startsWith(destDirPath + File.separator)) {
	        throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
	    }

	    return destFile;
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