package perf;

import com.google.protobuf.CodedOutputStream;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Java benchmark for https://github.com/thesamet/protobuf-issue-6977
 *
 * === 3.7.0
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10  29,559 ± 0,499  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  71,854 ± 0,623  ops/us
 *
 * === 3.8.0
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   2,178 ± 0,016  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  33,218 ± 0,285  ops/us
 *
 * === 3.19.4
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   2,164 ± 0,027  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  32,970 ± 0,217  ops/us
 *
 * === 3.19.4 (reverted to 3.7.0 code)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10  29,605 ± 0,501  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  44,623 ± 1,239  ops/us
 *
 * === 3.19.4 (without unsafe "optimization")
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10  30,740 ± 0,287  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  74,092 ± 0,785  ops/us
 *
 *
 *
 *
 * === 3.19.4 (Zulu 1.8 - Hotspot)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   2,167 ± 0,029  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  33,069 ± 0,259  ops/us
 *
 * === 3.19.4 (Zulu 11 - Hotspot)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   2,145 ± 0,027  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  33,125 ± 0,396  ops/us
 *
 * === 3.19.4 (Zulu 17 - Hotspot)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   2,603 ± 0,114  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  33,221 ± 0,433  ops/us
 *
 * === 3.19.4 (IBM Semeru 1.8 - OpenJ9)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   3,168 ± 0,027  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  23,607 ± 0,517  ops/us
 *
 * === 3.19.4 (IBM Semeru 11 - OpenJ9)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   2,747 ± 0,471  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  23,053 ± 0,371  ops/us
 *
 * == 3.19.4 (IBM Semeru 17 - OpenJ9)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   3,052 ± 0,056  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  23,341 ± 0,526  ops/us
 *
 * == 3.19.4 (Graal 17 v22 CE)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10  30,300 ± 1,755  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  64,162 ± 3,061  ops/us
 *
 *
 *
 *
 *
 * === 3.19.4 - w/o main path (Zulu 1.8 - Hotspot)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10  30,519 ± 1,438  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  75,816 ± 0,410  ops/us
 *
 * === 3.19.4 - w/o main path (Zulu 11 - Hotspot)
 * Benchmark                              Mode  Cnt    Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   31,747 ± 0,662  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  132,609 ± 0,993  ops/us
 *
 * === 3.19.4 - w/o main path (Zulu 17 - Hotspot)
 * Benchmark                              Mode  Cnt    Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   31,331 ± 0,479  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  138,394 ± 1,933  ops/us
 *
 * === 3.19.4 - w/o main path (IBM Semeru 1.8 - OpenJ9)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   8,732 ± 0,182  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  26,527 ± 2,117  ops/us
 *
 * === 3.19.4 - w/o main path (IBM Semeru 11 - OpenJ9)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   8,891 ± 0,098  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  29,003 ± 3,290  ops/us
 *
 * == 3.19.4 - w/o main path (IBM Semeru 17 - OpenJ9)
 * Benchmark                              Mode  Cnt   Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   8,866 ± 0,112  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  27,589 ± 2,277  ops/us
 *
 * == 3.19.4 - w/o main path (Graal 17 v22 CE)
 * Benchmark                              Mode  Cnt    Score   Error   Units
 * WriteUint32Bench.testWriteInt32NoTag  thrpt   10   32,001 ± 0,661  ops/us
 * WriteUint32Bench.testWriteMessage     thrpt   10  146,126 ± 3,125  ops/us
 *
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(2)
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class WriteUint32Bench {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(".*" + WriteUint32Bench.class.getSimpleName() + ".*")
                .verbosity(VerboseMode.NORMAL)
                .build();
        new Runner(options).run();
    }

    Msg.Message3 msg = Msg.Message3.newBuilder().setA(4).setB(5).setC(6).build();
    byte[] b = new byte[1000];

    @Benchmark
    public byte[] testWriteMessage() throws IOException {
        return msg.toByteArray();
    }

    @Benchmark
    public int testWriteInt32NoTag() throws IOException {
        CodedOutputStream cos = CodedOutputStream.newInstance(b);
        for (int i = 0; i < 100; i++) {
            cos.writeInt32NoTag(i);
        }
        return cos.getTotalBytesWritten();
    }

}
