/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.domain.entities.intel;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandPoints;
import com.bueno.domain.entities.hand.Round;
import com.bueno.domain.entities.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class IntelTest {

    @Mock private Game game;
    @Mock private Hand hand;
    @Mock private Round round;
    @Mock Player p1;
    @Mock Player p2;

    @BeforeAll
    static void init(){
        LogManager.getLogManager().reset();
    }

    @BeforeEach
    void setUp() {
        lenient().when(p1.getUuid()).thenReturn(UUID.randomUUID());
        lenient().when(p2.getUuid()).thenReturn(UUID.randomUUID());
        lenient().when(p1.getUsername()).thenReturn("name1");
        lenient().when(p2.getUsername()).thenReturn("name2");
        lenient().when(p1.getScore()).thenReturn(0);
        lenient().when(p2.getScore()).thenReturn(0);
        lenient().when(hand.getPossibleActions()).thenReturn(EnumSet.of(PossibleAction.ACCEPT));
        lenient().when(hand.getPoints()).thenReturn(HandPoints.ONE);
        lenient().when(hand.getFirstToPlay()).thenReturn(p1);
        lenient().when(hand.getLastToPlay()).thenReturn(p2);
    }

    @Test
    @DisplayName("Should not allow null game in static constructor")
    void shouldNotAllowNullGameInStaticConstructor() {
        assertThrows(NullPointerException.class, () -> Intel.ofGame(null));
    }

    @Test
    @DisplayName("Should not allow null hand in static constructor")
    void shouldNotAllowNullHandInStaticConstructor() {
        assertThrows(NullPointerException.class, () -> Intel.ofHand(null, Event.HAND_START));
    }

    @Test
    @DisplayName("Should return that game is done if game is done")
    void shouldReturnThatGameIsDoneIfGameIsDone() {
        when(game.isDone()).thenReturn(true);
        when(game.currentHand()).thenReturn(hand);
        final Intel intel = Intel.ofGame(game);
        assertTrue(intel.isGameDone());
    }

    @Test
    @DisplayName("Should have no game winner from hand static constructor")
    void shouldHaveNoGameWinnerFromHandStaticConstructor() {
        final Intel intel = Intel.ofHand(hand, Event.HAND_START);
        assertTrue(intel.gameWinner().isEmpty());
    }

    @Test
    @DisplayName("Should game not be done from hand static constructor")
    void shouldGameNotBeDoneFromHandStaticConstructor() {
        final Intel intel = Intel.ofHand(hand, Event.HAND_START);
        assertFalse(intel.isGameDone());
    }

    @Test
    @DisplayName("Should intel have non null timestamp")
    void shouldIntelHaveNonNullTimestamp() {
        final Intel intel = Intel.ofHand(hand, Event.HAND_START);
        assertNotNull(intel.timestamp());
    }

    @Test
    @DisplayName("Should two intel objects be different if created in different moments")
    void shouldTwoIntelObjectsBeDifferentIfCreatedInDifferentMoments() {
        assertNotEquals(Intel.ofHand(hand, Event.HAND_START), Intel.ofHand(hand, Event.HAND_START));
    }

    @Test
    @DisplayName("Should get default values of player and opponent if current player is null")
    void shouldGetDefaultValuesOfPlayerAndOpponentIfCurrentPlayerIsNull() {
        final Intel intel = Intel.ofHand(hand, Event.HAND_START);
        assertAll(
                () -> assertEquals(Optional.empty(), intel.currentPlayerUuid()),
                () -> assertNull(intel.currentPlayerUsername()),
                () -> assertNull(intel.currentOpponentUsername()),
                () -> assertEquals(0, intel.currentPlayerScore()),
                () -> assertEquals(0, intel.currentOpponentScore())
        );
    }

    @Test
    @DisplayName("Should correctly obtain the name of the round winners")
    void shouldCorrectlyObtainTheNameOfTheRoundWinners() {
        when(round.getWinner()).thenReturn(Optional.of(p1)).thenReturn(Optional.empty()).thenReturn(Optional.of(p2));
        when(hand.getRoundsPlayed()).thenReturn(List.of(round, round, round));

        final List<Optional<String>> expected = List.of(Optional.of("name1"), Optional.empty(), Optional.of("name2"));
        final Intel intel = Intel.ofHand(hand, Event.PLAY);

        assertEquals(expected, intel.roundWinners());
    }
}