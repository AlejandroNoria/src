import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Class that supports Tweet Classification Mechanism
 * Dictionary of positive words, negative words and all tweets are kept in this class.
 */
public class ReviewClassifier{
    /**
     * Positive word dictionary. Import from file.
     */
    protected HashSet<String> positiveWords =  new HashSet<String>();
    /**
     * Negative word dictionary. Import from file.
     */
    protected HashSet<String> negativeWords = new HashSet<String>();

    /**
     * Number of positive review files
     */
    private int posFilesCount = 0;
    /**
     * Number of negative review files
     */
    private int negFilesCount = 0;
    /**
     * Number of correct classified positive files
     */
    private int correctPosCount = 0;
    /**
     * Number of correct classified negative files
     */
    private int correctNegCount = 0;


    /**
     * Read in files and start classification
     *
     * @param pathToPosWords Path to positive word file
     * @param pathToNegWords Path to negative word file
     * @param pathToPosReviewsFolder Path to folder holding positive reviews
     * @param pathToNegReviewsFolder Path to folder holding negative reviews
     *
     * @throws IOException
     *
     */
    public void readInFiles(String pathToPosWords, String pathToNegWords, String pathToPosReviewsFolder, String pathToNegReviewsFolder) throws IOException {
        //Read in positive words
        readInWords(pathToPosWords, positiveWords);
        System.out.println(positiveWords.size() + " positive words loaded.\n");

        //Read in negative words
        readInWords(pathToNegWords, negativeWords);
        System.out.println(negativeWords.size() + " negative words loaded.\n");

        //Classify positive reviews
        classifyReviews(pathToPosReviewsFolder, true);

        //Classify negative reviews
        classifyReviews(pathToNegReviewsFolder, false);
    }

    /**
     * Read positive/negative words in to HashSet
     *
     * @param fileName path to file containing words
     * @param dictionary word dictionary
     *
     * @throws IOException
     *
     */
    private void readInWords(String fileName, HashSet<String> dictionary) throws IOException {

        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            if(!line.isEmpty() && !line.startsWith(";") && !line.equals(""))

            {
                dictionary.add(line);
            }
        }

    }

    /**
     * Read positive/negative reviews, and call for classification
     *
     * @param folderPath path to review folder containing words
     * @param target target polarity. True = positive, False = negative
     *
     * @throws IOException
     *
     */
    private void classifyReviews(String folderPath, boolean target) throws IOException {

        File file = new File(folderPath);
        String [] fileList = file.list();

        String fileSeparatorChar = System.getProperty("file.separator");

        int totalFiles = 0;
        int positiveFiles = 0;
        int negativeFiles = 0;
        boolean actually;

        String groundTruth = "";
        String classification = "";

        if(target){
            groundTruth = "positive";
        }
        else {
            groundTruth = "negative";
        }

        for(int i = 0; i < fileList.length; i++)
        {
            if(fileList[i].contains(".txt"))
            {
                totalFiles++;
                actually = classifyReview(folderPath + fileSeparatorChar + fileList[i]);

                if(actually){
                    positiveFiles++;
                    classification = "positive";
                }
                else {
                    negativeFiles++;
                    classification = "negative";
                }

            }

        }

        if(target)
        {
            posFilesCount = totalFiles;
            correctPosCount = totalFiles - negativeFiles;
        }
        else
        {
            negFilesCount = totalFiles;
            correctNegCount = totalFiles - positiveFiles;
        }

    }

    /**
     * Read positive/negative reviews, and call for classification
     *
     * @param fileName path to review file
     *
     * @return True = positive review, False = negative review
     *
     * @throws IOException
     *
     */
    public boolean classifyReview(String fileName) throws IOException {

        int positive = 0;
        int negative = 0;

        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        String text = "";
        String [] array;

        while(scanner.hasNextLine())
        {
            text = scanner.nextLine();
            text = text.replaceAll("<br />"," ");
            text = text.replaceAll("\\p{Punct}"," ");
            text.toLowerCase();

            array = text.split("\\s+");

            for(int i = 0; i < array.length; i++)
            {
                if(positiveWords.contains(array[i]))
                {
                    positive++;
                }
                else if(negativeWords.contains(array[i]))
                {
                    negative++;
                }
            }


        }
        scanner.close();

        return (positive > negative);

    }

    /**
     * Output the result
     *
     */
    public void outputResult() {

        System.out.println();
        System.out.println("Positive Words: " + positiveWords.size());
        System.out.println("Negative Words: " + negativeWords.size());

        System.out.println("Number of positive reviews: " + posFilesCount);
        System.out.println("Correctly classified: " + correctPosCount);
        System.out.println("Misclassified: " + (posFilesCount - correctPosCount));
        double d = correctPosCount;
        double d2 = posFilesCount;
        System.out.println("Correct classification rate: " + String.format("%.1f",(d/d2) * 100) + "%");

        System.out.println("Number of negative reviews: " + negFilesCount);
        System.out.println("Correctly Classified: " + correctNegCount);
        System.out.println("Misclassified: " + (negFilesCount - correctNegCount ));
        d = correctNegCount;
        d2 = negFilesCount;
        System.out.println("Correct classification rate: " + String.format("%.1f", (d/d2) * 100) + "%");

        System.out.println("Number of all reviews " + (negFilesCount + posFilesCount));
        System.out.println("Correctly Classified: " + (correctNegCount + correctPosCount));
        System.out.println("Missclassified: " + ((negFilesCount - correctNegCount) + (posFilesCount - correctPosCount)));
        d = (correctNegCount + correctPosCount);
        d2 = (negFilesCount + posFilesCount);
        System.out.println("Overall correct classification rate: " + String.format("%.1f", (d/d2) * 100) + "%");

    }
}