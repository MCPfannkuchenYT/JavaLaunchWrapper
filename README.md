# JavaLaunchWrapper
Java Launch Wrapper is a very small and lightweight Launcher for your Java Applications.

### What is the purpose of this
Sometimes Users have very different JVMs installed. Sometimes OpenJDK, sometimes AdoptOpenJDK, sometimes Oracle, sometimes even the AWS Distros.
Additionally they can have the version 16, instead of 8, or viceversa. Or your program only works on a JVM with JavaFX installed. All of these Problems can easily be solved with this.

### What does this tool do
Before your Program is being Launched, a new JVM will be downloaded and your Program will then be run over that one. JLW automatically detects the Users Operating System to download the propiertary JVM
It also updates your Program before launching.

### How to use
Download a release from the releases Page, or from the Github Actions Page. Now you need to copy a config.jlw next to the .class Files in the Jar.
It should look something like this:
```
# Specify different JVMs for different Operating Systems
WINDOWS=https://mgnet.work/JDK16-WIN.zip
LINUX=https://mgnet.work/JDK16-UNIX.zip
MACOS=https://mgnet.work/JDK16-MACOS.zip
# Download Link for Jar and Main Class
entry=your.main.Class
jar=linkToJar.de/lol.jar
# Name
name=YourApp

```