package ru.izedikus.filefilter.output;

/**
 * Represents all output messages used in the application.
 * Supports string formatting via {@link #getValue(Object...)}.
 */
public enum OutputMessage {

    // --- Flag warnings ---
    INCORRECT_FLAG("Неопознанный флаг %s, он будет проигнорирован"),

    // --- Prefix ---
    INCORRECT_PREFIX("Префикс содержит недопустимые символы /\\*:?|\"<> и будет проигнорирован"),
    MISSING_PREFIX("Объявленный префикс не указан. Укажите его в формате <flag> <prefix>"),
    MANY_PREFIXES("Обнаружено несколько префиксов, использован будет только первый"),

    // --- Output ---
    INCORRECT_OUTPUT_PATH("Невозможно использовать путь: %s, выходные файлы будут размещены в каталоге с программой"),
    MISSING_OUTPUT_PATH("Объявленный путь выходных файлов не указан. Укажите его в формате <flag> <path>"),
    MANY_OUTPUTS("Обнаружено несколько путей к выходным файлам, использован будет только первый"),

    // --- Multiplicity ---
    MANY_ADDS("Обнаружено несколько флагов -a или --add. Зачем вам столько?"),
    MANY_FULLS("Обнаружено несколько флагов -a или --full. Зачем вам столько?"),
    MANY_SHORTS("Обнаружено несколько флагов -s или --short. Зачем вам столько?"),
    SHORT_AND_FULL("Обнаружено одновременное использование флагов -s и -f, использован будет только первый из указанных"),

    // --- Input file ---
    INPUT_FILE_NOT_FOUND("Входной файл %s не найден"),

    // --- Runtime Exceptions ---
    ZERO_INPUT_FILES_EXCEPTION("Пригодных к обработке файлов не обнаружено, программа завершает работу. Создайте и укажите не пустые .txt файлы для корректного запуска"),
    INCORRECT_OUTPUT_PATH_EXCEPTION("Запись в указанном каталоге невозможна, программа завершает работу. Как у вас это получилось?"),

    // --- Summary ---
    SUCCESS("Файлы успешно были обработаны!"),

    // --- Short statistics ---
    SHORT_STATS("КРАТКАЯ СТАТИСТИКА:"),
    SHORT_STATS_INT("Целых чисел обнаружено: %s"),
    SHORT_STATS_FLOAT("Вещественных чисел обнаружено: %s"),
    SHORT_STATS_STRING("Строк обнаружено: %s"),

    // --- Full statistics ---
    FULL_STATS("ПОЛНАЯ СТАТИСТИКА:"),
    FULL_STATS_INT("""
            Целых чисел обнаружено: %s
            Сумма обнаруженных целых чисел: %s
            Минимальное обнаруженное целое число: %s
            Максимальное обнаруженное целое число: %s
            Среднее значение обнаруженных целых чисел: %s
            """),
    FULL_STATS_FLOAT("""
            Вещественных чисел обнаружено: %s
            Сумма обнаруженных вещественных чисел: %s
            Минимальное обнаруженное вещественное число: %s
            Максимальное обнаруженное вещественное число: %s
            Среднее значение обнаруженных вещественных чисел: %s
            """),
    FULL_STATS_STRING("""
            Строк обнаружено: %s
            Строка минимальной длины: %s
            Строка максимальной длины: %s
            """),

    // --- End message ---
    END("Программа завершает работу");


    private final String value;

    OutputMessage(String text) {
        this.value = text;
    }

    /**
     * Returns the formatted value of the message using optional arguments.
     *
     * @param args arguments to insert into the message
     * @return formatted string
     */
    public String getValue(Object... args) {
        return String.format(value, args);
    }
}
