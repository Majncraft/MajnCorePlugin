package cz.majncraft.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import cz.majncraft.MajnCorePlugin;
import cz.majncraft.api.MajnPlugin;

public final class ClassFinder {
//Code from http://www.dzone.com/snippets/get-all-classes-within-package
    private final static char DOT = '.';
    private final static char SLASH = '/';
    private final static String CLASS_SUFFIX = ".class";
    private final static String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the given '%s' package exists?";

    public final static List<Class<? extends MajnPlugin>> find(final String scannedPackage) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final String scannedPath = scannedPackage.replace(DOT, SLASH);
        final Enumeration<URL> resources;
        try {
        	MajnCorePlugin.instance.getLogger().info("Scanning: "+scannedPath);
            resources = classLoader.getResources(scannedPath);
        } catch (IOException e) {
        	MajnCorePlugin.instance.getLogger().info(e.getLocalizedMessage());
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage), e);
        }
        final List<Class<? extends MajnPlugin>> classes = new LinkedList<Class<? extends MajnPlugin>>();
        if(!resources.hasMoreElements())
        	MajnCorePlugin.instance.getLogger().info("Nothing in plugins found.");
        while (resources.hasMoreElements()) {
            final File file = new File(resources.nextElement().getFile());
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    private final static List<Class<? extends MajnPlugin>> find(final File file, final String scannedPackage) {
        final List<Class<? extends MajnPlugin>> classes = new LinkedList<Class<? extends MajnPlugin>>();
        final String resource = scannedPackage + DOT + file.getName();
    	MajnCorePlugin.instance.getLogger().info("Found package:"+resource);
        if (file.isDirectory()) {
            for (File nestedFile : file.listFiles()) {
                classes.addAll(find(nestedFile, scannedPackage));
            }
        } else if (resource.endsWith(CLASS_SUFFIX)) {
            final int beginIndex = 0;
            final int endIndex = resource.length() - CLASS_SUFFIX.length();
            final String className = resource.substring(beginIndex, endIndex);
            try {
            	MajnCorePlugin.instance.getLogger().info("Found class:"+className);
            	Class c=Class.forName(className);
            	if (MajnPlugin.class.isAssignableFrom(c))
            		classes.add(c);
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }

}