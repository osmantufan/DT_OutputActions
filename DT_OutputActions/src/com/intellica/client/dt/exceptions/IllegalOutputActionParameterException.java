package com.intellica.client.dt.exceptions;

public class IllegalOutputActionParameterException
  extends IllegalArgumentException
{
  public IllegalOutputActionParameterException() {}
  
  public IllegalOutputActionParameterException(String s)
  {
    super(s);
  }
  
  public IllegalOutputActionParameterException(String message, Throwable cause)
  {
    super(message, cause);
  }
  
  public IllegalOutputActionParameterException(Throwable cause)
  {
    super(cause);
  }
}
