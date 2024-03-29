package tw.core;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tw.core.exception.OutOfRangeAnswerException;
import tw.core.generator.AnswerGenerator;
import tw.core.model.GuessResult;

import java.util.List;

/**
 * 在GameTest文件中完成Game中对应的单元测试
 */


public class GameTest {
    private Game game = null;

    @Before
    public void init() throws OutOfRangeAnswerException {
        Answer actualAnswer = Answer.createAnswer("1 2 3 4");
        AnswerGenerator answerGenerator = Mockito.mock(AnswerGenerator.class);
        Mockito.when(answerGenerator.generate()).thenReturn(actualAnswer);
        game = new Game(answerGenerator);
    }

    @Test
    public void testGuessMethodWhileGuessCorrect(){
        GuessResult result = game.guess(Answer.createAnswer("1 2 3 4"));
        Assert.assertThat(result.getResult(), Is.is("4A0B"));
        Assert.assertThat(result.getInputAnswer().toString(), Is.is("1 2 3 4"));
    }

    @Test
    public void testGuessMethodWhileAllNumberIsIncorrect(){
        GuessResult result = game.guess(Answer.createAnswer("5 6 7 8"));
        Assert.assertThat(result.getResult(), Is.is("0A0B"));
        Assert.assertThat(result.getInputAnswer().toString(), Is.is("5 6 7 8"));
    }

    @Test
    public void testGuessMethodWhileSomeNumberIsCorrect(){
        GuessResult result = game.guess(Answer.createAnswer("1 3 2 4"));
        Assert.assertThat(result.getResult(), Is.is("2A2B"));
        Assert.assertThat(result.getInputAnswer().toString(), Is.is("1 3 2 4"));
    }

    @Test
    public void testGuessHistoryMethod(){
        game.guess(Answer.createAnswer("2 3 4 5"));
        game.guess(Answer.createAnswer("3 4 5 6"));
        List<GuessResult> guessResults = game.guessHistory();
        Assert.assertThat(guessResults.get(0).getInputAnswer().toString(),Is.is("2 3 4 5"));
        Assert.assertThat(guessResults.get(0).getResult(),Is.is("0A3B"));
        Assert.assertThat(guessResults.get(1).getInputAnswer().toString(),Is.is("3 4 5 6"));
        Assert.assertThat(guessResults.get(1).getResult(),Is.is("0A2B"));
    }

    @Test
    public void testCheckStatusMethodReturnSuccess(){
        game.guess(Answer.createAnswer("2 3 4 5"));
        game.guess(Answer.createAnswer("3 4 5 6"));
        game.guess(Answer.createAnswer("1 2 3 4"));
        String result = game.checkStatus();
        Assert.assertThat(result,Is.is("success"));
    }

    @Test
    public void testCheckStatusMethodReturnFail(){
        game.guess(Answer.createAnswer("2 3 4 5"));
        game.guess(Answer.createAnswer("3 4 5 6"));
        game.guess(Answer.createAnswer("2 4 6 8"));
        game.guess(Answer.createAnswer("4 5 6 7"));
        game.guess(Answer.createAnswer("3 5 4 7"));
        game.guess(Answer.createAnswer("4 8 6 7"));
        String result = game.checkStatus();
        Assert.assertThat(result,Is.is("fail"));
    }

    @Test
    public void testCheckStatusMethodReturnContinue(){
        game.guess(Answer.createAnswer("2 3 4 5"));
        String result = game.checkStatus();
        Assert.assertThat(result,Is.is("continue"));
    }
}
