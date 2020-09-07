package com.zizhizhan.legacies.core.scan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.zizhizhan.legacies.core.reflect.ReflectionHelper;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

@Slf4j
public abstract class ClassScanner {

    private final static ClassLoader classloader = ReflectionHelper.getContextClassLoader();

    private ClassScanner() {

    }

    public static void scan(ClassVisitor classVisitor, File... paths) {
        for (File file : paths) {
            index(file, classVisitor);
        }
    }

    public static void scan(ClassVisitor classVisitor, String... packages) {
        for (String p : packages) {
            try {
                String fileP = p.replace('.', '/');
                Enumeration<URL> urls = classloader.getResources(fileP);

                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    try {
                        URI uri = getURI(url);
                        index(uri, fileP, classVisitor);
                    } catch (URISyntaxException e) {
                        log.warn("URL, " + url + ", cannot be converted to a URI", e);
                    }
                }
            } catch (IOException ex) {
                String s = "The resources for the package" + p + ", could not be obtained";
				log.error(s);
                throw new RuntimeException(s, ex);
            }
        }
    }

    private static void index(File file, ClassVisitor classVisitor) {
        if (file.isDirectory()) {
            indexDir(file, true, classVisitor);
        } else if (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")) {
            indexJar(file, classVisitor);
        } else {
			log.warn("File, " + file.getAbsolutePath()
                    + ", is ignored, it not a directory, a jar file or a zip file");
        }
    }

    private static void index(URI u, String filePackageName, ClassVisitor classVisitor) {
        String scheme = u.getScheme();
        if (scheme.equals("file")) {
            File f = new File(u.getPath());
            if (f.isDirectory()) {
                indexDir(f, false, classVisitor);
            } else {
				log.warn("URL, " + u + ", is ignored. The path, " + f.getPath() + ", is not a directory");
            }
        } else if (scheme.equals("jar") || scheme.equals("zip")) {
            URI jarUri = URI.create(u.getRawSchemeSpecificPart());
            String jarFile = jarUri.getPath();
            jarFile = jarFile.substring(0, jarFile.indexOf('!'));
            indexJar(new File(jarFile), filePackageName, classVisitor);
        } else {
			log.warn("URL, " + u + ", is ignored, it not a file or a jar file URL");
        }
    }

    private static void indexDir(File root, boolean indexJars, ClassVisitor classVisitor) {
        for (File child : root.listFiles()) {
            if (child.isDirectory()) {
                indexDir(child, indexJars, classVisitor);
            } else if (indexJars && child.getName().endsWith(".jar")) {
                indexJar(child, classVisitor);
            } else if (child.getName().endsWith(".class")) {
                analyzeClassFile(child.toURI(), classVisitor);
            }
        }
    }

    private static void indexJar(File file, ClassVisitor classVisitor) {
        indexJar(file, "", classVisitor);
    }

    private static void indexJar(File file, String parent, ClassVisitor classVisitor) {
        final JarFile jar = getJarFile(file);
        try {
            final Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry e = entries.nextElement();
                if (!e.isDirectory() && e.getName().startsWith(parent) && e.getName().endsWith(".class")) {
                    analyzeClassFile(jar, e, classVisitor);
                }
            }
        } catch (Exception e) {
			log.error("Exception while processing file, " + file, e);
        } finally {
            try {
                if (jar != null) {
                    jar.close();
                }
            } catch (IOException ex) {
                String s = "Error closing jar file, " + jar.getName();
				log.error(s);
            }
        }

    }

    private static JarFile getJarFile(File file) {
        if (file == null) {
            return null;
        }
        try {
            return new JarFile(file);
        } catch (IOException ex) {
            String s = "File, " + file.getAbsolutePath() + ", is not a jar file";
			log.error(s);
            throw new RuntimeException(s, ex);
        }
    }

    private static URI getURI(URL url) throws URISyntaxException {
        if (url.getProtocol().equalsIgnoreCase("vfsfile")) {
            return new URI(url.toString().substring(3));
        } else {
            return url.toURI();
        }
    }

    private static void analyzeClassFile(URI classFileUri, ClassVisitor classVisitor) {
        getClassReader(classFileUri).accept(classVisitor, 0);
    }

    private static void analyzeClassFile(JarFile jarFile, JarEntry entry, ClassVisitor classVisitor) {
        getClassReader(jarFile, entry).accept(classVisitor, 0);
    }

    private static ClassReader getClassReader(JarFile jarFile, JarEntry entry) {
        InputStream is = null;
        try {
            is = jarFile.getInputStream(entry);
            ClassReader cr = new ClassReader(is);
            return cr;
        } catch (IOException ex) {
            String s = "Error accessing input stream of the jar file, " +
                    jarFile.getName() + ", entry, " + entry.getName();
			log.error(s);
            throw new RuntimeException(s, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                String s = "Error closing input stream of the jar file, " +
                        jarFile.getName() + ", entry, " + entry.getName() + ", closed.";
				log.error(s);
            }
        }
    }

    private static ClassReader getClassReader(URI classFileUri) {
        InputStream is = null;
        try {
            is = classFileUri.toURL().openStream();
            ClassReader cr = new ClassReader(is);
            return cr;
        } catch (IOException ex) {
            String s = "Error accessing input stream of the class file URI, " + classFileUri;
			log.error(s);
            throw new RuntimeException(s, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                String s = "Error closing input stream of the class file URI, " + classFileUri;
				log.error(s);
            }
        }
    }


}
