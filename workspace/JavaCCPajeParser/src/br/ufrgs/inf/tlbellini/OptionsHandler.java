package br.ufrgs.inf.tlbellini;

import br.ufrgs.inf.tlbellini.plugins.*;
import ml.options.Options;

public class OptionsHandler {
	
	Options opt;
	String filename;
	String comment = " ";
	
	public OptionsHandler(String args []){
	// get user input
	  opt = new Options(args);
	  opt.getSet().addOption("f", Options.Separator.BLANK, Options.Multiplicity.ONCE);
	  opt.getSet().addOption("m", Options.Separator.BLANK, Options.Multiplicity.ZERO_OR_ONE);
	  opt.getSet().addOption("p", Options.Separator.BLANK, Options.Multiplicity.ZERO_OR_ONE);
	  opt.getSet().addOption("l", Options.Separator.BLANK, Options.Multiplicity.ZERO_OR_ONE);
	}
	
	public void printOptionsHelper(){
		//true=ignoreUnmatched false=requireLast	
		if(!this.opt.check(true, false))
	  {
		System.out.println("Your input is incorrect");
		System.out.println("Please use the following notation:");
		System.out.println("-f <path-to-filename>");
		System.out.println("-m <comment> (optional)");
		System.out.println("-p <plugin> (optional, default: null) ");
		System.out.println("-l <number-of-lines> (optional): number of lines per dump if pjdump chosen, default: 100000");
		System.out.println("Use the following notation for the plugins: ");
		System.out.println("pjdump: PajeDump - dumps the data");
		System.exit(1);
	  }
	}
	
	public void checkEntry(){
		if(opt.getSet().isSet("f"))
	   	  {
	   	    String[] entry = opt.getSet().getOption("f").getResultValue(0).split("/");
	        filename = entry[entry.length-1];
	   	    System.out.println(filename);
	   	  }
	   	  if(opt.getSet().isSet("m"))
	   	  {
	   	    comment = opt.getSet().getOption("m").getResultValue(0);
	   	    System.out.println(comment);
	   	  }
	   	  if(opt.getSet().isSet("p"))
	   	  {
			int lines = opt.getSet().isSet("l")? Integer.parseInt(opt.getSet().getOption("l").getResultValue(0)) : 1000000;
	   	    switch(opt.getSet().getOption("p").getResultValue(0))
	   	    {
	   	      case "pjdump": PajeGrammar.plugin = new PajeDumpPlugin(lines);
	   	      break;
	   	      default: break;	
	   	    }
	   	  }else
	   	  {
			PajeGrammar.plugin = new PajeNullPlugin();
	   	  }
	}

}
