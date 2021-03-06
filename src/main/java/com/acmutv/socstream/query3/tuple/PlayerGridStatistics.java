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

package com.acmutv.socstream.query3.tuple;

import lombok.Data;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The tuple representing a set of grid statistics.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
@Data
public class PlayerGridStatistics {

  /**
   * The regular expression
   */
  //private static final String REGEXP = "^(\\d+),(\\d+),(\\d+),(\\d+),(\\d+);(\\d+),(.+)$";
  private static final String REGEXP = "^(\\d+),(\\d+),(.+)$";

  /**
   * The pattern matcher used to match strings on {@code REGEXP}.
   */
  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  /**
   * Player ID
   */
  private long pid;

  /**
   * The timestamp (start instant).
   */
  private long tsStart;

  /**
   * The grid statistics.
   */
  private Map<String,Long> stats = new HashMap<>();


  public PlayerGridStatistics(long pid, long tsStart, Map<String,Long> stats) {
    this.pid = pid;
    this.tsStart = tsStart;
    this.stats = stats;
  }

  /**
   * Creates an empty sensor event..
   * This constructor is mandatory for Flink serialization.
   */
  public PlayerGridStatistics(){}

  /**
   * Parses {@link PlayerGridStatistics} from string.
   * String input= (pid,tsStart,tsLast,x,y,stats)
   * @param string the string to parse.
   * @return the parsed {@link PlayerGridStatistics}.
   * @throws IllegalArgumentException when {@code string} cannot be parsed.
   */
  public static PlayerGridStatistics valueOf(String string) throws IllegalArgumentException {
    if (string == null) throw new IllegalArgumentException();
    Matcher matcher = PATTERN.matcher(string);
    if (!matcher.matches()) throw new IllegalArgumentException(string);
    long pid = Long.valueOf(matcher.group(1));
    long tsStart = Long.valueOf(matcher.group(2));
    String strStats = matcher.group(3);
    Map<String,Long> stats = new HashMap<>(); //TODO
    return new PlayerGridStatistics(pid,tsStart,stats);
  }

  @Override
  public String toString() {
    return String.format("%d,%d%s",
            this.tsStart, this.pid, this.printCellOccupation());
  }

  public String printCellOccupation(){

    long lifetime = this.stats.values().stream().mapToLong(i -> i.longValue()).sum();

    String occupation ="";
    String data = this.stats.toString();

    int length = data.length();
    data = data.substring(1,length-1);

    if(data.equals(""))
      return occupation;

    double percentage = 0.0;
    String[] cells = data.split(",");

    if(lifetime == 0)
      return ", NOT AVAILABLE";

    for(int i=0; i<cells.length;i++){
      String[] cell = cells[i].split("=");
      String[] coordinate = cell[0].split(";");
      percentage = (Double) Double.valueOf(cell[1])/lifetime;

      if(percentage != 0.0)
        occupation += "," + coordinate[0] + ";" + coordinate[1] + ","+ String.format("%.2f", percentage);
      else
        occupation +="";
    }
    occupation = occupation.replace(" ","");
    return occupation;
  }
}
