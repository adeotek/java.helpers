package com.hinter.java.utils;

import com.sun.jndi.toolkit.url.Uri;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public final class Helpers {
    private static final Logger appLogger = LogManager.getLogger(Helpers.class);

    public static boolean isStringEmptyOrNull(String input, boolean trimInput) {
        if(input == null) {
            return true;
        }
        if(trimInput) {
            return input.trim().length()==0;
        }
        return input.length()==0;
    }//isStringEmptyOrNull

    public static boolean isStringEmptyOrNull(String input) {
        return isStringEmptyOrNull(input, true);
    }//isStringEmptyOrNull

    public static String ltrimString(String input, String character) {
        if(input==null || input.length()==0) { return input; }
        String regEx = "^"+character+"+";
        return input.replaceAll(regEx, "");
    }//ltrimString

    public static String rtrimString(String input, String character) {
        if(input==null || input.length()==0) { return input; }
        String regEx = character+"+$";
        return input.replaceAll(regEx, "");
    }//rtrimString

    public static String trimString(String input, String character) {
        if(input==null || input.length()==0) { return input; }
        return rtrimString(ltrimString(input, character), character);
    }//trimString

    /**
     * Convert byte array to hex string
     * @param bytes
     * @return String
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for(int idx=0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }//bytesToHex

    /**
     * Get utf8 byte array.
     * @param str
     * @return  array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try { return str.getBytes("UTF-8"); } catch (Exception ex) { return null; }
    }//getUTF8Bytes

    public static String formatDate(Date dt, String format) {
        if(dt==null || isStringEmptyOrNull(format)) { return null; }
        DateFormat dateFormatter = new SimpleDateFormat(format);
        dateFormatter.setLenient(false);
        return dateFormatter.format(dt);
    }//formatDate

    public static String formatDate(Date dt) {
        return formatDate(dt, "yyyy-MM-dd HH:mm:ss");
    }//formatDate

    public static String formatDate(long ts, String format) {
        if(isStringEmptyOrNull(format)) { return null; }
        DateFormat dateFormatter = new SimpleDateFormat(format);
        dateFormatter.setLenient(false);
        return dateFormatter.format(new Date(ts));
    }//formatDate

    public static String formatDate(long ts) {
        return formatDate(ts, "yyyy-MM-dd HH:mm:ss");
    }//formatDate

    public static String getCurrentDateTime(String format) {
        if(isStringEmptyOrNull(format)) { return null; }
        DateFormat dateFormatter = new SimpleDateFormat(format);
        dateFormatter.setLenient(false);
        return dateFormatter.format(new Date());
    }//getCurrentDateTime

    public static String getCurrentDateTime() {
        return getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
    }//getCurrentDateTime

    public static String getCurrentTimeStampForFileName() {
        return getCurrentDateTime("yyyyMMdd_HHmmss");
    }//getCurrentTimeStampForFileName

    public static Date getDateFromString(String input, String format) {
        if(isStringEmptyOrNull(input) || isStringEmptyOrNull(format)) { return null; }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        Date result;
        try {
            result = dateFormatter.parse(input);
        } catch (ParseException e) {
            // silent
            result = null;
        }
        return result;
    }//getDateFromString

    public static Date getDateFromString(String input) {
        return getDateFromString(input, "yyyy-MM-dd HH:mm:ss");
    }//getDateFromString

    public static boolean checkIfFileExists(String fileName) {
        if(isStringEmptyOrNull(fileName)) { return false; }
        try {
            File file = new File(fileName);
            if(file.exists()) {
                return true;
            }
        } catch (Exception e) {
            // silent
        }
        return false;
    }//checkIfFileExists

    public static long getFileSize(File file) {
        try {
            if(file==null || !file.exists() || file.isDirectory()) { return 0; }
            return file.length();
        } catch (Exception e) {
            // silent
            return 0;
        }
    }//getFileSize

    public static long getFileSize(String fileName) {
        if(isStringEmptyOrNull(fileName)) { return 0; }
        try {
            return getFileSize(new File(fileName));
        } catch (Exception e) {
            // silent
            return 0;
        }
    }//getFileSize

    public static String getFileNameFromPath(String fullName) {
        if(isStringEmptyOrNull(fullName)) {
            return null;
        }
        return fullName.substring(fullName.lastIndexOf('/')+1);
    }//getFileNameFromPath

    public static String removeFileExtension(String fileName) {
        if(isStringEmptyOrNull(fileName)) {
            return null;
        }
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }//removeFileExtension

    public static String getFileExtension(String fileName) {
        if(isStringEmptyOrNull(fileName)) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }//getFileExtension

    public static String getPathFromFileName(String fileName, String fileSeparator) {
        if(isStringEmptyOrNull(fileName)) {
            return null;
        }
        String result;
        try {
            int cut = fileName.lastIndexOf('/');
            if (cut > -1) {
                result = fileName.substring(0, cut);
                if(fileSeparator.equals("\\")) {
                    result = result.replace("/", fileSeparator);
                    if(result.startsWith(fileSeparator)) {
                        result = result.substring(fileSeparator.length());
                    }
                }
            } else {
                result = "";
            }
        } catch (Exception e) {
            // silent
            result = null;
        }
        return result;
    }//getPathFromFileName

    public static String getPathFromUri(Uri fileUri, String fileSeparator) {
        try {
            return getPathFromFileName(fileUri.getPath(), fileSeparator);
        } catch (Exception e) {
            // silent
            return null;
        }
    }//getPathFromUri

    public static boolean createSubDirectory(String parent, String subDir) {
        try {
            File directory = new File(Helpers.rtrimString(parent, File.separator) + File.separator + subDir);
            if (directory.exists() && directory.isDirectory()) {
                return true;
            }
            if (directory.mkdirs()) {
                return true;
            }
        }catch (Exception e){
            // silent
        }
        return false;
    }//createSubDirectory

    public static String getSubDirectory(String parent, String subDir) {
        try {
            File directory = new File(Helpers.rtrimString(parent, File.separator) + File.separator + subDir);
            if (directory.exists() && directory.isDirectory()) {
                return Helpers.rtrimString(parent, File.separator) + File.separator + subDir + File.separator;
            }
            if (directory.mkdirs()) {
                return Helpers.rtrimString(parent, File.separator) + File.separator + subDir + File.separator;
            }
        } catch (Exception e) {
            // silent
        }
        return null;
    }//getSubDirectory

    public static void copyFile(File src, File dst, Boolean withDeleteSource) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        if(withDeleteSource) {
            src.delete();
        }
    }//copyFile

    public static boolean deleteFile(String fileName) {
        if(isStringEmptyOrNull(fileName)) { return false; }
        try {
            File f = new File(fileName);
            if(f.exists()) {
                if(f.delete()) {
                    return true;
                }
            }
        } catch (Exception e) {
            // silent
        }
        return false;
    }//deleteFile

    public static File[] getFileListing(String location, String endFilter){
        File[] files;
        try {
            File f = new File(location);
            final String lPattern = endFilter;
            files = f.listFiles((dir, name) -> name.endsWith(lPattern));
        }catch (Exception e){
            files = null;
        }
        return files;
    }//getFileListing

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }//convertStreamToString

    public static String getStringFromFile(File file) throws Exception {
        if(file==null || !file.exists() || file.isDirectory()) {
            throw new Exception("Invalid input file");
        }
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }//getStringFromFile

    public static String getStringFromFile(String file) throws Exception {
        if(isStringEmptyOrNull(file)) {
            throw new Exception("Invalid input file string");
        }
        return getStringFromFile(new File(file));
    }//getStringFromFile

    public static byte[] readBytes(InputStream inputStream) {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        // we need to know how may bytes were read to write them to the byteBuffer
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        } catch (Exception e) {
            return null;
        }
        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }//readBytes

    public static boolean writeToFile(String message, String fileName) {
        appLogger.debug("Triggered: -");
        if (Helpers.isStringEmptyOrNull(fileName)) {
            appLogger.error("Error: invalid file name");
            return false;
        }
        boolean success;
        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(file);
            writer.write(message);
            writer.flush();
            writer.close();
            success = true;
        } catch (IOException ioe) {
            appLogger.error("IOException: " + ioe.getMessage());
            success = false;
        } catch (Exception e) {
            appLogger.error("Exception: " + e.getMessage());
            success = false;
        }
        return success;
    }//writeToFile

    public static String getBase64EncodedFileContent(File file) {
        if(file==null || !file.exists() || file.isDirectory()) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] b = readBytes(fis);
            String encData = new String(Base64.getEncoder().encode(b));
            return encData;
        } catch(FileNotFoundException e) {
            return null;
        } catch(Exception ee) {
            return null;
        }
    }//getBase64EncodedFileContent

    public static String getBase64EncodedFileContent(String file) {
        if(isStringEmptyOrNull(file)) {
            return null;
        }
        return getBase64EncodedFileContent(new File(file));
    }//getBase64EncodedFileContent

    public static boolean isPortOpen(String hostname, int port, int timeout) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, port), timeout);
            socket.close();
            return true;
        } catch(ConnectException ce) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }//isPortOpen

    public static boolean isPortOpen(String hostname, int port) {
        return isPortOpen(hostname, port, 1000);
    }//isPortOpen

    public static String hash(String salt, String algorithm, boolean noTime) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance(algorithm);
        String input = "";
        if (!isStringEmptyOrNull(salt)) {
            input += salt;
        }
        if (!noTime) {
            input += Long.valueOf(System.currentTimeMillis()).toString();
        }
        if (isStringEmptyOrNull(input)) {
            return null;
        }
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }//hash

    public static String uidHash(String salt) throws NoSuchAlgorithmException {
        return hash(salt, "SHA1", false);
    }//uidHash

    public static String sha1(String input) throws NoSuchAlgorithmException {
        return hash(input, "SHA1", true);
    }//sha1

    public static String md5(String input) throws NoSuchAlgorithmException {
        return hash(input, "MD5", true);
    }//md5
}//Helpers
