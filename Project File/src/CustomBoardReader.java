import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * CustomBoardReader is an XML file parser which reads the XML files
 * that defines the locations of the premium squares on the game board
 * @author Tuna Uygun
 * @version 11/29/22
 */
public class CustomBoardReader extends DefaultHandler {
    private int row;
    private int column;
    private BoardModel.Multipliers multiplier;

    private boolean readingRow;
    private boolean readingColumn;
    private boolean readingMultiplier;

    private ArrayList<int[]> tripleWordSquares;
    private ArrayList<int[]> tripleLetterSquares;
    private ArrayList<int[]> doubleWordSquares;
    private ArrayList<int[]> doubleLetterSquares;

    /**
     * Constructs a new custom board reader object
     */
    public CustomBoardReader() {
        super();
        readingRow = false;
        readingColumn = false;
        readingMultiplier = false;
        tripleWordSquares = new ArrayList<int[]>();
        tripleLetterSquares = new ArrayList<>();
        doubleWordSquares = new ArrayList<>();
        doubleLetterSquares = new ArrayList<>();
    }

    /**
     * Gets called when the parser reaches the start of an element. Then, sets the correspoding
     * boolean flag to indicate which type of element is being read by the parser.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace processing
     *                  is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If there are no attributes, it shall be an
     *                   empty Attributes object.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equalsIgnoreCase("Row")){
            readingRow = true;
        }else if(qName.equalsIgnoreCase("Column")){
            readingColumn = true;
        }else if(qName.equalsIgnoreCase("Multiplier")){
            readingMultiplier = true;
        }
    }

    /**
     * Gets called when the parser reaches the end of an element. When the end of a square
     * element is reached, it adds the coordinate of the square to one of the premium square
     * arraylists base on its multiplier.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace processing
     *                  is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are not available.
     */
    @Override
    public void endElement(String uri, String localName, String qName) {
        if(qName.equalsIgnoreCase("Square")) {
            int[] coordinates = new int[]{row, column};
            if(multiplier == BoardModel.Multipliers.TW){
                tripleWordSquares.add(coordinates);
            }else if(multiplier == BoardModel.Multipliers.TL){
                tripleLetterSquares.add(coordinates);
            }else if(multiplier == BoardModel.Multipliers.DW){
                doubleWordSquares.add(coordinates);
            }else if(multiplier == BoardModel.Multipliers.DL){
                doubleLetterSquares.add(coordinates);
            }
        }else if(qName.equalsIgnoreCase("Board")) {
            System.out.println("Completed reading the board!");
        }
    }

    /**
     * Gets called when reading the characters inside an XML element. Updates the row, column and multiplier
     * fields with the data from the XML element depending on which element is being read by the parser
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the character array.
     */
    @Override
    public void characters(char[] ch, int start, int length) {
        String data = new String(ch, start, length);
        if(readingRow){
            row = Integer.parseInt(data)-1;
            readingRow = false;
        }else if(readingColumn){
            column = Integer.parseInt(data)-1;
            readingColumn = false;
        }else if(readingMultiplier){
            multiplier = BoardModel.Multipliers.valueOf(data);
            readingMultiplier = false;
        }
    }

    /**
     * Reads the XML file containing the custom board data and updates the four ArrayLists of
     * coordinates with the location of the premium squares
     * @param fileName The path to the XML file containing custom board data
     */
    public void readCustomBoardFile(String fileName){
        try {
            File inputFile = new File(fileName);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputFile, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a 2D array of 2 integers given an ArrayList that contains arrays of 2 integers
     * @param arrayList An ArrayList that contains arrays of 2 integers
     * @return An array that contains arrays of 2 integers
     */
    private int[][] arraylistToArray(ArrayList<int[]> arrayList){
        int[][] array = new int[arrayList.size()][2];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    /**
     * Updates the multipliers of the game board with the custom multiplier
     * data from the xml file
     * @param b The boardmodel object that models the game board
     */
    public void setMultiplier(BoardModel b){
        b.setSquareMultipliers(arraylistToArray(tripleWordSquares),
                arraylistToArray(doubleWordSquares),
                arraylistToArray(tripleLetterSquares),
                arraylistToArray(doubleLetterSquares));
    }
}
