package com.ecn.ferretmvc.model;

import java.io.IOException;
import java.net.URL;

import org.json.simple.parser.ParseException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CallerRunsPolicyDemo  {
	 class MyRunnable implements Runnable {
		private final URL url;
		private final URL urlvep;

		MyRunnable(URL url, URL urlvep) {
			this.url = url;
			this.urlvep = urlvep;
			 
		}

		@Override
		public void run() {
			
		}

		
		}
}


