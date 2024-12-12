package org.sample;

import org.openjdk.jmh.annotations.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.TimeUnit;
import java.security.*;
import java.math.BigInteger;

// @BenchmarkMode(Mode.All) // Mede o tempo médio
@BenchmarkMode(Mode.AverageTime) // Mede o tempo médio
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 100, time = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS) // Resultados em nanosegundos
@State(Scope.Thread) // Estado isolado por thread
public class ECDSABenchmark {

    private byte[] message;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private BigInteger[] signature;

    @Setup(Level.Invocation)
    public void setup() throws Exception {
        message = "651A8CE312285A4D402A739E6F0E0B7F800DAB68DF032F0CB5A3EF46195BECEA".getBytes();
        KeyPair keyPair = ECDSA.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
        signature = ECDSA.signMessage(message, privateKey);
    }

    @Benchmark
    public void benchmarkKeyGeneration() throws Exception {
        KeyPair keyPair = ECDSA.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    @Benchmark
    public void benchmarkSigning() {
        signature = ECDSA.signMessage(message, privateKey);
    }

    @Benchmark
    public void benchmarkVerification() {
        boolean isVerified = ECDSA.verifySignature(message, signature, publicKey);
    }
}
