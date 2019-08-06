package com.avacado.stupidapps.wanda.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class AvacadoUtils
{
  
  public static String generateNewApiKey() {
    return RandomStringUtils.random(256, true, true);
  }

}
