/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.domain.entities.deck;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck trucoDeck;

    @BeforeEach
    void setUp() {
        trucoDeck = new Deck();
    }

    @AfterEach
    void tearDown() {
        trucoDeck = null;
    }

    @Test
    @DisplayName("Should create truco deck with 40 cards")
    void shouldCreateDeckWith40Cards() {
        assertEquals(40, trucoDeck.size());
    }

    @Test
    @DisplayName("Should create sorted deck")
    void shouldCreateSortedDeck() {
        List<Card> firstEight = List.of(
                new Card(1, Suit.DIAMONDS), new Card(1, Suit.SPADES), new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(2, Suit.DIAMONDS), new Card(2, Suit.SPADES), new Card(2, Suit.HEARTS), new Card(2, Suit.CLUBS));
        assertEquals(firstEight, trucoDeck.take(8));
    }

    @Test
    @DisplayName("Should get shuffled cards after shuffling")
    void shouldGetShuffledCardsAfterShuffling() {
        trucoDeck.shuffle();
        List<Card> firstEight = List.of(
                new Card(1, Suit.DIAMONDS), new Card(1, Suit.SPADES), new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(2, Suit.DIAMONDS), new Card(2, Suit.SPADES), new Card(2, Suit.HEARTS), new Card(2, Suit.CLUBS));
        assertNotEquals(firstEight, trucoDeck.take(8));
    }

    @Test
    @DisplayName("Should be able to deal multiple cards ")
    void shouldDealMultipleCorrectly() {
        List<Card> cards = trucoDeck.take(3);
        assertEquals(3, cards.size());
    }

    @Test
    @DisplayName("Should be able to deal a single card")
    void shouldDealSingleCard() {
        Card card = trucoDeck.takeOne();
        assertNotNull(card);
    }
}