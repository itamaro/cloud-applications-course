import java.io.File;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    // Changed this to HashSet (hope it's OK)
    Set<String> stopWordsArray = new HashSet<String>(Arrays.asList(new String[] 
           {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"}));

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];

        // Read input file, line by line
        List<String> titles = new ArrayList<String>();
        Scanner inFile = new Scanner(new File(this.inputFileName));
        long lines = 0;
        while (inFile.hasNext()) {
            titles.add(inFile.nextLine());
            lines++;
        }
        String[] titleArray = titles.toArray(new String[0]);

        // Initialize word-freq hash table
        Hashtable<String, Integer> wordFreq = new Hashtable<String, Integer>();
        // Iterate over the lines as specified by getIndexes()
        for (Integer index: getIndexes()) {
            // System.out.println(index + " :: " + titleArray[index]);

            // 1. Divide current title into a list of words using tokenizer
            StringTokenizer tokens = new StringTokenizer(titleArray[index], this.delimiters);
            while (tokens.hasMoreTokens()) {
                // 2. Convert words to lower case and remove surrounding spaces (needed?)
                String word = tokens.nextToken().toLowerCase().trim();
                // 3. Ignore common words from stopWordsArray (AKA HashSet)
                if (!stopWordsArray.contains(word)) {
                    // System.out.println("Keeping " + word);
                    // 4. Keep track of word frequencies
                    if (wordFreq.containsKey(word)) {
                        // 4b- not first occurrence
                        wordFreq.put(word, wordFreq.get(word) + 1);
                    } else {
                        // 4a- first occurrence
                        wordFreq.put(word, 1);
                    }
                }
            }
        }

        // 5. Sort the list by frequency in descending order (tie breaker using lexigraphy)
        ArrayList<Map.Entry<String, Integer>> freqList = new ArrayList(wordFreq.entrySet());
        Collections.sort(freqList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                int comp = o2.getValue().compareTo(o1.getValue());
                if (comp == 0) {
                    // both words have same count - fallback to lexigraphic order.
                    return o1.getKey().compareTo(o2.getKey());
                }
                return comp;
            }
        });
        // 6. Return top 20 words
        int freqPos = 0;
        for (Map.Entry<String, Integer> freqEntry: freqList) {
            // System.out.println(freqEntry.getKey() + " - " + freqEntry.getValue());
            ret[freqPos++] = freqEntry.getKey();
            if (freqPos >= 20) {
                break;
            }
        }
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
