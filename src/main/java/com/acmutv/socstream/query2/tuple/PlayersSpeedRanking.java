/*
  The MIT License (MIT)

  Copyright (c) 2017 Giacomo Marciani and Michele Porretta

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.socstream.query2.tuple;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The tuple representing a top-k ranking based average speed.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlayersSpeedRanking implements Serializable {

  /**
   * The regular expression
   */
  private static final String REGEXP =
      "^(\\d+),(\\d+),\\[(.*)\\]$";

  /**
   * The pattern matcher used to match strings on {@code REGEXP}.
   */
  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  /**
   * The timestamp (start instant).
   */
  private long tsStart;

  /**
   * The timestamp (stop instant).
   */
  private long tsStop;

  /**
   * The ranking.
   */
  private List<RankingElement> rank;

  /**
   * Creates a new {@link PlayersSpeedRanking} with the specified time interval.
   * @param tsStart the timestamp for the start instant.
   * @param tsStop the timestamp for the end instant.
   * @param rank the ranking.
   */
  public PlayersSpeedRanking(long tsStart, long tsStop, List<RankingElement> rank) {
    this.tsStart = tsStart;
    this.tsStop = tsStop;
    this.rank = rank;
  }

  /**
   * Creates a new {@link PlayersSpeedRanking} with the specified time interval.
   * @param tsStart the timestamp for the start instant.
   * @param tsStop the timestamp for the end instant.
   */
  public PlayersSpeedRanking(long tsStart, long tsStop) {
    this.tsStart = tsStart;
    this.tsStop = tsStop;
    this.rank = new ArrayList<>();
  }

  /**
   * Empty constructor.
   * This constructor is mandatory for Flink serialization.
   */
  public PlayersSpeedRanking() {
    this.tsStart = 0;
    this.tsStop = 0;
    this.rank = new ArrayList<>();
  }

  /**
   * Parses {@link PlayersSpeedRanking} from string.
   * @param string the string to parse.
   * @return the parsed {@link PlayersSpeedRanking}.
   * @throws IllegalArgumentException when {@code string} cannot be parsed.
   */
  public static PlayersSpeedRanking valueOf(String string) throws IllegalArgumentException {
    if (string == null) throw new IllegalArgumentException();
    Matcher matcher = PATTERN.matcher(string);
    if (!matcher.matches()) throw new IllegalArgumentException(string);
    long tsStart = Long.valueOf(matcher.group(1));
    long tsStop = Long.valueOf(matcher.group(2));
    String strRank = matcher.group(3);
    String strElems[] = strRank.split(", ");

    PlayersSpeedRanking result = new PlayersSpeedRanking(tsStart, tsStop);

    if (strRank.isEmpty()) return result;

    for (String strElem : strElems) {
      RankingElement elem = RankingElement.valueOf(strElem);
      result.getRank().add(elem);
    }

    return result;
  }

  @Override
  public String toString() {
    return String.format("%d,%d,%s",
        this.tsStart, this.tsStop, this.rank.toString());
  }
}
