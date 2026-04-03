import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ChunkSplitter
 *
 * Responsabilitati:
 *  - Primeste un array de cuvinte si o dimensiune de chunk.
 *  - Imparte array-ul in bucati (chunk-uri) de dimensiune fixa.
 *  - Ultimul chunk poate fi mai mic daca numarul total de cuvinte
 *    nu e divizibil exact cu dimensiunea chunk-ului.
 */
public class ChunkSplitter {

    private final int chunkSize;

    /**
     * @param chunkSize numarul de cuvinte per chunk
     */
    public ChunkSplitter(int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize trebuie sa fie > 0");
        }
        this.chunkSize = chunkSize;
    }

    /**
     * Imparte array-ul de cuvinte in chunk-uri de dimensiune egala.
     *
     * @param words  array-ul complet de cuvinte tokenizate
     * @return       lista de chunk-uri (fiecare chunk este un String[])
     */
    public List<String[]> split(String[] words) {
        List<String[]> chunks = new ArrayList<>();
        int total = words.length;

        for (int start = 0; start < total; start += chunkSize) {
            int end = Math.min(start + chunkSize, total);
            chunks.add(Arrays.copyOfRange(words, start, end));
        }

        return chunks;
    }

    public int getChunkSize() {
        return chunkSize;
    }
}
