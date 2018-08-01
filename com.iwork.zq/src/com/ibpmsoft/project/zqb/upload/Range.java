package com.ibpmsoft.project.zqb.upload;

public class Range
{
  private long from;
  private long to;
  private long size;

  public Range(long from, long to, long size)
  {
    this.from = from;
    this.to = to;
    this.size = size;
  }

  public long getFrom() {
    return this.from;
  }

  public void setFrom(long from) {
    this.from = from;
  }

  public long getTo() {
    return this.to;
  }

  public void setTo(long to) {
    this.to = to;
  }

  public long getSize() {
    return this.size;
  }

  public void setSize(long size) {
    this.size = size;
  }
}