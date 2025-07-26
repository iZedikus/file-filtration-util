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
    public static Statistics generateStatisticsAndOutputFiles(Arguments args) {
        try (LazyFileWriter intsWriter = new LazyFileWriter("ints.txt", args.addFlag());
             LazyFileWriter floatsWriter = new LazyFileWriter("floats.txt", args.addFlag());
             LazyFileWriter stringsWriter = new LazyFileWriter("strings.txt", args.addFlag())) {

            TokenHandler processor = new TokenProcessor();

            handleData(args, intsWriter, floatsWriter, stringsWriter, processor);

            return processor.getStatistics();

        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка при создании файлов", e);
        }
    }

    static void handleData(
            Arguments args,
            LazyFileWriter intsWriter,
            LazyFileWriter floatsWriter,
            LazyFileWriter stringsWriter,
            TokenHandler processor
    ) {
        List<String> filePaths = args.inputFiles();
        for (String path : filePaths) {
            try (Scanner scanner = new Scanner(new File(path))) {
                handleScanner(scanner, intsWriter, floatsWriter, stringsWriter, processor);
            } catch (FileNotFoundException e) {
                System.out.println("Возникли трудности с обработкой файла " + path);
            }
        }
    }

    static void handleScanner(
            Scanner scanner,
            LazyFileWriter intsWriter,
            LazyFileWriter floatsWriter,
            LazyFileWriter stringsWriter,
            TokenHandler processor
    ) {
        while (scanner.hasNext()) {
            try {
                if (scanner.hasNextBigInteger()) {
                    BigInteger value = scanner.nextBigInteger();
                    processor.handleInteger(value);
                    intsWriter.write(value + System.lineSeparator());

                } else if (scanner.hasNextBigDecimal()) {
                    BigDecimal value = scanner.nextBigDecimal();
                    processor.handleFloat(value);
                    floatsWriter.write(value + System.lineSeparator());

                } else {
                    String value = scanner.next();
                    processor.handleString(value);
                    stringsWriter.write(value + System.lineSeparator());
                }
            } catch (IOException e) {
                System.out.println("Ошибка при записи в выходные файлы:" + e.getMessage());
                return;
            }
        }
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
