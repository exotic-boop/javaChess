public interface BoardGame {
    void buildBoard(Square[][] squares);

    void executeInput(int x, int y);

    String getMessage();

    String getStatus();
}
