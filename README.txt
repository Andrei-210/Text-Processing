================================================================
  Text Processing — Numararea Frecventei Cuvintelor
  Varianta: Secventiala  |  Java 17  |  Structura modulara
================================================================

FISIERE INCLUSE
---------------
  Main.java              — punct de intrare; orchestrare + timing + output
  TextTokenizer.java     — citire fisier, normalizare, filtrare stop-words
  ChunkSplitter.java     — impartire array de cuvinte in chunk-uri
  FrequencyCounter.java  — calcul frecventa per chunk + combinare globala
  TFIDFCalculator.java   — calcul TF-IDF + returnare / afisare Top N
  WordFrequency.java     — model de date (cuvant, frecventa, scor TF-IDF)
  README.txt             — acest fisier

RESPONSABILITATILE FIECAREI CLASE
----------------------------------
  Main.java
    - Valideaza argumentele din linie de comanda
    - Instantiaza si apeleaza celelalte clase in ordine
    - Masoara si afiseaza timpii pentru fiecare etapa
    - NU contine logica de procesare

  TextTokenizer.java
    - Citeste fisierul .txt de pe disc
    - Converteste textul la litere mici
    - Elimina semnele de punctuatie
    - Filtreaza stop-words si cuvinte <= 2 caractere
    - Returneaza String[] de cuvinte curate

  ChunkSplitter.java
    - Primeste array-ul de cuvinte si dimensiunea chunk-ului (10.000)
    - Produce o List<String[]> cu bucatile de text
    - Ultimul chunk poate fi mai mic

  FrequencyCounter.java
    - Itereaza secvential prin fiecare chunk
    - Construieste un HashMap<String,Integer> per chunk
    - Combina toate hartile locale intr-un HashMap global
    - Returneaza un obiect Result cu ambele structuri

  TFIDFCalculator.java
    - Calculeaza document-frequency per cuvant
    - Aplica formula: TF = count/total, IDF = ln(N/df), scor = TF*IDF
    - Returneaza Map<String,Double> cu scorurile
    - Ofera getTopN() si printTop() pentru afisare

  WordFrequency.java
    - POJO: word (String), count (int), tfIdf (double)
    - Comparable, sortare descrescatoare dupa tfIdf

COMPILARE
---------
  javac *.java

RULARE
------
  java Main <cale_catre_fisier.txt>

  Exemple:
    java Main text.txt
    java Main C:\Users\User\Documents\carte.txt

  Pentru fisiere foarte mari (>200 MB):
    java -Xmx2g Main text.txt

OUTPUT ASTEPTAT IN CONSOLA
--------------------------
  =======================================================
    Text Processing — Frecventa Cuvintelor (Secvential)
  =======================================================
  Fisier      : /path/to/text.txt
  Chunk size  : 10000 cuvinte
  -------------------------------------------------------
  Cuvinte totale (dupa tokenizare) : 1,250,000
  Timp citire + tokenizare         : 320 ms
  Chunk-uri create                 : 125
  -------------------------------------------------------
  Timp procesare frecvente         : 540 ms
  Timp calcul TF-IDF               : 45 ms
  -------------------------------------------------------
  TIMP TOTAL                       : 905 ms
  =======================================================

    TOP 10 CUVINTE (TF-IDF)
    --------------------------------------------------
    Loc  Cuvant                    Frecventa  TF-IDF
    --------------------------------------------------
    1    example                   4200       0.003241
    ...
    --------------------------------------------------
  =======================================================

NOTE TEHNICE
------------
  - Stop-words : lista pentru engleza in TextTokenizer.java
                 Modifica STOP_WORDS daca textul e in romana
  - CHUNK_SIZE : configurabil in Main.java (default 10.000)
  - TOP_N      : configurabil in Main.java (default 10)
  - IDF = 0    : cuvintele din toate chunk-urile primesc scor 0

================================================================
