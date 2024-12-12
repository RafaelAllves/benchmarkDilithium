package org.sample;

import org.openjdk.jmh.annotations.*;
import org.openquantumsafe.Signature;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.TimeUnit;

// @BenchmarkMode(Mode.All) // Mede o tempo médio
@BenchmarkMode(Mode.AverageTime) // Mede o tempo médio
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 100, time = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS) // Resultados em nanosegundos
@State(Scope.Thread) // Estado isolado por thread
public class MLDSA3Benchmark {

    private byte[] message;
    private Signature sig;
    private byte[] publicKey;
    private byte[] privateKey;
    private byte[] signature;

    @Setup(Level.Invocation)
    public void setup() {
        message = "651A8CE312285A4D402A739E6F0E0B7F800DAB68DF032F0CB5A3EF46195BECEA".getBytes();
        sig = new Signature("Dilithium3");
        publicKey = sig.generate_keypair();
        privateKey = sig.export_secret_key();
        signature = sig.sign(message);
    }

    @Benchmark
    public void benchmarkKeyGeneration() {
        Signature sig = new Signature("Dilithium3");
        byte[] publicKey = sig.generate_keypair();
        byte[] privateKey = sig.export_secret_key();
    }

    @Benchmark
    public void benchmarkSigning() {
        byte[] signature = sig.sign(message);
    }

    @Benchmark
    public void benchmarkVerification() {
        boolean isVerified = sig.verify(message, signature, publicKey);
    }

    @Benchmark
    public long measureMemoryUsage() {
        return getMemoryUsed();
    }

    @Benchmark
    public long measureCpuUsage() {
        return getCpuTime();
    }

    private long getCpuTime() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    private long getMemoryUsed() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return heapUsage.getUsed();
    }
}
