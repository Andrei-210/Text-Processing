import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * TextTokenizer
 *
 * Responsabilitati:
 *  - Citeste continutul unui fisier text de pe disc.
 *  - Normalizeaza textul (litere mici, eliminare punctuatie).
 *  - Filtreaza stop-words si cuvinte prea scurte (<= 2 caractere).
 *  - Returneaza un array de cuvinte curate, gata de procesat.
 */
public class TextTokenizer {

    // Cuvinte comune (stop-words) excluse din analiza
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "the","a","an","and","or","but","in","on","at","to","for","of","with",
        "is","it","its","this","that","was","are","be","as","by","from","have",
        "had","has","not","he","she","they","we","you","i","do","did","does",
        "so","if","up","out","no","his","her","their","our","my","your","all",
        "been","were","will","would","could","should","can","than","then","when",
        "there","what","which","who","one","also","into","more","about","over",
        "after","before","just","like","how","some","such","other","any","these",
        "those","me","him","them","us","an","am","s","t","re","ll","ve","d","m"
    ));

    /**
     * Citeste fisierul de la calea specificata si returneaza
     * un array de cuvinte normalizate si filtrate.
     *
     * @param path  calea catre fisierul .txt
     * @return      array de cuvinte curate
     * @throws IOException daca fisierul nu poate fi citit
     */
    public String[] tokenize(Path path) throws IOException {
        // Citire continut brut
        String content = Files.readString(path);

        // Normalizare: litere mici + elimina tot ce nu e litera sau spatiu
        String cleaned = content.toLowerCase()
                                .replaceAll("[^a-z\\s]", " ");

        // Tokenizare: imparte dupa orice spatiu alb
        String[] tokens = cleaned.trim().split("\\s+");

        // Filtrare: elimina cuvinte goale, prea scurte si stop-words
        return Arrays.stream(tokens)
                     .filter(w -> !w.isEmpty())
                     .filter(w -> w.length() > 2)
                     .filter(w -> !STOP_WORDS.contains(w))
                     .toArray(String[]::new);
    }
}
