import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TFIDFCalculator
 *
 * Responsabilitati:
 *  - Primeste frecventele globale si per chunk de la FrequencyCounter.
 *  - Calculeaza scorul TF-IDF pentru fiecare cuvant din vocabular.
 *  - Returneaza lista sortata descrescator pentru afisarea Top 10.
 *
 * Formula:
 *   TF  = count(cuvant) / total_cuvinte_document
 *   IDF = ln( N / df )
 *         N  = numarul total de chunk-uri (tratate ca "documente")
 *         df = numarul de chunk-uri in care apare cuvantul
 *   TF-IDF = TF * IDF
 *
 * Nota: cuvintele prezente in toate chunk-urile vor avea IDF = 0
 * si sunt astfel eliminate natural din top (efect de penalizare).
 */
public class TFIDFCalculator {

    /**
     * Calculeaza scorul TF-IDF pentru toate cuvintele.
     *
     * @param globalFreq   frecventa totala per cuvant (din FrequencyCounter)
     * @param chunkFreqs   frecventele individuale per chunk
     * @param totalChunks  numarul total de chunk-uri
     * @return             map cuvant -> scor TF-IDF
     */
    public Map<String, Double> compute(Map<String, Integer> globalFreq,
                                       List<Map<String, Integer>> chunkFreqs,
                                       int totalChunks) {

        int totalWords = globalFreq.values()
                                   .stream()
                                   .mapToInt(Integer::intValue)
                                   .sum();

        // Document frequency: in cate chunk-uri apare fiecare cuvant
        Map<String, Integer> docFreq = buildDocFreq(chunkFreqs);

        Map<String, Double> tfidf = new HashMap<>();

        for (Map.Entry<String, Integer> entry : globalFreq.entrySet()) {
            String word  = entry.getKey();
            int    count = entry.getValue();

            double tf  = (double) count / totalWords;
            double idf = Math.log((double) totalChunks
                                  / docFreq.getOrDefault(word, 1));

            tfidf.put(word, tf * idf);
        }

        return tfidf;
    }

    /**
     * Returneaza primele {@code topN} cuvinte sortate descrescator dupa scor.
     *
     * @param tfidfScores  map cuvant -> scor TF-IDF
     * @param topN         numarul de rezultate dorite
     * @return             lista de WordFrequency sortata
     */
    public List<WordFrequency> getTopN(Map<String, Double> tfidfScores,
                                       Map<String, Integer> globalFreq,
                                       int topN) {

        List<WordFrequency> list = new ArrayList<>();

        tfidfScores.entrySet()
                   .stream()
                   .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                   .limit(topN)
                   .forEach(e -> list.add(
                       new WordFrequency(e.getKey(),
                                         globalFreq.getOrDefault(e.getKey(), 0),
                                         e.getValue())
                   ));

        return list;
    }

    /**
     * Afiseaza Top N in consola cu format tabelar.
     *
     * @param top  lista produsa de getTopN()
     */
    public void printTop(List<WordFrequency> top) {
        System.out.println("\n  TOP " + top.size() + " CUVINTE (TF-IDF)");
        System.out.println("  " + "-".repeat(50));
        System.out.printf("  %-4s %-25s %-10s %-12s%n",
                          "Loc", "Cuvant", "Frecventa", "TF-IDF");
        System.out.println("  " + "-".repeat(50));

        int rank = 1;
        for (WordFrequency wf : top) {
            System.out.printf("  %-4d %-25s %-10d %.6f%n",
                              rank++, wf.getWord(), wf.getCount(), wf.getTfIdf());
        }

        System.out.println("  " + "-".repeat(50));
    }

    // ── Helper ────────────────────────────────────────────────────────────

    /**
     * Construieste document-frequency: pentru fiecare cuvant,
     * in cate chunk-uri apare cel putin o data.
     */
    private Map<String, Integer> buildDocFreq(List<Map<String, Integer>> chunkFreqs) {
        Map<String, Integer> docFreq = new HashMap<>();
        for (Map<String, Integer> chunkMap : chunkFreqs) {
            for (String word : chunkMap.keySet()) {
                docFreq.merge(word, 1, Integer::sum);
            }
        }
        return docFreq;
    }
}
