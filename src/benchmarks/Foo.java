package benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@State(Scope.Thread)
public class Foo {
    public int shiftVal = 1;
    public int val = 1;

    @Benchmark
    public void measureShift() {
        shiftVal = shiftVal << 2;
    }


    @Benchmark
    public void measureMultiply() {
        val = val * 2;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(Foo.class.getSimpleName())
            .threads(4)
            .warmupIterations(10)
            .forks(1)
            .build();

        new Runner(opt).run();
    }
}
