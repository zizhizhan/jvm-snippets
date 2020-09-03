/**
 * Copyright 2006 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.zizhizhan.legacies.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.ArrayList;
import java.util.zip.CRC32;


/**
 * Static methods to aid in file and directory manipulation.
 *
 * @author <a href="mailto:gmolik@hotels.com">Gregory Molik</a>
 */
public class FileUtils {
    /**
     * No instantiation of this class...
     */
    private FileUtils() {
    }

    /**
     * Returns files in a directory. If tree is true, the method will return all
     * files in the tree. If relative is true, the files will be returned
     * relative to the specified directory, else returned with the full path.
     *
     * @param directory source directory
     * @param recursive true to return all files in the directory tree
     * @param relative  true to return the file list relative to the source directory
     * @return ArrayList list of files
     */
    static public List getFiles(File directory, boolean recursive,
                                boolean relative) {
        return getFiles(new File(directory, File.separator), directory,
                recursive, relative, null);
    }

    /**
     * Returns files in a directory. If tree is true, the method will return all
     * files in the tree. If relative is true, the files will be returned
     * relative to the specified directory, else returned with the full path.
     *
     * @param directory source directory
     * @param recursive true to return all files in the directory tree
     * @param relative  true to return the file list relative to the source directory
     * @param filter    FilenameFilter to use on all files
     * @return ArrayList list of files
     */
    static public List getFiles(File directory, boolean recursive,
                                boolean relative, FilenameFilter filter) {
        return getFiles(new File(directory, File.separator), directory,
                recursive, relative, filter);
    }

    /**
     * Copies files from the source to destination directory. This is
     * non-transactional. If a file copy should fail during processing, the
     * previously copied files are not removed or reverted.
     *
     * @param source      source directory
     * @param destination destination directory
     * @param recursive   true to copy the complete source tree
     * @return int total number of files copied
     * @throws Exception if the copy fails
     */
    static public int copyDirectory(File source, File destination,
                                    boolean recursive, boolean verify) throws Exception {
        File parent = destination.getParentFile();
        parent.mkdirs();
        List files = getFiles(source, recursive, true);
        for (int i = 0; i < files.size(); i++) {
            File from = new File(source + File.separator + files.get(i));
            File to = new File(destination + File.separator + files.get(i));
            copyFile(from, to, verify);
        }
        return files.size();
    }

    /**
     * Copies a file from source to destination. If verify is true, the CRC-32
     * values will be compared. If they are not identical, and exception is
     * thrown. If a copy should fail the verification process, the file is not
     * removed or reverted.
     *
     * @param source      source directory
     * @param destination destination directory
     * @param verify      do CRC32 verification of the content
     * @throws Exception if the copy or the CRC check fails
     */
    static public void copyFile(File source, File destination, boolean verify)
            throws Exception {
        copyFile(source, destination);
        if (verify) {
            long src = getCrc(source);
            long dest = getCrc(destination);
            if (src != dest) {
                throw new IOException(
                        "Error during file copy.  CRC does not match.");
            }
        }
    }

    /**
     * Recursive implementation for the getFiles(File, boolean, boolean) method.
     *
     * @param base      base directory
     * @param directory directory relative to the base directory
     * @param recursive true to return the complete tree of files
     * @param relative  true to return the tree relative to the base directory
     * @return ArrayList list of files
     */
    static private List getFiles(File base, File directory,
                                 boolean recursive, boolean relative, FilenameFilter filter) {
        ArrayList list = new ArrayList();
        if (directory.isFile()) {
            if (relative) {
                if (directory.toString().startsWith(base.toString())) {
                    directory = new File(directory.toString().substring(
                            base.toString().length()));
                }
            }
            list.add(directory);
        } else {
            File[] files = directory.listFiles(filter);
            for (int i = 0; files != null && i < files.length; i++) {
                if (recursive) {
                    list.addAll(getFiles(base, files[i], recursive, relative,
                            filter));
                } else {
                    list.add(files[i]);
                }
            }
        }
        return list;
    }

    /**
     * Copies a file from source to destination. If the copy should fail, the
     * resulting state is undefined.
     *
     * @param source      source file
     * @param destination destination file
     * @throws Exception if the file copy fails
     */
    static private void copyFile(File source, File destination)
            throws Exception {
        if (!destination.getParentFile().exists()) {
            if (!destination.getParentFile().exists()) {
                destination.getParentFile().mkdirs();
            }
            destination.createNewFile();
        }
        FileChannel srcChannel = new FileInputStream(source).getChannel();
        FileChannel destChannel = new FileOutputStream(destination)
                .getChannel();
        destChannel.lock();
        destChannel.transferFrom(srcChannel, 0, srcChannel.size());
        destChannel.close();
        srcChannel.close();
    }

    /**
     * Returns the CRC-32 value of a file. Use carefully. The CRC check will
     * load the complete file into memory before processing.
     *
     * @param file
     * @return long - the CRC-32 value
     * @throws Exception any number of possible exceptions
     */
    static private long getCrc(File file) throws IOException {
        FileChannel channel = new FileInputStream(file).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
        channel.read(buffer);
        channel.close();
        CRC32 crc = new CRC32();
        crc.update(buffer.array());
        return crc.getValue();
    }

}
