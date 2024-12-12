package org.sample;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class MyBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MLDSA2Benchmark.class.getSimpleName())
                .forks(5) // Número de forks
                .warmupIterations(10) // Iterações de aquecimento
                .measurementIterations(100) // Iterações de medição
                .build();

        new Runner(opt).run();
    }
}
