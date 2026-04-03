import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FrequencyCounter
 *
 * Responsabilitati:
 *  - Calculeaza frecventa cuvintelor pentru un singur chunk (HashMap local).
 *  - Proceseaza secvential toate chunk-urile si retine hartile individuale
 *    (necesare ulterior pentru calculul IDF in TFIDFCalculator).
 *  - Combina toate hartile locale intr-un HashMap global cu frecventele totale.
 */
public class FrequencyCounter {

    /**
     * Rezultatul procesarii: frecventa globala + frecventele per chunk.
     */
    public static class Result {
        /** Frecventa totala a fiecarui cuvant in intregul document. */
        public final Map<String, Integer> globalFreq;

        /** Frecventele individuale per chunk (folosite pentru calculul IDF). */
        public final List<Map<String, Integer>> chunkFreqs;

        public Result(Map<String, Integer> globalFreq,
                      List<Map<String, Integer>> chunkFreqs) {
            this.globalFreq = globalFreq;
            this.chunkFreqs = chunkFreqs;
        }
    }

    /**
     * Proceseaza secvential toate chunk-urile si returneaza frecventele.
     *
     * @param chunks  lista de chunk-uri produsa de ChunkSplitter
     * @return        obiect Result cu harta globala si hartile per chunk
     */
    public Result compute(List<String[]> chunks) {
        Map<String, Integer> globalFreq = new HashMap<>();
        List<Map<String, Integer>> chunkFreqs = new ArrayList<>(chunks.size());

        for (String[] chunk : chunks) {
            Map<String, Integer> chunkMap = computeChunk(chunk);
            chunkFreqs.add(chunkMap);

            // Merge harta locala in cea globala
            for (Map.Entry<String, Integer> entry : chunkMap.entrySet()) {
                globalFreq.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }

        return new Result(globalFreq, chunkFreqs);
    }

    /**
     * Calculeaza frecventa cuvintelor dintr-un singur chunk.
     *
     * @param chunk  array de cuvinte al unui chunk
     * @return       HashMap cu frecventele cuvintelor din acel chunk
     */
    private Map<String, Integer> computeChunk(String[] chunk) {
        Map<String, Integer> freq = new HashMap<>();
        for (String word : chunk) {
            freq.merge(word, 1, Integer::sum);
        }
        return freq;
    }
}
