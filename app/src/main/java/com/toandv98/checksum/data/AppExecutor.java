package com.toandv98.checksum.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static final int NUMBER_CPU = Runtime.getRuntime().availableProcessors();

    private AppExecutor() {
    }

    private static class Holder {
        private static final ExecutorService INSTANCE = Executors.newFixedThreadPool(NUMBER_CPU);
    }

    public static ExecutorService getInstance() {
        return Holder.INSTANCE;
    }
}
