package ru.izedikus.filefilter;

import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.models.Statistics;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileController {
    public static Statistics generateStatisticsAndOutputFiles(Arguments args) throws IOException {
        Long countInt = 0L;
        Long countFloat = 0L;
        Long countString = 0L;

        BigInteger sumInt = new BigInteger("0");
        BigInteger minInt = new BigInteger("0");
        BigInteger maxInt = new BigInteger("0");
        BigDecimal avgInt = new BigDecimal("0.0");

        BigDecimal sumFloat = new BigDecimal("0.0");
        BigDecimal minFloat = new BigDecimal("0.0");
        BigDecimal maxFloat = new BigDecimal("0.0");
        BigDecimal avgFloat = new BigDecimal("0.0");

        int minStringLen = Integer.MAX_VALUE;
        int maxStringLen = 0;

        boolean isAppend = args.addFlag();
        LazyFileWriter intsFileWriter = new LazyFileWriter("ints.txt", isAppend);
        LazyFileWriter floatsFileWriter = new LazyFileWriter("floats.txt", isAppend);
        LazyFileWriter stringsFileWriter = new LazyFileWriter("strings.txt", isAppend);

        List<Scanner> scanners = getInputFileScanners(args.inputFiles());

        for (Scanner scanner : scanners) {
            if (scanner.hasNextBigInteger()) {
                BigInteger curInt = scanner.nextBigInteger();
                countInt++;

                sumInt = sumInt.add(curInt);
                if (curInt.compareTo(minInt) < 0) {
                    minInt = curInt;
                } else if (curInt.compareTo(maxInt) > 0) {
                    maxInt = curInt;
                }

                String integerStr = curInt.toString();
                intsFileWriter.write(integerStr, 0, integerStr.length());

            } else if (scanner.hasNextBigDecimal()) {
                BigDecimal curFloat = scanner.nextBigDecimal();
                countFloat++;

                sumFloat = sumFloat.add(curFloat);
                if (curFloat.compareTo(minFloat) < 0) {
                    minFloat = curFloat;
                } else if (curFloat.compareTo(maxFloat) > 0) {
                    maxFloat = curFloat;
                }

                String floatStr = curFloat.toString();
                floatsFileWriter.write(floatStr, 0, floatStr.length());

            } else {
                String curString = scanner.next();
                countString++;

                if (curString.length() > maxStringLen) {
                    maxStringLen = curString.length();
                } else if (curString.length() < minStringLen) {
                    minStringLen = curString.length();
                }

                stringsFileWriter.write(curString, 0, curString.length());
            }
        }

        return new Statistics(
                countInt,
                countFloat,
                countString,
                sumInt,
                minInt,
                maxInt,
                avgInt,
                sumFloat,
                minFloat,
                maxFloat,
                avgFloat,
                minStringLen,
                maxStringLen
        );
    }

    static List<Scanner> getInputFileScanners(List<String> filePaths) {
        List<Scanner> scanners = new ArrayList<>();
        for (String filePath : filePaths) {
            try {
                scanners.add(new Scanner(new File(filePath)));
            } catch (NullPointerException | FileNotFoundException e) {
                // FIXME: Syserr файл не существует
            }
        }
        if (scanners.isEmpty()) {
            // FIXME: Syserr пригодных к обработке файлов нет
        }

        return scanners;
    }

    static class LazyFileWriter extends Writer {
        private BufferedWriter realWriter;
        private final String fileName;
        private final boolean append;

        public LazyFileWriter(String fileName, boolean append) {
            this.fileName = fileName;
            this.append = append;
        }

        private void init() throws IOException {
            if (realWriter == null) {
                realWriter = new BufferedWriter(
                        new FileWriter(fileName, append),
                        8192
                );
            }
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            init();
            realWriter.write(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException {
            if (realWriter != null) {
                realWriter.flush();
            }
        }

        @Override
        public void close() throws IOException {
            if (realWriter != null) {
                realWriter.close();
            }
        }
    }
}
