import checker.Coord;
import checker.Game;
import org.junit.Test;
import static checker.Check.checkersMap;
import static org.junit.Assert.assertEquals;

public class JavaCheckersTest{

    @Test
    public void test() {
        Game game = new Game(9, 9);

        //проверка на белую шашку, которой нельзя сделать ход
        game.start();
        assertEquals("WHITE", checkersMap.get(new Coord(6, 6)).toString());

        //проверка на белую шашку, которой можно сделать ход
        assertEquals("WHITEPOSSIBLE", checkersMap.get(new Coord(5, 5)).toString());

        //берем шашку в руку и проверяем - взяли ли мы её, а также возможность хода в одну из клеток
        game.pressLeftButton(new Coord(5, 5));
        assertEquals("WHITESTART", checkersMap.get(new Coord(5, 5)).toString());
        assertEquals("POSSIBLE", checkersMap.get(new Coord(4, 4)).toString());

        //делаем ход шашкой, и проверяем что она оказалась на новом месте, а её прежнее место стало пустой клеткой
        game.pressLeftButton(new Coord(4, 4));
        assertEquals("WHITE", checkersMap.get(new Coord(4, 4)).toString());
        assertEquals("BLACKSQUARE", checkersMap.get(new Coord(5, 5)).toString());

        //делаем ход черными, затем убийство белыми, и проверяем - исчезла ли черная пешка и в нужном ли месте - белая
        game.pressLeftButton(new Coord(6, 2));
        game.pressLeftButton(new Coord(5, 3));
        game.pressLeftButton(new Coord(4, 4));
        game.pressLeftButton(new Coord(6, 2));
        assertEquals("WHITE", checkersMap.get(new Coord(6, 2)).toString());
        assertEquals("BLACKSQUARE", checkersMap.get(new Coord(5, 3)).toString());
        assertEquals("BLACKSQUARE", checkersMap.get(new Coord(4, 4)).toString());
    }
}