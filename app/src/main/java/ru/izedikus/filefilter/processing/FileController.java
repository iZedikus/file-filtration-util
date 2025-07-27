package ru.izedikus.filefilter.processing;

import ru.izedikus.filefilter.exceptions.IncorrectOutputPathException;
import ru.izedikus.filefilter.exceptions.ZeroInputFilesException;
import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.models.Statistics;
import ru.izedikus.filefilter.output.OutputController;
import ru.izedikus.filefilter.output.OutputMessage;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class FileController {
    public static Statistics generateStatisticsAndOutputFiles(Arguments args) {
        String outputPath = args.outputPathIfBeenFlagged().isBlank()
                ? Paths.get("").toAbsolutePath().toString()
                : args.outputPathIfBeenFlagged();
        String prefix = args.prefixIfBeenFlagged();
        boolean append = args.addFlag();

        try (LazyFileWriter intsWriter = new LazyFileWriter(Paths.get(outputPath + prefix + "ints.txt"), append);
             LazyFileWriter floatsWriter = new LazyFileWriter(Paths.get(outputPath + prefix + "floats.txt"), append);
             LazyFileWriter stringsWriter = new LazyFileWriter(Paths.get(outputPath + prefix + "strings.txt"), append)) {

            TokenHandler processor = args.fullStatsFlag()
                    ? new TokenFullStatsProcessor()
                    : new TokenShortStatsProcessor();

            handleData(args, intsWriter, floatsWriter, stringsWriter, processor);

            return processor.getStatistics();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
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
        if (args.inputFiles().isEmpty()) {
            throw new ZeroInputFilesException();
        }
        for (String path : filePaths) {
            try (Scanner scanner = new Scanner(new File(path))) {
                handleScanner(scanner, intsWriter, floatsWriter, stringsWriter, processor);
            } catch (FileNotFoundException e) {
                OutputController.printMessage(OutputMessage.INPUT_FILE_NOT_FOUND, path);
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
                throw new IncorrectOutputPathException(e.getMessage());
            }
        }
    }

    static class LazyFileWriter extends Writer {
        BufferedWriter realWriter;
        final Path filePath;
        final boolean append;

        public LazyFileWriter(Path filePath, boolean append) {
            this.filePath = filePath;
            this.append = append;
        }

        private void init() throws IOException {
            if (realWriter == null) {
                Files.createDirectories(filePath.getParent());
                realWriter = new BufferedWriter(
                        new FileWriter(filePath.toString(), append),
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
