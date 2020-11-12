import java.io.IOException;

/**
  Main Application for Assignment 1
  @author vangelis
  @author jelena
  @author junye
 */
public class MainApp {

    /**
     * Main method demonstrates how to use Stanford NLP library classifier.
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.out.println("Wrong number of args");
            System.out.println("Example: java movieReviewClassification.SentimentAnalysisApp <path_to_pos_words> <path_to_neg_words> <path_to pos_reviews_folder> <path_to_neg_reviews_folder>");
            return;
        }

        String pathToPosWords = args[0];
        String pathToNegWords = args[1];
        String pathToPosReviewsFolder = args[2];
        String pathToNegReviewsFolder = args[3];

        System.out.println("Path to positive words: " + pathToPosWords);
        System.out.println("Path to negative words: " + pathToNegWords);
        System.out.println("Path to positive reviews folder: " + pathToPosReviewsFolder);
        System.out.println("Path to negative reviews folder: " + pathToNegReviewsFolder);

        ReviewClassifier reviewClassifier = new ReviewClassifier();

        //Read in files and start classification
        reviewClassifier.readInFiles(pathToPosWords, pathToNegWords, pathToPosReviewsFolder, pathToNegReviewsFolder);

        //Output result
        reviewClassifier.outputResult();

        return;

    }

}
