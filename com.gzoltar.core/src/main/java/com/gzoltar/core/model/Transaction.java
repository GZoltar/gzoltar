package com.gzoltar.core.model;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Transaction {

  private final String name;

  private final BitSet activity;

  private final boolean isError;

  private int hashCode;

  public Transaction(final String name, final boolean[] activityArray, final boolean isError) {
    this.name = name;
    this.activity = new BitSet(activityArray.length);
    this.isError = isError;

    for (int i = 0; i < activityArray.length; i++) {
      if (activityArray[i]) {
        this.activity.set(i);
      }
    }

    this.hashCode = activity.hashCode();
  }

  public Transaction(final String name, final boolean[] activityArray, final int hashCode,
      final boolean isError) {
    this(name, activityArray, isError);
    this.hashCode = hashCode;
  }

  public String getName() {
    return this.name;
  }

  public boolean hasActivations() {
    return this.activity.cardinality() != 0;
  }

  public BitSet getActivity() {
    return this.activity;
  }

  public List<Integer> getActiveComponents() {
    List<Integer> list = new ArrayList<Integer>();

    for (int i = 0; i < this.activity.length(); i++) {
      if (this.activity.get(i)) {
        list.add(i);
      }
    }

    return list;
  }

  public int numberActivities() {
    return this.activity.cardinality();
  }

  public boolean isError() {
    return this.isError;
  }

  public int hashCode() {
    return this.hashCode;
  }

}
