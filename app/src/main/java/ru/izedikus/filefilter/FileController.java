package ru.izedikus.filefilter;

import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.models.Statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

public class FileController {
    public static Statistics generateStatisticsAndOutputFiles(Arguments args) {
        Long countInt = 0L;
        Long countFloat = 0L;
        Long countString = 0L;

        BigInteger sumInt = new BigInteger("0");
        BigInteger minInt = new BigInteger("0");
        BigInteger maxInt = new BigInteger("0");
        BigInteger avgInt = new BigInteger("0");

        BigDecimal sumFloat = new BigDecimal("0.0");
        BigDecimal minFloat = new BigDecimal("0.0");
        BigDecimal maxFloat = new BigDecimal("0.0");
        BigDecimal avgFloat = new BigDecimal("0.0");

        Long minStringLen = 0L;
        Long maxStringLen = 0L;



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

    class LazyFileWriter extends Writer {
        private FileWriter realWriter;
        private final String fileName;
        private final boolean append;

        public LazyFileWriter(String fileName, boolean append) {
            this.fileName = fileName;
            this.append = append;
        }

        private void init() throws IOException {
            if (realWriter == null) {
                realWriter = new FileWriter(fileName, append);
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
