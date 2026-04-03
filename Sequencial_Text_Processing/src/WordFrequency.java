/**
 * WordFrequency
 *
 * Model de date care incapsuleaza:
 *  - cuvantul analizat
 *  - frecventa sa absoluta in document
 *  - scorul TF-IDF calculat de TFIDFCalculator
 */
public class WordFrequency implements Comparable<WordFrequency> {

    private final String word;
    private final int    count;
    private final double tfIdf;

    public WordFrequency(String word, int count, double tfIdf) {
        this.word  = word;
        this.count = count;
        this.tfIdf = tfIdf;
    }

    public String getWord()   { return word;  }
    public int    getCount()  { return count; }
    public double getTfIdf()  { return tfIdf; }

    /** Sortare naturala: descrescator dupa scorul TF-IDF. */
    @Override
    public int compareTo(WordFrequency other) {
        return Double.compare(other.tfIdf, this.tfIdf);
    }

    @Override
    public String toString() {
        return String.format("%-25s freq=%-8d tfidf=%.6f", word, count, tfIdf);
    }
}
