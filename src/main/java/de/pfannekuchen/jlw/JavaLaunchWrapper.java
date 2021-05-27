package de.pfannekuchen.jlw;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.jlw.Util.OS;

public class JavaLaunchWrapper {

	public static final void main(final String[] args) {
		try {
			start();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showConfirmDialog(null, "Please check the Console.", "Something went wrong!", JOptionPane.CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	static final void start() throws Exception {
		final Properties properties = new Properties();
		/* Try loading the Properties from the Config File */
		try (final InputStream stream = JavaLaunchWrapper.class.getResourceAsStream("config.jlw")) {
			properties.load(stream);
			stream.close();
		}
		final String JVM = properties.getProperty(Util.getOS().name());
		final String NAME = properties.getProperty("name");
		final String destination = ((Util.getOS() == OS.WINDOWS) ? (System.getenv("AppData") + "/" + NAME + "-JVM") : (System.getProperty("user.name") + "/" + NAME + "-JVM"));
		if (!new File(destination).exists()) {
			final Thread t = new Thread(() -> {
				JOptionPane.showConfirmDialog(null, "Since this is the first launch\n a few Files will be downloaded. \nThis shouldn't take to long...", "Thank you for installing " + NAME, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
			});
			t.setDaemon(true);
			t.start();
			Util.downloadZip(JVM, destination);
			t.interrupt();
		}
		final File jarFile = new File(destination + "/app.jar");
		if (jarFile.exists()) jarFile.delete();
		FileUtils.copyURLToFile(new URL(properties.getProperty("jar")), jarFile);
		final ProcessBuilder builder = new ProcessBuilder(destination + "/bin/java" + (Util.getOS() == OS.WINDOWS ? ".exe" : ""), "-cp", jarFile.getAbsolutePath(), (String) properties.get("entry"));
		builder.inheritIO();
		builder.start();
	}
	
}
