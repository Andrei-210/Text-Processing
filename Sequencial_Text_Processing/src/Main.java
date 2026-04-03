import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Main — Punct de intrare al aplicatiei (Varianta Secventiala)
 *
 * Responsabilitate exclusiva: orchestreaza fluxul de executie,
 * masoara timpii si afiseaza rezultatele finale.
 * Toata logica de procesare este delegata claselor specializate.
 *
 * Flux:
 *   1. Validare argumente
 *   2. TextTokenizer   -> tokenizeaza fisierul
 *   3. ChunkSplitter   -> imparte cuvintele in chunk-uri
 *   4. FrequencyCounter-> calculeaza frecventele
 *   5. TFIDFCalculator -> calculeaza scorurile TF-IDF
 *   6. Afisare Top 10
 *
 * Compilare:
 *   javac Main.java WordFrequency.java TextTokenizer.java
 *         ChunkSplitter.java FrequencyCounter.java TFIDFCalculator.java
 *
 * Rulare:
 *   java Main <cale_fisier.txt>
 */
public class Main {

    private static final int CHUNK_SIZE = 10_000;
    private static final int TOP_N      = 10;

    public static void main(String[] args) {

        // ── 1. Validare argumente ─────────────────────────────────────────
        if (args.length < 1) {
            System.err.println("Utilizare: java Main <cale_fisier.txt>");
            System.exit(1);
        }

        Path filePath = Paths.get(args[0]);
        if (!Files.exists(filePath)) {
            System.err.println("Eroare: fisierul '" + args[0] + "' nu a fost gasit.");
            System.exit(1);
        }

        printHeader(filePath);

        // ── 2. Tokenizare ─────────────────────────────────────────────────
        TextTokenizer tokenizer = new TextTokenizer();
        String[] allWords;

        long startRead = System.currentTimeMillis();
        try {
            allWords = tokenizer.tokenize(filePath);
        } catch (Exception e) {
            System.err.println("Eroare la citirea fisierului: " + e.getMessage());
            System.exit(1);
            return;
        }
        long endRead = System.currentTimeMillis();

        System.out.printf("Cuvinte totale (dupa tokenizare) : %,d%n", allWords.length);
        System.out.printf("Timp citire + tokenizare         : %d ms%n", endRead - startRead);

        // ── 3. Impartire in chunk-uri ─────────────────────────────────────
        ChunkSplitter splitter = new ChunkSplitter(CHUNK_SIZE);
        List<String[]> chunks  = splitter.split(allWords);

        System.out.printf("Chunk-uri create                 : %d%n", chunks.size());
        System.out.println("-".repeat(55));

        // ── 4. Calcul frecvente ───────────────────────────────────────────
        FrequencyCounter counter = new FrequencyCounter();

        long startProcess = System.currentTimeMillis();
        FrequencyCounter.Result freqResult = counter.compute(chunks);
        long endProcess = System.currentTimeMillis();

        System.out.printf("Timp procesare frecvente         : %d ms%n", endProcess - startProcess);

        // ── 5. Calcul TF-IDF ──────────────────────────────────────────────
        TFIDFCalculator tfidfCalc = new TFIDFCalculator();

        long startTfidf = System.currentTimeMillis();
        Map<String, Double> tfidfScores = tfidfCalc.compute(
                freqResult.globalFreq,
                freqResult.chunkFreqs,
                chunks.size()
        );
        List<WordFrequency> top = tfidfCalc.getTopN(
                tfidfScores,
                freqResult.globalFreq,
                TOP_N
        );
        long endTfidf = System.currentTimeMillis();

        System.out.printf("Timp calcul TF-IDF               : %d ms%n", endTfidf - startTfidf);

        // ── 6. Timp total + afisare rezultate ─────────────────────────────
        long totalTime = (endRead    - startRead)
                       + (endProcess - startProcess)
                       + (endTfidf   - startTfidf);

        System.out.println("-".repeat(55));
        System.out.printf("TIMP TOTAL                       : %d ms%n", totalTime);
        System.out.println("=".repeat(55));

        tfidfCalc.printTop(top);
        System.out.println("=".repeat(55));
    }

    // ── Helper de afisare antet ───────────────────────────────────────────

    private static void printHeader(Path filePath) {
        System.out.println("=".repeat(55));
        System.out.println("  Text Processing - Frecventa Cuvintelor (Secvential)");
        System.out.println("=".repeat(55));
        System.out.println("Fisier      : " + filePath.toAbsolutePath());
        System.out.println("Chunk size  : " + CHUNK_SIZE + " cuvinte");
        System.out.println("-".repeat(55));
    }
}
