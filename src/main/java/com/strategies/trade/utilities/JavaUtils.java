package com.strategies.trade.utilities;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JavaUtils {

    public static void hardWait(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO:
    public static String getEffectiveConfig(String keyValue, String configFileValue) {
        String property = System.getProperty(keyValue);
        String env = System.getenv(keyValue);

        if (env != null) {
            if (!env.equalsIgnoreCase("")) {
                return env;
            }
        } else if (property != null) {
            if (!property.equalsIgnoreCase("")) {
                return property;
            }
        }
        return configFileValue;
    }

    public static Double setDoublePrecision(Double d) {
        return BigDecimal.valueOf(d)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static String getCallerClassName() {
        List<String> allTestPackages = Arrays.asList(new String[]{"com.mckinsey.iService.mobileWebTests", "com.mckinsey.iService.apiTests", "com.mckinsey.iService.paymentAPIs", "com.mckinsey.iService.tests"});
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        Optional<String> collect = Arrays.asList(stElements).stream()
//                .map(item -> item.getClassName().substring(0, item.getClassName().lastIndexOf(".")))
                .filter(item -> allTestPackages.contains(item.getClassName().substring(0, item.getClassName().lastIndexOf("."))))
                .map(item -> item.getClassName().substring(item.getClassName().lastIndexOf(".") + 1))
                .reduce((first, second) -> second);
//        .findFirst();

        if (collect.isPresent()) {
            return collect.get();
        } else {
            return null;
        }
//        CustomLogging.witeInfo(collect);

//        for (int i=1; i<stElements.length; i++) {
//            StackTraceElement ste = stElements[i];
//
//            if (ste.getClass().getPackage().getPlanTitle().equalsIgnoreCase("com.mckinsey.iService.mobilePages")) {
//                return ste.getClassName();
//            }
//            if (!ste.getClassName().equals(KDebug.class.getPlanTitle()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
//                return ste.getClassName();
//            }
//        }
//        return null;
    }

    public static void deleteDirectoryStream(Path path) {
        try {
            if (path.toFile().exists()) {
                Files.walk(path)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int generateRandomIntIntRange() {
        int max = 999999;
        int min = 9999;
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static String trimStringOfMultipleLines(String str) {
        return Arrays.stream(str.split("\n"))
                .map(item -> item.trim())
                .collect(Collectors.joining("\n"));
    }

    public synchronized static <T> void serialize(String fileName, List<T> obj) {
        try {
            FileOutputStream f;
            ObjectOutputStream o;
            if (!Files.exists(Paths.get(fileName))) {
                f = new FileOutputStream(new File(fileName));
                o = new ObjectOutputStream(f);

            } else {
                f = new FileOutputStream(fileName, true);
                o = new ObjectOutputStream(f) {
                    protected void writeStreamHeader() throws IOException {
                        reset();
                    }
                };
            }


            for (T indObj : obj) {
                o.writeObject(indObj);
            }

            o.close();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> deSerialize(String fileName, Class<T> t) {
        List<T> allObjs = new ArrayList<>();
        try {

            FileInputStream fi = new FileInputStream(new File(fileName));
            ObjectInputStream oi = new ObjectInputStream(fi);
            while (fi.available() > 0) {
                // Read objects
                T pr1 = (T) oi.readObject();
                allObjs.add(pr1);
//                System.out.println(pr1.toString());
            }
            oi.close();
            fi.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return allObjs;
    }

    public static String getValidFileName(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    public static void writeToFile(String filePath, List<String> content) {
        boolean parentDirsCreated = new File(filePath).getParentFile().mkdirs();
        try {
            Files.write(Paths.get(filePath), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeToFile(String filePath, byte[] content) {
        boolean parentDirsCreated = new File(filePath).getParentFile().mkdirs();
        try {
            Files.write(Paths.get(filePath), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String executeCommand(String[] command, Long timeOutinSec) {
        CustomLogging.writeLog("About to execute command: " + Arrays.toString(command));

        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = null;
        try {
            process = pb.start();

            if (timeOutinSec == null) process.waitFor();
            else process.waitFor(timeOutinSec, TimeUnit.SECONDS);

            String collect = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));

            CustomLogging.writeLog("Executed command successfully with result: " + collect);
            return collect;
        } catch (IOException | InterruptedException ignored) {
            CustomLogging.writeLog("Not able to Execute command successfully");
            return "";
        } finally {
            if (process != null) {
                try {
                    process.getInputStream().close();
                    process.getOutputStream().close();
                    process.getErrorStream().close();
                } catch (IOException e) {
                    CustomLogging.writeLog("Exception occured while clearing the resources of ProcessBuilder");
                }
                process.destroy();
            }
        }
    }
    public static String executeCommand(String[] command) {
        return executeCommand(command, null);
    }
    public static boolean createFile(String filePath) {
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (parentFile != null) parentFile.mkdirs();

        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean containsCaseInsensitive(String s, List<String> l) {
        return l.stream().anyMatch(x -> x.equalsIgnoreCase(s));
    }

    public static boolean containsCaseInsensitive(String s, String[] l) {
        return containsCaseInsensitive(s, Arrays.asList(l));
    }

    public static String toString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        CustomLogging.writeLog(exceptionAsString);
        return exceptionAsString;
    }

    public static String readFile(String path) {
        return readFile(new File(path));
    }

    public static String readFile(File file) {
        String content = "";
        try {
            content = new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }
}
