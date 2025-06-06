/**
 * Java Settlers - An online multiplayer version of the game Settlers of Catan
 * This file Copyright (C) 2020-2021,2024 Jeremy D Monin <jeremy@nand.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * The maintainer of this program can be reached at jsettlers@nand.net
 **/

package soctest.game;

import soc.game.SOCDevCard;
import soc.game.SOCDevCardConstants;
import soc.game.SOCGame;
import soc.game.SOCInventory;
import soc.game.SOCPlayer;
import soc.game.SOCResourceSet;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A few tests for {@link SOCDevCard} and {@link SOCDevCardConstants}
 * and related {@link SOCGame} methods.
 *
 * @since 2.4.00
 */
public class TestDevCard
{

    /** Test expected values and min/max range of {@link SOCDevCardConstants} card type fields. */
    @Test
    public void testDevCardConstants()
    {
        assertEquals(0, SOCDevCardConstants.MIN);
        assertEquals(1, SOCDevCardConstants.MIN_KNOWN);
        assertEquals(0, SOCDevCardConstants.UNKNOWN);
        assertEquals(1, SOCDevCardConstants.ROADS);
        assertEquals(2, SOCDevCardConstants.DISC);
        assertEquals(3, SOCDevCardConstants.MONO);
        assertEquals(4, SOCDevCardConstants.CAP);
        assertEquals(5, SOCDevCardConstants.MARKET);
        assertEquals(6, SOCDevCardConstants.UNIV);
        assertEquals(7, SOCDevCardConstants.TEMPLE);  // was .TEMP before v2.5.00
        assertEquals(8, SOCDevCardConstants.CHAPEL);
        assertEquals(9, SOCDevCardConstants.KNIGHT);
        assertEquals(1 + SOCDevCardConstants.KNIGHT, SOCDevCardConstants.MAXPLUSONE);
    }

    /**
     * Test {@link SOCDevCard#getCardTypeName(int)}, lightly test {@link SOCDevCard#getCardType(String)}.
     * @see #testGetCardType()
     */
    @Test
    public void testGetDevCardTypeName()
    {
        // hardcode currently known names, to ensure compat when loading a savegame across different versions
        final String[] knownNames =
            { "UNKNOWN", "ROADS", "DISC", "MONO", "CAP", "MARKET", "UNIV",
              "TEMPLE" /* not ambiguous abbreviation TEMP */, "CHAPEL", "KNIGHT"
            };

        assertEquals(SOCDevCardConstants.MAXPLUSONE, knownNames.length);
        for (int i = 0; i < knownNames.length; ++i)
        {
            final String name = SOCDevCard.getCardTypeName(i);
            assertEquals(knownNames[i], name);
            assertTrue("expected regex match for \"" + name + "\"",
                TestPlayingPiece.TYPENAME_PATTERN.matcher(name).matches());

            assertEquals(i, SOCDevCard.getCardType(knownNames[i]));
        }

        assertEquals
            (Integer.toString(SOCDevCardConstants.MAXPLUSONE),
             SOCDevCard.getCardTypeName(SOCDevCardConstants.MAXPLUSONE));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDevCardTypeName_negative()
    {
        assertEquals("-WONTREACH", SOCDevCard.getCardTypeName(-42));  // < 0: should throw exception
    }

    /**
     * The rest of the {@link SOCDevCard#getCardType(String)} tests.
     * @see #testGetDevCardTypeName()
     */
    @Test
    public void testGetCardType()
    {
        assertEquals(42, SOCDevCard.getCardType("42"));
        assertEquals(1, SOCDevCard.getCardType("1"));
        assertEquals
            (SOCDevCardConstants.MAXPLUSONE,
             SOCDevCard.getCardType(Integer.toString(SOCDevCardConstants.MAXPLUSONE)));
        assertEquals
            (100 + SOCDevCardConstants.MAXPLUSONE,
             SOCDevCard.getCardType(Integer.toString(100 + SOCDevCardConstants.MAXPLUSONE)));

        assertEquals(0, SOCDevCard.getCardType("THIS_CARDTYPE_WILL_NEVER_BE_DEFINED"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetCardType_null()
    {
        assertEquals(42, SOCDevCard.getCardType(null));  // null: should throw exception
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetCardType_empty()
    {
        assertEquals(42, SOCDevCard.getCardType(""));  // empty string: should throw exception
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetCardType_wrongCharClass_p()
    {
        assertEquals(42, SOCDevCard.getCardType("{something}"));  // punctuation: should throw exception
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetCardType_wrongCharClass_lc()
    {
        assertEquals(42, SOCDevCard.getCardType("something"));  // lowercase: should throw exception
    }

    @Test(expected=NumberFormatException.class)
    public void testGetCardType_notReallyNumeric()
    {
        assertEquals(42, SOCDevCard.getCardType("4XYZ"));  // malformed: should throw exception
    }

    /**
     * Test {@link SOCGame#getDevCardDeck()}, {@link SOCGame#buyDevCard()},
     * {@link SOCGame#shuffleDevCardDeck(int)} and related methods.
     * @since 2.7.00
     */
    @Test
    public void testGetDevCardsAndReshuffle()
    {
        SOCGame ga = new SOCGame("TestDevCard");
        ga.initAtServer();  // set up game data as if at server, but don't create a board that won't be used
        assertEquals(SOCGame.NEW, ga.getGameState());
        assertEquals(-1, ga.getCurrentPlayerNumber());  // will return cards from dev card deck, without needing a player to give them to

        assertEquals(25, ga.getNumDevCards());  // hardcode 25 because field SOCGame.NUM_DEVCARDS_STANDARD is private
        int[] cards = ga.getDevCardDeck();
        assertEquals(25, cards.length);

        // can't put card in deck when it's at full size
        try
        {
            ga.shuffleDevCardDeck(SOCDevCardConstants.ROADS);
            fail("ga.shuffleDevCardDeck(ROADS) should fail when deck already full");
        }
        catch(IllegalStateException e) {}

        int cardType = ga.buyDevCard();
        assertEquals(cards[24], cardType);
        assertEquals(24, ga.getNumDevCards());

        ga.shuffleDevCardDeck(0);
        assertEquals(24, ga.getNumDevCards());
        // TODO reexamine getDevCardDeck contents to verify shuffled?

        ga.shuffleDevCardDeck(cardType);
        assertEquals(25, ga.getNumDevCards());
        // TODO reexamine getDevCardDeck contents to verify shuffled?

        /* force short deck to test corner cases */

        ga.setNumDevCards(2);
        assertEquals(2, ga.getNumDevCards());
        ga.shuffleDevCardDeck(0);  // should not fail

        cardType = ga.buyDevCard();
        assertEquals(1, ga.getNumDevCards());
        cards = ga.getDevCardDeck();
        assertEquals(1, cards.length);

        cardType = ga.buyDevCard();
        assertEquals(cards[0], cardType);
        assertEquals(0, ga.getNumDevCards());

        ga.shuffleDevCardDeck(cardType);
        assertEquals(1, ga.getNumDevCards());
        cards = ga.getDevCardDeck();
        assertEquals(1, cards.length);
        assertEquals(cardType, cards[0]);

        // TODO test 6-player length vs NUM_DEVCARDS_6PLAYER?

        /* test actually buying a card */

        ga.addPlayer("p0", 0);
        ga.setCurrentPlayerNumber(0);
        ga.setGameState(SOCGame.PLAY1);
        SOCPlayer pl = ga.getPlayer(0);
        assertEquals("p0", pl.getName());
        pl.getResources().add(new SOCResourceSet(0, 1, 1, 1, 0, 0));
        int cardTypePl = ga.buyDevCard();
        assertEquals("same card recently re-added to empty deck", cardType, cardTypePl);
        assertEquals(0, ga.getNumDevCards());
        assertTrue(pl.getResources().isEmpty());
        SOCInventory inv = pl.getInventory();
        assertEquals(1, inv.getTotal());
        assertEquals(1, inv.getAmount(cardTypePl));
    }

}
