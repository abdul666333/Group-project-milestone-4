import java.io.Serializable;

/**
 * Square class represents board squares
 *  @author Umniyah Mohammed 101158792
 *  @author Hasan Al-Hasoo
 *  @version 1.0
 */
public class Square implements Serializable {

    private Tile tile; //tile placed on the square if available
    private int rowNum;
    private int columnNum;
    private BoardModel.Multipliers multiplier;

    /**
     * Constructor of Square class that takes row and column numbers
     * @param column
     * @param row
     */
    public Square(int row, int column){
        super();
        this.rowNum = row;
        this.columnNum = column;
    }

    //could be used by gui later on
    /**
     * getRowNum is an accessor
     * @return rowNum, int
     * @author Umniyah Mohammed
     */
    public int getRowNum(){
        return rowNum;
    }
    /**
     * getColumnNum is an accessor
     * @return columnNum, int
     * @author Umniyah Mohammed
     */
    public int getColumnNum(){
        return columnNum;
    }

    /**
     * placeTile method sets the value of tile to the tile instance
     * @param _tile
     * @return boolean
     * @author Umniyah Mohammed
     */
    public boolean placeTile(Tile _tile)
    {
        tile = _tile;
        return true;
    }

    /**
     * getTile method is an accessor
     * @return the tile on that square
     * @author Umniyah Mohammed
     */
    public Tile getTile()
    {
        return tile;
    }

    /**
     * removeTile removes a tile from the square
     * @author Umniyah Mohammed
     */
    public void removeTile(){
        tile = null;
    }

    /**
     * hasTile checks whether the square is empty
     * @return true if the square has a tile
     * @author Umniyah Mohammed
     */
    public boolean hasTile(){
        return tile != null;
    }

    /**
     * setMultiplier take a string and sets the value to the instance
     * @param multiplier
     * @author Umniyah Mohammed
     */
    public void setMultiplier(BoardModel.Multipliers multiplier)
    {
        this.multiplier = multiplier;
    }

    /**
     * getMultiplier method is an accessor
     *
     * @return an int multiplier (to be used to adjust the score accordingly)
     * @author Umniyah Mohammed
     * @author Hasan Al-Hasoo
     */
    public int getLetterMultiplier()
    {
        if(multiplier == BoardModel.Multipliers.TL){
            multiplier = BoardModel.Multipliers.N;
            return BoardModel.Multipliers.TL.getMultiplier();
        }
        if(multiplier == BoardModel.Multipliers.DL){
            multiplier = BoardModel.Multipliers.N;
            return BoardModel.Multipliers.DL.getMultiplier();
        }
        else {
            return BoardModel.Multipliers.N.getMultiplier();
        }
    }

    public int getWordMultiplier()
    {
        if(multiplier == BoardModel.Multipliers.TW){
            multiplier = BoardModel.Multipliers.N;
            return BoardModel.Multipliers.TW.getMultiplier();
        }
        if(multiplier == BoardModel.Multipliers.DW){
            multiplier = BoardModel.Multipliers.N;
            return BoardModel.Multipliers.DW.getMultiplier();
        }
        else {
            return BoardModel.Multipliers.N.getMultiplier();
        }
    }

    /**
     * getLetter gets the letter value of the tile
     * @return char
     * @author Umniyah Mohammed
     */
    public char getLetter(){
        return tile.getChar();
    }

    /**
     * getScoreValue gets the score value of the tile
     * @return int
     * @author Umniyah Mohammed
     * @author Hasan Al-Hasoo
     */
    public int getScoreValue(){
        //int tempMultiplier = multiplier == null ? 1: multiplier.getMultiplier();
        return this.getLetterMultiplier() * tile.getScore();
    }

    public BoardModel.Multipliers getMultiplier() {
        return this.multiplier;
    }
}